package org.example.dajava.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YeuCauDatXeRequest {
    private String ngayNhan;
    private String ngayNgay;
    private Integer soNgayThue;
    private String bienSoXe;
    private String email;
    private boolean Hide;
    private boolean TrangThaiChapNhan;
}
