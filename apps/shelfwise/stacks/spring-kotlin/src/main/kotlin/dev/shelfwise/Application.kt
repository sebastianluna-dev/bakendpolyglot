package dev.shelfwise

import dev.shelfwise.data.LoanRepository
import dev.shelfwise.domain.LoanRules
import dev.shelfwise.domain.LoanService
import java.net.URI
import java.net.URLDecoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Clock
import java.time.Duration
import kotlin.system.exitProcess
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean

/**
 * Typed configuration from the environment variables every stack reads, validated before Spring
 * starts; every problem is reported at once (Chapter 16).
 */
data class AppConfig(
    val port: Int, val jdbcUrl: String, val dbUser: String?, val dbPassword: String?,
    val loanDays: Int, val maxLoansPerMember: Int, val logLevel: String,
) {
    fun springProperties(): Map<String, Any> = buildMap {
        put("server.port", port)
        put("spring.datasource.url", jdbcUrl)
        dbUser?.let { put("spring.datasource.username", it) }
        dbPassword?.let { put("spring.datasource.password", it) }
        put("logging.level.dev.shelfwise", logLevel.uppercase())
    }

    companion object {
        fun load(env: (String) -> String?): AppConfig {
            val problems = mutableListOf<String>()
            fun num(key: String, default: Int, range: IntRange): Int {
                val raw = env(key)
                if (raw.isNullOrEmpty()) return default
                return raw.toIntOrNull()?.takeIf { it in range }
                    ?: default.also { problems += "$key: must be an integer between ${range.first} and ${range.last}" }
            }
            val port = num("PORT", 3000, 1..65535)
            val loanDays = num("LOAN_DAYS", 21, 1..60)
            val maxLoans = num("MAX_LOANS_PER_MEMBER", 5, 1..50)

            // postgres://user:pass@host:port/db  →  jdbc:postgresql://host:port/db + credentials
            val uri = env("DATABASE_URL")?.takeIf { it.isNotEmpty() }?.let { runCatching { URI.create(it) }.getOrNull() }
            if (uri == null) problems += "DATABASE_URL: is required and must be a postgres:// URL"
            val info = uri?.rawUserInfo?.split(":", limit = 2).orEmpty().map { URLDecoder.decode(it, Charsets.UTF_8) }

            val level = env("LOG_LEVEL") ?: "info"
            if (level !in setOf("debug", "info", "warn", "error")) problems += "LOG_LEVEL: must be debug, info, warn or error"

            check(problems.isEmpty()) { "Invalid configuration:\n  " + problems.joinToString("\n  ") }
            val port5432 = if (uri!!.port > 0) uri.port else 5432
            return AppConfig(
                port, "jdbc:postgresql://${uri.host}:$port5432${uri.path}", info.getOrNull(0), info.getOrNull(1),
                loanDays, maxLoans, level,
            )
        }
    }
}

@SpringBootApplication
class ShelfwiseApplication {
    @Bean
    fun clock(): Clock = Clock.systemUTC()

    // LoanService has no Spring annotations; this function is the only place it meets the container.
    @Bean
    fun loanService(loans: LoanRepository, config: AppConfig, clock: Clock) =
        LoanService(loans, LoanRules(config.loanDays, config.maxLoansPerMember), clock)
}

// The JRE image has no curl, so the container checks itself (compose healthcheck).
private fun healthcheck(): Int = runCatching {
    val port = System.getenv("PORT") ?: "3000"
    val request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:$port/health")).timeout(Duration.ofSeconds(2)).build()
    if (HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding()).statusCode() == 200) 0 else 1
}.getOrDefault(1)

fun main(args: Array<String>) {
    if (args.firstOrNull() == "--healthcheck") exitProcess(healthcheck())

    val config = try {
        AppConfig.load(System::getenv)
    } catch (e: IllegalStateException) {
        System.err.println(e.message)
        exitProcess(2)
    }
    runApplication<ShelfwiseApplication>(*args) {
        setDefaultProperties(config.springProperties())
        addInitializers(ApplicationContextInitializer<ConfigurableApplicationContext> { it.beanFactory.registerSingleton("appConfig", config) })
    }
}
