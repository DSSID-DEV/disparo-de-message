package com.dssiddev.disparowhatsapp.config.security;

import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepositorio userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       var user = userRepository.findByUsernameOrTelefoneOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: "+username));
        return UserDetailsImpl.build(user);
    }
}
