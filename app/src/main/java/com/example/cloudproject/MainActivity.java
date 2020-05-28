package com.example.cloudproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.cloudproject.models.Hunt;
import com.example.cloudproject.models.Location;
import com.example.cloudproject.ui.hunt.HuntInformationFragment;
import com.example.cloudproject.ui.hunt.HuntInformationFragmentDirections;
import com.example.cloudproject.ui.huntlist.HuntListFragment;
import com.example.cloudproject.ui.huntlist.HuntListFragmentDirections;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

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

public class MainActivity extends AppCompatActivity implements HuntListFragment.OnListFragmentInteractionListener, HuntInformationFragment.OnFragmentInteractionListener{

    private AppBarConfiguration mAppBarConfiguration;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public Hunt hunt = new Hunt("You have no hunt selected!","","","",new ArrayList<Location>());
    public int locationTracker = 0;
    android.location.Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private NavController navController;
    public Location hintLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
                    if(!hunt.getLocations().isEmpty()){
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

                        if(distance<10.0){
                            //This means the user found the location
                            if(locationTracker<hunt.getLocations().size()){
                                locationTracker++;
                                Toast.makeText(getApplicationContext(), "You've found the location!", Toast.LENGTH_LONG).show();
                                //Show the details for the found location
                                //Title, description, photo
                                navController.navigate(R.id.nav_found_location);

                            }else{
                                Toast.makeText(getApplicationContext(), "You've finished the hunt!", Toast.LENGTH_LONG).show();
                                //The app should stop looking for the goal location now
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
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            // imageView.setImageBitmap(photo);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
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
    public void onListFragmentInteraction(Hunt item) {
        //Do something with the item here

        //We want to open another fragment and pass the item to it, so it know what to display
        navController.navigate(HuntListFragmentDirections.actionNavHuntListToNavHuntInformation(item));
        //Navigate to the hunt information fragment
        //Also pass the hunt to the other fragment
    }

    @Override
    public void onFragmentInteraction(Hunt item) {
        //This means that this hunt is now the current active hunt
        //We should store this and display it in the current hunt fragment
        //We should also store which location (as in 3 out of 5) the hunt is at
        hunt = item;
        locationTracker = 0;
        navController.navigate(HuntInformationFragmentDirections.actionNavHuntInformationToNavHunt(item));


    }
}

