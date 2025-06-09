package williamnogueira.dev.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Value("${rate.limiter.replenishRate}")
    private int replenishRate;

    @Value("${rate.limiter.burstCapacity}")
    private int burstCapacity;

    @Bean
    public KeyResolver forwardedKeyResolver() {
        return exchange -> {
            var address = XForwardedRemoteAddressResolver.maxTrustedIndex(1).resolve(exchange);
            return Mono.justOrEmpty(address)
                    .map(socketAddress -> socketAddress.getAddress().getHostAddress());
        };
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(replenishRate, burstCapacity);
    }
}
