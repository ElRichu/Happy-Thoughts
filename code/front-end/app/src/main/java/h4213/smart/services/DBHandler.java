package h4213.smart.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import h4213.smart.models.MyCallBack;

public class DBHandler {

    private static final String TAG = "DBHandler";

    private FirebaseDatabase database;
    private String userName;
    private ArrayList<String> interests;

    public DBHandler(){
        database = FirebaseDatabase.getInstance();
        userName = "name";
        interests = new ArrayList<>();
    }

    public void saveUserName(String userId, String userName){
        DatabaseReference ref = database.getReference();
        ref.child(userId).child("name").setValue(userName);
    }

    public void saveInterests(String userId, ArrayList<String> interests){
        DatabaseReference ref = database.getReference();
        // wipe interests
        ref.child(userId).child("interests").setValue(interests);
    }

    public void getUserNameFromDB(String userId, MyCallBack callBack){
        DatabaseReference ref = database.getReference(userId + "/name");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                list.add(dataSnapshot.getValue(String.class));
                callBack.onCallBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void getInterests(String userId, MyCallBack callBack){
        DatabaseReference ref = database.getReference(userId + "/interests");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> listInteret = new ArrayList<>();
                Iterable<DataSnapshot> list = dataSnapshot.getChildren();
                for(DataSnapshot item:list){
                    String interet = (String) item.getValue();
                    listInteret.add(interet);
                }
                callBack.onCallBack(listInteret);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error accessing interests in database", databaseError.toException() );
            }
        });
    }
}
