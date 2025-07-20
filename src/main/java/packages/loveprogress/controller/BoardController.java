package packages.loveprogress.controller;

import jakarta.annotation.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import packages.loveprogress.dto.PostDTO;
import packages.loveprogress.service.BoardService;

import java.security.Principal;

@Controller
@RequestMapping("/board")
public class BoardController
{
    @Resource
    private BoardService boardService;

    // 게시글 목록
    @GetMapping("/{type:(?i)info|event|booth}")
    public String boardList(@PathVariable String type, Model model)
    {
        String upperType = type.toUpperCase();

        PostDTO noticePost = boardService.getNoticePost(upperType);
        model.addAttribute("noticePost", noticePost);

        model.addAttribute("boardName", getBoardName(upperType));
        model.addAttribute("boardType", type.toLowerCase());
        model.addAttribute("postType", upperType);
        return "board/boardList";
    }

    // 게시글 작성 폼
    @GetMapping("/{type:(?i)info|event|booth}/write")
    public String writePost(@PathVariable String type, Model model)
    {
        String upperType = type.toUpperCase();  // 동일
        model.addAttribute("boardName", getBoardName(upperType));
        model.addAttribute("boardType", type.toLowerCase());
        model.addAttribute("postType", upperType);
        model.addAttribute("isEdit", false);
        return "board/writeForm";
    }

    // 게시글 수정
    @GetMapping("/{postType}/edit/{postId}")
    public String editPost
    (@PathVariable String postType, @PathVariable Long postId,
    Model model, Principal principal)
    {
        PostDTO post = boardService.getPostDetail(postType, postId);

        // 작성자 본인인지 확인
        if (!principal.getName().equals(post.getUserId())) { throw new AccessDeniedException("게시글 수정 권한 없음"); }

        model.addAttribute("post", post);
        model.addAttribute("boardName", getBoardName(postType));
        model.addAttribute("boardType", postType.toLowerCase());
        model.addAttribute("postType", postType);
        model.addAttribute("isEdit", true);
        return "board/writeForm";
    }

    // 게시글 상세
    @GetMapping("/{postType}/{postId}")
    public String boardDetail
    (@PathVariable String postType, @PathVariable Long postId,
    Model model, Principal principal)
    {
        PostDTO post = boardService.getPostDetail(postType, postId);
        String writerName = post.getUserRole().equals("ADMIN") ? "교무처장" : post.getUserId();

        String loginUserId = (principal != null) ? principal.getName() : null;
        boolean isAuthor = loginUserId != null && loginUserId.equals(post.getUserId());

        // 비밀글 접근 제한
        if (post.isPostSecret())
        {
            if (principal == null || (!isAuthor && !hasAdminRole(principal)))
            { throw new AccessDeniedException("비밀글 접근 권한 없음"); }
        }

        // 이전/다음글 정보 추가
        PostDTO prevPost = boardService.getPrevPost(postType, postId);
        PostDTO nextPost = boardService.getNextPost(postType, postId);

        model.addAttribute("post", post);
        model.addAttribute("writerName", writerName);
        model.addAttribute("boardName", getBoardName(postType));
        model.addAttribute("boardType", postType.toLowerCase());
        model.addAttribute("postType", postType);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("prevPost", prevPost);
        model.addAttribute("nextPost", nextPost);

        return "board/boardDetail";
    }

    // 비밀글 접근 제한
    private boolean hasAdminRole(Principal principal)
    {
        if (principal == null) return false; // 🔥 null일 경우 바로 false
        if (principal instanceof UsernamePasswordAuthenticationToken auth)
        {
            return auth.getAuthorities().stream()
                    .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    // 게시판 이름
    private String getBoardName(String postType)
    {
        return switch (postType.toUpperCase()) {
            case "INFO" -> "INFORMATION";
            case "EVENT" -> "EVENT";
            case "BOOTH" -> "♥";
            default -> "BOARD";
        };
    }
}