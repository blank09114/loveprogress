package packages.loveprogress.api;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.QnaDTO;
import packages.loveprogress.service.QnaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaApi
{
    @Resource
    private QnaService qnaService;

    // 질문 등록
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerQna(@RequestBody QnaDTO dto)
    {
        Map<String, Object> response = new HashMap<>();

        // 로그인 여부 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isMember = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

        if (isMember)
        {
            // userId 주입
            String userId = auth.getName();
            dto.setUserId(userId);
        }

        try
        {
            qnaService.registerQna(dto, isMember);
            response.put("success", true);
            response.put("message", "질문이 등록되었습니다.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "오류가 발생했습니다.");
        }

        return ResponseEntity.ok(response);
    }

    // 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<QnaDTO>> getQnaList()
    {
        List<QnaDTO> qnaList = qnaService.getAllQna();
        return ResponseEntity.ok(qnaList);
    }

    // 비회원 비밀글 조회
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyQnaPassword(@RequestBody Map<String, Object> body)
    {
        int qnaId = (int) body.get("qnaId");
        String inputPw = (String) body.get("qnaPw");
        return ResponseEntity.ok(qnaService.verifyQnaPassword(qnaId, inputPw));
    }
}