package com.debugcc.mitour.Fragments.home;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugcc.mitour.Adapters.CategoryPlaceAdapter;
import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.AsynchronousTasks;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CityMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "CityMapFragment";
    private GoogleMap mMap;
    private City mCity;
    private RecyclerView.Adapter mCategoryAdapter;

    public CityMapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCity = City.CURRENT_CITY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.city_map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AsynchronousTasks.getCategoriesPlaces();

        /// Charge categories

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView recyclerView_categoriesPlaces = (RecyclerView) this.getActivity().findViewById(R.id.recycler_categories_places_on_city);
        recyclerView_categoriesPlaces.setHasFixedSize(true);
        mCategoryAdapter = new CategoryPlaceAdapter(CategoryPlace.CATEGORIES);
        recyclerView_categoriesPlaces.setAdapter(mCategoryAdapter);
        //recyclerView_categoriesPlaces.setLayoutManager(layoutManager);
        //recyclerView_categoriesPlaces.setAnimation(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();

        /// Click on an Category

        ((CategoryPlaceAdapter) mCategoryAdapter).setOnItemClickListener(new CategoryPlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, CategoryPlace category) {
                Log.d(TAG, "onItemClick: " + pos + " category " + category.getName());
            }
        });
    }

    /**
     * MAPS FUNCTIONS
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


}
