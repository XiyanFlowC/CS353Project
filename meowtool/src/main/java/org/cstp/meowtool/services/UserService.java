package org.cstp.meowtool.services;

import org.cstp.meowtool.database.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("securityUserService")
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userMapper.selectName(username);
        if (user == null) throw new UsernameNotFoundException("Not an existing username!");
        
        return user;
    }
    
}
