package dev.seatlock;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seatlock.domain.Dtos.EventDto;
import dev.seatlock.domain.Dtos.SeatDto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SeatLockApplication implements CachingConfigurer {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--healthcheck")) System.exit(healthcheck());
        AppConfig config;
        try {
            config = AppConfig.load(System::getenv);
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(2);
            return;
        }
        var app = new SpringApplication(SeatLockApplication.class);
        app.setDefaultProperties(config.springProperties());
        app.addInitializers((ApplicationContextInitializer<ConfigurableApplicationContext>) ctx -> ctx.getBeanFactory().registerSingleton("appConfig", config));
        app.run(args);
    }

    // The JRE image has no curl, so the container checks itself (compose healthcheck).
    private static int healthcheck() {
        var port = System.getenv().getOrDefault("PORT", "3000");
        try {
            var res = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/health")).timeout(Duration.ofSeconds(2)).build(),
                    HttpResponse.BodyHandlers.discarding());
            return res.statusCode() == 200 ? 0 : 1;
        } catch (Exception e) {
            return 1;
        }
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    /** JSON in Redis, typed per cache, with TTLs (Chapter 25). */
    @Bean
    RedisCacheManagerBuilderCustomizer caches(ObjectMapper json) {
        var events = new Jackson2JsonRedisSerializer<>(json, EventDto.class);
        var layouts = new Jackson2JsonRedisSerializer<List<SeatDto>>(json, json.getTypeFactory().constructCollectionType(List.class, SeatDto.class));
        return b -> b
                .withCacheConfiguration("events", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(60)).serializeValuesWith(SerializationPair.fromSerializer(events)))
                .withCacheConfiguration("seat-layouts", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(5)).serializeValuesWith(SerializationPair.fromSerializer(layouts)));
    }

    /** A Redis outage means a slower read, not a failed one. */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }
}
