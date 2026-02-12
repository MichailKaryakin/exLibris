package org.example.exlibris.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(

        @NotBlank String refreshToken
) {
}
