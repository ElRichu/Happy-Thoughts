#SOMMAIRE:
#Ligne 15 : Recuperation tweets positif depuis Timeline
#Ligne 70 : Recuperation categorie tweet (fonctionnel mais pas super efficace)
#Ligne 190 : Recuperation des tweets positfs gérant différantes langues
#Ligne 260 : Code complet : Récupère tweets (lemonde et abc) -> Identifie catégories -> Cherche positivité -> Renvoi id des positif
#Ligne 400 : autre...






######################RECUPERATION TWEET POSITIF DEPUIS TIMELINE###################


import tweepy
import sys
import meaningcloud
import pandas

auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN', 'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

api = tweepy.API(auth)

tweets = api.home_timeline(tweet_mode="extended", include_rts=False, count=50)

liste_tweets=[]
liste_id=[]

for tweet in tweets:
    liste_tweets.append(tweet.full_text.split('http')[0])
    liste_id.append(tweet.id)



model = 'IAB_en'
license_key = 'af76f805c5f86590fee318303f3d3ac3'


i=0;
liste_sentiment = []
for tweet in liste_tweets:
    sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang='en', txt=tweet, txtf='plain').sendReq())
    liste_sentiment.append([str(tweet),liste_id[i], sentiment_response.getResponse().get('score_tag')])
    i=i+1



df = pandas.DataFrame(liste_sentiment, columns=["tweet", "id", "polarity"])


for i in range (len(df)):
    print(df.loc[[i],["tweet"]].values)
    print(df.loc[[i],["id"]].values)
    print(df.loc[[i], ["polarity"]] )


for i in range(len(df)):
    if (str(df.iloc[i]["polarity"]) == "P" or str(df.iloc[i]["polarity"]) == "P+"):
        print(df.iloc[i]["id"])







######################RECUPERATION CATEGORIES TWEETS (version de Clement sera surement mieux)###################

import json

auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN', 'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

api = tweepy.API(auth)


tweets = api.search(q="from:ABC",tweet_mode='extended', count=50)





liste_tweets=[]


for tweet in tweets:
    liste_tweets.append(tweet.full_text.split('http')[0])


