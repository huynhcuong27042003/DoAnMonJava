package org.example.dajava.Controller.FunctionController;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.TaiKhoan;
import org.example.dajava.Model.Xe;
import org.example.dajava.Model.YeuCauDatXe;
import org.example.dajava.Service.MailService;
import org.example.dajava.Service.TaiKhoanService;
import org.example.dajava.Service.XeService;
import org.example.dajava.Service.YeuCauDatXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RentMotorController{
    private final TaiKhoanService taikhoanService;
    @Autowired
    private final XeService xeService;
    @Autowired
    private final YeuCauDatXeService yeuCauDatXeService;
    @Autowired
    private final MailService mailService;
    @GetMapping("/checkorder/{id}")
    public String CheckOrder(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
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
        YeuCauDatXe datXe = yeuCauDatXeService.findById(id);
        model.addAttribute("datXe", datXe);
        return "Function/checkrequestorder";
    }
    @PostMapping("/denyorder/{id}")
    public  String DenyOrder(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        YeuCauDatXe datXeexisting= yeuCauDatXeService.findById(id);
        if (datXeexisting != null) {
            datXeexisting.setHide(true); // Set hide to true
            yeuCauDatXeService.save(datXeexisting); // Lưu thay đổi vào cơ sở dữ liệu nếu cần
            redirectAttributes.addFlashAttribute("errorMessage", "Đã từ chối yêu cầu");
        }
        return "redirect:/";
    }
    @PostMapping("/acceptorder/{id}")
    public String AcceptorOrder(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        YeuCauDatXe datXeexisting = yeuCauDatXeService.findById(id);
        if (datXeexisting != null) {
            datXeexisting.setTrangThaiChapNhan(true);
            yeuCauDatXeService.save(datXeexisting);
            String recipientEmail = datXeexisting.getTaiKhoan().getEmail(); // Thay email người dùng tại đây
            String subject = "Xác nhận thuê xe";
            String body = "<html>" +
                    "<body>" +
                    "<p>Xin chào,</p>" +
                    "<p>Bạn đã chọn thuê xe thành công.</p>" +
                    "<p>Thông tin chi tiết:</p>" +
                    "<ul>" +
                    "<p>Biển số xe: " + datXeexisting.getXe().getBienSoXe() + "</p>" +
                    "<p>Tên xe: " + datXeexisting.getXe().getTenXe() + "</p>" +
                    "</ul>" +
                    "<button href=\"http://example.com\">Thanh toán</button>" +
                    "</body>" +
                    "</html>";
            mailService.SendMail(recipientEmail, subject, body);
        }
        return "redirect:/";
    }
    @GetMapping("/payment")
    public String payment(@AuthenticationPrincipal UserDetails userDetails,Model model, RedirectAttributes redirectAttributes) {
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Lấy email từ UserDetails
            model.addAttribute("email", email);
            TaiKhoan tk = taikhoanService.findTKByEmail(email);
            model.addAttribute("tk", tk);
            List<YeuCauDatXe> yeuCauDatXe = yeuCauDatXeService.findYCByEmail(email);
            model.addAttribute("yeuCauDatXeList", yeuCauDatXe);
        }
        return "Function/payment";
    }
}
