package williamnogueira.dev.api_gateway.routes;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import williamnogueira.dev.api_gateway.utils.RouteUtils;

import static williamnogueira.dev.api_gateway.utils.constants.RouteConstants.GATEWAY_HEADER;
import static williamnogueira.dev.api_gateway.utils.constants.RouteConstants.LOAD_BALANCER;
import static williamnogueira.dev.api_gateway.utils.constants.RouteConstants.SERVICE_ROUTE;

@Configuration
@RequiredArgsConstructor
public class ProductsRoute {

    private final KeyResolver forwardedKeyResolver;
    private final RedisRateLimiter redisRateLimiter;

    private static final String ROUTE_NAME = "products";

    @Bean
    public RouteLocator productRouting(RouteLocatorBuilder builder) {
        var routeId = ROUTE_NAME.concat(SERVICE_ROUTE);

        return builder.routes()
                .route(routeId, r -> r
                        .path(RouteUtils.routePath(ROUTE_NAME))
                        .filters(f -> f
                                .requestRateLimiter(rl -> { // Rate Limiter
                                    rl.setKeyResolver(forwardedKeyResolver);
                                    rl.setRateLimiter(redisRateLimiter);
                                })
                                .circuitBreaker(cb -> { // Circuit Breaker + Fallback
                                    cb.setName(ROUTE_NAME);
                                    cb.setRouteId(routeId);
                                    cb.setFallbackUri(RouteUtils.fallbackPath(ROUTE_NAME));
                                })
                                .addRequestHeader(GATEWAY_HEADER, ROUTE_NAME))
                        .uri(LOAD_BALANCER + ROUTE_NAME)) // Load Balanced URI
                .build();
    }
}