json_data = '''{
 "institutions": [
   { "defense": ["Army","Department of Defense","Security","Florence Parly","Ryan McCarthy"] },
   { "international relations": ["ONU","FMI","OMC","World Bank","summit","Foreign Affairs"] },
   { "europe": ["Ursula Von der Leyen","Charles Michel","David Sassoli","Jeppe Tranholm-Mikkelsen","euro","Commission européenne","Conseil européen","Parlement européen","Espace Schengen"] },
   { "law": ["justice","criminal justice","lawyer","military justice","rights","social justice"] },
   { "associations": ["Cure Violence","Oxfam","Handicap International","Mercy Corps","Acumen","Skoll Fondation","Médecins sans frontières"] },
   { "religions": ["christianity ","Islam ","Atheist","Agnostic","Hinduism","Buddhism ","Spiritism ","Judaism ","Shinto "] }
 ],
 "entertainment": [
   { "actors": ["Johnny Depp","Ben Affleck","Kevin Spacey","Robert De Niro"," Brad Pitt","Tom Hanks","Leonardo DiCaprio","Scarlett Johansson","Cameron Diaz","Jennifer Aniston","Sally Field"," Amy Adams"," Angelina Jolie","Jennifer Lawrence","Gérard Depardieu","Jean-Paul Belmondo","Jean Dujardin","Daniel Auteuil","Vincent Cassel","Catherine Deneuve","Marion Cotillard","Leïla Bekhti","Géraldine Nakache" ] },
   { "directors": ["Steven Spielberg","Martin Scorsese","James Cameron","Christopher Nolan","David Fincher","Quentin Tarantino","Clint Eastwood","Ridley Scott","Woody Allen","Tim Burton","Luc Besson","Jacques Audiard"] },
   { "films": ["Le Parrain","Pulp Fiction","Titanic","Forrest Gumps","La liste de Scheindler","Star Wars","Avengers","Indiana Jones","Harry Potter","Le Seigneur des anneaux","Blade Runner","Le temps d’un automne","Gladiator","Fifty shades of grey","Qu’est ce qu’on a fait au bon dieu","Le Prénom","La vie d’adèle","Camping","OSS 117","Amélie Poulain"] },
   { "distributors": ["Walt Disney","Sony","Universal","20th Century Fox","Dreaworks","Lionsgate"] },
   { "internet stars": ["Cyprien","Norman","Le rire jaune","Gloden Moustache","Natoo","Seb la frite","EnjoyPhenix","PewDiePie","Dude Perfect","Smosh","Ryan Higa"] },
   { "comedians": ["Dave Chappelle","Jerry Seinfeld","Gad Elmaleh","Blanche Gardin","Kev Adams","Florence Foresti","Jamel Debbouze","Dany Boon","Franck Dubosc","Elie Semoun"] },
   { "festivals": ["Oktoberfest","Rio Carnaval","Coachella","Eurockéennes de Belfort","Bonnaroo"] },
   { "theatre and opera": ["La comédie française","The Royal Opera House","The Burgtheater","The Teatro La Fenice","The Bolshoi","The Metropolitan Opera","Sydney Opera House","Tartuffe","L’avare","Le mariage de Figaro","Cyrano de bergerac","Hamlet","Roméo et Juliette"] },
   { "videogames": [ "Twitch","minecraft","League of Legends","TFT","Zerator"] }
 ],
 "music": [
   { "rock": ["The Beatles","The Rolling Stones","Pink Floyd","AC/DC","Bob Dylan","Queen","Maroon 5","Coldplay"] },
   { "pop": ["Beyoncé","Taylor Swift","Lady Gaga","Justin Bieber","Rihanna","Madonna","Britney Spears","Ariana Grande","Selena Gomez","Mariah Carey","Bruno Mars","Adele","Miley Cirus","Ed Sheeran","Jay-Z","Shakira","Shawn Mendes","Charlie Puth","Adam Levine"] },
   { "rap": ["Eminem","Cardi B ","Drake","Nekfeu","Orelsan","Lomepal","MC Solaar"] },
   { "jazz": ["Miles Davis","Louis Armstrong ","Duke Ellington","Emma Fitzgerald","Charlie Parker"]},
   { "electro": ["Avicii","Skrillex","Steve Agello","Sebastian Ingrosso"] },
   { "variety": ["Céline Dion","Angèle","Zaz","Jenifer","Amel Bent","Chritophe Maé","Vitaa","Patrick Bruel","Garou","Calogero","Kendji","Slimane"] },
   { "radio": ["NRJ","RTL","RLC","Fun","Skyrock","RMC","Europe 1","France Inter"] }
 ],
 "sports": [
   { "football": ["Lionel Messi","Cristiano Ronaldo","Zinédine Zidane","Zlatan Ibrahimovic","David Beckham","Mbappé","Neymar","Manchester United","Liverpool","Arsenal","Real Madrid","FC Barcelone","Inter Milan","PSG","OGC Nice","Dortmund"] },
   { "rugby": ["All Blacks","Englad Rugby","wallabies","WRU","Equipe de France","Dan Carter","Jonah Lomu","Richie McCaw","Brian O’Driscoll"] },
   { "Cycling": ["Lance Amstrong","Tour de France","Christopher Froome","Romain Bardet","Egan Bernal","Bradley Wiggins"] },
   { "Athletics": ["Usain Bolt","Running","Jumping","Throwing event","Combined events"]},
   { "tennis": ["Open d’Australie","Wimbledon","Roland Garros","US Open","Federer","Nadal","Novak Djokovic","Gaël Monfils","Richard Gasquet","Serena Williams","Maria Sharapova","Martina Navratilova","Steffi Graff"] },
   { "basketball": ["NBA","LeBron James","Michael Jordan","Kobe Bryant","Kim Tillie","Kevin Tillie","Tony Parker","Lakers","Celtics","Chicago Bulls","Rockets de Houston"] },
   { "press": ["l’équipe","sport 21"] },
   { "olympic games": ["Paris 2021","winter games","summer games"] }
 ],
 "television": [
   { "international channels": ["Disney Channel","BBC","Canal+","Euronews","MTV"] },
   { "presenters": ["Ellen DeFeneres","Graham Norton","Oprah Winfrey","Jummy Fallon","Nagui","Nikos Aliagas","Michel Drucker","Laurent Riquier","Stéphane Bern"] },
   { "series": ["Game of Thrones","Breaking Bad","Stranger Things","Black Mirror","The Big Bang Theory","Westworld","Narcos","La casa de papel","Grey’s Anatomy","desperate housewives"] },
   { "reality TV": ["The Bachelor","American Idol","the Voice","Top Chef","Dancing with the stars","koh-lanta","Pekin express","LEs Anges","Questions pour un champion","Qui veut gagner des millions"]}
 ],
 "current affairs": [
   { "financial press": ["L’Economiste","New York Financial Press","Solomon"] },
   { "international press": ["Courrier international","Le Monde","UPI"] },
   { "political press": ["American Politics Science Review","European Journal of International Relations","Foreign Policy","New Political Economy","World Politics"]},
   { "cultural press": ["Art Press","Beaux Arts Magazine ","Univers des arts"] },
   { "scientific press": ["World Scientific","Science et vie junior","europa Bio","The national academies Press"] },
   { "people press": ["Voici","Purepeople","Closer","Elle"] }
 ],
 "lifestyle": [
   { "gastronomy": ["French gastronomy","Mirazur","Noma","Asador Etxebarri","best chef award","Virgilio Martinez","paul bocuse","european gastronomy","american food","asiatic food"] },
   { "travel": ["fly","company","travelling","hitchhiking","America","Africa","Europe","Asia","Oceania"] },
   { "fashion": ["fashion week","LMVH","Louis Vuitton","Channel","Dior","H&M","Pull&Bear","Primark"]},
   { "health": ["nutrition","diet","medicine","OMS"]}
 ],
 "culture": [
   { "museums": ["Louvre Museum","British Museum","Metropolitan Museum of New York","Vatican Museum","National Gallery","Prado Museum","Orsay Museum","Museum of Modern  Art","Smithsonian Institution","Chapelle Sixtine","Guggenheim"] },
   { "reading": ["Pride and Prejudice","To kill a Mockingbird","The great Gatsby","Into the wild","Persuasion","Moby-Dick","Frankenstein","Nineteen Eighty-Four","The Adventures of Huckleberry Finn"] },
   { "design": ["architecture","house architecture","modern","vintage","recycling"] },
   { "photography": ["landscape","black and white picture","food photography"]},
   { "drawing": ["Vincent van Gogh","Monet","Picasso","Léonard de Vinvi","Dali","Michel-Ange","Renoir","Matisse","Gustave Klimt","Frida Kahlo","Manet","Jackson Pollock","street art","Banksy"]},
   { "science and space": ["space travel","Mercury","Apollo Program","Pioneer","galaxy"]}
 ]
}'''

