package org.example.dajava.Repository;

import org.example.dajava.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByIdAndToken(int id, String token);

    // Xóa token khi hết hạn
    void deleteByExpirationTimeBefore(LocalDateTime now);
}