package com.example.cloudproject.ui.huntlist.dummy;

import com.example.cloudproject.models.Hunt;
import com.example.cloudproject.models.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        ArrayList<Location> locations = new ArrayList<Location>();
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
        ));


    }

    private static void addItem(Hunt item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getTitle(), item);
    }
}
