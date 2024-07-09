package org.example.dajava.Controller.ManageController;

import org.example.dajava.Model.KhuyenMai;
import org.example.dajava.Service.KhuyenMaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/khuyenmai")
@RequiredArgsConstructor
public class KhuyenMaiController {

    @Autowired
    private final KhuyenMaiService khuyenMaiService;
    private final HandleImage handleImage;

    // Hiển thị tất cả KhuyenMai
    @GetMapping("/list")
    public String getAllKhuyenMai(Model model) {
        List<KhuyenMai> khuyenMaiList = khuyenMaiService.getAllKhuyenMai();
        model.addAttribute("khuyenMaiList", khuyenMaiList);
        return "khuyenmai/list"; // Trả về view để hiển thị tất cả KhuyenMai
    }
    @GetMapping("/detail/{id}")
    public String viewKhuyenMaiDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<KhuyenMai> khuyenMaiOptional = khuyenMaiService.getKhuyenMaiById(id);
        if (khuyenMaiOptional.isPresent()) {
            model.addAttribute("khuyenmai", khuyenMaiOptional.get());
            return "khuyenmai/detail"; // Trả về view để hiển thị chi tiết KhuyenMai
        } else {
            redirectAttributes.addFlashAttribute("error", "Khuyến mãi không tồn tại!");
            return "redirect:/khuyenmai/list"; // Chuyển hướng đến danh sách KhuyenMai nếu không tìm thấy
        }
    }
    // Hiển thị form để thêm mới KhuyenMai
    @GetMapping("/add")
    public String addKhuyenMaiForm(Model model) {
        model.addAttribute("khuyenmai", new KhuyenMai());
        return "khuyenmai/add"; // Trả về view để thêm mới KhuyenMai
    }

    @PostMapping("/add")
    public String addKhuyenMaiSubmit(@Valid @ModelAttribute("khuyenmai") KhuyenMai khuyenMai,
                                     BindingResult bindingResult,
                                     @RequestParam("image") MultipartFile image,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "khuyenmai/add"; // Trả về view với lỗi xác thực
        }

        try {
            if (!image.isEmpty()) {
                String imageName = handleImage.saveImage(image);
                khuyenMai.setHinhAnhKhuyenMai(imageName);
            }
            khuyenMaiService.createKhuyenMai(khuyenMai);
            redirectAttributes.addFlashAttribute("message", "Khuyến mãi thêm thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu hình ảnh: " + e.getMessage());
            return "redirect:/khuyenmai/add";
        }

        return "redirect:/khuyenmai/list"; // Chuyển hướng đến danh sách KhuyenMai
    }
    // Xử lý khi submit form thêm mới KhuyenMai
//    @PostMapping("/add")
//    public String addKhuyenMaiSubmit(@Valid @ModelAttribute("khuyenmai") KhuyenMai khuyenMai, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//       try{
//           khuyenMaiService.createKhuyenMai(khuyenMai);
//           return "redirect:/khuyenmai/list"; // Chuyển hướng đến danh sách KhuyenMai
//       }catch (Exception e){
//           System.out.println(e.getMessage());
//           return "redirect:/khuyenmai/list"; // Chuyển hướng đến danh sách KhuyenMai
//       }

//        if (bindingResult.hasErrors()) {
//            return "khuyenmai/add"; // Trả về view với lỗi xác thực
//        }
//        khuyenMaiService.createKhuyenMai(khuyenMai);
//        redirectAttributes.addFlashAttribute("message", "Khuyến mãi thêm thành công!");



//
    // Hiển thị form để chỉnh sửa KhuyenMai hiện có

    @GetMapping("/edit/{id}")
    public String editKhuyenMaiForm(@PathVariable Integer id, Model model) {
        KhuyenMai khuyenMai = khuyenMaiService.getKhuyenMaiById(id)
                .orElseThrow(() -> new IllegalArgumentException("KhuyenMai not found" + id));
        model.addAttribute("khuyenmai", khuyenMai);
        return "khuyenmai/edit";
    }
    @PostMapping("/edit/{id}")
    public String editKhuyenMaiSubmit(@PathVariable Integer id, @Valid @ModelAttribute("khuyenmai") KhuyenMai khuyenMai,
                                      BindingResult bindingResult, @RequestParam("image") MultipartFile image,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "khuyenmai/edit";
        }

        try {
            // Lấy đối tượng KhuyenMai hiện có
            Optional<KhuyenMai> existingKhuyenMaiOptional = khuyenMaiService.getKhuyenMaiById(id);
            if (!existingKhuyenMaiOptional.isPresent()) {
                throw new IllegalArgumentException("KhuyenMai không tìm thấy: " + id);
            }
            KhuyenMai existingKhuyenMai = existingKhuyenMaiOptional.get();

            // Cập nhật hình ảnh nếu có file mới
            if (!image.isEmpty()) {
                String imageName = handleImage.saveImage(image);
                existingKhuyenMai.setHinhAnhKhuyenMai(imageName);
            }

            // Cập nhật các thuộc tính khác của KhuyenMai
            existingKhuyenMai.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
            existingKhuyenMai.setCode(khuyenMai.getCode());
            existingKhuyenMai.setNoiDungKhuyenMai(khuyenMai.getNoiDungKhuyenMai());
            existingKhuyenMai.setPhanTramKhuyenMai(khuyenMai.getPhanTramKhuyenMai());
            existingKhuyenMai.setNgayKhuyenMai(khuyenMai.getNgayKhuyenMai());
            existingKhuyenMai.setNgayKetThuc(khuyenMai.getNgayKetThuc());

            khuyenMaiService.updateKhuyenMai(existingKhuyenMai);
            redirectAttributes.addFlashAttribute("message", "Khuyến mãi cập nhật thành công!");

        } catch (IOException e) {
            bindingResult.rejectValue("hinhAnhKhuyenMai", "error.khuyenMai", "Lưu hình ảnh thất bại: " + e.getMessage());
            return "khuyenmai/edit";
        }

        return "redirect:/khuyenmai/list";
    }
    // Xử lý xóa KhuyenMai hiện có
    @GetMapping("/delete/{id}")
    public String deleteKhuyenMai(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        khuyenMaiService.deleteKhuyenMai(id);
        redirectAttributes.addFlashAttribute("message", "Khuyến mãi xóa thành công!");
        return "redirect:/khuyenmai/list"; // Chuyển hướng đến danh sách KhuyenMai
    }
}
