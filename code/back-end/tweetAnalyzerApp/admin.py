from django.contrib import admin

# Register your models here.
from tweetAnalyzerApp.models import *

admin.site.register(Tweet)
admin.site.register(Category)
admin.site.register(SubCategory)
admin.site.register(Subject)
admin.site.register(Media)
admin.site.register(Token)