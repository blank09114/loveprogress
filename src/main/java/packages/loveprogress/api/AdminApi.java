package packages.loveprogress.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.BannerDTO;
import packages.loveprogress.dto.BoothDTO;
import packages.loveprogress.dto.MainLinkDTO;
import packages.loveprogress.dto.QnaDTO;
import packages.loveprogress.service.AdminService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApi
{
    private final AdminService adminService;

    // 배너 수정
    @PostMapping("/banner")
    public ResponseEntity<?> updateBanner(@RequestParam Map<String, String> param)
    {
        BannerDTO dto = new BannerDTO();
        dto.setNum(Integer.parseInt(param.get("num")));

        if (param.containsKey("img")) dto.setImg(param.get("img"));
        if (param.containsKey("color")) dto.setColor(param.get("color"));
        if (param.containsKey("url")) dto.setUrl(param.get("url"));

        adminService.updateBanner(dto);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 배너 초기화
    @PostMapping("/banner/clear")
    public ResponseEntity<?> clearBanner(@RequestParam("num") int num)
    {
        adminService.clearBanner(num);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 링크 연결
    @PostMapping("/link")
    public ResponseEntity<?> updateMainLink(@RequestParam int id, @RequestParam String url)
    {
        adminService.updateMainLink(new MainLinkDTO(id, url));
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 링크 초기화
    @PostMapping("/link/clear")
    public ResponseEntity<?> clearMainLink(@RequestParam int id)
    {
        adminService.clearMainLink(id);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 부스 등록
    @PostMapping("/booth/register")
    public ResponseEntity<?> registerBooth(@RequestBody Map<String, Object> payload)
    {
        // 1. 부스 정보 파싱
        Map<String, Object> boothData = (Map<String, Object>) payload.get("booth");
        BoothDTO booth = new BoothDTO();
        booth.setName((String) boothData.get("name"));
        booth.setImg((String) boothData.get("img"));
        booth.setHoverImg((String) boothData.get("hoverImg"));
        booth.setPw((String) boothData.get("pw"));
        booth.setOwner((String) boothData.get("owner"));
        booth.setOwnerLink((String) boothData.get("ownerLink"));
        booth.setUser1((String) boothData.get("user1"));
        booth.setUser1Link((String) boothData.get("user1Link"));
        booth.setUser2((String) boothData.get("user2"));
        booth.setUser2Link((String) boothData.get("user2Link"));
        booth.setUser3((String) boothData.get("user3"));
        booth.setUser3Link((String) boothData.get("user3Link"));

        // 2. 태그 리스트 파싱
        List<Integer> tagList = (List<Integer>) payload.get("tagList");

        // 3. 서비스 호출
        adminService.registerBooth(booth, tagList);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 부스 수정
    @PostMapping("/booth/update")
    public ResponseEntity<?> updateBooth(@RequestBody Map<String, Object> payload)
    {
        // 기존 부스 정보 파싱
        Map<String, Object> boothData = (Map<String, Object>) payload.get("booth");
        List<Integer> tagList = (List<Integer>) payload.get("tagList");

        BoothDTO booth = new BoothDTO();

        // 강제 캐스팅 오류 방지 (Object → Number → int)
        Object boothIdObj = boothData.get("boothId");
        if (boothIdObj instanceof Number) { booth.setBoothId(((Number) boothIdObj).intValue()); }
        else if (boothIdObj instanceof String) { booth.setBoothId(Integer.parseInt((String) boothIdObj)); }
        else { return ResponseEntity.badRequest().body(Map.of("result", "fail", "reason", "invalid boothId")); }

        // 선택적 필드 업데이트 (프론트에서 변경된 필드만 보내준다는 전제)
        if (boothData.containsKey("name")) booth.setName((String) boothData.get("name"));
        if (boothData.containsKey("img")) booth.setImg((String) boothData.get("img"));
        if (boothData.containsKey("hoverImg")) booth.setHoverImg((String) boothData.get("hoverImg"));
        if (boothData.containsKey("pw")) booth.setPw((String) boothData.get("pw"));
        if (boothData.containsKey("owner")) booth.setOwner((String) boothData.get("owner"));
        if (boothData.containsKey("ownerLink")) booth.setOwnerLink((String) boothData.get("ownerLink"));
        if (boothData.containsKey("user1")) booth.setUser1((String) boothData.get("user1"));
        if (boothData.containsKey("user1Link")) booth.setUser1Link((String) boothData.get("user1Link"));
        if (boothData.containsKey("user2")) booth.setUser2((String) boothData.get("user2"));
        if (boothData.containsKey("user2Link")) booth.setUser2Link((String) boothData.get("user2Link"));
        if (boothData.containsKey("user3")) booth.setUser3((String) boothData.get("user3"));
        if (boothData.containsKey("user3Link")) booth.setUser3Link((String) boothData.get("user3Link"));

        adminService.updateBooth(booth, tagList);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 부스 삭제
    @PostMapping("/booth/delete")
    public ResponseEntity<?> deleteBooth(@RequestBody Map<String, Object> payload)
    {
        Integer boothId = (Integer) payload.get("boothId");
        adminService.deleteBooth(boothId);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 질문 조회
    @GetMapping("/qna/list")
    public ResponseEntity<?> getAllQna()
    {
        List<QnaDTO> qnaList = adminService.getAllQna();
        return ResponseEntity.ok().body(qnaList);
    }

    // 질문 삭제
    @DeleteMapping("/qna/{qnaId}")
    public ResponseEntity<?> deleteQna(@PathVariable int qnaId)
    {
        adminService.deleteQna(qnaId);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 답변 등록/수정
    @PostMapping("/qna/answer")
    public ResponseEntity<?> answerQna(@RequestBody Map<String, Object> payload)
    {
        Integer qnaId = (Integer) payload.get("qnaId");
        String answerContent = (String) payload.get("answerContent");

        if (qnaId == null || answerContent == null || answerContent.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of("result", "fail", "reason", "invalid input"));

        adminService.answerQna(qnaId, answerContent.trim());
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 부스 회원 등록
    @PostMapping("/user/promote")
    public ResponseEntity<?> promoteUser(@RequestParam("userId") String userId)
    {
        adminService.promoteUser(userId);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    // 비밀번호 초기화
    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("userId") String userId)
    {
        adminService.resetPW(userId);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }
}

