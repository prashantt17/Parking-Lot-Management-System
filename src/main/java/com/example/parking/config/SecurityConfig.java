package com.example.parking.config;

import com.example.parking.entity.AppUser;
import com.example.parking.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Set;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AppUserRepository userRepo;

    public SecurityConfig(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/api/parking/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/parking/**").permitAll()
                        .requestMatchers("/api/user/me").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
                        .successHandler((request, response, authentication) -> {
                            // ✅ Redirects properly to controller endpoint
                            response.sendRedirect("/api/user/me");
                        })
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String email;
            String name;

            if ("google".equals(registrationId)) {
                email = oAuth2User.getAttribute("email");
                name = oAuth2User.getAttribute("name");
            } else if ("github".equals(registrationId)) {
                email = oAuth2User.getAttribute("email"); // may be null if private
                name = oAuth2User.getAttribute("login");
            } else {
                email = null;
                name = null;
            }

            if (email != null) {
                AppUser user = userRepo.findByEmail(email).orElseGet(() ->
                        userRepo.save(new AppUser(email, name, Set.of("ROLE_USER")))
                );

                List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                String keyAttribute = "google".equals(registrationId) ? "sub" : "login";

                return new DefaultOAuth2User(
                        authorities,
                        oAuth2User.getAttributes(),
                        keyAttribute
                );
            }

            return oAuth2User; // fallback
        };
    }
}
