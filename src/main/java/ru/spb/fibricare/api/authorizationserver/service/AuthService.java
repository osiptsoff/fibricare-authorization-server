package ru.spb.fibricare.api.authorizationserver.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.jsonwebtoken.JwtException;
import ru.spb.fibricare.api.authorizationserver.dto.RefreshDto;
import ru.spb.fibricare.api.authorizationserver.dto.TokenDto;
import ru.spb.fibricare.api.authorizationserver.dto.UserDto;
import ru.spb.fibricare.api.authorizationserver.service.exception.IncorrectPasswordException;

public interface AuthService {
    TokenDto authenticate(UserDto  dto) throws UsernameNotFoundException,
        IncorrectPasswordException;
    RefreshDto refresh(String refreshToken) throws JwtException;
    Integer getRefreshTokenExpirationTimeSec();
}
