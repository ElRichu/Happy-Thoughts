from django.db.models import QuerySet
from django.shortcuts import render, get_object_or_404
from rest_framework import status
from rest_framework.response import Response
import json
from rest_framework.views import APIView
from tweetAnalyzerApp.models import *
from tweetAnalyzerApp.serializers import *


class CategoriesView(APIView):
    def get(self, request):
        categories = Category.objects.all()

        category_serializer = CategorySerializer(categories, many=True)
        return Response({"Categories": category_serializer.data})


class TweetsView(APIView):
    def get(self, request):
        tweets = Tweet.objects.all()

        tweet_serializer = TweetSerializer(tweets, many=True)
        return Response({"Tweets": tweet_serializer.data})


class TweetDetailView(APIView):
    def get(self, request, id):
        tweet = get_object_or_404(Tweet, id=id)
        tweet_serializer = TweetSerializer(tweet)
        return Response({"Tweets": tweet_serializer.data})


class TweetPerSubCategoryView(APIView):
    def post(self, request):
        sub_category_data = json.loads(request.body)
        print(sub_category_data)
        tweets = Tweet.objects.none()
        for sub_category in sub_category_data["interet"]:
            tweet = Tweet.objects.filter(sub_category__name=sub_category.replace(" ", "")).order_by("-date_time")[:5]
            print(tweet.values())
            tweets |= tweet
        tweet_serializer = TweetSerializer(tweets, many=True)
        return Response({"Tweets": tweet_serializer.data})


class UpdateTweetsPerUserTimeline(APIView):
    def put(self, request):
        token = request.query_params().get("token")
        secret_token = request.query_params().get("secret-token")
        username = request.query_params().get("username")
        if token is not None and secret_token is not None and username is not None:
            token_serializer = TokenSerializer()
            if token_serializer.is_valid():
                token_serializer.save()

                # UPDATE DATABASE WITH NEW TWEETS

                return Response(token_serializer.data())
            else:
                return Response(token_serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)
