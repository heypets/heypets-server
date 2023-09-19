package com.heypets.heypetsserver.core.security.login.util;

import com.heypets.heypetsserver.domain.user.entity.User;
import com.heypets.heypetsserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
