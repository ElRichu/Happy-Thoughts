from nltk import download as dl
dl("stopwords")
dl("wordnet")  # Necessary download for classifier

from nltk.corpus import stopwords
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.corpus import wordnet as wn
import string


categories = {
    "defense.n.01": "defense",
    "europe.n.01": "europe",
    "law.n.06": "law",
    "association.n.01": "associations",
    "religion.n.01": "religions",
    "actor.n.01": "actors",
    "film_director.n.01": "directors",
    "movie.n.01": "films",
    "distributor.n.01": "distributors",
    "internet.n.01": "internet_stars",
    "comedian.n.01": "comedians",
    "festival.n.02": "festivals",
    "theater.n.01": "theater",
    "game.n.03": "videogames",
    "television.n.01": "television",
    "rock_'n'_roll.n.01": "rock",
    "pop_music.n.01": "pop",
    "rap.n.05": "rap",
    "jazz.n.02": "jazz",
    "radio.n.01": "radio",
    "football.n.01": "football",
    "rugby.n.01": "rugby",
    "cycling.n.01": "cycling",
    "athletics.n.03": "athletics",
    "tennis.n.01": "tennis",
    "basketball.n.01": "basketball",
    "sport.n.02": "press",
    "game.n.02": "olympic_games",
    "economics.n.01": "financial_press",
    "politics.n.02": "political_press",
    "culture.n.02": "cultural_press",
    "science.n.02": "scientific_press",
    "celebrity.n.01": "people_press",
    "gastronomy.n.02": "gastronomy",
    "travel.n.01": "travel",
    "fashion.n.03": "fashion",
    "health.n.01": "health",
    "museum.n.01": "museums",
    "book.n.01": "reading",
    "design.n.04": "design",
    "photography.n.01": "photography",
    "drawing.n.03": "drawing",
    "universe.n.01": "science"
}

stop = set(stopwords.words('english'))
punc = set(string.punctuation)
lemma = WordNetLemmatizer()


def tweets_categories(tweets):
    for i in tweets:
        i += [get_category(i[1])]
    return tweets


def get_category(text):
    stop_free = " ".join([i for i in text.lower().split() if i not in stop])
    punc_free = "".join([i for i in stop_free if i not in punc])
    normalized = [lemma.lemmatize(i) for i in punc_free.split()]
    score = {}
    for i in categories:
        nb_word = len(normalized)
        score[categories[i]] = 0
        for j in normalized:
            try:
                # limit the impact of words with many different senses because it could lead to misinterpretation
                nb_sense = len(wn.synsets(j))
                score[categories[i]] += wn.path_similarity(wn.synset(i), wn.synsets(j)[0])/nb_sense
                nb_word -= (nb_sense-1)/nb_sense
            except IndexError:  # word can not be found in wordnet
                nb_word -= 1    # in this case we should not count this word
            except TypeError:  # or no similarity is found
                pass           # in this case we count the word
        if nb_word == 0:  # if no word is considered interesting, the category cannot be defined
            return None
        score[categories[i]] /= nb_word  # nb_word is the same at each iteration
    maxScore = max(score, key=lambda x: score[x])
    if score[maxScore] > 0.048:
        return maxScore
    return None

