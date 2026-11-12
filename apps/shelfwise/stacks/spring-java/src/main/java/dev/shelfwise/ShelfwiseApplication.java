package dev.shelfwise;

import dev.shelfwise.data.LoanRepository;
import dev.shelfwise.domain.LoanService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShelfwiseApplication {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("--healthcheck")) System.exit(healthcheck());

        AppConfig config;
        try {
            config = AppConfig.load(System::getenv);
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(2);
            return;
        }
        var app = new SpringApplication(ShelfwiseApplication.class);
        app.setDefaultProperties(config.springProperties());
        app.addInitializers(ctx -> ctx.getBeanFactory().registerSingleton("appConfig", config));
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

    // LoanService has no Spring annotations; this method is the only place it meets the container.
    @Bean
    LoanService loanService(LoanRepository loans, AppConfig config, Clock clock) {
        return new LoanService(loans, new LoanService.Rules(config.loanDays(), config.maxLoansPerMember()), clock);
    }
}
