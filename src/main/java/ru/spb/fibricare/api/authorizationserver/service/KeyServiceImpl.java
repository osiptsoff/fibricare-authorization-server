package ru.spb.fibricare.api.authorizationserver.service;


import java.util.Base64;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.spb.fibricare.api.authorizationserver.dto.KeyDto;
import ru.spb.fibricare.api.authorizationserver.security.jwt.JwtUtility;

@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {
    private final JwtUtility jwtUtility;

    @Override
    public KeyDto getAccessPublicKey() {
        var key = jwtUtility.getAccessPublicKey();

        return new KeyDto(Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}
