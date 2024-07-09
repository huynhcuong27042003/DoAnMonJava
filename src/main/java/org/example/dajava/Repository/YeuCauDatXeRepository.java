package org.example.dajava.Repository;

import org.example.dajava.Model.TaiKhoan;
import org.example.dajava.Model.YeuCauDatXe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YeuCauDatXeRepository extends JpaRepository<YeuCauDatXe, Integer> {
    @Query("SELECT yc FROM YeuCauDatXe yc WHERE yc.xe.taiKhoan.email = ?1 AND yc.trangThaiChapNhan = false AND yc.hide = false AND yc.ngayTra >= CURRENT_DATE")
    List<YeuCauDatXe> findTaiKhoanByEmail(String email);

    @Query("SELECT yc FROM YeuCauDatXe yc WHERE yc.taiKhoan.email=?1 AND yc.xe.bienSoXe=?2")
    List<YeuCauDatXe> findByEmailAndBienSoXe(String email, String bienSoXe);
}
