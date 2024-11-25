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
import com.example.careflow_backend.utils.OtpUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    private final NotificationService notificationService;

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

    public ResponseEntity<?> registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse) {
        try {
            log.info("[AuthService:registerUser] User Registration Started with :::{}", userRegistrationDto);

            Optional<UserEntity> existingUserByEmail = userInfoRepo.findByEmailId(userRegistrationDto.userEmail());
            if (existingUserByEmail.isPresent()) {
                UserEntity existingUser = existingUserByEmail.get();
                // If the user is already verified, throw an error
                log.info("[existingUser1 :::{}", existingUser);
                if (existingUser.isVerified()) {
                    log.info("[existingUser1- is verified........");
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered and verified.");
                }
            }

            Optional<UserEntity> existingUserByMobile = userInfoRepo.findByMobileNumber(userRegistrationDto.userMobileNo());
            if (existingUserByMobile.isPresent()) {
                UserEntity existingUser2 = existingUserByMobile.get();
                log.info("[existingUser2 :::{}", existingUser2);
                // If the user is already verified, throw an error
                if (existingUser2.isVerified()) {
                    log.info("[existingUser2- is verified........");
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Mobile number is already registered.");
                }
            }

            if(existingUserByEmail.isPresent() || existingUserByMobile.isPresent()){
                log.info("[Saving existing user starting........");
                UserEntity existingUser = existingUserByMobile.get();
//                UserEntity existingUser = entityMapper.convertToUserEntity(userRegistrationDto);
                existingUser.setAddress(userRegistrationDto.userAddress());
                existingUser.setDescription(userRegistrationDto.description());
                existingUser.setUserName(userRegistrationDto.userName());
                existingUser.setMobileNumber(userRegistrationDto.userMobileNo());
                existingUser.setRoles(userRegistrationDto.userRole());
                existingUser.setEmailId(userRegistrationDto.userEmail());
                existingUser.setName(userRegistrationDto.name());
                existingUser.setPassword(userRegistrationDto.userPassword());

                String otp = OtpUtils.generateOtp(); // Utility class for OTP generation
                LocalDateTime otpExpiry = OtpUtils.generateOtpExpiry(); // Utility class for expiry calculation

                existingUser.setOtp(otp);
                existingUser.setOtpExpiry(otpExpiry);
                userInfoRepo.save(existingUser);

                String formattedPhoneNumber = formatPhoneNumber(userRegistrationDto.userMobileNo());

                // Send OTP to the user's phone number
                notificationService.sendOtp(formattedPhoneNumber, otp);
            } else{
                UserEntity userDetailsEntity = entityMapper.convertToUserEntity(userRegistrationDto);
                log.info("[Saving new user starting........");
                // Generate OTP and expiry time
                String otp = OtpUtils.generateOtp();
                LocalDateTime otpExpiry = OtpUtils.generateOtpExpiry();

                userDetailsEntity.setOtp(otp);
                userDetailsEntity.setOtpExpiry(otpExpiry);
                userInfoRepo.save(userDetailsEntity);

                String formattedPhoneNumber = formatPhoneNumber(userRegistrationDto.userMobileNo());

                // Send OTP to the user's phone number
                notificationService.sendOtp(formattedPhoneNumber, otp);
            }

            log.info("[AuthService:registerUser] OTP sent to user phone: {}", userRegistrationDto.userMobileNo());

            return ResponseEntity.ok("OTP sent to your phone. Please verify to complete registration.");

        } catch (ResponseStatusException e) {
            log.error("[AuthService:registerUser] Registration error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[AuthService:registerUser] Unexpected error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    // Function to format the phone number (removes leading zero and adds country code)
    public String formatPhoneNumber(String mobileNumber) {
        // Check if the number starts with '0', and replace it with '+94'
        if (mobileNumber.startsWith("0")) {
            return "+94" + mobileNumber.substring(1);
        }
        // If the number is already in correct format (e.g., +94776750158), return as is
        return mobileNumber;
    }

}

