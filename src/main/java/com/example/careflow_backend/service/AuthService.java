package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.RefreshTokenEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.config.jwtConfig.JwtTokenGenerator;
import com.example.careflow_backend.dto.AuthResponseDto;
import com.example.careflow_backend.dto.TokenType;
import com.example.careflow_backend.dto.UserRegistrationDto;
import com.example.careflow_backend.mapper.EntityMapper;
import com.example.careflow_backend.repository.RefreshTokenRepo;
import com.example.careflow_backend.repository.UserRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static com.example.careflow_backend.config.jwtConfig.JwtTokenGenerator.getRolesOfUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepo userInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final EntityMapper entityMapper;

    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try
        {
            System.out.println(" userInfoEntity - " +  authentication.getName());
            var userInfoEntity = userInfoRepo.findByUserName(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});

            System.out.println(" userInfoEntityID-" +  userInfoEntity.getId());
            //log4j

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication , userInfoEntity.getId() , userInfoEntity.getEmailId());
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            String roles = getRolesOfUser(authentication);

            System.out.println(" roles - " +  roles);

            //Let's save the refreshToken as well
            saveUserRefreshToken(userInfoEntity,refreshToken);
            creatRefreshTokenCookie(response,refreshToken);
            
            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userInfoEntity.getUserName());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .name(userInfoEntity.getName())
                    .tokenType(TokenType.Bearer)
                    .userRole(roles)       // Set the user role here
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    private void saveUserRefreshToken(UserEntity userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {         //when access token get expair, can get the again acceses token using refresh token

        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        UserEntity userInfoEntity = refreshTokenEntity.getUser();


        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userInfoEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication , userInfoEntity.getId() , userInfoEntity.getEmailId());
        log.info("AccessToken ", accessToken);

        System.out.println("accessToken " + accessToken);
        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .name(userInfoEntity.getName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(UserEntity userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getEmailId();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse) {
        try {
            log.info("[AuthService:registerUser] User Registration Started with :::{}", userRegistrationDto);

            // Check if email already exists
            Optional<UserEntity> existingUserByEmail = userInfoRepo.findByEmailId(userRegistrationDto.userEmail());
            if (existingUserByEmail.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered.");
            }

            // Check if mobile number already exists
            Optional<UserEntity> existingUserByMobile = userInfoRepo.findByMobileNumber(userRegistrationDto.userMobileNo());
            if (existingUserByMobile.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Mobile number is already registered.");
            }

            // Proceed with registration
            UserEntity userDetailsEntity = entityMapper.convertToUserEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);

            log.info("User Details Entity: {}", userDetailsEntity);

            UserEntity savedUserDetails = userInfoRepo.save(userDetailsEntity);

            // Generate JWT tokens
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication, savedUserDetails.getId() , savedUserDetails.getEmailId());
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            log.info("savedUserDetails Entity: {}", savedUserDetails);
            saveUserRefreshToken(savedUserDetails, refreshToken);

            String roles = getRolesOfUser(authentication);
            creatRefreshTokenCookie(httpServletResponse, refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered", savedUserDetails.getUserName());

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .name(savedUserDetails.getName())
                    .tokenType(TokenType.Bearer)
                    .userRole(roles)
                    .build();

        } catch (ResponseStatusException e) {
            log.error("[AuthService:registerUser] Registration error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[AuthService:registerUser] Unexpected error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

}

