package h4213.smart.models;

import java.util.ArrayList;

public class Interests {

    private ArrayList<String> interests;
    private static Interests instance = null;

    private Interests(){
        interests = new ArrayList<>();
    }

    public static Interests getInstance(){
        if(instance == null){
            instance = new Interests();
        }

        return instance;
    }

    public void addInterest(String interet){
        interet = interet.replaceAll(" ", "");
        this.interests.add(interet);
    }

    public void removeInteret(String interet){
        interet = interet.replaceAll(" ", "");
        if(this.interests.contains(interet))
            this.interests.remove(interet);
    }

    public boolean containInteret(String interet){
        interet = interet.replaceAll(" ", "");
        if(this.interests.contains(interet))
            return true;

        return false;
    }

    public void clear(){
        interests.clear();
    }

    public ArrayList<String> getInterests(){
        return this.interests;
    }

}
