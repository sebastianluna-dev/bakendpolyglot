package dev.shelfwise.http;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** A servlet filter: Spring's middleware. The id also goes into the logging MDC (Chapter 39). */
@Component
class RequestIdFilter extends OncePerRequestFilter {
    static final String HEADER = "X-Request-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        var id = req.getHeader(HEADER);
        if (id == null || id.isEmpty()) id = UUID.randomUUID().toString().replace("-", "");
        res.setHeader(HEADER, id);
        MDC.put("requestId", id);
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.remove("requestId");
        }
    }
}
