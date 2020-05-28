package com.example.cloudproject.ui.hunt;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cloudproject.R;
import com.example.cloudproject.models.Hunt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HuntInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HuntInformationFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private Hunt hunt;
    private OnFragmentInteractionListener mListener;

    public HuntInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HuntInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HuntInformationFragment newInstance(String param1, String param2) {
        HuntInformationFragment fragment = new HuntInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hunt = HuntInformationFragmentArgs.fromBundle(getArguments()).getHunt();

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(hunt.getTitle());
        TextView numberOfLocationsView = (TextView) view.findViewById(R.id.numberOfLocationsView);
        numberOfLocationsView.setText(hunt.getLocations().size() + " locations");
        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionView);
        descriptionView.setText(hunt.getDescription());

        Button selectHuntButton = (Button) view.findViewById(R.id.selectHuntButton);
        selectHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(hunt);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hunt_information, container, false);
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Hunt item);
    }
}
