package h4213.smart.services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class CategoriesExtractor {

    private ArrayList<String> categoryNames;
    private ArrayList<ArrayList<String>> subCategoryName;

    public CategoriesExtractor(){
        categoryNames = new ArrayList<>();
        subCategoryName = new ArrayList<>();
    }


    public void extract(String json) throws  ParseException {
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.parse(json);
        JSONObject jsonObject = (JSONObject) object;

        Set<String> keys = jsonObject.keySet();
        for(String key:keys){
            categoryNames.add(key);
            JSONArray arr = (JSONArray) jsonObject.get(key);
            getSubCat(arr);
        }
    }

    private void getSubCat(JSONArray category) {

        Iterator iterator = category.iterator();
        int i=0;
        ArrayList<String> subList = new ArrayList<>();

        while(iterator.hasNext()){
            JSONObject sub = (JSONObject) category.get(i);
            Set<String> keys = sub.keySet();

            for(String key:keys){
                subList.add(key);
            }

            iterator.next();
            i++;
        }
        subCategoryName.add(subList);

    }


    public ArrayList<String> getCategoryNames() {
        return categoryNames;
    }

    public ArrayList<ArrayList<String>> getSubCategoryName() {
        return subCategoryName;
    }
}