parsed_json = (json.loads(json_data))
#print(json.dumps(parsed_json, indent=4, sort_keys=True))

loaded_json = json.loads(json_data)
#print(type(json.dumps(parsed_json, indent=4, sort_keys=True)))


for tweet in liste_tweets:
    for key1, value1 in parsed_json.items():
        for element in value1:
            for key2, value2 in element.items():
                if (key2 in tweet):
                    print("Tweet sélectionné -> " , tweet)
                    print("Detection mot :", key2, "En rapport avec thème :" , key1)
                    print("----------------------")
                for mot in value2:
                    if (mot in tweet):
                        print("Tweet sélectionné -> " , tweet)
                        print("Detection mot :", mot, "En rapport avec sous thème :" , key2 , "En rapport avec thème :", key1)
                        print("----------------------")








#############################RECUPERATION DES TWEETS POSITIFS COMME EN 1 MAIS AVEC LA GESTION DES LANGUES##########################
from langdetect import detect

auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN', 'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

api = tweepy.API(auth)

tweets = api.home_timeline(tweet_mode="extended", include_rts=False, count=50)

liste_tweets=[]
liste_id=[]

for tweet in tweets:
    liste_tweets.append(tweet.full_text.split('http')[0])
    liste_id.append(tweet.id)


liste_tweetsFR=[]
liste_tweetsEN=[]
liste_idFR=[]
liste_idEN=[]
i = 0;
for tweet in liste_tweets:
    try:
        if(detect(tweet) == 'en'):
            liste_tweetsEN.append(tweet)
            liste_idEN.append(liste_id[i])
        if(detect(tweet) == 'fr'):
            liste_tweetsFR.append(tweet)
            liste_idFR.append(liste_id[i])
    except:
        print("erreur")
    i = i+1;


