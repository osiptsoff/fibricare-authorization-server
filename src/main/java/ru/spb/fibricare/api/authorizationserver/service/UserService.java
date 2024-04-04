package ru.spb.fibricare.api.authorizationserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.spb.fibricare.api.authorizationserver.model.User;

public interface UserService extends UserDetailsService {
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException;
}
