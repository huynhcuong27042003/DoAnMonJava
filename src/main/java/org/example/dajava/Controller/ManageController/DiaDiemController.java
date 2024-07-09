package org.example.dajava.Controller.ManageController;
import org.example.dajava.Model.DiaDiem;
import org.example.dajava.Service.DiaDiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/diadiem")
@RequiredArgsConstructor
public class DiaDiemController {
    private final DiaDiemService diaDiemService;
    @GetMapping("/list")
    public String listDiaDiem(Model model) {
        List<DiaDiem> diaDiems = diaDiemService.getAllDiaDiem();
        model.addAttribute("diaDiems", diaDiems);
        return "diadiem/list";
    }


    @GetMapping("/add")
    public String addDiaDiemForm(Model model) {
        model.addAttribute("diaDiem", new DiaDiem());
        return "diadiem/add";
    }

    @PostMapping("/add")
    public String addDiaDiemSubmit(@Valid @ModelAttribute("diaDiem") DiaDiem diaDiem,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "diadiem/add";
        }
        diaDiemService.addDiaDiem(diaDiem);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm địa điểm thành công");
        return "redirect:/diadiem/list";
    }

    @GetMapping("/edit/{id}")
    public String editDiaDiemForm(@PathVariable("id") Integer id, Model model) {
        DiaDiem diaDiem = diaDiemService.getDiaDiemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid địa điểm ID: " + id));
        model.addAttribute("diaDiem", diaDiem);
        return "diadiem/edit";
    }

    // Xử lý submit form sửa địa điểm
    @PostMapping("/edit/{id}")
    public String editDiaDiemSubmit(@PathVariable("id") Integer id,
                                    @Valid @ModelAttribute("diaDiem") DiaDiem diaDiem,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "diadiem/edit";
        }

        diaDiem.setMaDiaDiem(id);
        diaDiemService.editDiaDiem(diaDiem);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa địa điểm thành công");
        return "redirect:/diadiem/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteDiaDiem(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        diaDiemService.deleteDiaDiem(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa địa điểm thành công");
        return "redirect:/diadiem/list";
    }


}