model = 'IAB_en'
license_key = 'af76f805c5f86590fee318303f3d3ac3'


i=0;
liste_sentiment = []
for tweet in liste_tweetsEN:
    sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang='en', txt=tweet, txtf='plain').sendReq())
    liste_sentiment.append([str(tweet),liste_idEN[i], sentiment_response.getResponse().get('score_tag')])
    i=i+1
i=0;
for tweet in liste_tweetsFR:
    sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang='fr', txt=tweet, txtf='plain').sendReq())
    liste_sentiment.append([str(tweet),liste_idFR[i], sentiment_response.getResponse().get('score_tag')])
    i=i+1


df = pandas.DataFrame(liste_sentiment, columns=["tweet", "id", "polarity"])


for i in range (len(df)):
    print(df.loc[[i],["tweet"]].values)
    print(df.loc[[i],["id"]].values)
    print(df.loc[[i], ["polarity"]] )


for i in range(len(df)):
    if (str(df.iloc[i]["polarity"]) == "P" or str(df.iloc[i]["polarity"]) == "P+"):
        print(df.iloc[i]["id"])





################CODE PYTHON COMPLET : Récupère tweets (lemonde et abc) -> Identifie catégories -> Cherche positivité -> Renvoi id des positif########

import json
from langdetect import detect


auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN', 'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

api = tweepy.API(auth)


tweets = api.search(q="from:ABC",tweet_mode='extended', count=50)
tweets2 = api.search(q="from:lemondefr",tweet_mode='extended', count=50)
tweets = tweets + tweets2

liste_tweets=[]
liste_ids=[]

for tweet in tweets:
    liste_tweets.append(tweet.full_text.split('http')[0])
    liste_ids.append(tweet.id)



model = 'IAB_en'
license_key = 'af76f805c5f86590fee318303f3d3ac3'



