package packages.loveprogress.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO
{
    private Long postId;
    private String postType;
    private String postTitle;
    private String postContent;
    private String userId;
    private boolean postSecret;
    private LocalDateTime postDate;
    private String userRole;
}
