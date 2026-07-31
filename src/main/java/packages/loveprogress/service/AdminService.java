package packages.loveprogress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packages.loveprogress.dto.*;
import packages.loveprogress.mapper.AdminMapper;
import packages.loveprogress.mapper.MainMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService
{
    private final MainMapper mainMapper;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    // 배너 조회
    public List<BannerDTO> getAllBanners() { return mainMapper.getAllBanners(); }

    // 배너 수정
    public void updateBanner(BannerDTO banner) { adminMapper.updateBanner(banner); }

    // 배너 초기화
    public void clearBanner(int num) { adminMapper.clearBanner(num); }

    // 링크 조회
    public List<MainLinkDTO> getAllMainLinks() { return adminMapper.getAllMainLinks(); }

    // 링크 수정
    public void updateMainLink(MainLinkDTO link) { adminMapper.updateMainLink(link); }

    // 링크 초기화
    public void clearMainLink(int id) { adminMapper.clearMainLink(id); }

    // 전체 게시글 조회
    public List<PostDTO> getPostsByType(String type) { return adminMapper.getPostsByType(type); }

    // 현재 공지글 조회
    public Long getNoticePostId(String postType) { return adminMapper.getNoticePostId(postType); }

    // 공지 등록
    @Transactional
    public void updateNotice(String postType, Long postId) { adminMapper.updateNotice(postType, postId); }

    // 부스 조회
    public List<BoothDTO> getAllBooths() { return adminMapper.getAllBooths(); }

    // 부스 등록
    @Transactional
    public void registerBooth(BoothDTO booth, List<Integer> tagIds)
    {
        // 부스 정보 삽입
        adminMapper.insertBooth(booth); // boothId 자동 생성됨

        // 태그 ID 리스트가 비어있지 않은 경우만 매핑
        if (tagIds != null && !tagIds.isEmpty()) { adminMapper.insertBoothTagMap(booth.getBoothId(), tagIds); }
    }

    // 부스 수정
    @Transactional
    public void updateBooth(BoothDTO booth, List<Integer> tagIds)
    {
        adminMapper.updateBooth(booth); // null 아닌 값만 동적 쿼리로 반영

        // 태그가 들어왔다면 기존 매핑 삭제 후 재삽입
        if (tagIds != null)
        {
            adminMapper.deleteBoothTags(booth.getBoothId());
            if (!tagIds.isEmpty()) { adminMapper.insertBoothTagMap(booth.getBoothId(), tagIds); }
        }
    }

    // 부스 삭제
    @Transactional
    public void deleteBooth(int boothId)
    {
        // 태그 맵핑 먼저 제거
        adminMapper.deleteBoothTags(boothId);

        // 부스 자체 삭제
        adminMapper.deleteBooth(boothId);
    }

    // 질문 조회
    public List<QnaDTO> getAllQna() { return adminMapper.getAllQna(); }

    // 질문 삭제
    @Transactional
    public void deleteQna(int qnaId) { adminMapper.deleteQna(qnaId); }

    // 답변 등록/수정
    @Transactional
    public void answerQna(int qnaId, String answerContent) { adminMapper.updateAnswer(qnaId, answerContent); }

    // 회원 조회
    public List<UserDTO> getAllUsers() { return adminMapper.selectAllUsers(); }

    // 부스 회원 등록
    public void promoteUser(String userId)
    {
        UserDTO user = UserDTO.builder().userId(userId).build();
        adminMapper.promoteUser(user);
    }

    // 비밀번호 초기화
    public void resetPW(String userId) {
        String encodedPw = passwordEncoder.encode("702430");

        UserDTO user = UserDTO.builder()
                .userId(userId)
                .userPw(encodedPw)
                .build();

        adminMapper.updateUserPassword(user);  // 쿼리에서 user_pw = #{userPw}
    }
}