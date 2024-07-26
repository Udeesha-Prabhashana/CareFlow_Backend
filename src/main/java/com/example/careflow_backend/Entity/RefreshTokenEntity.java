package com.example.careflow_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="REFRESH_TOKENS")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "REFRESH_TOKEN", nullable = false, length = 10000)
    private String refreshToken;

    @Column(name = "REVOKED")    //When sing-out, refresh token can not be use because Revoked is true
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude // Exclude from toString to prevent circular reference
    private UserEntity user;
}
