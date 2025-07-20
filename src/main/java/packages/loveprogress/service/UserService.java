package packages.loveprogress.service;

import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.UserDTO;
import packages.loveprogress.mapper.UserMapper;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    // 회원가입 처리
    public void signup(UserDTO dto)
    {
        // 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(dto.getUserPw());

        // DTO 수정
        dto.setUserPw(encodedPw);
        dto.setRole("USER");

        // DB 저장
        userMapper.insertUser(dto);
    }

    // 로그인
    public UserDTO login(String userId, String rawPw)
    {
        UserDTO user = userMapper.findUserById(userId);
        if (user == null) return null;

        // 비밀번호 일치 확인
        if (passwordEncoder.matches(rawPw, user.getUserPw())) { return user; }

        return null;
    }

    // ID 중복 확인
    public boolean isIdTaken(String userId) { return userMapper.findUserById(userId) != null; }

    // 비밀번호 변경
    public boolean updatePassword(String userId, String newPassword)
    {
        String encodedPw = passwordEncoder.encode(newPassword);
        return userMapper.updatePassword(userId, encodedPw) > 0;
    }
}
