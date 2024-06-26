package ru.spb.fibricare.api.authorizationserver.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDto {
    private TokenDto token;
    private Long userId;
    private List<RoleDto> roles;
}
