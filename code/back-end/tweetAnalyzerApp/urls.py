from django.urls import include, re_path, path

from tweetAnalyzerApp.views import *

urlpatterns = [
    path('tweets/', TweetsView.as_view()),
    path('tweets/<int:id>/', TweetDetailView.as_view()),
    path('tweets-sub-category/', TweetPerSubCategoryView.as_view()),
    path('categories/', CategoriesView.as_view()),
]
