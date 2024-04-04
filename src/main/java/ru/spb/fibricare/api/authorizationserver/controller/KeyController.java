package ru.spb.fibricare.api.authorizationserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.fibricare.api.authorizationserver.dto.KeyDto;
import ru.spb.fibricare.api.authorizationserver.service.KeyService;

@RestController
@RequestMapping("/key")
@RequiredArgsConstructor
public class KeyController {
    private final KeyService keyService;

    @GetMapping()
    public KeyDto getKey() {
        return keyService.getAccessPublicKey();
    }
}
