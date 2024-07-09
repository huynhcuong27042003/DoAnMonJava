package org.example.dajava.Controller.FunctionController;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Controller.ManageController.HandleImage;
import org.example.dajava.Model.TaiKhoan;
import org.example.dajava.Model.YeuCauDatXe;
import org.example.dajava.Service.TaiKhoanService;
import org.example.dajava.Service.YeuCauDatXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/userinfor")
@RequiredArgsConstructor
public class TaiKhoanController {
    @Autowired
    private final TaiKhoanService taiKhoanService;
    private final HandleImage handleImage;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private YeuCauDatXeService yeucauDatXeService;
    @GetMapping("/update/{id}")
    public String taiKhoan(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        TaiKhoan tk = taiKhoanService.findTKByEmail(id);
        model.addAttribute("tk", tk);
        List<YeuCauDatXe> yeuCauDatXe = yeucauDatXeService.findYCByEmail(id);
        model.addAttribute("yeuCauDatXeList", yeuCauDatXe);
        return "Function/userinfor";
    }
    @GetMapping("/changepassword/{id}")
    public String changePassword(@PathVariable String id, Model model) {
        TaiKhoan tk = taiKhoanService.findTKByEmail(id);
        model.addAttribute("tk", tk);
        return "Function/changepassword";
    }
    @PostMapping("/update/{id}")
    public String updateTk(@PathVariable String id, @ModelAttribute("tk") TaiKhoan tk, BindingResult result,@RequestParam("image") MultipartFile image, Model model){
        if (result.hasErrors()) {
            return "Function/userinfor";
        }
        TaiKhoan existingTk = taiKhoanService.findTKByEmail(id);
        if (existingTk != null) {
            if (!image.isEmpty()) {
                String imageName = null;
                try {
                    imageName = handleImage.saveImage(image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                existingTk.setAvarta(imageName);
            }
            existingTk.setTenNguoiDung(tk.getTenNguoiDung());
            existingTk.setNgaySinh(tk.getNgaySinh());
            existingTk.setSDT(tk.getSDT());
            existingTk.setDiaChi(tk.getDiaChi());
            taiKhoanService.updateInfor(existingTk);
        }
        return "redirect:/userinfor/update/" + id;
    }
    @PostMapping("/updateGPLX/{id}")
    public String updateGplx(@PathVariable String id, @ModelAttribute("taikhoan") TaiKhoan tk, Model model) {
        TaiKhoan existingTk = taiKhoanService.findTKByEmail(id);
        if (existingTk != null) {
            existingTk.setGPLX(tk.getGPLX());
            taiKhoanService.updateGPLX(existingTk);
        }
        return "redirect:/userinfor/" + id;
    }
    @PostMapping("/changepassword/{id}")
    public String passwordChange(@PathVariable String id,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        TaiKhoan tk = taiKhoanService.findTKByEmail(id);
        if (tk == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại");
            return "redirect:/userinfor/changepassword/" + id;
        }

        if (!passwordEncoder.matches(currentPassword, tk.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không chính xác");
            return "redirect:/userinfor/changepassword/" + id;
        }

        tk.setMatKhau(passwordEncoder.encode(newPassword));
        taiKhoanService.changePassword(tk);
        redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi thành công");
        return "redirect:/userinfor/changepassword/" + id;
    }

}
