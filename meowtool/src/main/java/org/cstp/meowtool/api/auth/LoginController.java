package org.cstp.meowtool.api.auth;

import org.cstp.meowtool.database.User;
import org.cstp.meowtool.database.UserMapper;
import org.cstp.meowtool.utils.JwtUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Api(tags="Authentication")
@RestController
public class LoginController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Data
    static class LoginData {
        private String username;
        private String password;
    }

    @ApiOperation("Try to login with username and password.")
    @PostMapping("/auth/login")
    public Result login(@RequestBody LoginData data) {
        User user = userMapper.selectName(data.username);
        if (user == null) return new Result(-102, "Username error.", null);
        
        // TODO: enhance this method, add salt and encryption to password
        if (user.getPassword().compareTo(data.password) == 0) {
            if (!user.isEnabled()) return new Result(-103, "User blocked.", null);

            return Result.succ(jwtUtil.issueToken(data.username));
        } else return new Result(-102, "Password error.", null);
    }
}
