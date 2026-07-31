package packages.loveprogress.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import packages.loveprogress.dto.UserDTO;

@Mapper
public interface UserMapper
{
    // 회원가입
    void insertUser(UserDTO user);

    // ID로 유저 조회
    UserDTO findUserById(String userId);

    // 비밀번호 변경
    int updatePassword(@Param("userId") String userId, @Param("userPw") String userPw);
}