package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;
import packages.loveprogress.dto.CommentDTO;

import java.util.List;

@Mapper
public interface CommentMapper
{
    // 댓글 조회
    List<CommentDTO> selectComments(Long postId);

    // 댓글 등록
    void insertComment(CommentDTO commentDTO);

    // 댓글 삭제
    int deleteComment(Long commentId);

    // 댓글 수정
    int updateComment(CommentDTO commentDTO);
}