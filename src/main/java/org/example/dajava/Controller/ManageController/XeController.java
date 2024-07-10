package org.example.dajava.Controller.ManageController;

import org.example.dajava.Model.Xe;
import org.example.dajava.Service.*;
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
@RequestMapping("/xe")
@RequiredArgsConstructor
public class XeController {

    @Autowired
    private final XeService xeService;
    @Autowired
    private final DiaDiemService diaDiemService;
    @Autowired
    private final HangXeService hangXeService;
    @Autowired
    private final LoaiXeService loaiXeService;
    @Autowired
    private final TaiKhoanService taiKhoanService;
    @Autowired
    private final HandleImage handleImage;
    @GetMapping("/list")
    public String getAllNotHiddenXe(Model model) {
        List<Xe> xeList = xeService.getAllNotHiddenXe();
        model.addAttribute("xeList", xeList);
        model.addAttribute("hangxe", hangXeService.getAllHangXe());
        model.addAttribute("loaixe", loaiXeService.getAllLoaiXe());
        model.addAttribute("diadiem", diaDiemService.getAllDiaDiem());
        return "xe/list";
    }
    @GetMapping("/list-hide")
    public String getAllHiddenXe(Model model) {
        List<Xe> xeList = xeService.getAllHiddenXe();
        model.addAttribute("xeList", xeList);
        model.addAttribute("hangxe", hangXeService.getAllHangXe());
        model.addAttribute("loaixe", loaiXeService.getAllLoaiXe());
        model.addAttribute("diadiem", diaDiemService.getAllDiaDiem());
        return "xe/list-hide"; // Return the view for displaying all hidden Xe
    }
    @PostMapping("/acceptxe/{id}")
    public String acceptXe(@PathVariable String id) {
        Xe xeexsiting = xeService.findXeByBienSoXe(id);
        if (xeexsiting != null) {
            xeService.publicXe(xeexsiting);
        }
        return "redirect:/xe/list-hide";
    }
    @GetMapping("/add")
    public String addXeForm(Model model) {
        model.addAttribute("xe", new Xe());
        model.addAttribute("hangxe", hangXeService.getAllHangXe());
        model.addAttribute("loaixe", loaiXeService.getAllLoaiXe());
        model.addAttribute("diadiem", diaDiemService.getAllDiaDiem());
        return "xe/add"; // Return the view for adding a new Xe
    }
    @PostMapping("/add")
    public String addXeSubmit(@Valid @ModelAttribute("xe") Xe xe, BindingResult bindingResult, @RequestParam("imageFile1") MultipartFile imageFile1,
                              @RequestParam("imageFile2") MultipartFile imageFile2, @RequestParam("imageFile3") MultipartFile imageFile3,
                              @RequestParam("imageFile4") MultipartFile imageFile4, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "xe/add"; // Return the view with validation errors
        }

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
            return "xe/add"; // Return the view with errors
        }

        xeService.createXe(xe);

        redirectAttributes.addFlashAttribute("message", "Xe added successfully!");
        return "redirect:/xe/list"; // Redirect to the list of Xe
    }

    @GetMapping("/detail/{bienSo}")
    public String getXeDetail(@PathVariable("bienSo") String bienSo, Model model) {
        // Lấy chi tiết xe từ service
        Xe xe = xeService.getDetailXe(bienSo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid xe BienSoXe:" + bienSo));

        // Thêm xe vào model để hiển thị trên view
        model.addAttribute("xe", xe);

        return "xe/detail"; // Trả về view để hiển thị chi tiết xe
    }

    @GetMapping("/edit/{bienSo}")
    public String editXeForm(@PathVariable String bienSo, Model model) {
        Xe xe = xeService.getXeByBienSo(bienSo)
                .orElseThrow(() -> new IllegalArgumentException("Xe not found" + bienSo));
        model.addAttribute("xe", xe);
        model.addAttribute("hangxe", hangXeService.getAllHangXe());
        model.addAttribute("loaixe", loaiXeService.getAllLoaiXe());
        model.addAttribute("diadiem", diaDiemService.getAllDiaDiem());
        return "xe/edit";

    }

    @PostMapping("/edit/{bienSo}")
    public String editXeSubmit(@PathVariable String bienSo, @Valid Xe xe, BindingResult bindingResult,
                               @RequestParam("imageFile1") MultipartFile imageFile1,
                               @RequestParam("imageFile2") MultipartFile imageFile2,
                               @RequestParam("imageFile3") MultipartFile imageFile3,
                               @RequestParam("imageFile4") MultipartFile imageFile4,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            xe.setBienSoXe(bienSo);
            return "xe/edit";
        }

        try {
            // Get the existing Xe object
            Optional<Xe> existingXeOptional = xeService.getXeByBienSo(bienSo);
            if (!existingXeOptional.isPresent()) {
                throw new IllegalArgumentException("Xe not found" + bienSo);
            }
            Xe existingXe = existingXeOptional.get();

            // Update images if new files are provided
            if (!imageFile1.isEmpty()) {
                String fileName1 = handleImage.saveImage(imageFile1);
                existingXe.setHinhAnh1(fileName1);
            }
            if (!imageFile2.isEmpty()) {
                String fileName2 = handleImage.saveImage(imageFile2);
                existingXe.setHinhAnh2(fileName2);
            }
            if (!imageFile3.isEmpty()) {
                String fileName3 = handleImage.saveImage(imageFile3);
                existingXe.setHinhAnh3(fileName3);
            }
            if (!imageFile4.isEmpty()) {
                String fileName4 = handleImage.saveImage(imageFile4);
                existingXe.setHinhAnh4(fileName4);
            }

            // Update other attributes of Xe
            existingXe.setTenXe(xe.getTenXe());
            existingXe.setNamSanXuat(xe.getNamSanXuat());
            existingXe.setGiaThue(xe.getGiaThue());
            existingXe.setNhienLieu(xe.getNhienLieu());
            existingXe.setLucPhanKhoi(xe.getLucPhanKhoi());
            existingXe.setMoTa(xe.getMoTa());
            existingXe.setTrangThai(xe.isTrangThai());
            existingXe.setHide(xe.isHide());
    //        existingXe.setTaiKhoan(xe.getTaiKhoan());

            xeService.updateXe(existingXe);

        } catch (IOException e) {
            bindingResult.rejectValue("hinhAnh", "error.xe", "Failed to save image: " + e.getMessage());
            return "xe/edit"; // Return the view with errors
        }

        redirectAttributes.addFlashAttribute("message", "Xe updated successfully!");
        return "redirect:/xe/list";
    }

    @GetMapping("/hide/{bienSo}")
    public String hideXe(@PathVariable String bienSo, RedirectAttributes redirectAttributes) {
        xeService.hideXe(bienSo);
        redirectAttributes.addFlashAttribute("message", "Xe hidden successfully!");
        return "redirect:/xe/list";
    }


}
