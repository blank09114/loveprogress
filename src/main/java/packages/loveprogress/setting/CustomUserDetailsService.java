package packages.loveprogress.setting;


import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import packages.loveprogress.dto.UserDTO;
import packages.loveprogress.mapper.UserMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userMapper.findUserById(username);
        if (user == null) throw new UsernameNotFoundException("사용자 없음");

        return new CustomUserDetails(user);
    }
}