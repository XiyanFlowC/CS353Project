package org.cstp.meowtool.api.auth;

import org.cstp.meowtool.database.templates.User;
import org.cstp.meowtool.database.templates.UserMapper;
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
public class RegisterController {
    @Data
    static class RegisterData {
        private String username;
        private String password;
        private String email;
    }

    @Autowired
    UserMapper userMapper;

    @ApiOperation("Try to register a new user.")
    @PostMapping("/auth/register")
    public Result register(@RequestBody RegisterData data) {
        // TODO: enhance this method! add salt, encrypt the password!
        if (null != userMapper.selectName(data.username)) return Result.fail("Username has already occupied.");
        if (null != userMapper.selectEmail(data.email)) return Result.fail("Email address has already occupied.");
        
        User user = new User();
        user.setUsername(data.username);
        user.setEmail(data.email);
        user.setPassword(data.password);
        user.setSalt("");

        user.setRating(0);
        user.setDisable(false);
        user.setRole("USER");
        int ret = userMapper.insertUser(user);

        if (ret != 1) return new Result(-9001, "Server internal error", data);
        return Result.succ(userMapper.selectName(data.username));
    }
}
