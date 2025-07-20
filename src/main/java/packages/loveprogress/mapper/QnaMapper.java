package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;
import packages.loveprogress.dto.QnaDTO;

import java.util.List;

@Mapper
public interface QnaMapper
{
    // 질문 등록
    void insertQna(QnaDTO dto);

    // 전체 조회
    List<QnaDTO> selectAllQna();

    // 비회원 비밀글 열람
    QnaDTO getQnaById(int qnaId);
}
