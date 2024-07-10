package org.example.dajava.Service;

import org.example.dajava.Model.Token;
import org.example.dajava.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token save(Token token) {
        return tokenRepository.save(token);
    }
    public void saveToken(Long id, String token, LocalDateTime expirationTime) {
        Token newToken = new Token();
        newToken.setId(id);
        newToken.setToken(token);
        newToken.setExpirationTime(expirationTime);
        tokenRepository.save(newToken);
    }

    public Token findTokenByIdAndToken(int id, String token) {
        return tokenRepository.findByIdAndToken(id, token);
    }

    // Hàm xóa token khi hết hạn
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteByExpirationTimeBefore(now);
    }
}