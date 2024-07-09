package org.example.dajava.Repository;

import org.example.dajava.Model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ITaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    Optional<TaiKhoan> findByEmail(String email);
    Optional<TaiKhoan> findByResetToken(String token); // Thêm phương thức này
    @Query("SELECT u FROM TaiKhoan u WHERE u.email=?1")
    TaiKhoan findTKByEmail(String email);
}