json_data = '''{
 "institutions": [
   { "defense": ["Army","Department of Defense","Security","Florence Parly","Ryan McCarthy"] },
   { "international relations": ["ONU","FMI","OMC","World Bank","summit","Foreign Affairs"] },
   { "europe": ["Ursula Von der Leyen","Charles Michel","David Sassoli","Jeppe Tranholm-Mikkelsen","euro","Commission européenne","Conseil européen","Parlement européen","Espace Schengen"] },
   { "law": ["justice","criminal justice","lawyer","military justice","rights","social justice"] },
   { "associations": ["Cure Violence","Oxfam","Handicap International","Mercy Corps","Acumen","Skoll Fondation","Médecins sans frontières"] },
   { "religions": ["christianity ","Islam ","Atheist","Agnostic","Hinduism","Buddhism ","Spiritism ","Judaism ","Shinto "] }
 ],
 "entertainment": [
   { "actors": ["Johnny Depp","Ben Affleck","Kevin Spacey","Robert De Niro"," Brad Pitt","Tom Hanks","Leonardo DiCaprio","Scarlett Johansson","Cameron Diaz","Jennifer Aniston","Sally Field"," Amy Adams"," Angelina Jolie","Jennifer Lawrence","Gérard Depardieu","Jean-Paul Belmondo","Jean Dujardin","Daniel Auteuil","Vincent Cassel","Catherine Deneuve","Marion Cotillard","Leïla Bekhti","Géraldine Nakache" ] },
   { "directors": ["Steven Spielberg","Martin Scorsese","James Cameron","Christopher Nolan","David Fincher","Quentin Tarantino","Clint Eastwood","Ridley Scott","Woody Allen","Tim Burton","Luc Besson","Jacques Audiard"] },
   { "films": ["Le Parrain","Pulp Fiction","Titanic","Forrest Gumps","La liste de Scheindler","Star Wars","Avengers","Indiana Jones","Harry Potter","Le Seigneur des anneaux","Blade Runner","Le temps d’un automne","Gladiator","Fifty shades of grey","Qu’est ce qu’on a fait au bon dieu","Le Prénom","La vie d’adèle","Camping","OSS 117","Amélie Poulain"] },
   { "distributors": ["Walt Disney","Sony","Universal","20th Century Fox","Dreaworks","Lionsgate"] },
   { "internet stars": ["Cyprien","Norman","Le rire jaune","Gloden Moustache","Natoo","Seb la frite","EnjoyPhenix","PewDiePie","Dude Perfect","Smosh","Ryan Higa"] },
   { "comedians": ["Dave Chappelle","Jerry Seinfeld","Gad Elmaleh","Blanche Gardin","Kev Adams","Florence Foresti","Jamel Debbouze","Dany Boon","Franck Dubosc","Elie Semoun"] },
   { "festivals": ["Oktoberfest","Rio Carnaval","Coachella","Eurockéennes de Belfort","Bonnaroo"] },
   { "theatre and opera": ["La comédie française","The Royal Opera House","The Burgtheater","The Teatro La Fenice","The Bolshoi","The Metropolitan Opera","Sydney Opera House","Tartuffe","L’avare","Le mariage de Figaro","Cyrano de bergerac","Hamlet","Roméo et Juliette"] },
   { "videogames": [ "Twitch","minecraft","League of Legends","TFT","Zerator"] }
 ],
 "music": [
   { "rock": ["The Beatles","The Rolling Stones","Pink Floyd","AC/DC","Bob Dylan","Queen","Maroon 5","Coldplay"] },
   { "pop": ["Beyoncé","Taylor Swift","Lady Gaga","Justin Bieber","Rihanna","Madonna","Britney Spears","Ariana Grande","Selena Gomez","Mariah Carey","Bruno Mars","Adele","Miley Cirus","Ed Sheeran","Jay-Z","Shakira","Shawn Mendes","Charlie Puth","Adam Levine"] },
   { "rap": ["Eminem","Cardi B ","Drake","Nekfeu","Orelsan","Lomepal","MC Solaar"] },
   { "jazz": ["Miles Davis","Louis Armstrong ","Duke Ellington","Emma Fitzgerald","Charlie Parker"]},
   { "electro": ["Avicii","Skrillex","Steve Agello","Sebastian Ingrosso"] },
   { "variety": ["Céline Dion","Angèle","Zaz","Jenifer","Amel Bent","Chritophe Maé","Vitaa","Patrick Bruel","Garou","Calogero","Kendji","Slimane"] },
   { "radio": ["NRJ","RTL","RLC","Fun","Skyrock","RMC","Europe 1","France Inter"] }
 ],
 "sports": [
   { "football": ["Lionel Messi","Cristiano Ronaldo","Zinédine Zidane","Zlatan Ibrahimovic","David Beckham","Mbappé","Neymar","Manchester United","Liverpool","Arsenal","Real Madrid","FC Barcelone","Inter Milan","PSG","OGC Nice","Dortmund"] },
   { "rugby": ["All Blacks","Englad Rugby","wallabies","WRU","Equipe de France","Dan Carter","Jonah Lomu","Richie McCaw","Brian O’Driscoll"] },
   { "Cycling": ["Lance Amstrong","Tour de France","Christopher Froome","Romain Bardet","Egan Bernal","Bradley Wiggins"] },
   { "Athletics": ["Usain Bolt","Running","Jumping","Throwing event","Combined events"]},
   { "tennis": ["Open d’Australie","Wimbledon","Roland Garros","US Open","Federer","Nadal","Novak Djokovic","Gaël Monfils","Richard Gasquet","Serena Williams","Maria Sharapova","Martina Navratilova","Steffi Graff"] },
   { "basketball": ["NBA","LeBron James","Michael Jordan","Kobe Bryant","Kim Tillie","Kevin Tillie","Tony Parker","Lakers","Celtics","Chicago Bulls","Rockets de Houston"] },
   { "press": ["l’équipe","sport 21"] },
   { "olympic games": ["Paris 2021","winter games","summer games"] }
 ],
 "television": [
   { "international channels": ["Disney Channel","BBC","Canal+","Euronews","MTV"] },
   { "presenters": ["Ellen DeFeneres","Graham Norton","Oprah Winfrey","Jummy Fallon","Nagui","Nikos Aliagas","Michel Drucker","Laurent Riquier","Stéphane Bern"] },
   { "series": ["Game of Thrones","Breaking Bad","Stranger Things","Black Mirror","The Big Bang Theory","Westworld","Narcos","La casa de papel","Grey’s Anatomy","desperate housewives"] },
   { "reality TV": ["The Bachelor","American Idol","the Voice","Top Chef","Dancing with the stars","koh-lanta","Pekin express","LEs Anges","Questions pour un champion","Qui veut gagner des millions"]}
 ],
 "current affairs": [
   { "financial press": ["L’Economiste","New York Financial Press","Solomon"] },
   { "international press": ["Courrier international","Le Monde","UPI"] },
   { "political press": ["American Politics Science Review","European Journal of International Relations","Foreign Policy","New Political Economy","World Politics"]},
   { "cultural press": ["Art Press","Beaux Arts Magazine ","Univers des arts"] },
   { "scientific press": ["World Scientific","Science et vie junior","europa Bio","The national academies Press"] },
   { "people press": ["Voici","Purepeople","Closer","Elle"] }
 ],
 "lifestyle": [
   { "gastronomy": ["French gastronomy","Mirazur","Noma","Asador Etxebarri","best chef award","Virgilio Martinez","paul bocuse","european gastronomy","american food","asiatic food"] },
   { "travel": ["fly","company","travelling","hitchhiking","America","Africa","Europe","Asia","Oceania"] },
   { "fashion": ["fashion week","LMVH","Louis Vuitton","Channel","Dior","H&M","Pull&Bear","Primark"]},
   { "health": ["nutrition","diet","medicine","OMS"]}
 ],
 "culture": [
   { "museums": ["Louvre Museum","British Museum","Metropolitan Museum of New York","Vatican Museum","National Gallery","Prado Museum","Orsay Museum","Museum of Modern  Art","Smithsonian Institution","Chapelle Sixtine","Guggenheim"] },
   { "reading": ["Pride and Prejudice","To kill a Mockingbird","The great Gatsby","Into the wild","Persuasion","Moby-Dick","Frankenstein","Nineteen Eighty-Four","The Adventures of Huckleberry Finn"] },
   { "design": ["architecture","house architecture","modern","vintage","recycling"] },
   { "photography": ["landscape","black and white picture","food photography"]},
   { "drawing": ["Vincent van Gogh","Monet","Picasso","Léonard de Vinvi","Dali","Michel-Ange","Renoir","Matisse","Gustave Klimt","Frida Kahlo","Manet","Jackson Pollock","street art","Banksy"]},
   { "science and space": ["space travel","Mercury","Apollo Program","Pioneer","galaxy"]}
 ]
}'''

