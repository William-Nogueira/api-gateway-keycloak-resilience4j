package williamnogueira.dev.api_gateway.utils;

import lombok.experimental.UtilityClass;

import static williamnogueira.dev.api_gateway.utils.constants.FallbackConstants.FALLBACK_FORWARD;
import static williamnogueira.dev.api_gateway.utils.constants.FallbackConstants.FALLBACK_URI;

@UtilityClass
public class RouteUtils {
    public static String routePath(String basePath) {
        return String.format("/%s/**", basePath);
    }

    public static String fallbackPath(String basePath) {
        return String.join("", FALLBACK_FORWARD, FALLBACK_URI, basePath);
    }
}
