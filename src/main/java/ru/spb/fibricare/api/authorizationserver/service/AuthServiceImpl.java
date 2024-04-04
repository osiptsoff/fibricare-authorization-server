package ru.spb.fibricare.api.authorizationserver.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import ru.spb.fibricare.api.authorizationserver.dto.RefreshDto;
import ru.spb.fibricare.api.authorizationserver.dto.TokenDto;
import ru.spb.fibricare.api.authorizationserver.dto.UserDto;
import ru.spb.fibricare.api.authorizationserver.model.User;
import ru.spb.fibricare.api.authorizationserver.security.jwt.JwtUtility;
import ru.spb.fibricare.api.authorizationserver.service.exception.IncorrectPasswordException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    @Override
    public TokenDto authenticate(UserDto dto) throws UsernameNotFoundException,
            IncorrectPasswordException {
        User user = userService.loadUserByUsername(dto.getLogin());

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Given password is incorrect");
        }

        String refreshToken = jwtUtility.generateRefreshToken(user);

        return new TokenDto("refresh", refreshToken);
    }

    @Override
    public RefreshDto refresh(String refreshToken) throws JwtException {
        User user = jwtUtility.parseAndValidateRefreshToken(refreshToken);

        String accessToken = jwtUtility.generateAccessToken(user);

        return new RefreshDto(new TokenDto("access", accessToken), user.getId());
    }

    @Override
    public Integer getRefreshTokenExpirationTimeSec() {
        return jwtUtility.getRefreshLifespawn();
    }
}
