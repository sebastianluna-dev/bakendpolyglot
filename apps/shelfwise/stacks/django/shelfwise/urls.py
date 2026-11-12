from django.urls import path

from catalog import views

urlpatterns = [
    path("health", views.health),
    path("books", views.BookListCreate.as_view()),
    path("books/<str:pk>", views.BookDetail.as_view()),
    path("books/<str:pk>/copies", views.CopyCreate.as_view()),
    path("members", views.MemberCreate.as_view()),
    path("members/<str:pk>/loans", views.MemberLoans.as_view()),
    path("loans", views.LoanCreate.as_view()),
    path("loans/<str:pk>/return", views.LoanReturn.as_view()),
]

handler404 = "catalog.problems.not_found_view"
