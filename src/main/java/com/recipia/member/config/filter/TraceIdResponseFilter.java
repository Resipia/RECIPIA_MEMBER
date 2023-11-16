package com.recipia.member.config.filter;

import jakarta.servlet.*;
import brave.Tracer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TraceIdResponseFilter implements Filter {

    private final Tracer tracer;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            if (response instanceof HttpServletResponse) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                // 현재 Span에서 TraceID 추출
                String traceId = tracer.currentSpan().context().traceIdString();
                // TraceID를 HTTP 응답 헤더에 추가
                httpServletResponse.setHeader("X-Trace-Id", traceId);
            }
        }
    }
}
