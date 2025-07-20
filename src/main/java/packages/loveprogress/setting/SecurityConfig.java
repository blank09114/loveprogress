package packages.loveprogress.setting;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSecurity logout1 = http
        .csrf
        (
            csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // 관리자 전용
                .requestMatchers("/admin/**", "/about/edit", "/about/edit/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/board/info/write").hasRole("ADMIN")
                .requestMatchers("/board/event/write").hasRole("ADMIN")
                .requestMatchers("/api/board/info/**").hasRole("ADMIN")
                .requestMatchers("/api/board/event/**").hasRole("ADMIN")

                // 부스 계정 전용
                .requestMatchers("/board/booth/**").hasAnyRole("BOOTH", "ADMIN")
                .requestMatchers("/board/BOOTH/**").hasAnyRole("BOOTH", "ADMIN")
                .requestMatchers("/api/board/booth/**").hasAnyRole("BOOTH", "ADMIN")

                // 로그인만 되어 있으면 허용
                .requestMatchers("/pwChange").authenticated()
                .requestMatchers("/api/user/password").authenticated()

                // 그 외 모두 허용
                .anyRequest().permitAll()
            )
            .formLogin
        (
            form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login/request")
            .usernameParameter("userId")
            .passwordParameter("userPw")
            .defaultSuccessUrl("/")
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout
        (
            logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
        )
        .exceptionHandling
        (
            ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
            request.getRequestDispatcher("/forbidden").forward(request, response);
        }));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    { return config.getAuthenticationManager(); }
}