parsed_json = (json.loads(json_data))
#print(json.dumps(parsed_json, indent=4, sort_keys=True))

loaded_json = json.loads(json_data)
#print(type(json.dumps(parsed_json, indent=4, sort_keys=True)))
result=[]
i = -1
for tweet in liste_tweets:
    i = i+1
    for key1, value1 in parsed_json.items():
        for element in value1:
            for key2, value2 in element.items():
                if (key2 in tweet):
                    print("Tweet sélectionné -> " , tweet)
                    print("Detection mot :", key2, "En rapport avec thème :" , key1)
                    sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang=detect(tweet), txt=tweet, txtf='plain').sendReq())
                    score = sentiment_response.getResponse().get('score_tag')
                    print("Le tweet a une positivité : ", score, "  Son id est : ", liste_ids[i])
                    if (str(score) == "P" or str(score) == "P+"):
                        result.append(liste_ids[i])
                    print("----------------------")
                for mot in value2:
                    if (mot in tweet):
                        print("Tweet sélectionné -> " , tweet)
                        print("Detection mot :", mot, "En rapport avec sous thème :" , key2 , "En rapport avec thème :", key1)
                        sentiment_response = meaningcloud.SentimentResponse(meaningcloud.SentimentRequest(license_key, lang=detect(tweet), txt=tweet, txtf='plain').sendReq())
                        score = sentiment_response.getResponse().get('score_tag')
                        print("Le tweet a une positivité : ", score, "  Son id est : ", liste_ids[i])
                        print("----------------------")
                        if (str(score) == "P" or str(score) == "P+"):
                            result.append(liste_ids[i])

