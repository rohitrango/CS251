from django.contrib import admin
from .models import *
# Register your models here.
admin.site.register(Member)
admin.site.register(Course)
admin.site.register(Feedback)
admin.site.register(FeedbackQuestion)
admin.site.register(FeedbackRatingAnswer)
admin.site.register(FeedbackMCQChoice)
admin.site.register(FeedbackShortAnswer)
admin.site.register(Assignment)
