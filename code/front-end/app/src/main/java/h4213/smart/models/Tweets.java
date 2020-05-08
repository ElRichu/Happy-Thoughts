package h4213.smart.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tweets {

    private String userName; // with @
    private String text;
    private Date date;
    private String category;
    private ArrayList<String> images;
    private String score;

    public Tweets(String userName, String text, String date, String category, ArrayList<String> images, String score) throws ParseException {
        this.userName = userName;
        this.text = text;
        this.category = category;
        this.images = images;
        this.score = score;

        String pattern = "YYYY-MM-dd'T'hh:mm:ss'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.date = simpleDateFormat.parse(date);
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public String affiche(){
        return "Tweet de " + userName + " -- " + text + " -- " + date;
    }

    public String toSay(){return  userName + " tweeted " + text + "\n"; }

    public String getCategory(){
        return this.category;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getScore(){
        return this.score;
    }
}
