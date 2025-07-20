package packages.loveprogress.controller;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import packages.loveprogress.service.AboutService;

@Controller
@RequestMapping("/about")
public class AboutController {

    @Resource
    private AboutService aboutService;

    // 어바웃 페이지
    @GetMapping("/")
    public String about(Model model)
    {
        String content = aboutService.getContent();
        model.addAttribute("content", content);
        return "about/about";
    }

    // 수정 페이지
    @GetMapping("/edit")
    public String aboutEdit(Model model)
    {
        String content = aboutService.getContent();
        model.addAttribute("content", content);
        return "about/aboutEdit";
    }

    // 수정 요청
    @PostMapping("/edit/request")
    public String editRequest(@RequestParam("content") String content)
    {
        aboutService.updateContent(content);
        return "redirect:/about/";
    }
}