package packages.loveprogress.setting;

import lombok.Getter;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import packages.loveprogress.dto.UserDTO;

import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserDTO user;

    public CustomUserDetails(UserDTO user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + user.getRole());
    }

    @Override public String getPassword() { return user.getUserPw(); }
    @Override public String getUsername() { return user.getUserId(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}