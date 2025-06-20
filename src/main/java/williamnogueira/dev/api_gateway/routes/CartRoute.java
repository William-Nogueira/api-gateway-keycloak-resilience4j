package williamnogueira.dev.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import williamnogueira.dev.api_gateway.utils.RouteUtils;

import static williamnogueira.dev.api_gateway.utils.constants.RouteConstants.GATEWAY_HEADER;
import static williamnogueira.dev.api_gateway.utils.constants.RouteConstants.SERVICE_ROUTE;

@Configuration
public class CartRoute {

    @Value("${simple-uri}")
    private String uri;

    private static final String ROUTE_NAME = "cart";

    @Bean
    public RouteLocator cartRouting(RouteLocatorBuilder builder) {
        var routeId = ROUTE_NAME.concat(SERVICE_ROUTE);
        return builder.routes()
                .route(routeId, r -> r
                        .path(RouteUtils.routePath(ROUTE_NAME))
                        .filters(f -> f
                                .circuitBreaker(cb -> { // Circuit Breaker + Fallback
                                    cb.setName(ROUTE_NAME);
                                    cb.setRouteId(routeId);
                                    cb.setFallbackUri(RouteUtils.fallbackPath(ROUTE_NAME));
                                })
                                .addRequestHeader(GATEWAY_HEADER, ROUTE_NAME)
                                .rewritePath("/cart","/cart?delay=5000")) // Timeout error for demo purposes
                        .uri(uri)) // Simple URI without load balancing
                .build();
    }
}
