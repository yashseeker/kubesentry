package com.yash.kubesentry.security;

import com.yash.kubesentry.model.entity.User;
import com.yash.kubesentry.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.Email;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
//         that's an adapter
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                )
        );
    }
}