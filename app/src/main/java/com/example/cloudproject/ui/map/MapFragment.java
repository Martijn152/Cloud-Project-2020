package com.example.cloudproject.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudproject.models.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.cloudproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

public class MapFragment extends Fragment {

    private boolean mLocationPermissionGranted = false;
    private GoogleMap googleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap map) {
            //Get the locations of this user (their saved spots)
            //Then display these
            googleMap = map;

            ArrayList<Location> locationList = new ArrayList<Location>();
            Location location1 = new Location(56.0299629,14.1501808, "Tivoli Badet", "A pool", "Martijn", "id1");
            Location location2 = new Location(56.0285586,14.1469743, "Naturum Vattenriket", "A place", "Martijn", "id2");
            Location location3 = new Location(56.0273787,14.153089, "Kristianstad Theater", "A theater", "Martijn", "id3");
            Location location4 = new Location(56.0312172,14.1583827, "Systembolaget", "A liquor store", "Martijn", "id4");
            Location location5 = new Location(56.0466502,14.1545984, "Pinocchio Pizzeria", "A pizzeria", "Martijn", "id5");
            locationList.add(location1);
            locationList.add(location2);
            locationList.add(location3);
            locationList.add(location4);
            locationList.add(location5);

            for (Location location: locationList
                 ) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney).title(location.getName()));
            }

            //Change this to the current location of the phone
            getLocationPermission();
            updateUI();
        }
    };

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateUI();
                }
            }
        }

    }

    public void updateUI(){
        if (mLocationPermissionGranted) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

            Task locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        android.location.Location lastKnownLocation = (android.location.Location) task.getResult();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()), 15.0f));
                    } else {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.0299629,14.1501808), 15.0f));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });

            googleMap.setMyLocationEnabled(true);

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }
}
