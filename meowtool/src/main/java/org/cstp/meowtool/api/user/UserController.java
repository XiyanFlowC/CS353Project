package org.cstp.meowtool.api.user;

import org.springframework.web.bind.annotation.RestController;
import lombok.Data;
import org.cstp.meowtool.database.templates.User;
import org.cstp.meowtool.database.templates.UserMapper;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/user/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userMapper.selectId(id);
    }

    @PostMapping("/user/user")
    public Result createUser(User user) {
        if (userMapper.selectName(user.getUsername()) != null) return Result.fail("username has already registered.");
        if (userMapper.selectEmail(user.getEmail()) != null) return Result.fail("email address has already registered.");
        
        user.setRole("USER");
        int ret = userMapper.insertUser(user);
        if (ret == 0) return Result.succ(0, "user registered succssfully.");
        else return Result.fail("failed to create user.");
    }

    // Test only
    // @GetMapping("/user/list")
    // public User[] getUsers() {
    //     return userMapper.selectAll();
    // }
}
