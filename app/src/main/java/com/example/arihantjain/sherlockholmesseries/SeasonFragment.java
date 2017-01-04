package com.example.arihantjain.sherlockholmesseries;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.arihantjain.sherlockholmesseries.MainActivity.bottomSheetBehavior;

public class SeasonFragment extends Fragment {
    Communicator comm;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_season, container, false);
        comm = (Communicator) getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        return view;
    }
    public void onLoadingSeasons(ArrayList<SeasonModel> result){

        MainActivity.load.setImageDrawable(null);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        SeasonRecycleViewAdapter arrayAdapter = new SeasonRecycleViewAdapter(getActivity(),result);
        recyclerView.setAdapter(arrayAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),recyclerView,new RecyclerItemClickListener.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        if(MainActivity.detector.isConnected()) {
                            comm.respond(position);
                            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

    }

}
