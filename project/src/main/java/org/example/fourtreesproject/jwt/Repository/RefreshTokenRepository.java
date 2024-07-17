package org.example.fourtreesproject.jwt.Repository;

import org.example.fourtreesproject.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByEmail(String email);
}
