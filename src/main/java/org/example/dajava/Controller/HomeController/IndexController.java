package org.example.dajava.Controller.HomeController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.dajava.Controller.ManageController.HandleImage;
import org.example.dajava.Model.TaiKhoan;
import org.example.dajava.Model.Xe;
import org.example.dajava.Model.YeuCauDatXe;
import org.example.dajava.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final TaiKhoanService taikhoanService;
    @Autowired
    private final XeService xeService;
    @Autowired
    private final YeuCauDatXeService yeuCauDatXeService;
    @Autowired
    private final MailService mailService;
    @Autowired
    private final DiaDiemService diaDiemService;
    @Autowired
    private final HangXeService hangXeService;
    @Autowired
    private final LoaiXeService loaiXeService;

    @Autowired
    private final HandleImage handleImage;
    @GetMapping("/")
    public String Index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Xe> xeList = xeService.getAllNotHiddenXe();

        model.addAttribute("xeList", xeList);
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Lấy email từ UserDetails
            model.addAttribute("email", email);
            TaiKhoan tk = taikhoanService.findTKByEmail(email);
            model.addAttribute("tk", tk);

            List<YeuCauDatXe> yeuCauDatXe = yeuCauDatXeService.findYCByEmail(email);
            model.addAttribute("yeuCauDatXeList", yeuCauDatXe);
        }
        return "/Home/Index";
    }
    @GetMapping("/login")
    public String login() {
        return "/Home/Login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new TaiKhoan());
        return "/Home/Index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") TaiKhoan taikhoan,@NotNull BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "/Home/Index";
        }
        taikhoanService.save(taikhoan);
        taikhoanService.setDefaultChucVu(taikhoan.getEmail());
        return "redirect:/";
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/Home/ForgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes) {
        Optional<TaiKhoan> optionalTaiKhoan = taikhoanService.findByEmail(email);
        if (optionalTaiKhoan.isPresent()) {
            TaiKhoan taikhoan = optionalTaiKhoan.get();
            String recipientEmail = email;
            String subject = "Đặt lại mật khẩu";

            // Generate reset token and update the TaiKhoan entity
            String resetToken = UUID.randomUUID().toString();
            taikhoan.setResetToken(resetToken);
            taikhoan.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            taikhoanService.save(taikhoan); // Save updated TaiKhoan entity

            // Prepare email body using HTML content directly
            String body = "<html>" +
                    "<body>" +
                    "<p>Xin chào,</p>" +
                    "<p>Bạn có muốn đặt lại mật khẩu?</p>" +
                    "<p>Để đặt lại mật khẩu của bạn, vui lòng nhấn vào đường dẫn sau:</p>" +
                    "<a href='http://localhost:8080/reset-password?token=" + resetToken + "'>Đặt lại mật khẩu</a>" +
                    "</body>" +
                    "</html>";

            // Send email
            mailService.SendMail(recipientEmail, subject, body);

            redirectAttributes.addFlashAttribute("errorMessage", "Yêu cầu đã được gửi đến email của bạn");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy email trong hệ thống.");
        }
        return "redirect:/";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        String email = taikhoanService.getEmailByResetToken(token);
        model.addAttribute("token", token);
        model.addAttribute("email", email);
        return "/Home/ResetPassword";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "/Home/ResetPassword";
        }

        try {
            taikhoanService.resetPassword(email, token, password);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "/Home/ResetPassword";
        }
    }
    @GetMapping("/addbyuser")
    public String AddXeByUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Xe> xeList = xeService.findAll();

        model.addAttribute("xeList", xeList);
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Lấy email từ UserDetails
            model.addAttribute("email", email);
            TaiKhoan tk = taikhoanService.findTKByEmail(email);
            model.addAttribute("tk", tk);

           List<YeuCauDatXe> yeuCauDatXe = yeuCauDatXeService.findYCByEmail(email);
           model.addAttribute("yeuCauDatXeList", yeuCauDatXe);
        }
        model.addAttribute("xe", new Xe());
        model.addAttribute("hangxe", hangXeService.getAllHangXe());
        model.addAttribute("loaixe", loaiXeService.getAllLoaiXe());
        model.addAttribute("diadiem", diaDiemService.getAllDiaDiem());
        return "xe/addbyuser"; // Return the view for adding a new Xe
    }

    @PostMapping("/addbyuser")
    public String addXeSubmit(@Valid @ModelAttribute("xe") Xe xe, BindingResult bindingResult, @RequestParam("imageFile1") MultipartFile imageFile1,
                              @RequestParam("imageFile2") MultipartFile imageFile2, @RequestParam("imageFile3") MultipartFile imageFile3,
                              @RequestParam("imageFile4") MultipartFile imageFile4, RedirectAttributes redirectAttributes,@AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "xe/addbyuser"; // Return the view with validation errors
        }
        String email = userDetails.getUsername();
        TaiKhoan taiKhoan = taikhoanService.findTKByEmail(email);
        try {
            if (!imageFile1.isEmpty()) {
                String fileName1 = handleImage.saveImage(imageFile1);
                xe.setHinhAnh1(fileName1);
            }
            if (!imageFile2.isEmpty()) {
                String fileName2 = handleImage.saveImage(imageFile2);
                xe.setHinhAnh2(fileName2);
            }
            if (!imageFile3.isEmpty()) {
                String fileName3 = handleImage.saveImage(imageFile3);
                xe.setHinhAnh3(fileName3);
            }
            if (!imageFile4.isEmpty()) {
                String fileName4 = handleImage.saveImage(imageFile4);
                xe.setHinhAnh4(fileName4);
            }
        } catch (IOException e) {
            bindingResult.rejectValue("hinhAnh", "error.xe", "Failed to save image: " + e.getMessage());
            return "xe/addbyuser"; // Return the view with errors
        }
        xe.setTaiKhoan(taiKhoan);
        xeService.createXe(xe);

        redirectAttributes.addFlashAttribute("message", "Xe added successfully!");
        return "redirect:/"; // Redirect to the list of Xe
    }
}
