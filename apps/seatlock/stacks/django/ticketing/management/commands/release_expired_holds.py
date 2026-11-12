from django.core.management.base import BaseCommand

from ticketing.services import release_expired_holds


class Command(BaseCommand):
    help = "Release seats whose hold expired (for cron or a Kubernetes CronJob)."

    def handle(self, *args, **options):
        self.stdout.write(f"released {release_expired_holds()} holds")
