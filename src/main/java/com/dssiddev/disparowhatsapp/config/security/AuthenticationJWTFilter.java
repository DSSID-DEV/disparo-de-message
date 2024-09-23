package com.dssiddev.disparowhatsapp.config.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationJWTFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String jwt = getTokenHeader(request);
            if (isNotBlank(jwt) && jwtProvider.validateToken(jwt)) {
                String userName = jwtProvider.getSubjectJwt(jwt);
                var userDetails = userDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e) {
            log.error("Não é possível definir a autenticação do usuário: {}", e.getMessage());
        }
        chain.doFilter(request, response);
    }

    private boolean isNotBlank(String jwt) {
        return !jwt.isBlank();
    }

    private String getTokenHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader(ConstantesValues.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(ConstantesValues.BEARER)) return headerAuth.substring(7, headerAuth.length());

        return null;
    }
}
