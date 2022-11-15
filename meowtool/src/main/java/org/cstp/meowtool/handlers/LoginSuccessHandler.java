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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/jso;charset=UTF-8");
        ServletOutputStream output = response.getOutputStream();
        response.setHeader(jwtUtil.getHeader(), jwtUtil.issueToken(authentication.getName()));

        output.write(new ObjectMapper().writeValueAsString(Result.succ(100, "login success.")).getBytes());
        output.close();
    }
}
