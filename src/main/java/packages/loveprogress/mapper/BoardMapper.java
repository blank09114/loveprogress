package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import packages.loveprogress.dto.PostDTO;

import java.util.List;

@Mapper
public interface BoardMapper
{
    // 게시글 목록 조회
    List<PostDTO> selectPostList(String postType);

    // 공지 조회
    PostDTO selectNoticePost(String postType);

    // 게시글 상세 조회
    PostDTO selectPostDetail(String postType, Long postId);

    // 이전글, 다음글
    PostDTO selectPrevPost(@Param("postType") String postType, @Param("postId") Long postId);
    PostDTO selectNextPost(@Param("postType") String postType, @Param("postId") Long postId);

    // 게시글 작성
    void insertPost(PostDTO post);

    // 게시글 수정
    int updatePost(PostDTO postDTO);

    // 게시글 삭제
    int deletePost(@Param("postType") String postType, @Param("postId") Long postId);
    String selectUserRole(String userId);
}
