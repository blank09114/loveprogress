package packages.loveprogress.controller;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.UserDTO;
import packages.loveprogress.service.AdminService;
import packages.loveprogress.service.MainService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController
{
    @Resource
    private MainService mainService;

    @Resource
    private AdminService adminService;

    // 메인
    @GetMapping("/main")
    public String adminMain(Model model)
    {
        model.addAttribute("banners", mainService.getAllBanners());
        model.addAttribute("mainLinks", adminService.getAllMainLinks());
        return "admin/adminMain";
    }

    // 게시판
    @GetMapping("/board")
    public String adminBoard(Model model)
    {
        model.addAttribute("infoPosts", adminService.getPostsByType("INFO"));
        model.addAttribute("eventPosts", adminService.getPostsByType("EVENT"));
        model.addAttribute("boothPosts", adminService.getPostsByType("BOOTH"));

        model.addAttribute("infoNoticeId", adminService.getNoticePostId("INFO"));
        model.addAttribute("eventNoticeId", adminService.getNoticePostId("EVENT"));
        model.addAttribute("boothNoticeId", adminService.getNoticePostId("BOOTH"));

        return "admin/adminBoard";
    }

    // 공지 등록
    @PostMapping("/notice")
    public String updateNotice
    (@RequestParam("type") String type, @RequestParam("postId") Long postId)
    {
        adminService.updateNotice(type, postId);
        return "redirect:/admin/board";
    }

    // 부스
    @GetMapping("/booth")
    public String adminBooth(Model model)
    {
        model.addAttribute("boothList", adminService.getAllBooths());
        return "admin/adminBooth";
    }

    // QnA
    @GetMapping("/qna")
    public String adminQna()
    {
        return "admin/adminQna";
    }

    // 회원 관리
    @GetMapping("/user")
    public String manageUsers(Model model)
    {
        List<UserDTO> userList = adminService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin/adminUser";
    }
}