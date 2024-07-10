package org.example.dajava.Controller.FunctionController;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.KhuyenMai;
import org.example.dajava.Model.TaiKhoan;
import org.example.dajava.Model.Xe;
import org.example.dajava.Model.YeuCauDatXe;
import org.example.dajava.Service.KhuyenMaiService;
import org.example.dajava.Service.TaiKhoanService;
import org.example.dajava.Service.XeService;
import org.example.dajava.Service.YeuCauDatXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/xeinfor")
@RequiredArgsConstructor
public class MotorInforController {
    @Autowired
    private final XeService xeService;
    private final TaiKhoanService taikhoanService;
    @Autowired
    private YeuCauDatXeService yeuCauDatXeService;
    @Autowired
    private final KhuyenMaiService khuyenMaiService;
    @GetMapping("/{id}")
    public String Xeinfor(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails, Model model){
        Xe xe = xeService.findXeByBienSoXe(id);
        model.addAttribute("xe", xe);
        if (userDetails != null) {
            String email = userDetails.getUsername(); // Lấy email từ UserDetails
            model.addAttribute("email", email);
            TaiKhoan tk = taikhoanService.findTKByEmail(email);
            model.addAttribute("tk", tk);
            List<YeuCauDatXe> yeuCauDatXe = yeuCauDatXeService.findYCByEmail(email);
            model.addAttribute("yeuCauDatXeList", yeuCauDatXe);
        }
        return "Function/xeinfor";
    }
    @PostMapping("rentmotor")
    public String rentMotor(@AuthenticationPrincipal UserDetails userDetails,
                            @ModelAttribute YeuCauDatXe yeuCauDatXe,
                            @RequestParam("bienSoXe") String bienSoXe,
                            RedirectAttributes redirectAttributes) {
        // Lấy email người dùng đang đăng nhập
        String email = userDetails.getUsername();

        // Tìm tài khoản theo email
        TaiKhoan taiKhoan = taikhoanService.findTKByEmail(email);

        // Tìm xe theo biển số xe
        Xe xe = xeService.findXeByBienSoXe(bienSoXe);
        // Kiểm tra xem xe có thuộc sở hữu của người dùng hiện tại không
        if (xe.getTaiKhoan().getEmail().equals(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể đặt xe của chính mình.");
            return "redirect:/";
        }
        // Kiểm tra các yêu cầu đặt xe hiện có cho xe này từ người dùng
        List<YeuCauDatXe> existingRequests = yeuCauDatXeService.findByEmailAndBienSoXe(email, bienSoXe);

        for (YeuCauDatXe existingRequest : existingRequests) {
            if (!existingRequest.isHide()) {
                // Kiểm tra ngày nhận mới với ngày trả của yêu cầu hiện tại
                if (yeuCauDatXe.getNgayNhan().isBefore(existingRequest.getNgayTra())) {
                    // Nếu ngày nhận mới không hợp lệ, trả về thông báo lỗi
                    redirectAttributes.addFlashAttribute("errorMessage", "Xe đã được đặt.");
                    return "redirect:/";
                }
            }
        }

        // Thiết lập các giá trị cho yêu cầu đặt xe
        yeuCauDatXe.setTaiKhoan(taiKhoan);
        yeuCauDatXe.setXe(xe);

        // Thêm yêu cầu đặt xe thông qua service
        yeuCauDatXeService.addYeuCauDatXe(yeuCauDatXe);
        redirectAttributes.addFlashAttribute("errorMessage", "Chờ chủ xe xác nhận");
        return "redirect:/";
    }
    @GetMapping("/validateDiscountCode")
    public ResponseEntity<Map<String, Object>> validateDiscountCode(@RequestParam String code) {
        KhuyenMai khuyenMai = khuyenMaiService.findByCode(code);

        Map<String, Object> response = new HashMap<>();
        if (khuyenMai != null) {
            LocalDate today = LocalDate.now();
            if (today.isAfter(khuyenMai.getNgayKetThuc()) || today.isBefore(khuyenMai.getNgayKhuyenMai())) {
                response.put("isValid", false);
                response.put("message", "Mã khuyến mãi đã hết hạn.");
            } else {
                response.put("isValid", true);
                response.put("discountPercentage", khuyenMai.getPhanTramKhuyenMai());
            }
        } else {
            response.put("isValid", false);
            response.put("message", "Mã khuyến mãi không hợp lệ.");
        }

        return ResponseEntity.ok(response);
    }
}
