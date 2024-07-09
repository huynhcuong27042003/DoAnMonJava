package org.example.dajava.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TaiKhoan implements UserDetails{
    @Id
    @Email(message = "Email phải đúng định dạng")
    private String email;

    @NotBlank(message = "Tên người dùng không được để trống")
    private String tenNguoiDung;

    @NotBlank(message = "Password là bắt buộc")
    @Column(name = "password", length = 250)
    private String MatKhau;

    @Pattern(regexp = "^0\\d{9}$", message = "SDT phải bắt đầu bằng 0 và đủ 10 ký tự")
    @NotBlank(message = "Số điện thoại là bắt buộc")
    @Column(unique = true)
    private String SDT;

    @Column(nullable = true)
    private String DiaChi;

    @Column(nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate NgaySinh;

    @Pattern(regexp = "^\\d{12}$", message = "GPLX phải đủ 12 ký tự và luôn là chữ số")
    @Column(nullable = true)
    private String GPLX;

    @Column(nullable = true)
    private String Avarta;

    @OneToMany(mappedBy = "taiKhoan")
    private List<Xe> xes;

    @OneToMany(mappedBy = "taiKhoan")
    private List<YeuCauDatXe> yeuCauDatXes;

    @PrePersist
    @PreUpdate
    private void validateAge() {
        if (NgaySinh != null) {
            LocalDate birthDate = NgaySinh;
            LocalDate today = LocalDate.now();
            int age = Period.between(birthDate, today).getYears();
            if (age < 18) {
                throw new IllegalArgumentException("Người dùng phải đủ 18 tuổi");
            }

        }
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tai_khoan_chuc_vu",
            joinColumns = @JoinColumn(name = "email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "idChucVu", referencedColumnName = "IDChucVu")
    )
    private Set<ChucVu> roles = new HashSet<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<ChucVu> userRoles = this.getRoles();
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getTenChucVu()))
                .toList();
    }

    @Override
    public String getPassword() {
        return MatKhau;
    }

    @Override
    public String getUsername() {
        return tenNguoiDung;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return getEmail() != null && getEmail().equals(taiKhoan.getEmail());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    // Getters and Setters
}
