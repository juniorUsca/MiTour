package com.debugcc.mitour.Fragments.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.debugcc.mitour.Adapters.CategoryPlaceAdapter;
import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.Models.Marker;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.Route;
import com.debugcc.mitour.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;


    private GoogleMap mMap;
    private FloatingActionButton fab_myposition;
    private static String TAG = "MapsFragment";
    private LocationManager mLocationManager;

    private ArrayList<MarkerOptions> mMarkers;
    private LatLng center;
    private ArrayList<LatLng> mLatLngs;
    private MarkerOptions mMarkerMyLocation;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        fab_myposition = (FloatingActionButton) getActivity().findViewById(R.id.fab_mylocation);
        fab_myposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Espera un momento que cargue el mapa", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /// Charge categories
        ArrayList<CategoryPlace> categoriesPlaces = new ArrayList<>();

        CategoryPlace[] categoriesPl = Utils.readSharedList(getActivity(), Utils.FIRE_DB_CATEGORIES, CategoryPlace[].class);
        for (CategoryPlace cp : categoriesPl) {

            Bitmap bm = Utils.getPicture(
                    getActivity(),
                    cp.getName(),
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_home_vector) );

            cp.setImage(bm);

            categoriesPlaces.add(cp);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView recyclerView_categoriesPlaces = (RecyclerView) this.getActivity().findViewById(R.id.recycler_categoriesPlaces);
        recyclerView_categoriesPlaces.setHasFixedSize(true);
        recyclerView_categoriesPlaces.setAdapter(new CategoryPlaceAdapter(categoriesPlaces));
        recyclerView_categoriesPlaces.setLayoutManager(layoutManager);
        //recyclerView_categoriesPlaces.setAnimation(new DefaultItemAnimator());


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mLocationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        /// Adding markers
        mMarkers = new ArrayList<>();
        mLatLngs = new ArrayList<>();

        Marker[] markers_array = Utils.readSharedList(getActivity(), Utils.FIRE_DB_MARKERS, Marker[].class);
        center = new LatLng(
                Double.parseDouble(markers_array[0].getLat()),
                Double.parseDouble(markers_array[0].getLng())
        );
        for (Marker item : markers_array) {

            LatLng ll = new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()));
            mLatLngs.add(ll);
            mMarkers.add(new MarkerOptions()
                    .position(ll)
                    .title(item.getName())
                    .snippet(item.getDetails())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        for (int i = 0; i < mMarkers.size(); i++) {
            mMap.addMarker(mMarkers.get(i));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                center
                , 15));


        Route mRoute = new Route();
        mRoute.drawRoute(mMap,
                getContext(),
                mLatLngs,
                Route.TRANSPORT_WALKING,
                false,
                Route.LANGUAGE_SPANISH,
                true);


        fab_myposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerOnMyLocation();
            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "onMapReady: NO PERMISSION" );
        }
        LocationListener locationListener = new MyLocationListener();
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


        //centerOnMyLocation();

        //Route mRoute = new Route();
        //mRoute.drawRoute(mMap,getContext(),l1,l6,"driving",true,"es");

    }



    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    private void centerOnMyLocation() {
        Location location = getLastBestLocation();
        if (location != null) {
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

            mMarkerMyLocation = new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lens));

            Log.e(TAG, "centerOnMyLocation: CENTRANDO Y BORRANDO" );

            /*mMap.clear();
            for (int i = 0; i < mMarkers.size(); i++) {
                mMap.addMarker(mMarkers.get(i));
            }
            mMap.addMarker(mMarkerMyLocation);*/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

            Log.e(TAG, "centerOnMyLocation: CENTRADo" );
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            Log.e(TAG, "centerOnMyLocation: GEOCODER" );
            List<Address> addresses;
            try {
                Log.e(TAG, "centerOnMyLocation: " + "addressing BEGIN" );
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Log.e(TAG, "centerOnMyLocation: " + "addressing END" );
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getLocality();
                    Toolbar tb = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    tb.setTitle(cityName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Location getLastBestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "centerOnMyLocation: NO HAY PERMISOS!!");
        }
        Log.e(TAG, "getting GPS PROVIDER");
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.e(TAG, "getting NETWORK PROVIDER");
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

            mMarkerMyLocation = new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lens));
            Log.e(TAG, "onLocationChanged: LOCATION CHANGED" );
            //mMap.clear();
            //for (int i = 0; i < mMarkers.size(); i++) {
                //mMap.addMarker(mMarkers.get(i));
            //}
            //mMap.addMarker(mMarkerMyLocation);
//TODO: error NULL
            /*
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getLocality();
                    Toolbar tb = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    tb.setTitle(cityName);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }*/

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " );
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: ");
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (provider == getString(R.string.provider_gps)) {
                Log.e(TAG, "onProviderDisabled: " + provider);

                /*mMap.clear();
                for (int i = 0; i < mMarkers.size(); i++) {
                    mMap.addMarker(mMarkers.get(i));
                }*/

                /// TODO: CREAR MENSAJE DE ACTIVAR GPS
            }
        }
    }
}
