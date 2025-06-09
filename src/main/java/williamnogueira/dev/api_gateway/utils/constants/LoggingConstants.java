package williamnogueira.dev.api_gateway.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoggingConstants {
    public static final String INCOMING_REQUEST = "Incoming request | user={} method={} uri={} traceId={}";
    public static final String RESPONSE_STATUS = "Response status | statusCode={}";
}
