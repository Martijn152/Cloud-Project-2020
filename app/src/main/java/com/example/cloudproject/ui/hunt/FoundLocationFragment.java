package com.example.cloudproject.ui.hunt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cloudproject.MainActivity;
import com.example.cloudproject.R;
import com.example.cloudproject.models.Hunt;

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
        titleView.setText(hunt.getLocations().get(currentLocationNumber-1).getName());

        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionView);
        descriptionView.setText(hunt.getLocations().get(currentLocationNumber-1).getDescription());

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
