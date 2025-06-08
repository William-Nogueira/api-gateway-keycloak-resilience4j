package williamnogueira.dev.api_gateway.fallback.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record FallbackResponseDTO(
        Instant timestamp,
        int status,
        String error,
        String message
) {}
