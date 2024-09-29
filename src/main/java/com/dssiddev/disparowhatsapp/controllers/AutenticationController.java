package com.dssiddev.disparowhatsapp.controllers;


import com.dssiddev.disparowhatsapp.config.security.JwtProvider;
import com.dssiddev.disparowhatsapp.config.security.UserDetailsImpl;
import com.dssiddev.disparowhatsapp.models.dto.JwtDTO;
import com.dssiddev.disparowhatsapp.models.dto.LoginDTO;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import com.dssiddev.disparowhatsapp.services.RoleService;
import com.dssiddev.disparowhatsapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@PermitAll
@RestController
@CrossOrigin(origins = "https://feature-dev--e-post.netlify.app/", maxAge = 3600)
@RequestMapping("/auth")
public class AutenticationController {

    @Autowired
    private UserService service;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManger;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    UserRepositorio repositorio;

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> authenticationUser(@Valid @RequestBody LoginDTO login) {

        var authentication = authenticationManger.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJWT(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
       return ResponseEntity.ok(getObjectReturn(jwt, userDetails));
    }

    private JwtDTO getObjectReturn(String token, UserDetailsImpl userDetails) {
        return new JwtDTO(token, userNameFormatado(userDetails),
                obterPermissao(userDetails.getAuthorities()));
    }

    private String userNameFormatado(UserDetailsImpl userDetails) {
        String format = userDetails.getUsername().replace("@ePost","");
        boolean temSobrenome = format.split("\\.").length > 1;
        return temSobrenome ? addPrimeiraLetraMaiuscula(format.split("\\.")[0])+" "
                +addPrimeiraLetraMaiuscula(format.split("\\.")[1]) : addPrimeiraLetraMaiuscula(format);
    }

    private String addPrimeiraLetraMaiuscula(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String obterPermissao(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority grantedAuthority: authorities) {
            if (grantedAuthority.getAuthority().equals(RoleType.ROLE_DEV.toString())) return grantedAuthority.getAuthority();
            if (grantedAuthority.getAuthority().equals(RoleType.ROLE_ADMIN.toString())) return grantedAuthority.getAuthority();
        }
        return RoleType.ROLE_USER.toString();
    }

}
