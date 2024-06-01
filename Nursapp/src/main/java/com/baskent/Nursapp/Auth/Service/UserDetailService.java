package com.baskent.Nursapp.Auth.Service;

import com.baskent.Nursapp.DataAccessLayer.UserRepository;
import com.baskent.Nursapp.Entity.User.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] userIdAndbolumId = username.split("/");
        if (userIdAndbolumId == null || userIdAndbolumId.length != 2) {
            throw new UsernameNotFoundException("Username and domain must be provided");
        }
        Users user = userRepository.getKullaniciByIdandBolum(Long.parseLong(userIdAndbolumId[1]), Long.parseLong(userIdAndbolumId[0]));
        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("Username not found for domain, username=%s, domain=%s",
                            userIdAndbolumId[0], userIdAndbolumId[1]));
        }
        return user;
    }
}
