package org.cstp.meowtool.api.auth;

import org.cstp.meowtool.database.templates.User;
import org.cstp.meowtool.database.templates.UserMapper;
import org.cstp.meowtool.utils.JwtUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userMapper.selectName(username);
        if (user == null) return new Result(-100, "Username error.", null);
        
        // TODO: enhance this method, add salt and encryption to password
        if (user.getPassword().compareTo(password) == 0) {
            return Result.succ(jwtUtil.issueToken(username));
        } else return new Result(-101, "Password error.", null);
    }
}
