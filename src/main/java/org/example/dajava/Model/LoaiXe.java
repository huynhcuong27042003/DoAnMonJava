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
public class LoaiXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaLoai;
    @NotBlank(message = "Tên loại xe là bắt buộc")
    @Column(unique = true)
    private String TenLoai;
    @OneToMany(mappedBy = "loaiXe")
    private List<Xe> xes;
}