package williamnogueira.dev.api_gateway.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RouteConstants {
    public static final String GATEWAY_HEADER = "X-Gateway-Request";
    public static final String TRACE_HEADER = "X-Trace-ID";
    public static final String SERVICE_ROUTE = "-route";
    public static final String LOAD_BALANCER = "lb://";
}
