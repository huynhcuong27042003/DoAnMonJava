package org.example.dajava.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YeuCauDatXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maYeuCau;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate ngayNhan;
    @Future(message = "Ngày trả phải lớn hơn ngày kết thúc")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate ngayTra;
    private int soNgayThue;
    private boolean trangThaiChapNhan;
    private boolean hide;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "bienSoXe", nullable = false)
    private Xe xe;

    @OneToMany(mappedBy = "yeuCauDatXe")
    private List<HoaDon> hoaDons;
    @AssertTrue(message = "Ngày nhận phải trước ngày trả")
    public boolean isNgayNhanBeforeNgayTra() {
        return ngayNhan != null && ngayTra != null && ngayNhan.isBefore(ngayTra);
    }
    // Getters and Setters
}
