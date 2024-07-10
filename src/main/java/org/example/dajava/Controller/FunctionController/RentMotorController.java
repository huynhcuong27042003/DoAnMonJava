package org.example.dajava.Controller.FunctionController;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.*;
import org.example.dajava.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
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
    @Autowired
    private final PhuongThucThanhToanService phuongThucThanhToanService;
    @Autowired
    private final HoaDonService hoaDonService;
    @Autowired
    private final TokenService tokenService;
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
                    "<a href=\"http://localhost:8080/payment/" + id +"\">Thanh toán</a>" +
                    "</body>" +
                    "</html>";
            mailService.SendMail(recipientEmail, subject, body);
            redirectAttributes.addFlashAttribute("errorMessage", "Đã gửi thông báo đến khách hàng.");
        }
        return "redirect:/";
    }
    @GetMapping("/payment/{id}")
    public String payment(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes, HoaDon hoaDon) {
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
        Long tongDonGia = datXe.getXe().getGiaThue() * datXe.getSoNgayThue();
        model.addAttribute("tongDonGia", tongDonGia);
        Long tienCoc = tongDonGia * 20 /100;
        model.addAttribute("tienCoc", tienCoc);
        model.addAttribute("pttts",phuongThucThanhToanService.findAll());
        return "Function/payment";
    }
    @PostMapping("/payment")
    public String processPayment(@RequestParam("phuongThucThanhToan") int phuongThucThanhToanId,@RequestParam("mayeucau") int mayeucau , RedirectAttributes redirectAttributes) {
        YeuCauDatXe yeuCauDatXe = yeuCauDatXeService.findById(mayeucau);
        if (yeuCauDatXe != null) {
            // Tạo hóa đơn mới
            HoaDon hoaDon = new HoaDon();
            hoaDon.setYeuCauDatXe(yeuCauDatXe);
            hoaDon.setTaiKhoan(yeuCauDatXe.getTaiKhoan());
            hoaDon.setXe(yeuCauDatXe.getXe());
            hoaDon.setTongDonGia(yeuCauDatXe.getXe().getGiaThue() * yeuCauDatXe.getSoNgayThue());
            hoaDon.setTienCoc(hoaDon.getTongDonGia() * 20 / 100);
            hoaDon.setNgayLap(LocalDateTime.now());

            PhuongThucThanhToan phuongThuc = phuongThucThanhToanService.findPTById(phuongThucThanhToanId);
            hoaDon.setPhuongThucThanhToan(phuongThuc);

            hoaDonService.save(hoaDon);
            redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Yêu cầu đặt xe không tồn tại.");
        }
        return "redirect:/";
    }
}
