from django.urls import path
from . import views

urlpatterns = [
    path('', views.index),
    path("signal", views.signal),
    path("calendar", views.calendar),
    path("upcoming", views.upcoming),
    path("random", views.random)
]