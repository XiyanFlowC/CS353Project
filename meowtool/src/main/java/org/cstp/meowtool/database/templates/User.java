package org.cstp.meowtool.database.templates;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class User implements UserDetails {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private String rating;
    private String password;
    private String salt;
    private boolean disable;
    
    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return !disable;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return !disable;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] roles = role.split(";");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (String string : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + string));
        }
        return authorities;
    }
}
