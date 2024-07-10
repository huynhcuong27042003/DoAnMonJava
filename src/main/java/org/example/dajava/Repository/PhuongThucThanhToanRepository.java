package org.example.dajava.Repository;

import org.example.dajava.Model.LoaiXe;
import org.example.dajava.Model.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    @Query("SELECT pt from PhuongThucThanhToan pt WHERE pt.MaPhuongThuc=?1")
    PhuongThucThanhToan findByMaPhuongThuc(int maPhuongThuc);
}