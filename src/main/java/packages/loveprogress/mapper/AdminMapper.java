package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import packages.loveprogress.dto.*;

import java.util.List;

@Mapper
public interface AdminMapper
{
    // 배너 수정
    void updateBanner(BannerDTO banner);

    // 배너 초기화
    void clearBanner(int num);

    // 링크 정보 불러오기
    List<MainLinkDTO> getAllMainLinks();

    // 링크 연결 수정
    void updateMainLink(MainLinkDTO link);

    // 링크 연결 초기화
    void clearMainLink(int id);

    // 전체 게시글 조회
    List<PostDTO> getPostsByType(@Param("type") String type);

    // 현 공지글 조회
    Long getNoticePostId(@Param("postType") String postType);

    // 공지 등록
    void updateNotice(@Param("postType") String postType, @Param("postId") Long postId);

    // 부스 조회
    List<BoothDTO> getAllBooths();

    // 부스 등록
    void insertBooth(BoothDTO booth);
    void insertBoothTagMap(@Param("boothId") int boothId, @Param("tagIds") List<Integer> tagIds);

    // 부스 수정
    void updateBooth(BoothDTO booth);

    // 부스 태그 삭제
    void deleteBoothTags(int boothId);

    // 부스 삭제
    void deleteBooth(@Param("boothId") int boothId);

    // 회원 조회
    List<UserDTO> selectAllUsers();

    // 부스 회원 등록
    void promoteUser(UserDTO user);

    // 전체 질문 조회
    List<QnaDTO> getAllQna();

    // 질문 삭제
    void deleteQna(int qnaId);

    // 답변 등록/수정
    void updateAnswer(@Param("qnaId") int qnaId, @Param("answerContent") String answerContent);

    // 비밀번호 초기화
    void updateUserPassword(UserDTO user);
}