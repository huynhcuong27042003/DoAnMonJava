package org.example.dajava.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaHoaDon;
    private LocalDateTime NgayLap;
    private Long TongDonGia;
    private Long TienCoc;
    @ManyToOne
    @JoinColumn(name = "maPhuongThuc", nullable = false)
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "maYeuCau", nullable = false)
    private YeuCauDatXe yeuCauDatXe;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "bienSoXe", nullable = false)
    private Xe xe;
}