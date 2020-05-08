from django.core.management.base import BaseCommand

from TweetsRequest import *
from category_getter import *
from tweetAnalyzerApp.models import *
from datetime import datetime, timedelta, time


class Command(BaseCommand):

    def handle(self, *args, **options):
        self.stdout.write("Updating database with new tweets : ")
        self.stdout.write("Retrieving tweets.")
        tweet_list = request_tweet_sans_compte(400)
        self.stdout.write("Retrieved {} positive tweets.".format(len(tweet_list)))
        self.stdout.write("Categorizing tweets.")
        tweet_list = tweets_categories(tweet_list)
        for array in tweet_list:

            if not Tweet.objects.filter(id=array[0].id).exists():
                tweet = Tweet()
                tweet.date_time = array[0].created_at
                tweet.id = array[0].id
                if array[3] is not None:
                    try:
                        print(array[3])
                        tweet.sub_category = SubCategory.objects.get(name=array[3])
                    except Tweet.DoesNotExist:
                        tweet.sub_category = None
                else:
                    tweet.sub_category = SubCategory.objects.get(name="other")
                tweet.name = array[0].user.name
                tweet.username = array[0].user.screen_name
                tweet.full_text = array[1]
                tweet.score = array[2]
                tweet.save()

                media = None
                if 'media' in array[0].entities:
                    for image in array[0].extended_entities['media']:
                        if image['media_url'] is not None and image['media_url'] != "":
                            media_query = Media.objects.filter(url=image['media_url'])
                            if not media_query.exists():
                                media = Media()
                                media.url = image['media_url']
                                media.save()
                                tweet.medias.add(media)
                            else:
                                tweet.medias.add(media_query.first())
