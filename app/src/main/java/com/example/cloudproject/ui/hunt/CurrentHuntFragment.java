package com.example.cloudproject.ui.hunt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cloudproject.MainActivity;
import com.example.cloudproject.R;
import com.example.cloudproject.models.Hunt;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentHuntFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentHuntFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Hunt hunt;
    private int currentLocationNumber;

    public CurrentHuntFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HuntFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentHuntFragment newInstance(String param1, String param2) {
        CurrentHuntFragment fragment = new CurrentHuntFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(hunt.getTitle());

        TextView numberOfLocationsView = (TextView) view.findViewById(R.id.numberOfLocationsView);

        numberOfLocationsView.setText("Location " + (currentLocationNumber + 1) + " out of " + hunt.getLocations().size());

        if(!hunt.getLocations().isEmpty()){

            // Reference to an image file in Cloud Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(hunt.getLocations().get(currentLocationNumber).getPhoto());
            final ImageView image = (ImageView) view.findViewById(R.id.nextLocationImageView);

            final long ONE_MEGABYTE = 1024 * 1024 * 10;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image.setImageBitmap(bmp);
                }
            });
        }


        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionView);
        descriptionView.setText(hunt.getDescription());


        Button selectHuntButton = (Button) view.findViewById(R.id.getHintButton);

        selectHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.hintLocation = hunt.getLocations().get(currentLocationNumber);
                Toast.makeText(getActivity(), "A marker has been added.", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        hunt = activity.hunt;
        currentLocationNumber = activity.locationTracker;
        return inflater.inflate(R.layout.fragment_current_hunt, container, false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Hunt item);
    }
}


