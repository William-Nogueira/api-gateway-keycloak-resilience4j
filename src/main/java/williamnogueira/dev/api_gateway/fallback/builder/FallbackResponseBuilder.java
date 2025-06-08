package williamnogueira.dev.api_gateway.fallback.builder;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import williamnogueira.dev.api_gateway.fallback.dto.FallbackResponseDTO;

import java.time.Instant;
import java.util.concurrent.TimeoutException;

import static williamnogueira.dev.api_gateway.utils.constants.FallbackConstants.*;

@Slf4j
@UtilityClass
public class FallbackResponseBuilder {

    public static Mono<ResponseEntity<FallbackResponseDTO>> buildFallbackResponse(String routeName, Exception cause) {
        var message = getMessageFromException(cause, routeName);
        var status = getStatusFromException(cause);

        log.warn(FALLBACK_LOG_MESSAGE, routeName, message);

        var response = FallbackResponseDTO.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        return Mono.just(ResponseEntity.status(status)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response)
        );
    }

    private static String getMessageFromException(Exception cause, String routeName) {
        var formattedRouteName = StringUtils.capitalize(routeName);
        return switch (cause) {
            case TimeoutException ignored -> formattedRouteName.concat(TIMEOUT_FALLBACK_MESSAGE);
            case null, default -> formattedRouteName.concat(UNAVAILABLE_FALLBACK_MESSAGE);
        };
    }

    private static HttpStatus getStatusFromException(Exception cause) {
        return switch (cause) {
            case TimeoutException ignored -> HttpStatus.REQUEST_TIMEOUT;
            case null, default -> HttpStatus.SERVICE_UNAVAILABLE;
        };
    }
}
