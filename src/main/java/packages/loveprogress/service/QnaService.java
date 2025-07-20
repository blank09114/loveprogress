package packages.loveprogress.service;

import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.QnaDTO;
import packages.loveprogress.mapper.QnaMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QnaService {

    @Resource
    private QnaMapper qnaMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    // QnA 등록
    public void registerQna(QnaDTO dto, boolean isMember)
    {
        if (!isMember && dto.getQnaPw() != null)
        { dto.setQnaPw(passwordEncoder.encode(dto.getQnaPw())); }
        qnaMapper.insertQna(dto);
    }

    // 전체 조회
    public List<QnaDTO> getAllQna() { return qnaMapper.selectAllQna(); }

    // 비회원 비밀글 보기
    public Map<String, Object> verifyQnaPassword(int qnaId, String inputPw)
    {
        QnaDTO dto = qnaMapper.getQnaById(qnaId);
        Map<String, Object> result = new HashMap<>();

        if (dto == null || dto.getQnaPw() == null)
        {
            result.put("success", false);
            result.put("message", "잘못된 접근입니다.");
            return result;
        }

        boolean matches = passwordEncoder.matches(inputPw, dto.getQnaPw());
        if (matches)
        {
            result.put("success", true);
            result.put("qnaContent", dto.getQnaContent());
            result.put("answerContent", dto.getAnswerContent());
        }
        else
        {
            result.put("success", false);
            result.put("message", "비밀번호가 일치하지 않습니다.");
        }
        return result;
    }
}