package org.example.dajava.Controller.ManageController;

import lombok.RequiredArgsConstructor;
import org.example.dajava.Model.PhuongThucThanhToan;
import org.example.dajava.Service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/phuongthucthanhtoan")
@RequiredArgsConstructor
public class PhuongThucThanhToanController {
    @Autowired
    private final PhuongThucThanhToanService phuongThucThanhToanServiceservice;
    @Autowired
    private PhuongThucThanhToanService phuongThucThanhToanService;

    @GetMapping("/list")
    public String findAll(Model model) {
        model.addAttribute("phuongthucthanhtoan", phuongThucThanhToanServiceservice.findAll());
        return "/phuongthucthanhtoan/list";
    }

    @GetMapping("/add")
    public String ThanhToanForm(Model model) {
        model.addAttribute("phuongthucthanhtoan", new PhuongThucThanhToan());
        return "/phuongthucthanhtoan/add";
    }

    @PostMapping("/add")
    public String createThanhToan(@Valid PhuongThucThanhToan phuongThucThanhToan, BindingResult result) {
        if (result.hasErrors()) {
            return "/phuongthucthanhtoan/add";
        }
        phuongThucThanhToanService.createThanhToan(phuongThucThanhToan);
        return "redirect:/phuongthucthanhtoan/list";

    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Optional<PhuongThucThanhToan> optionalPhuongThucThanhToan = phuongThucThanhToanService.findById(id);
        if (optionalPhuongThucThanhToan.isPresent()) {
            model.addAttribute("phuongthucthanhtoan", optionalPhuongThucThanhToan.get());
            return "/phuongthucthanhtoan/edit";
        } else {
            return "redirect:/phuongthucthanhtoan/list";
        }
    }

    @PostMapping("/edit")
    public String editSubmit(@Valid @ModelAttribute("phuongthucthanhtoan") PhuongThucThanhToan phuongThucThanhToan, BindingResult result) {
        if (result.hasErrors()) {
            return "/phuongthucthanhtoan/edit";
        }
        phuongThucThanhToanService.updateThanhToan(phuongThucThanhToan);
        return "redirect:/phuongthucthanhtoan/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        phuongThucThanhToanService.deleteById(id);
        return "redirect:/phuongthucthanhtoan/list";
    }

}