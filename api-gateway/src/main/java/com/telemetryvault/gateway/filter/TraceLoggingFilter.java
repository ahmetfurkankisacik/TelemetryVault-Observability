package com.telemetryvault.gateway.filter;

import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TraceLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(TraceLoggingFilter.class);
    private final Tracer tracer;

    public TraceLoggingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        String traceId = tracer.currentSpan() != null ? tracer.currentSpan().context().traceId() : "N/A";
        String spanId = tracer.currentSpan() != null ? tracer.currentSpan().context().spanId() : "N/A";

        log.info("Incoming Gateway Request: Method={} Path={} TraceID={} SpanID={}",
                request.getMethod(), request.getURI().getPath(), traceId, spanId);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Outgoing Gateway Response: Path={} Status={} TraceID={}",
                    request.getURI().getPath(), exchange.getResponse().getStatusCode(), traceId);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
