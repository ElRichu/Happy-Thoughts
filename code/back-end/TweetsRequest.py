import tweepy
import sys
import meaningcloud
import pandas
import json

def request_tweet_sans_compte(nombreTweet):

    auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
    auth.set_access_token('1257599001433706496-HKChl1uTs0M7aHo2B80dk4Yy9mwyh4', 'VE2qcM0lGIY4ux1HGOaz4y2jfuIVZ0qMCMrSbPJkdadRM')

    api = tweepy.API(auth)
    page_list=[]
    tweets =[]
    n = 0
    if (nombreTweet>200):
        for page in tweepy.Cursor(api.home_timeline, count=200,tweet_mode="extended").pages(int(nombreTweet/200)):
            page_list.append(page)
            n = n+1
            print(n)

        for page in page_list:
            for tweet in page:
                tweets.append(tweet)
    else:
        tweets = api.home_timeline(count=nombreTweet,tweet_mode="extended")

    liste_tweets=[]
    liste_ids=[]


    for tweet in tweets:
        liste_tweets.append(tweet.full_text)
        liste_ids.append(tweet.id)
        



    model = 'IAB_en'
    license_key = 'af76f805c5f86590fee318303f3d3ac3'

    result=[]
    i = -1
    for tweet in liste_tweets:
        i = i+1
        if len(tweet.split(' ')) >= 10:
            sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang='en', txt=tweet, txtf='plain').sendReq())
            score = sentiment_response.getResponse().get('score_tag')
            if (str(score) == "P"):
                result.append([tweets[i],tweet,1])
            if (str(score) == "P+"):
                result.append([tweets[i],tweet,2])


    return result


def request_tweet_avec_compte(nombreTweet, token, tokenSecret):


    auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
    auth.set_access_token('token', 'tokenSecret')

    api = tweepy.API(auth)
    page_list=[]
    tweets =[]
    n = 0
    if (nombreTweet>200):
        for page in tweepy.Cursor(api.home_timeline, count=200, tweet_mode="extended").pages(int(nombreTweet/200)):
            page_list.append(page)
            n = n+1
            print (n)

        for page in page_list:
            for tweet in page:
                tweets.append(tweet)
    else:
        tweets = api.home_timeline(count=nombreTweet,tweet_mode="extended")

    liste_tweets=[]
    liste_ids=[]

    for tweet in tweets:
        liste_tweets.append(tweet.full_text)
        liste_ids.append(tweet.id)



    model = 'IAB_en'
    license_key = 'af76f805c5f86590fee318303f3d3ac3'

    result=[]
    i = -1
    for tweet in liste_tweets:
        i = i+1
        if len(tweet.split(' ')) >= 10:
            sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang='en', txt=tweet, txtf='plain').sendReq())
            score = sentiment_response.getResponse().get('score_tag')
            if (str(score) == "P"):
                result.append([tweets[i],tweet,1])
            if (str(score) == "P+"):
                result.append([tweets[i],tweet,2])


    return result

def analysis_text_blob(tweets):

    sentiment_objects = [TextBlob(text) for text in tweets]

    sentiment_values = []
    for tweet in sentiment_objects:
        sentiment_values.append([tweet.sentiment.polarity, str(tweet)])

    return sentiment_values


def request_tweet():
    auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
    auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN',
                          'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

    api = tweepy.API(auth)

    tweets = api.home_timeline(tweet_mode="extended", include_rts=False, count=50)

    liste_tweets = []
    liste_id = []

    for tweet in tweets:
        liste_tweets.append(tweet.full_text.split('http')[0])
        liste_id.append(tweet.id)

    model = 'IAB_en'
    license_key = 'af76f805c5f86590fee318303f3d3ac3'

    i = 0
    liste_sentiment = []
    for tweet in liste_tweets:
        sentiment_response = meaningcloud.SentimentResponse(
            meaningcloud.SentimentRequest(license_key, lang='en', txt=tweet, txtf='plain').sendReq())
        liste_sentiment.append([str(tweet), liste_id[i], sentiment_response.getResponse().get('score_tag')])
        i = i + 1

    df = pandas.DataFrame(liste_sentiment, columns=["tweet", "id", "polarity"])

    for i in range(len(df)):
        print(df.loc[[i], ["tweet"]].values)
        print(df.loc[[i], ["id"]].values)
        print(df.loc[[i], ["polarity"]])

    id_list = []
    for i in range(len(df)):
        if (str(df.iloc[i]["polarity"]) == "P" or str(df.iloc[i]["polarity"]) == "P+"):
            id_list.append(id_list(df.iloc[i]["id"]))

    return id_list
