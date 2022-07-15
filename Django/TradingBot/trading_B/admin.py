from django.contrib import admin

# Register your models here.
from .models import Signal, Calendar, Upcoming, Random

admin.site.register(Signal)
admin.site.register(Calendar)
admin.site.register(Upcoming)
admin.site.register(Random)
