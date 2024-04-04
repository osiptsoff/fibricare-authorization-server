package ru.spb.fibricare.api.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDto {
    private TokenDto token;
    private Long userId;
}
