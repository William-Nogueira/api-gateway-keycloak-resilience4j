package williamnogueira.dev.api_gateway.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FallbackConstants {
    public static final String FALLBACK_LOG_MESSAGE = "Fallback activated for the {} route: {}";
    public static final String FALLBACK_FORWARD = "forward:";
    public static final String FALLBACK_URI = "/fallback/";
    public static final String FALLBACK_ROUTE = "{route}";
    public static final String TIMEOUT_FALLBACK_MESSAGE = " service timeout occurred.";
    public static final String UNAVAILABLE_FALLBACK_MESSAGE = " service is temporarily unavailable";
}
