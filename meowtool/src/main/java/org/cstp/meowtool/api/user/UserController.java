package org.cstp.meowtool.api.user;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.cstp.meowtool.database.User;
import org.cstp.meowtool.database.UserMapper;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "User Management")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @ApiOperation("Get User Information by User ID")
    @GetMapping("/user/{id}")
    public Result getUser(@PathVariable("id")@ApiParam(value="User ID", required = true, example = "1") Integer id) {
        return Result.succ(userMapper.selectId(id));
    }

    public Result fetchUser(@ApiParam(value = "User name to be queried.", required = true, example = "admin") String username) {
        return Result.succ(userMapper.selectName(username));
    }

    @ApiOperation("Create a New User (Admin)")
    @PostMapping("/user")
    public Result createUser(@ApiParam(value = "User Object", required = true)@RequestBody User user) {
        if (userMapper.selectName(user.getUsername()) != null) return Result.fail("username has already registered.");
        if (userMapper.selectEmail(user.getEmail()) != null) return Result.fail("email address has already registered.");
        
        int ret = userMapper.insertUser(user);
        if (ret == 0) return Result.succ(0, "user registered succssfully.");
        else return Result.fail("failed to create user.");
    }

    @ApiOperation("Disable or enable a user.")
    @PutMapping("/user/{id}")
    public Result disableUser(@PathVariable("id") Integer id, Boolean disable) {
        User user = userMapper.selectId(id);
        if (user == null) return Result.fail("Specified user does not exisit.");

        user.setDisable(disable);
        userMapper.updateUser(user);
        return Result.succ(null);
    }
}
