package org.cstp.meowtool.utils;

import org.cstp.meowtool.database.Group;
import org.cstp.meowtool.database.GroupMapper;
import org.cstp.meowtool.database.User;
import org.cstp.meowtool.database.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AuthUtil {
    @Autowired
    UserMapper userMapper;

    @Autowired
    GroupMapper groupMapper;

    public User getUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principle instanceof String) {
            return userMapper.selectName((String) principle); // if all works fine, always should return like this
        }

        if (principle instanceof User) {
            return (User)principle;
        }

        return null;
    }

    public boolean hasAuthority(String role) {
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
            if (authority.getAuthority().compareTo(role) == 0)
                return true;
        return false;
    }

    public boolean hasProjectRole(int projectId, String role) {
        User user = getUser();
        Group group = groupMapper.selectGroupByProjectUser(projectId, user.getId());
        String[] roles = group.getRole().split(";");
        for (String srole : roles) {
            if (srole.compareTo(role) == 0) return true;
        }
        return false;
    }

    public void addProjectRole(int projectId, String role) {
        User user = getUser();
        
        Group group = new Group();
        group.setProjId(projectId);
        group.setRole(role);
        group.setUserId(user.getId());

        groupMapper.insertGroup(group);
    }
}
