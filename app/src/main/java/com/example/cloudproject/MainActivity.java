package com.example.cloudproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudproject.models.Hunt;
import com.example.cloudproject.models.Location;
import com.example.cloudproject.ui.hunt.HuntInformationFragment;
import com.example.cloudproject.ui.hunt.HuntInformationFragmentDirections;
import com.example.cloudproject.ui.huntlist.HuntListFragment;
import com.example.cloudproject.ui.huntlist.HuntListFragmentDirections;
import com.example.cloudproject.utils.DatabaseHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements HuntListFragment.OnListFragmentInteractionListener, HuntInformationFragment.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public Hunt hunt;
    public int locationTracker = 0;
    public android.location.Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private NavController navController;
    public Location hintLocation;
    public boolean finished = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        hunt = new Hunt("You have no hunt selected!", "", "", "", new ArrayList<Location>());

        DatabaseHandler db = DatabaseHandler.getInstance();
        db.getHunts();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mCurrentLocation = location;
                            createLocationRequest();
                            startLocationUpdates();
                        }
                    }
                });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (android.location.Location location : locationResult.getLocations()) {
                    //Calculate distance here
                    //Do something if close
                    System.out.println("Current location");
                    System.out.println("Lat: " + location.getLatitude());
                    System.out.println("Long: " + location.getLongitude());


                    if (!hunt.getLocations().isEmpty()) {


                        //Then check if the hunt is already completed or not
                        if (locationTracker < hunt.getLocations().size() - 1) {

                            //Compare here
                            android.location.Location goalLocation = new android.location.Location(location);
                            goalLocation.setLatitude(hunt.getLocations().get(locationTracker).getLatitude());
                            goalLocation.setLongitude(hunt.getLocations().get(locationTracker).getLongitude());

                            System.out.println("Goal location");
                            System.out.println("Lat: " + goalLocation.getLatitude());
                            System.out.println("Long: " + goalLocation.getLongitude());

                            System.out.println("Distance:");

                            float distance = location.distanceTo(goalLocation);
                            System.out.println(distance);

                            //Then check for distance being less than this value
                            if (distance < 20.0) {
                                //This means the user found the location

                                Toast.makeText(getApplicationContext(), "You've found the location!", Toast.LENGTH_LONG).show();

                                //Load the next location
                                FirebaseFirestore db = DatabaseHandler.getInstance().db;

                                DocumentReference docRef = db.collection("locations").document(hunt.getLocations().get(locationTracker + 1).getName());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                        //If it is successful, increase the counter
                                        System.out.println(hunt.getLocations().get(locationTracker + 1).getName());

                                        String name = documentSnapshot.getId();
                                        String description = (String) documentSnapshot.get("description");
                                        double latitude = documentSnapshot.getGeoPoint("location").getLatitude();
                                        double longitude = documentSnapshot.getGeoPoint("location").getLongitude();
                                        String owner = documentSnapshot.getString("owner");
                                        String photo = documentSnapshot.getString("photo");

                                        System.out.println(name);
                                        System.out.println(description);
                                        System.out.println(latitude);
                                        System.out.println(longitude);
                                        System.out.println(owner);
                                        System.out.println(photo);

                                        Location location = new Location(latitude, longitude, name, description, owner, photo);

                                        hunt.getLocations().set(locationTracker + 1, location);


                                        //Show the details for the found location
                                        //Title, description, photo
                                        navController.navigate(R.id.nav_found_location);
                                    }
                                });
                            }
                        } else if (locationTracker < hunt.getLocations().size()) {
                            //In this case we still want to check, just not load the next one
                            //Then check for distance being less than this value

                            //Compare here
                            android.location.Location goalLocation = new android.location.Location(location);
                            goalLocation.setLatitude(hunt.getLocations().get(locationTracker).getLatitude());
                            goalLocation.setLongitude(hunt.getLocations().get(locationTracker).getLongitude());

                            System.out.println("Goal location");
                            System.out.println("Lat: " + goalLocation.getLatitude());
                            System.out.println("Long: " + goalLocation.getLongitude());

                            System.out.println("Distance:");

                            float distance = location.distanceTo(goalLocation);
                            System.out.println(distance);


                            if (distance < 20.0) {
                                //This means the user found the location

                                Toast.makeText(getApplicationContext(), "You've found the location!", Toast.LENGTH_LONG).show();
                                //Show the details for the found location
                                //Title, description, photo
                                finished = true;
                                navController.navigate(R.id.nav_found_location);
                            }
                        }
                    }
                }
            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }

            }

        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(

                R.id.nav_home, R.id.nav_gallery, R.id.nav_map, R.id.nav_hunt, R.id.nav_hunt_list)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        View headerView = navigationView.getHeaderView(0);
        TextView emailView = headerView.findViewById(R.id.nav_header_title);
        emailView.setText(mAuth.getCurrentUser().getEmail());
        TextView usernameView = headerView.findViewById(R.id.nav_header_subtitle);
        usernameView.setText(mAuth.getCurrentUser().getDisplayName());


    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            // Here we should open a new fragment for entering the other data of the location
            // It should display the photo and allow the user to add the location to the database
            // imageView.setImageBitmap(photo);
            navController.navigate(R.id.nav_new_location);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListFragmentInteraction(Hunt item) {
        //Do something with the item here
        navController.navigate(HuntListFragmentDirections.actionNavHuntListToNavHuntInformation(item));
        //We want to open another fragment and pass the item to it, so it know what to display
        //Navigate to the hunt information fragment
        //Also pass the hunt to the other fragment
    }

    @Override
    public void onFragmentInteraction(final Hunt item) {
        //This means that this hunt is now the current active hunt
        //We should store this and display it in the current hunt fragment
        //We should also store which location (as in 3 out of 5) the hunt is at
        finished = false;
        hunt = item;
        locationTracker = 0;
        FirebaseFirestore db = DatabaseHandler.getInstance().db;

        //Get the first location from the database
        DocumentReference docRef = db.collection("locations").document(item.getLocations().get(0).getName());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                //If it is successful, increase the counter
                System.out.println(item.getLocations().get(0).getName());

                String name = documentSnapshot.getId();
                String description = (String) documentSnapshot.get("description");
                double latitude = documentSnapshot.getGeoPoint("location").getLatitude();
                double longitude = documentSnapshot.getGeoPoint("location").getLongitude();
                String owner = documentSnapshot.getString("owner");
                String photo = documentSnapshot.getString("photo");

                System.out.println(name);
                System.out.println(description);
                System.out.println(latitude);
                System.out.println(longitude);
                System.out.println(owner);
                System.out.println(photo);

                Location location = new Location(latitude, longitude, name, description, owner, photo);

                item.getLocations().set(0, location);
                hunt = item;
                navController.navigate(HuntInformationFragmentDirections.actionNavHuntInformationToNavHunt(item));
            }
        });
    }
}

