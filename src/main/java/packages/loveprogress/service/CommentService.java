package packages.loveprogress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.CommentDTO;
import packages.loveprogress.mapper.CommentMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService
{
    private final CommentMapper commentMapper;

    // 댓글 조회
    public List<CommentDTO> getComments(Long postId, String loginUserId)
    {
        List<CommentDTO> list = commentMapper.selectComments(postId);
        for (CommentDTO dto : list) {
            dto.setWriterName(dto.getUserRole().equals("ADMIN") ? "교무처장" : dto.getUserId());
            dto.setOwner(loginUserId != null && loginUserId.equals(dto.getUserId()));
        }
        return list;
    }

    // 댓글 등록
    public void writeComment(CommentDTO commentDTO) { commentMapper.insertComment(commentDTO); }

    // 댓글 삭제
    public int deleteComment(Long commentId) { return commentMapper.deleteComment(commentId); }

    // 댓글 수정
    public int updateComment(CommentDTO commentDTO) { return commentMapper.updateComment(commentDTO); }
}
