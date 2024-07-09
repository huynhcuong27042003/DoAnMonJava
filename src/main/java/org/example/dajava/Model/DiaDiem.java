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
public class DiaDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaDiaDiem;
    @NotBlank(message = "Tên địa điểm là bắt buộc")
    @Column(unique = true)
    private String TenDiaDiem;
    @OneToMany(mappedBy = "diaDiem")
    private List<Xe> xes;
}
