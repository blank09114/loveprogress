package packages.loveprogress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/forbidden")
    public String forbidden() { return "error/403"; }
}