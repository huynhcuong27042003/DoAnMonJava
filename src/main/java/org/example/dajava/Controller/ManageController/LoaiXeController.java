package org.example.dajava.Controller.ManageController;
import org.example.dajava.Model.LoaiXe;
import org.example.dajava.Service.LoaiXeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/loaixe")
@RequiredArgsConstructor

public class LoaiXeController {
    private final LoaiXeService loaiXeService;

    @GetMapping("/list")
    public String getAllLoaiXe(Model model) {
        List<LoaiXe> list = loaiXeService.getAllLoaiXe();
        model.addAttribute("loaiXeList", list);
        return "loaixe/list";
    }

    @GetMapping("/add")
    public String addLoaiXeForm(Model model) {
        model.addAttribute("loaiXe", new LoaiXe());
        return "loaixe/add";
    }

    @PostMapping("/add")
    public String addLoaiXeSubmit(@ModelAttribute("loaiXe") @Valid LoaiXe loaiXe, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "loaixe/add";
        }
        loaiXeService.createLoaiXe(loaiXe);
        redirectAttributes.addFlashAttribute("message", "Đã thêm mới loại xe thành công!");
        return "redirect:/loaixe/list";
    }

    @GetMapping("/edit/{id}")
    public String editLoaiXeForm(@PathVariable("id") Integer id, Model model) {
        Optional<LoaiXe> loaiXe = loaiXeService.getLoaiXeById(id);
        loaiXe.ifPresent(lx -> model.addAttribute("loaiXe", lx));
        return "loaixe/edit";
    }

    @PostMapping("/edit/{id}")
    public String editLoaiXeSubmit(@PathVariable("id") Integer id, @ModelAttribute("loaiXe") @Valid LoaiXe loaiXe,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "loaixe/edit";
        }
        Optional<LoaiXe> updatedLoaiXe = loaiXeService.updateLoaiXe(id, loaiXe);
        if (updatedLoaiXe.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Đã cập nhật loại xe thành công!");
            return "redirect:/loaixe/list";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy loại xe để cập nhật!");
            return "redirect:/loaixe/edit/" + id; // handle not found case
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteLoaiXe(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        loaiXeService.deleteLoaiXe(id);
        redirectAttributes.addFlashAttribute("message", "Đã xóa loại xe thành công!");
        return "redirect:/loaixe/list";
    }
}
