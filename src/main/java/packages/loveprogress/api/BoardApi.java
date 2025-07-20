package packages.loveprogress.api;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.PostDTO;
import packages.loveprogress.service.BoardService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApi
{
    @Resource
    private BoardService boardService;

    // 게시글 작성
    @PostMapping("/{postType}/write")
    public ResponseEntity<?> writePost
    (@PathVariable String postType, @RequestParam String title, @RequestParam String content,
    @RequestParam(required = false, defaultValue = "false") boolean secret, Principal principal)
    {
        PostDTO post = new PostDTO();
        post.setPostType(postType.toUpperCase());
        post.setPostTitle(title);
        post.setPostContent(content);
        post.setPostSecret(secret);
        post.setUserId(principal.getName());

        boardService.writePost(post);

        return ResponseEntity.ok().build();
    }

    // 게시글 전체 보기
    @GetMapping("/{postType}/list")
    public List<PostDTO> getPostList(@PathVariable String postType) { return boardService.getPostList(postType.toUpperCase()); }

    // 게시글 수정
    @PostMapping("/{postType}/edit/{postId}")
    public ResponseEntity<?> editPost
    (@PathVariable String postType,
    @PathVariable Long postId,
    @RequestParam String title,
    @RequestParam String content,
    @RequestParam(required = false, defaultValue = "false") boolean secret,
    Principal principal)
    {
        // 기존 게시글 조회
        PostDTO existingPost = boardService.getPostDetail(postType.toUpperCase(), postId);

        // 작성자 확인
        if (!principal.getName().equals(existingPost.getUserId())) { return ResponseEntity.status(403).body("수정 권한이 없습니다."); }

        // 수정할 내용 세팅
        PostDTO post = new PostDTO();
        post.setPostId(postId);
        post.setPostType(postType.toUpperCase());
        post.setPostTitle(title);
        post.setPostContent(content);
        post.setPostSecret(secret);

        boardService.updatePost(post);

        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{postType}/delete/{postId}")
    public ResponseEntity<?> deletePost
    (@PathVariable String postType, @PathVariable Long postId, Principal principal)
    {
        String userId = principal.getName();
        String role = boardService.getUserRole(userId);

        if (!"ADMIN".equals(role)) { return ResponseEntity.status(403).body("관리자만 삭제할 수 있습니다."); }

        int result = boardService.deletePost(postType.toUpperCase(), postId);
        return result > 0 ? ResponseEntity.ok("삭제 완료") : ResponseEntity.status(500).body("삭제 실패");
    }
}
