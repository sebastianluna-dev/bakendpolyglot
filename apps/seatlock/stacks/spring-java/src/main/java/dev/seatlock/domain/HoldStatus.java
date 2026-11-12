package dev.seatlock.domain;

import java.time.Instant;

public final class HoldStatus {
    private HoldStatus() {}

    public static final String ACTIVE = "active", CHECKED_OUT = "checked_out", RELEASED = "released", EXPIRED = "expired";

    /** An active hold past its expiry is expired, whether or not the reaper has run yet. */
    public static String visible(String status, Instant expiresAt, Instant now) {
        return ACTIVE.equals(status) && !expiresAt.isAfter(now) ? EXPIRED : status;
    }
}
