package packages.loveprogress.api;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packages.loveprogress.dto.UserDTO;
import packages.loveprogress.service.UserService;

import java.security.Principal;

@RestController
public class UserApi
{
    @Resource
    private UserService userService;

    // ID 중복 확인
    @GetMapping("/signup/check-id")
    public ResponseEntity<?> checkDuplicateId(@RequestParam String userId)
    {
        boolean isTaken = userService.isIdTaken(userId);
        return ResponseEntity.ok(isTaken);
    }

    // 비밀번호 변경
    @PostMapping("/api/user/password")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO dto, Principal principal) {
        String userId = principal.getName();
        String newPw = dto.getUserPw();

        boolean success = userService.updatePassword(userId, newPw);

        return success ? ResponseEntity.ok("비밀번호 변경 완료")
                : ResponseEntity.status(500).body("비밀번호 변경 실패");
    }
}