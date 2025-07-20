package packages.loveprogress.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import packages.loveprogress.service.MainService;

@Controller
@RequiredArgsConstructor
public class MainController
{
    private final MainService mainService;

    @GetMapping("/")
    public String main(Model model)
    {
        model.addAttribute("banners", mainService.getAllBanners());
        model.addAttribute("mainLinks", mainService.getMainLinks());
        return "main/main";
    }
}