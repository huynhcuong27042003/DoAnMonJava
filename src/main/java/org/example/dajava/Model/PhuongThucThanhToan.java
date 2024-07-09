package org.example.dajava.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaPhuongThuc;
    @NotBlank(message = "Tên là bắt buộc")
    @Column(unique = true)
    private String TenPhuongThuc;
    @OneToMany(mappedBy = "phuongThucThanhToan")
    private List<HoaDon> hoaDons;

    // Getters and Setters
}

