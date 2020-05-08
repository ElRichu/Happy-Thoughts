# Happy Thougths

Cette application a pour but de montrer des tweets positifs, qui mettent de bonne humeur. Il n'est pas nécessaire de posséder un compte twitter, il suffit de renseigner certains centres d'intérêts au démarrage de l'application. L'application affichera ensuite les tweets jugés positifs en relation avec ces centres d'intérêts. 

L'application est divisée en 2 parties, le front-end qui est une application android, et le back-end qui est un serveur django. 

# Installation
- installer via SSH:
```
git clone git@gitlab.com:ElRichu/smart.git 
```
- installer via HTTP:
```
git clone https://gitlab.com/ElRichu/smart.git
```
### _Back-end_

Pour le back-end, il suffit d'installer les dépendances python nécessaire. Il faut donc tout d'abord avoir python>3.7 instalé et l'utilitaire python pip. A la racine /back-end il suffit ensuite de lancer la commande : 
``` 
python -m pip install -r requirements.txt 
```
Ensuite pour lancer le serveur, il faut lancer la commande :
```
python manage.py runserver <server_ip>:<port>
``` 
Le serveur est alors déployé à l'adresse ip et sur le port indiqué.

Pour mettre à jour la base de données avec de nouveau Tweets, on peut le faire manuellement en lançant la commande : 
```
python manage.py update_tweet_database
``` 
On peut aussi mettre en place une mise à jour automatique et régulière de la base de données en appelant la commande précédent via l'utilitaire "at" sous Windows ou "cron" sous UNIX. 

### _Front-end_

Créer un projet avec Android Studio ([download](https://developer.android.com/studio)), qui prends comme racine /front-end. 

Créer un émulateur de téléphone (AVD) ([documentation](https://developer.android.com/studio/run/managing-avds)). Ceci va permettre de lancer l'application sur l'ordinateur. Choisir l'image du téléphone souhaité, les tests ont été effectués avec le Pixel XL API 28. Il faut un niveau d'API à minimum 24 (Android 7.0 Nougat) pour pouvoir utiliser toutes les fonctionnalités de l'application. 

Modifier l'adresse du serveur Django. L'adresse est indiquée dans le fichier de resource string.xml (/front-end/app/src/main/res/values/string.xml) dans le texte de la balise __<string__ _name="tweets\_server\_address"_ __>__ adresse du serveur __</string>__.

Lancer le build avec une configuration par défault. 

Lancer l'AVD.

# Utilisation

Au lancement de l'application  il est demandé de s'authentifier. La première fois il faut se "sign up". Rentrer son adresse mail, un mot de passe et son nom pour une authentification classique, ou bien il est possible de s'authentifier via twitter ou google. 
Puis il faut renseigner ses centres d'intérêts via plusieurs catégories et sous-catégories.

Enfin on arrive sur la page d'accueil de l'application. Dès lors, on peut voir s'afficher les tweets qui devraient vous mettre de bonne humeur. Il est possible d'aller changer quelques paramètres dans l'onglet "paramètre" en bas de la barre de navigation qui est à gauche, comme changer son mot de passe, se déconnecter, modifier ses centres d'intérêts. 

Il est également possible de filtrer les tweets pour les afficher par catégorie, comme sport, science, autres. Pour cela il faut jouer un peu avec les boutons sur la gauches, en sachant que les icones Horloge et Rouage ne sont pas des filtres contrairement aux autres.  

Il est aussi possible de mettre une alarme, qui va lire les tweets à l'heure indiquée. Rien de mieux pour se lever de bonne humeur le matin. Pour cela, aller dans l'onglet identifié par une petite horloge. Choisir ensuite l'heure à laquelle on veut que son alarme sonne, cliquer sur le bouton pour activer l'alarme et attendre que ça sonne. Une douce voix lira les tweets affichés.
