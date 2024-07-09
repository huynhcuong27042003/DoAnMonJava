package org.example.dajava.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DanhGia {
    @Id
    private String BienSoXe;
    @Id
    private String Email;
    @Min(value = 1, message = "Số sao phải ít nhất là 1")
    @Max(value = 5, message = "Số sao không thể vượt quá 5")
    private Integer SoSao;
    private String NhanXet;

    @ManyToOne
    @JoinColumn(name = "bienSoXe", nullable = false)
    private Xe xe;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private TaiKhoan taiKhoan;

    // Getters and Setters
}
