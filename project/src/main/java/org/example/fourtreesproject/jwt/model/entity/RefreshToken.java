package org.example.fourtreesproject.jwt.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name="refresh_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 255)
    private String refreshToken;

    public void updateJwt(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
