package packages.loveprogress.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO
{
    private Long commentId;
    private Long postId;
    private String userId;
    private String commentContent;
    private LocalDateTime commentDate;
    private String userRole;
    private String writerName;
    private boolean owner;
}

