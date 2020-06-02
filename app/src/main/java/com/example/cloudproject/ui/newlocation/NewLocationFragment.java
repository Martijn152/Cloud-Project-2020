package com.example.cloudproject.ui.newlocation;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudproject.MainActivity;
import com.example.cloudproject.R;
import com.example.cloudproject.utils.DatabaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewLocationFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Bitmap photo;
    private Location currentLocation;

    public NewLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewLocationFragment newInstance(String param1, String param2) {
        NewLocationFragment fragment = new NewLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ImageView imageView = (ImageView) view.findViewById(R.id.locationImageView);
        imageView.setImageBitmap(photo);

        TextView coordinateView = (TextView) view.findViewById(R.id.coordinateView);
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        coordinateView.setText(latitude + "° N, " + longitude + "° E");

        EditText titleText = (EditText) view.findViewById(R.id.titleText);
        EditText descriptionText = (EditText) view.findViewById(R.id.descriptionText);

        Button addLocationButton = (Button) view.findViewById(R.id.addLocationButton);

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!titleText.getText().toString().equals("") && !descriptionText.getText().toString().equals("")){
                    //This means all fields were filled
                    //Now add the location to the database
                    System.out.println(titleText.getText().toString());
                    System.out.println(descriptionText.getText().toString());

                    long time = System.currentTimeMillis();


                    //Entering the information about the location
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("description", descriptionText.getText().toString());
                    docData.put("location", new GeoPoint(latitude, longitude));
                    docData.put("owner", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    docData.put("photo", "gs://ageless-lamp-278509.appspot.com/" + time);

                    DatabaseHandler.getInstance().db.collection("locations").document(titleText.getText().toString())
                            .set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });



                    //Adding the photo to storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ageless-lamp-278509.appspot.com/" + time);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storageReference.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                            //Close fragment here
                            Toast.makeText(getActivity(), "Location successfully added!", Toast.LENGTH_LONG).show();
                            getActivity().onBackPressed();
                        }
                    });





                }else {
                    //Notify user to fill all fields
                    Toast.makeText(getActivity(), "Please fill all the fields.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        currentLocation = activity.mCurrentLocation;
        photo = activity.photo;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_location, container, false);
    }
}
