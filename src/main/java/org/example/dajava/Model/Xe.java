package org.example.dajava.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Xe {
    @Id
    private String bienSoXe;

    private String tenXe;
    private String namSanXuat;
    private Long giaThue;
    private String nhienLieu;
    private String LucPhanKhoi;
    private String moTa;
    private boolean trangThai;
    private boolean hide;
    private String hinhAnh1;
    private String hinhAnh2;
    private String hinhAnh3;
    private String hinhAnh4;

    @ManyToOne
    @JoinColumn(name = "maDiaDiem", nullable = false)
    private DiaDiem diaDiem;

    @ManyToOne
    @JoinColumn(name = "maHangXe", nullable = false)
    private HangXe hangXe;

    @ManyToOne
    @JoinColumn(name = "maLoai", nullable = false)
    private LoaiXe loaiXe;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "xe")
    private List<DanhGia> danhGia;

    @OneToMany(mappedBy = "xe")
    private List<YeuCauDatXe> yeuCauDatXes;
}