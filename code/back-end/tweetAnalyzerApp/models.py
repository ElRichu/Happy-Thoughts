from django.db import models


# Create your models here.

class Media(models.Model):
    url = models.URLField()
    tweet = models.ManyToManyField("Tweet", related_name='medias', symmetrical=True, blank=True)

    def __str__(self):
        return self.url


class Token(models.Model):
    token = models.CharField(max_length=80)
    secret_token = models.CharField(max_length=80)
    user = models.CharField(max_length=80)

    def __str__(self):
        return self.token + " " + self.secret_token


class Category(models.Model):
    name = models.CharField(max_length=100)

    def __str__(self):
        return self.name


class SubCategory(models.Model):
    name = models.CharField(max_length=100)
    category = models.ForeignKey(Category, on_delete=models.CASCADE, null=True, blank=True)

    def __str__(self):
        return self.name


class Subject(models.Model):
    name = models.CharField(max_length=100)
    sub_category = models.ForeignKey(SubCategory, on_delete=models.CASCADE, null=True, blank=True)

    def __str__(self):
        return self.name


class Tweet(models.Model):
    id = models.BigIntegerField(primary_key=True)
    full_text = models.CharField(max_length=280)
    username = models.CharField(max_length=80)
    name = models.CharField(max_length=80)
    score = models.SmallIntegerField()
    date_time = models.DateTimeField()
    sub_category = models.ForeignKey(SubCategory, on_delete=models.CASCADE, null=True, blank=True)
    token = models.ForeignKey(Token, on_delete=models.CASCADE, null=True, blank=True)

    def __str__(self):
        return "ID : {}\nUSERNAME : {}\nNAME : {}\nTEXT : {}\nDATE : {}".format(self.id, self.username, self.name,
                                                                                self.full_text, self.date_time)
