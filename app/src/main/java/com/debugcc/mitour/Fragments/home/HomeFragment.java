package com.debugcc.mitour.Fragments.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugcc.mitour.Activities.home.CityDetailActivity;
import com.debugcc.mitour.Adapters.CityAdapter;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.AsynchronousTasks;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    private String mParam1;
    private String mParam2;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FloatingActionButton fab_main;
    private RecyclerView.Adapter mCityAdapter;
    private OnFragmentInteractionListener mListener;
    private boolean mTwoPane;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        /*fab_main = (FloatingActionButton) getActivity().findViewById(R.id.main_fab);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Planificar viaje", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AsynchronousTasks.getCitiesForHome();

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView recyclerView_citiesList = (RecyclerView) this.getActivity().findViewById(R.id.recycler_citiesList);
        recyclerView_citiesList.setHasFixedSize(true);
        //recyclerView_citiesList.setLayoutManager(layoutManager);
        mCityAdapter = new CityAdapter(City.POPULAR_CITIES);
        recyclerView_citiesList.setAdapter(mCityAdapter);
        //recyclerView_citiesList.setAnimation(new DefaultItemAnimator());

        if (getActivity().findViewById(R.id.city_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        /// Click on a city

        ((CityAdapter) mCityAdapter).setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, City city) {

                AsynchronousTasks.getMarkerFromCity(city.getID());

                if (mListener != null) {
                    mListener.onCitySelected();


                    City.CURRENT_CITY = city;

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, city.getID());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, city.getCity());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, city.getCountry());
                    //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "City");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                    Bundle params = new Bundle();
                    params.putString(FirebaseAnalytics.Param.ITEM_ID, city.getID());
                    params.putString("city", city.getCity());
                    params.putString("country", city.getCountry());
                    mFirebaseAnalytics.logEvent("cities_selected", params);

                    if (mTwoPane) {
                        /// recarga el fragmento details

                        //Bundle arguments = new Bundle();
                        //arguments.putString(CityDetailFragment.ARG_CITY_ID, city.getID());
                        CityDetailFragment fragment = new CityDetailFragment();
                        //fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        /// salta a un nuevo activity
                        Context context = getContext();
                        Intent intent = new Intent(context, CityDetailActivity.class);
                        //intent.putExtra(CityDetailFragment.ARG_CITY_ID, city.getID());

                        context.startActivity(intent);
                    }
                }
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * INTERFACE
     */
    public interface OnFragmentInteractionListener {
        void onCitySelected();
    }
}
