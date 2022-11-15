package org.cstp.meowtool.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cstp.meowtool.utils.JwtUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler{

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream output = response.getOutputStream();
        response.setHeader(jwtUtil.getHeader(), "");
        output.write(new ObjectMapper().writeValueAsString(Result.succ(0, "successfully logged-out!")).getBytes());
        output.close();
    }
    
}
