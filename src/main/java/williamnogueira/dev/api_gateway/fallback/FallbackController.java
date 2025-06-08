package williamnogueira.dev.api_gateway.fallback;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import williamnogueira.dev.api_gateway.fallback.dto.FallbackResponseDTO;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;
import static williamnogueira.dev.api_gateway.fallback.builder.FallbackResponseBuilder.buildFallbackResponse;
import static williamnogueira.dev.api_gateway.utils.constants.FallbackConstants.FALLBACK_ROUTE;
import static williamnogueira.dev.api_gateway.utils.constants.FallbackConstants.FALLBACK_URI;

@RestController
public class FallbackController {

    @GetMapping(FALLBACK_URI + FALLBACK_ROUTE)
    public Mono<ResponseEntity<FallbackResponseDTO>> genericFallback(@PathVariable String route, ServerWebExchange exchange) {
        return buildFallbackResponse(route, exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR));
    }
}
