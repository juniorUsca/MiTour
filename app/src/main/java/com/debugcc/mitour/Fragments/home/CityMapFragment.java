package com.debugcc.mitour.Fragments.home;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.debugcc.mitour.Adapters.CategoryPlaceAdapter;
import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.Models.Marker;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.AsynchronousTasks;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CityMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "CityMapFragment";

    private City mCity;
    private RecyclerView.Adapter mCategoryAdapter;

    private GoogleMap mMap;
    private LatLng center;

    public CityMapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCity = City.CURRENT_CITY;
        AsynchronousTasks.getCategoriesPlaces();
        AsynchronousTasks.getMarkerFromCity(mCity.getID());
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

        Activity activity = this.getActivity();
        Toolbar appBarLayout = (Toolbar) activity.findViewById(R.id.activity_city_map_toolbar);

        if (appBarLayout != null) {
            appBarLayout.setTitle(mCity.getCity());
            //appBarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        }
        activity.setTitle(mCity.getCity());

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView recyclerView_categoriesPlaces = (RecyclerView) activity.findViewById(R.id.recycler_categories_places_on_city);
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
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


        center = new LatLng(
                Double.parseDouble(Marker.MARKERS.get(0).getLat()),
                Double.parseDouble(Marker.MARKERS.get(0).getLng())
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                center
                , 15));


        for (int i = 0; i < Marker.MARKERS.size(); ++i) {
            LatLng ll = new LatLng(Double.parseDouble(Marker.MARKERS.get(i).getLat()),
                    Double.parseDouble(Marker.MARKERS.get(i).getLng()));
            //mLatLngs.add(ll);

            CategoryPlace ctP = CategoryPlace.CATEGORIES_MAP.get( Marker.MARKERS.get(i).getCategories().get(0) );

            final MarkerOptions markerOption = new MarkerOptions()
                    .position( ll )
                    .title( Marker.MARKERS.get(i).getName() )
                    .snippet( Marker.MARKERS.get(i).getDetails() );

            final com.google.android.gms.maps.model.Marker marker = mMap.addMarker( markerOption );

            Glide.with(getContext())
                    .load( ctP.getImageUrl() )
                    .asBitmap()
                    .override(42,42)
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap( resource );
                            marker.setIcon(icon);
                        }
                    });
        }


    }


}
