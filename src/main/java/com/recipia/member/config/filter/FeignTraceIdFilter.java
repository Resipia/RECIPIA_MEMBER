package com.recipia.member.config.filter;

import brave.Span;
import brave.propagation.TraceContext;
import jakarta.servlet.*;
import brave.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class FeignTraceIdFilter implements Filter {

    private final Tracer tracer;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Feign 클라이언트가 아닌 경우, 다음 필터로 이동
        if (!"true".equals(httpRequest.getHeader("X-Feign-Client"))) {
            chain.doFilter(request, response);
            return;
        }

        String incomingTraceId = httpRequest.getHeader("X-Trace-Id");
        if (incomingTraceId != null && !incomingTraceId.isEmpty()) {
            // 추출한 TraceID로 새로운 Span 시작
            TraceContext incomingContext = buildTraceContext(incomingTraceId);
            Span newSpan = tracer.newChild(incomingContext).start();
            try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan)) {
                chain.doFilter(request, response);
            } finally {
                newSpan.finish();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private TraceContext buildTraceContext(String traceId) {
        TraceContext.Builder contextBuilder = TraceContext.newBuilder();
        if (traceId.length() == 32) {
            long traceIdHigh = Long.parseUnsignedLong(traceId.substring(0, 16), 16);
            long traceIdLow = Long.parseUnsignedLong(traceId.substring(16), 16);
            contextBuilder.traceIdHigh(traceIdHigh).traceId(traceIdLow);
        } else {
            long traceIdLow = Long.parseUnsignedLong(traceId, 16);
            contextBuilder.traceId(traceIdLow);
        }
        contextBuilder.spanId(tracer.nextSpan().context().spanId());
        return contextBuilder.build();
    }
}
