package packages.loveprogress.controller;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import packages.loveprogress.dto.UserDTO;
import packages.loveprogress.service.UserService;

@Controller
public class UserController
{
    @Resource
    private UserService userService;

    // 회원가입 폼
    @GetMapping("/signup")
    public String signup() { return "main/signup"; }

    // 회원가입 요청
    @PostMapping("/signup/request")
    public String signupRequest(@ModelAttribute UserDTO dto, RedirectAttributes redirectAttributes)
    {
        try
        {
            userService.signup(dto);
            redirectAttributes.addFlashAttribute("message", "회원가입 성공");
            return "redirect:/login";
        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("error", "회원가입 실패: " + e.getMessage());
            return "redirect:/signup";
        }
    }

    // 로그인 폼 (로그인 실패 시 메시지 출력)
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model)
    {
        if (error != null) { model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다."); }
        return "main/login";
    }

    // 비밀번호 변경 폼
    @GetMapping("/pwChange")
    public String pwChange() { return "main/pwChange"; }
}