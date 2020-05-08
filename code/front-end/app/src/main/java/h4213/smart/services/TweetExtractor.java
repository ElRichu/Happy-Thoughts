package h4213.smart.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;

import h4213.smart.models.Tweets;

public class TweetExtractor {
    private static final String TAG = "TweetExtractor";
    private ArrayList<Tweets> listTweet;

    public TweetExtractor(){
        listTweet = new ArrayList<>();
    }

    public void extract(String json) throws ParseException, java.text.ParseException {
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.parse(json);
        JSONObject jsonObject = (JSONObject) object;

        System.out.println(jsonObject);
        JSONArray tList = (JSONArray) jsonObject.get("Tweets");

        Iterator iterator = tList.iterator();
        int i=0;

        while(iterator.hasNext()){
            JSONObject myTweet = (JSONObject) tList.get(i);

            String userName = (String) myTweet.get("username");
            String text = (String) myTweet.get("full_text");
            String date = (String) myTweet.get("date_time");
            String score = String.valueOf((Long) myTweet.get("score"));

            JSONObject detailSubCategory = (JSONObject) myTweet.get("sub_category");
            JSONObject detailCategory = (JSONObject) detailSubCategory.get("category");
            String category = (String) detailCategory.get("name");

            JSONArray arrayImage = (JSONArray) myTweet.get("medias");
            ArrayList<String> imageURL = new ArrayList<>();
            for(Object image: arrayImage){
                String url = (String) ((JSONObject)(image)).get("url");
                url = url.replace("http:", "https:");
                imageURL.add(url);
            }
            listTweet.add(new Tweets(userName,text,date, category,imageURL, score));
            iterator.next();
            i++;
        }
    }

    public ArrayList<Tweets> getListTweet() {
        return listTweet;
    }
}
