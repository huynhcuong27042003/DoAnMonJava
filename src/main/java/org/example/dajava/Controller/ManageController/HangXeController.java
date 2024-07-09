package org.example.dajava.Controller.ManageController;

import org.example.dajava.Model.HangXe;
import org.example.dajava.Service.HangXeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hangxe")
@RequiredArgsConstructor
public class HangXeController {
    private final HangXeService hangXeService;

    @GetMapping("/list")
    public String getAllHangXe(Model model) {
        List<HangXe> hangXes = hangXeService.getAllHangXe();
        model.addAttribute("hangXes", hangXes);
        return "hangxe/list"; // Thymeleaf view template name
    }

    @GetMapping("/add")
    public String addHangXeForm(Model model) {
        model.addAttribute("hangXe", new HangXe());
        return "hangxe/add"; // Thymeleaf view template name for add/edit form
    }

    @PostMapping("/add")
    public String addHangXeSubmit(@Valid HangXe hangXe, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "hangxe/add"; // Return to add/edit form with validation errors
        }
        hangXeService.createHangXe(hangXe);
        redirectAttributes.addFlashAttribute("message", "HangXe added successfully!");
        return "redirect:/hangxe/list"; // Redirect to list page
    }

    @GetMapping("/edit/{id}")
    public String editHangXeForm(@PathVariable("id") Integer id, Model model) {
        Optional<HangXe> hangXe = hangXeService.getHangXeById(id);
        if (hangXe.isPresent()) {
            model.addAttribute("hangXe", hangXe.get());
            return "hangxe/edit"; // Thymeleaf view template name for add/edit form
        } else {
            // Handle case where HangXe with given id is not found
            return "redirect:/hangxe/list"; // Redirect to list page
        }
    }

    @PostMapping("/edit/{id}")
    public String editHangXeSubmit(@PathVariable("id") Integer id, @Valid HangXe hangXe, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "hangxe/edit"; // Return to add/edit form with validation errors
        }
        Optional<HangXe> updatedHangXe = hangXeService.updateHangXe(id, hangXe);
        if (updatedHangXe.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "HangXe updated successfully!");
        } else {
            // Handle case where HangXe with given id is not found
            redirectAttributes.addFlashAttribute("error", "Failed to update HangXe. HangXe not found.");
        }
        return "redirect:/hangxe/list"; // Redirect to list page
    }

    @GetMapping("/delete/{id}")
    public String deleteHangXe(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        hangXeService.deleteHangXe(id);
        redirectAttributes.addFlashAttribute("message", "HangXe deleted successfully!");
        return "redirect:/hangxe/list"; // Redirect to list page
    }

}


