package org.example.dajava.Model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChucVu implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IDChucVu;
    @NotBlank(message = "TenChucVu là bắt buộc")
    @Column(unique = true)
    private String tenChucVu;
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<TaiKhoan> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return tenChucVu;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChucVu chucVu = (ChucVu) o;
        return getTenChucVu().equals(chucVu.getTenChucVu());

    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}