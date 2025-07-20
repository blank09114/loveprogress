package packages.loveprogress.api;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.CommentDTO;
import packages.loveprogress.service.CommentService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApi
{
    @Resource
    private CommentService commentService;

    // 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getComments(@RequestParam Long postId, Principal principal)
    {
        String loginUserId = (principal != null) ? principal.getName() : null;
        List<CommentDTO> list = commentService.getComments(postId, loginUserId);
        return ResponseEntity.ok(list);
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<?> writeComment(@RequestBody CommentDTO commentDTO, Principal principal)
    {
        commentDTO.setUserId(principal.getName());
        commentService.writeComment(commentDTO);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId)
    {
        int result = commentService.deleteComment(commentId);
        return result > 0 ? ResponseEntity.ok("삭제 완료") : ResponseEntity.status(500).body("삭제 실패");
    }

    // 댓글 수정
    @PatchMapping("/{commentId}/edit")
    public ResponseEntity<?> updateComment
    (@PathVariable Long commentId, @RequestBody CommentDTO commentDTO, Principal principal)
    {
        commentDTO.setCommentId(commentId);
        commentDTO.setUserId(principal.getName());
        int result = commentService.updateComment(commentDTO);
        return result > 0 ? ResponseEntity.ok("수정 완료") : ResponseEntity.status(500).body("수정 실패");
    }
}
