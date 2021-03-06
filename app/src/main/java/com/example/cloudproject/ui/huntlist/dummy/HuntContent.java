package com.example.cloudproject.ui.huntlist.dummy;

import android.util.Log;

import com.example.cloudproject.models.Hunt;
import com.example.cloudproject.models.Location;
import com.example.cloudproject.utils.DatabaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class HuntContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Hunt> ITEMS = new ArrayList<Hunt>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Hunt> ITEM_MAP = new HashMap<String, Hunt>();

    private static final int COUNT = 25;

    static {
        DatabaseHandler.getInstance().db.collection("hunts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Log.d(TAG, document.getId() + " => " + document.getData());

                                //Getting the values from the document
                                String title = document.getId();
                                String area = document.getString("area");
                                String description = document.getString("description");
                                ArrayList<DocumentReference> locations = (ArrayList<DocumentReference>) document.get("locations");
                                DocumentReference owner = document.getDocumentReference("owner");

                                ArrayList<Location> locationsList = new ArrayList<Location>();
                                for (DocumentReference location : locations
                                ) {
                                    Location newLocation = new Location(location.getId());
                                    locationsList.add(newLocation);
                                }

                                //Creating a new hunt with those values
                                Hunt hunt = new Hunt(title, description, area, owner.getId(), locationsList);

                                addItem(hunt);

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


/*        ArrayList<Location> locations = new ArrayList<Location>();
        locations.add(new Location(56.0299629, 14.1501808, "Tivoli Badet", "A pool", "Martijn", "id1"));
        locations.add(new Location(56.0285586, 14.1469743, "Naturum Vattenriket", "A place", "Martijn", "id2"));
        locations.add(new Location(56.0273787, 14.153089, "Kristianstad Theater", "A theater", "Martijn", "id3"));
        locations.add(new Location(56.0312172, 14.1583827, "Systembolaget", "A liquor store", "Martijn", "id4"));
        locations.add(new Location(56.0466502, 14.1545984, "Pinocchio Pizzeria", "A pizzeria", "Martijn", "id5"));

        // Add a sample item
        addItem(new Hunt(
                "Kristianstad Top 10",
                "This hunt takes you to the top 10 most popular sightseeing locations in Kristianstad.",
                "Kristianstad, Sweden",
                "Martijn", locations
        ));

        addItem(new Hunt(
                "Kristianstad Naturum Tour",
                "This hunt takes you to several beautiful locations in the Vattenriket.",
                "Kristianstad, Sweden",
                "Martijn", locations
        ));*/

    }

    private static void addItem(Hunt item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getTitle(), item);
    }
}
