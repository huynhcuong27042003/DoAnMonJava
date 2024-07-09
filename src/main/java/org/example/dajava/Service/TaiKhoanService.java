package org.example.dajava.Service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dajava.Model.TaiKhoan;
import org.example.dajava.Repository.IChucVuRepository;
import org.example.dajava.Repository.ITaiKhoanRepository;
import org.example.dajava.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class TaiKhoanService implements UserDetailsService {
    @Autowired
    ITaiKhoanRepository taiKhoanReponsitory;
    @Autowired
    IChucVuRepository chucVuReponsitory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailSender;
    public void save (@NotNull TaiKhoan taiKhoan) {
        taiKhoan.setMatKhau(new BCryptPasswordEncoder().encode(taiKhoan.getMatKhau()));
        taiKhoanReponsitory.save(taiKhoan);
    }
    //Gán chức vị mặc định là USER
    public void setDefaultChucVu(String email) {
        taiKhoanReponsitory.findByEmail(email).ifPresentOrElse(user -> {
            user.getRoles().add(chucVuReponsitory.findChucVuByIDChucVu(Role.USER.value));
            taiKhoanReponsitory.save(user);
        }, () -> {
            throw new UsernameNotFoundException("Người dùng không tồn tại");
        });
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var taikhoan = taiKhoanReponsitory.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Không tìm thấy email"));
        return org.springframework.security.core.userdetails.User
                .withUsername(taikhoan.getEmail())
                .password(taikhoan.getMatKhau())
                .authorities(taikhoan.getAuthorities())
                .accountExpired(!taikhoan.isAccountNonExpired())
                .accountLocked(!taikhoan.isAccountNonLocked())
                .credentialsExpired(!taikhoan.isCredentialsNonExpired())
                .disabled(!taikhoan.isEnabled())
                .build();

    }

    public TaiKhoan updateInfor(@NotNull TaiKhoan taikhoan) {
        TaiKhoan existingTk = taiKhoanReponsitory.findByEmail(taikhoan.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Tài khoản với ID " + taikhoan.getEmail() + "không tìm thấy"));
        existingTk.setTenNguoiDung(taikhoan.getTenNguoiDung());
        existingTk.setNgaySinh(taikhoan.getNgaySinh());
        existingTk.setSDT(taikhoan.getSDT());
        existingTk.setDiaChi(taikhoan.getDiaChi());
        existingTk.setAvarta(existingTk.getAvarta());
        /*if (taikhoan.getMatKhau() != null && !taikhoan.getMatKhau().isEmpty() && !passwordEncoder.matches(taikhoan.getMatKhau(), existingTk.getMatKhau())) {
            existingTk.setMatKhau(passwordEncoder.encode(taikhoan.getMatKhau()));
        } else {
            // Giữ nguyên mật khẩu cũ nếu mật khẩu mới không được cập nhật
            existingTk.setMatKhau(existingTk.getMatKhau());
        }*/
        return taiKhoanReponsitory.save(existingTk);
    }
    public TaiKhoan updateGPLX(@NotNull TaiKhoan taikhoan) {
        TaiKhoan existingTk = taiKhoanReponsitory.findByEmail(taikhoan.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Tài khoản với ID " + taikhoan.getEmail() + "không tìm thấy"));
        existingTk.setGPLX(taikhoan.getGPLX());
        existingTk.setTenNguoiDung(taikhoan.getTenNguoiDung());
        return taiKhoanReponsitory.save(existingTk);
    }
    public TaiKhoan changePassword(TaiKhoan taikhoan) {
        TaiKhoan existingTk = taiKhoanReponsitory.findByEmail(taikhoan.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản với ID " + taikhoan.getEmail() + " không tìm thấy"));
        existingTk.setMatKhau(taikhoan.getPassword());
        return taiKhoanReponsitory.save(existingTk);
    }

    public TaiKhoan findTKByEmail(String email) {
        return taiKhoanReponsitory.findTKByEmail(email);
    }

    public Optional<TaiKhoan> findByEmail(String email) throws UsernameNotFoundException {
        return taiKhoanReponsitory.findByEmail(email);
    }

    public String getEmailByResetToken(String token) {
        TaiKhoan taiKhoan = taiKhoanReponsitory.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn"));
        return taiKhoan.getEmail();
    }

    public void initiatePasswordReset(String email) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanReponsitory.findByEmail(email);
        if (optionalTaiKhoan.isPresent()) {
            TaiKhoan taiKhoan = optionalTaiKhoan.get();
            String resetToken = UUID.randomUUID().toString();
            taiKhoan.setResetToken(resetToken);
            taiKhoan.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            taiKhoanReponsitory.save(taiKhoan);

            // Send email with reset token
            sendResetTokenEmail(taiKhoan.getEmail(), resetToken);
        } else {
            throw new UsernameNotFoundException("Không tìm thấy email");
        }
    }

    public void resetPassword(String email, String token, String newPassword) {
        TaiKhoan taiKhoan = taiKhoanReponsitory.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email"));

        if (token.equals(taiKhoan.getResetToken()) && taiKhoan.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
            taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
            taiKhoan.setResetToken(null);
            taiKhoan.setResetTokenExpiry(null);
            taiKhoanReponsitory.save(taiKhoan);
        } else {
            throw new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn");
        }
    }

    private void sendResetTokenEmail(String email, String token) {
        String subject = "Yêu cầu đặt lại mật khẩu"; // Khai báo biến subject
        String text = "Để đặt lại mật khẩu của bạn, vui lòng truy cập vào đường dẫn sau: "
                + "http://localhost:8080/reset-password?token=" + token; // Khai báo biến text
        mailSender.SendMail(email, subject, text);
    }
    public List<TaiKhoan> taiKhoanList(){
        return taiKhoanReponsitory.findAll();
    }
}
