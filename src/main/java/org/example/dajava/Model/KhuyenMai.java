package org.example.dajava.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaKhuyenMai;

    @NotBlank(message = "Tên khuyến mãi là bắt buộc")
    private String TenKhuyenMai;

    @Pattern(regexp = "^[A-Z]{6}$", message = "Code phải đủ 6 ký tự và ghi hoa hết")
    private String Code;

    @NotBlank(message = "Nội dung khuyến mãi là bắt buộc")
    private String NoiDungKhuyenMai;

    @Min(value = 1, message = "Phần trăm khuyến mãi phải lớn hơn hoặc bằng 1")
    @Max(value = 100, message = "Phần trăm khuyến mãi phải nhỏ hơn hoặc bằng 100")
    private Float PhanTramKhuyenMai;

    private String HinhAnhKhuyenMai;

    @NotNull(message = "Ngày khuyến mãi là bắt buộc")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate NgayKhuyenMai;

    @NotNull(message = "Ngày kết thúc là bắt buộc")
    @Future(message = "Ngày kết thúc phải lớn hơn ngày hiện tại")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate NgayKetThuc;

    @AssertTrue(message = "Ngày khuyến mãi phải trước ngày kết thúc")
    public boolean isNgayKhuyenMaiBeforeNgayKetThuc() {
        return NgayKhuyenMai != null && NgayKetThuc != null && NgayKhuyenMai.isBefore(NgayKetThuc);
    }
}
