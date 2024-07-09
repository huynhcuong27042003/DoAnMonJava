package org.example.dajava.Controller.ManageController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chucvu")
@RequiredArgsConstructor
public class ChucVuController {
    /*private final ChucVuService chucVuService;

    @GetMapping("/list")
    public String getAllChucVu(Model model) {
        List<ChucVu> chucVuList = chucVuService.getAllChucVu();
        model.addAttribute("chucVuList", chucVuList);
        return "chucvu/list"; // Thymeleaf template name
    }

    @GetMapping("/add")
    public String addChucVuForm(Model model) {
        model.addAttribute("chucVu", new ChucVu());
        return "chucvu/add"; // Thymeleaf template name for add/edit form
    }

    @PostMapping("/add")
    public String addChucVuSubmit(@ModelAttribute @Valid ChucVu chucVu,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chucvu/add"; // Thymeleaf template name for add/edit form
        }
        ChucVu savedChucVu = chucVuService.addChucVu(chucVu);
        redirectAttributes.addFlashAttribute("message", "Chức vụ đã được thêm thành công.");
        return "redirect:/chucvu/list";
    }

    @GetMapping("/edit/{id}")
    public String editChucVuForm(@PathVariable Integer id, Model model) {
        Optional<ChucVu> chucVu = chucVuService.getChucVuById(id);
        if (chucVu.isPresent()) {
            model.addAttribute("chucVu", chucVu.get());
            return "chucvu/edit"; // Thymeleaf template name for add/edit form
        } else {
            return "redirect:/chucvu/list";
        }
    }

    @PostMapping("/edit")
    public String editChucVuSubmit(@ModelAttribute @Valid ChucVu chucVu,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chucvu/edit";
        }
        ChucVu updatedChucVu = chucVuService.editChucVu(chucVu);
        redirectAttributes.addFlashAttribute("message", "Chức vụ đã được cập nhật thành công.");
        return "redirect:/chucvu/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteChucVu(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        chucVuService.deleteChucVu(id);
        redirectAttributes.addFlashAttribute("message", "Chức vụ đã được xóa thành công.");
        return "redirect:/chucvu/list";
    }*/
}
