package dev.shelfwise.http

import dev.shelfwise.domain.DomainException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.UUID
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.resource.NoResourceFoundException

const val REQUEST_ID = "X-Request-Id"

/** A servlet filter: Spring's middleware. The id also goes into the logging MDC (Chapter 39). */
@Component
class RequestIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val id = req.getHeader(REQUEST_ID)?.takeIf { it.isNotEmpty() } ?: UUID.randomUUID().toString().replace("-", "")
        res.setHeader(REQUEST_ID, id)
        MDC.put("requestId", id)
        try {
            chain.doFilter(req, res)
        } finally {
            MDC.remove("requestId")
        }
    }
}

/** Every error leaves as application/problem+json (RFC 9457, Chapter 12). */
@RestControllerAdvice
class Problems {
    private val log = LoggerFactory.getLogger(javaClass)

    private fun render(e: DomainException, req: HttpServletRequest): ResponseEntity<Map<String, Any?>> {
        val body = linkedMapOf<String, Any?>(
            "type" to "https://shelfwise.dev/problems/${e.code}",
            "title" to e.title,
            "status" to e.status,
            "detail" to e.message,
            "instance" to req.requestURI,
        )
        body += e.extensions
        return ResponseEntity.status(e.status).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body)
    }

    @ExceptionHandler(DomainException::class)
    fun domain(e: DomainException, req: HttpServletRequest) = render(e, req)

    @ExceptionHandler(NoResourceFoundException::class)
    fun noRoute(req: HttpServletRequest) = render(DomainException.notFound("Route", req.requestURI), req)

    @ExceptionHandler(Exception::class)
    fun unexpected(e: Exception, req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<Map<String, Any?>> {
        if (e is ErrorResponse) { // Spring's own HTTP errors (405, 415 …): keep the status
            val status = e.statusCode.value()
            return render(DomainException("http-$status", status, "Request not supported", e.message ?: ""), req)
        }
        val requestId = res.getHeader(REQUEST_ID)
        log.error("unhandled error on {} (request {})", req.requestURI, requestId, e)
        return render(DomainException.internal(requestId), req)
    }
}
