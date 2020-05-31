package com.example.cloudproject.ui.hunt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudproject.MainActivity;
import com.example.cloudproject.R;
import com.example.cloudproject.models.Hunt;
import com.example.cloudproject.models.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoundLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoundLocationFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private Hunt hunt;
    private int currentLocationNumber;

    public FoundLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoundLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoundLocationFragment newInstance(String param1, String param2) {
        FoundLocationFragment fragment = new FoundLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(hunt.getLocations().get(currentLocationNumber).getName());

        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionView);
        descriptionView.setText(hunt.getLocations().get(currentLocationNumber).getDescription());

        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(hunt.getLocations().get(currentLocationNumber).getPhoto());
        final ImageView image = (ImageView) view.findViewById(R.id.locationPhotoView);

        final long ONE_MEGABYTE = 1024 * 1024 * 10;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bmp);
            }
        });


        MainActivity activity = (MainActivity) getActivity();
        activity.locationTracker++;

        if(activity.finished){
            Toast.makeText(getActivity(), "You've finished the hunt!", Toast.LENGTH_LONG).show();

            //The app should stop looking for the goal location now

            activity.hunt = new Hunt("You have no hunt selected!", "", "", "", new ArrayList<Location>());
            activity.locationTracker = 0;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        hunt = activity.hunt;
        currentLocationNumber = activity.locationTracker;
        return inflater.inflate(R.layout.fragment_found_location, container, false);
    }
}
