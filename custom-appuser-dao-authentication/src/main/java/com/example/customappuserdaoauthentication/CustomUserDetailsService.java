package com.example.customappuserdaoauthentication;

import com.example.customappuserdaoauthentication.user.AppAuthority;
import com.example.customappuserdaoauthentication.user.AppUser;
import com.example.customappuserdaoauthentication.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow();
        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getAuthorities().stream()
                        .map(AppAuthority::getAuthority)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
