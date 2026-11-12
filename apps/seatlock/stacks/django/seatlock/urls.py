from django.urls import path

from ticketing import views, webhooks

handler404 = "ticketing.problems.not_found_view"

urlpatterns = [
    path("health", views.health),
    path("events", views.EventsView.as_view()),
    path("events/<str:event_id>", views.EventView.as_view()),
    path("events/<str:event_id>/seats", views.SeatMapView.as_view()),
    path("holds", views.HoldsView.as_view()),
    path("holds/<str:hold_id>", views.HoldView.as_view()),
    path("holds/<str:hold_id>/checkout", views.CheckoutView.as_view()),
    path("orders/<str:order_id>", views.OrderView.as_view()),
    path("webhooks/payments", webhooks.payments),
    path("admin/holds/release-expired", views.ReaperView.as_view()),
]
