package packages.loveprogress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.PostDTO;
import packages.loveprogress.mapper.BoardMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService
{
    private final BoardMapper boardMapper;

    // 게시글 목록 조회
    public List<PostDTO> getPostList(String postType) { return boardMapper.selectPostList(postType); }

    // 공지 조회
    public PostDTO getNoticePost(String postType) { return boardMapper.selectNoticePost(postType); }

    // 게시글 상세 조회
    public PostDTO getPostDetail(String postType, Long postId) { return boardMapper.selectPostDetail(postType.toUpperCase(), postId); }

    // 이전글 조회
    public PostDTO getPrevPost(String postType, Long postId) { return boardMapper.selectPrevPost(postType.toUpperCase(), postId); }

    // 다음글 조회
    public PostDTO getNextPost(String postType, Long postId) { return boardMapper.selectNextPost(postType.toUpperCase(), postId); }

    // 게시글 작성
    public void writePost(PostDTO post) { boardMapper.insertPost(post); }

    // 게시글 수정
    public void updatePost(PostDTO post) { boardMapper.updatePost(post); }

    // 게시글 삭제
    public int deletePost(String postType, Long postId) { return boardMapper.deletePost(postType, postId); }

    // 유저 role 조회
    public String getUserRole(String userId) { return boardMapper.selectUserRole(userId); }
}
