"""The hold reaper as a background thread (one per worker). Chapter 24 names Celery beat or cron as
the usual homes for this; a thread keeps the companion app to one process type."""

import logging
import threading
import time

from django.conf import settings
from django.db import close_old_connections

from .services import release_expired_holds

log = logging.getLogger("seatlock.reaper")


def _loop(interval: int) -> None:
    while True:
        time.sleep(interval)
        try:
            released = release_expired_holds()
            if released:
                log.info("released %d expired holds", released)
        except Exception:  # keep the loop alive; the next run retries
            log.exception("reaper run failed")
        finally:
            close_old_connections()


def start_background_reaper() -> None:
    interval = settings.SEATLOCK["REAPER_INTERVAL_SECONDS"]
    if interval > 0:
        threading.Thread(target=_loop, args=(interval,), name="hold-reaper", daemon=True).start()
