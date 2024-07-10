package org.example.dajava.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int idYeucauDatXe;

    private String token;

    private LocalDateTime expirationTime;
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

}