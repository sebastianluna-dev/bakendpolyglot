"""Gunicorn settings. Each worker also runs the hold reaper on a timer (Chapter 24): the reaper is
idempotent and skips rows another worker has locked, so running it in every worker is safe."""

import os

bind = f"0.0.0.0:{os.environ.get('PORT', '3000')}"
workers = int(os.environ.get("WEB_CONCURRENCY", "2"))
threads = 8
accesslog = None


def post_worker_init(worker):
    from ticketing.reaper import start_background_reaper

    start_background_reaper()
