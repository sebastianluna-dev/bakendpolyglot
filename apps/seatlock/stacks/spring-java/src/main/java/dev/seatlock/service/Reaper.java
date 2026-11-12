package dev.seatlock.service;

import dev.seatlock.AppConfig;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/** Runs the hold reaper every REAPER_INTERVAL_SECONDS (Chapter 24); @Scheduled works too for a fixed rate. */
@Component
public class Reaper {
    private static final Logger log = LoggerFactory.getLogger(Reaper.class);
    private final HoldService holds;
    private final TaskScheduler scheduler;
    private final AppConfig config;

    public Reaper(HoldService holds, TaskScheduler scheduler, AppConfig config) {
        this.holds = holds;
        this.scheduler = scheduler;
        this.config = config;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        if (config.reaperIntervalSeconds() == 0) return;
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                int n = holds.releaseExpired();
                if (n > 0) log.info("released {} expired holds", n);
            } catch (RuntimeException e) {
                log.error("reaper run failed", e);
            }
        }, Duration.ofSeconds(config.reaperIntervalSeconds()));
    }
}