print(result)





















import tweepy

auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN', 'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

api = tweepy.API(auth)

#search_result = api.search(q="football from:FIFA.com", lang="en", count=5)

tweets = api.home_timeline(tweet_mode="extended", include_rts=False)

for tweet in tweets:
    print(tweet.full_text)


"""
auth = tweepy.OAuthHandler('74kR1Uv1LGw3qllwqyIrIW2WH', 'GyVxV3O4Wks9akUFxRQAmXh1nVvtTnvvLi1nsMdkLt4EjqxIrO')
auth.set_access_token('999402738290233344-GB6h7ahayXolXu6MYTMVlc4VKPAbOPN', 'c0wB3SwFjiTpTWnR6fyudBX9Sel7SxUwxKeJ4FLJpaHcV')

api = tweepy.API(auth)


# test authentication
try:
    api.verify_credentials()
    print("Authentication OK")
except:
    print("Error during authentication")


public_tweets = api.home_timeline()
for tweet in public_tweets:
    print(tweet.text)
    
for ids in api.followers_ids(screen_name='Dbook_MVP'):
    print(ids)
    
timeline = api.user_timeline(user_id=1169582672370438144, count=1)
#print(timeline)

#api.search(q="Devin Booker MVP", lang="en", rpp=5)

follower = api.get_user('1006495805379837953')
print(follower.screen_name)


ids = []
for page in tweepy.Cursor(api.friends, screen_name="Dbook_MVP").pages():
    ids.extend(page)
screen_names = [user.screen_name for user in api.lookup_users(user_ids=ids)]
print(screen_names)

for friend in tweepy.Cursor(api.friends, screen_name='Dbook_MVP').items:
    print(friend.screen_name)

streamListener = StreamListener()
strem = tweepy.Stream(auth = api.auth, listener = streamListener, tweet_mode='extended')

redirect_user(auth.get_authorization_url())

# Get access token
auth.get_access_token("verifier_value")

# Construct the API instance
api = tweepy.API(auth)
"""