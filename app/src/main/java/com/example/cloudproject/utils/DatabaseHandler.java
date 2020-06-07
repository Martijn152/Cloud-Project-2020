package com.example.cloudproject.utils;

import android.util.Log;

import com.example.cloudproject.models.Hunt;
import com.example.cloudproject.models.Location;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

public class DatabaseHandler {

    private static DatabaseHandler instance;
    public FirebaseFirestore db;

    //This class should be a singleton that is used for all database calls

    public static DatabaseHandler getInstance(){
        if(instance == null){
            instance = new DatabaseHandler();
        }
        return instance;
    }

    private DatabaseHandler (){
        db = FirebaseFirestore.getInstance();
    }

    public ArrayList<Hunt> getHunts(){




        db.collection("hunts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ArrayList<Hunt> list = new ArrayList<Hunt>();

                                Log.d(TAG, document.getId() + " => " + document.getData());

                                //Getting the values from the document
                                String title = document.getId();
                                String area = document.getString("area");
                                String description = document.getString("description");
                                ArrayList<DocumentReference> locations = (ArrayList<DocumentReference>) document.get("locations");
                                String owner = document.getString("owner");

                                ArrayList<Location> locationsList = new ArrayList<Location>();
                                for (DocumentReference location : locations
                                     ) {
                                    Location newLocation = new Location(location.getId());
                                    locationsList.add(newLocation);
                                }

                                //Creating a new hunt with those values
                                Hunt hunt = new Hunt(title, description, area, owner, locationsList);

                                list.add(hunt);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return null;
    }



}
