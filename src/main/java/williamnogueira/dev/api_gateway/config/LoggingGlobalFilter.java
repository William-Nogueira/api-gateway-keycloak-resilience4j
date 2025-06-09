package williamnogueira.dev.api_gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.PREFERRED_USERNAME;
import static williamnogueira.dev.api_gateway.utils.constants.LoggingConstants.INCOMING_REQUEST;
import static williamnogueira.dev.api_gateway.utils.constants.LoggingConstants.RESPONSE_STATUS;
import static williamnogueira.dev.api_gateway.utils.constants.RouteConstants.TRACE_HEADER;

@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var traceId = UUID.randomUUID().toString();
        var addTraceIdHeader = exchange.getRequest().mutate()
                .header(TRACE_HEADER, traceId)
                .build();

        var mutatedExchange = exchange.mutate()
                .request(addTraceIdHeader)
                .build();

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var authentication = securityContext.getAuthentication();

                    if (authentication.getPrincipal() instanceof Jwt jwt) {
                        var mutatedRequest = mutatedExchange.getRequest();
                        var jwtUsername = jwt.getClaimAsString(PREFERRED_USERNAME);
                        jwtUsername = Objects.nonNull(jwtUsername) ? jwtUsername : UNAUTHORIZED.getReasonPhrase();

                        log.info(INCOMING_REQUEST,
                                jwtUsername,
                                mutatedRequest.getMethod(),
                                mutatedRequest.getURI(),
                                mutatedRequest.getHeaders().getFirst(TRACE_HEADER)
                        );
                    }

                    return mutatedExchange;
                })
                .defaultIfEmpty(mutatedExchange)
                .flatMap(chain::filter)
                .then(Mono.fromRunnable(() -> log.info(RESPONSE_STATUS, mutatedExchange.getResponse().getStatusCode())));
    }
}
