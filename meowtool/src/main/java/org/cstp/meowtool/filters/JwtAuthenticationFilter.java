package org.cstp.meowtool.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cstp.meowtool.services.UserService;
import org.cstp.meowtool.utils.JwtUtil;
import org.cstp.meowtool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtil.getHeader());
        if (StringUtil.isNullOrEmpty(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.getClaims(jwt);
        if (claims.isEmpty()) {
            throw new JwtException("invalid token.");
        }
        if (jwtUtil.isExpired(claims)) {
            throw new JwtException("token expired.");
        }

        String username = claims.getSubject();
        UserDetails user = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        
        chain.doFilter(request, response);
    }
}
