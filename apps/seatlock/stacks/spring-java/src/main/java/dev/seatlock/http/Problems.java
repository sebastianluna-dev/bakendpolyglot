package dev.seatlock.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.domain.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Every error leaves as application/problem+json (RFC 9457, Chapter 12). */
@RestControllerAdvice
public class Problems {
    private static final Logger log = LoggerFactory.getLogger(Problems.class);
    private static final String TYPE_BASE = "https://seatlock.dev/problems/";

    public static Map<String, Object> body(DomainException e, String instance) {
        var body = new LinkedHashMap<String, Object>();
        body.put("type", TYPE_BASE + e.code());
        body.put("title", e.title());
        body.put("status", e.status());
        body.put("detail", e.getMessage());
        body.put("instance", instance);
        body.putAll(e.extensions());
        return body;
    }

    static ResponseEntity<Map<String, Object>> render(DomainException e, HttpServletRequest req) {
        var builder = ResponseEntity.status(e.status()).contentType(MediaType.APPLICATION_PROBLEM_JSON);
        if ("unauthorized".equals(e.code())) builder.header("WWW-Authenticate", "Bearer realm=\"seatlock\"");
        return builder.body(body(e, req.getRequestURI()));
    }

    /** For Spring Security's entry point and access-denied handler, which run outside MVC. */
    public static void write(ObjectMapper json, HttpServletRequest req, HttpServletResponse res, DomainException e) throws IOException {
        if ("unauthorized".equals(e.code())) res.setHeader("WWW-Authenticate", "Bearer realm=\"seatlock\"");
        res.setStatus(e.status());
        res.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        json.writeValue(res.getOutputStream(), body(e, req.getRequestURI()));
    }

    @ExceptionHandler(DomainException.class)
    ResponseEntity<Map<String, Object>> domain(DomainException e, HttpServletRequest req) {
        return render(e, req);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<Map<String, Object>> noRoute(HttpServletRequest req) {
        return render(DomainException.notFound("Route", req.getRequestURI()), req);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, Object>> unexpected(Exception e, HttpServletRequest req, HttpServletResponse res) {
        if (e instanceof ErrorResponse framework) { // Spring's own HTTP errors (405, 415 …): keep the status
            int status = framework.getStatusCode().value();
            return render(new DomainException("http-" + status, status, "Request not supported", e.getMessage(), Map.of()), req);
        }
        var requestId = res.getHeader("X-Request-Id");
        log.error("unhandled error on {} (request {})", req.getRequestURI(), requestId, e);
        return render(DomainException.internal(requestId), req);
    }
}
