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
public class HangXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaHangXe;
    @NotBlank(message = "Tên hãng xe là bắt buộc")
    @Column(unique = true)
    private String TenHang;
    @OneToMany(mappedBy = "hangXe")
    private List<Xe> xes;
    // Getters and Setters
}
