package packages.loveprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO
{
    private String userId;
    private String userPw;
    private String userEmail;
    private String userBoothName;
    private String userPhone;
    private String role;
    private LocalDateTime userDate;
}