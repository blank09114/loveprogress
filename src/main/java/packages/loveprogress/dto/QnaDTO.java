package packages.loveprogress.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnaDTO
{
    private Integer qnaId;
    private String userId;
    private String qnaUserName;
    private String qnaPw;
    private int qnaImg;
    private String qnaContent;
    private boolean qnaSecret;
    private LocalDateTime qnaDate;

    private String answerContent;
    private LocalDateTime answerDate;
}