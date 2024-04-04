package ru.spb.fibricare.api.authorizationserver.service;

import ru.spb.fibricare.api.authorizationserver.dto.KeyDto;

public interface KeyService {
    KeyDto getAccessPublicKey();
}
