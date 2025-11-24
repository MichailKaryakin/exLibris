package org.example.exlibris.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String email;
}
