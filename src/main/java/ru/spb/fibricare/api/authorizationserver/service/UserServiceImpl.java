package ru.spb.fibricare.api.authorizationserver.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.spb.fibricare.api.authorizationserver.model.User;
import ru.spb.fibricare.api.authorizationserver.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = userRepository.findByLogin(username);
        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException("No user with requested username");
        }

        return userOptional.get();
    }
}
