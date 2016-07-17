package com.debugcc.mitour.utils;

import android.util.Log;

import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.Models.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by dubgcc on 07/07/16.
 */
public class AsynchronousTasks {
    private static final String TAG = "AsynchronousTasks";
    private static final String FIRE_DB_CITIES = "cities";
    private static final String FIRE_DB_MARKERS = "markers";
    private static final String FIRE_DB_CATEGORIES_PLACES = "categories";
    private static DatabaseReference mDatabase;
    private static FirebaseDatabase mDatabaseInstance;

    //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    /*public AsynchronousTasks() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }*/

    public static DatabaseReference getDatabase() {
        if (mDatabaseInstance == null) {
            mDatabaseInstance = FirebaseDatabase.getInstance();
            mDatabaseInstance.setPersistenceEnabled(true);
        }
        return mDatabaseInstance.getReference();
    }

    public static void getCitiesForHome() {
        DatabaseReference reference = getDatabase().child(FIRE_DB_CITIES);
        //reference.keepSynced(true);

        reference.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: OBTENIENDO CIUDADES" );
                //Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                if (dataSnapshot.hasChildren()) {
                    City.CITIES.clear();
                    City.CITIES_MAP.clear();
                    City.POPULAR_CITIES.clear();
                    City.POPULAR_CITIES_MAP.clear();
                    Iterable<DataSnapshot> ds_cities = dataSnapshot.getChildren();
                    for (DataSnapshot ds : ds_cities) {
                        City item = new City();
                        item.setID(ds.getKey());
                        item.setCity(ds.child("city").getValue(String.class));
                        item.setCountry(ds.child("country").getValue(String.class));
                        item.setDetails(ds.child("details").getValue(String.class));
                        item.setPopular(ds.child("popular").getValue(Boolean.class));
                        item.setImageUrl(ds.child("image_url").getValue(String.class));
                        City.CITIES.add(item);
                        City.CITIES_MAP.put(item.getID(), item);
                        if (item.getPopular()) {
                            City.POPULAR_CITIES.add(item);
                            City.POPULAR_CITIES_MAP.put(item.getID(), item);
                        }

                        Log.d(TAG, "onDataChange: CITY" + item.toMap().toString());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    public static void getMarkerFromCity(String ID) {
        DatabaseReference reference = getDatabase().child(FIRE_DB_MARKERS).child(ID);
        //reference.keepSynced(true);

        reference.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: OBTENIENDO MARKERS");
                // Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                if (dataSnapshot.hasChildren()) {
                    Marker.MARKERS.clear();
                    Marker.MARKERS_MAP.clear();
                    Iterable<DataSnapshot> ds_markers = dataSnapshot.getChildren();
                    for (DataSnapshot ds : ds_markers) {
                        Marker item = new Marker();
                        item.setID(ds.getKey());
                        item.setName(ds.child("name").getValue(String.class));
                        item.setDetails(ds.child("details").getValue(String.class));
                        item.setLat(ds.child("lat").getValue(String.class));
                        item.setLng(ds.child("lng").getValue(String.class));
                        GenericTypeIndicator<List<String>> t_list = new GenericTypeIndicator<List<String>>() {
                        };
                        item.setCategories(ds.child("categories").getValue(t_list));

                        Marker.MARKERS.add(item);
                        Marker.MARKERS_MAP.put(item.getID(), item);

                        Log.d(TAG, "onDataChange: MARKER " + item.toMap().toString());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    public static void getCategoriesPlaces() {
        DatabaseReference reference = getDatabase().child(FIRE_DB_CATEGORIES_PLACES);
        //reference.keepSynced(true);

        reference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: OBTENIENDO CATEGORIAS DE LUGARES" );
                // Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                if (dataSnapshot.hasChildren()) {
                    CategoryPlace.CATEGORIES.clear();
                    CategoryPlace.CATEGORIES_MAP.clear();
                    Iterable<DataSnapshot> ds_categories = dataSnapshot.getChildren();
                    for (DataSnapshot ds : ds_categories) {
                        CategoryPlace item = new CategoryPlace();
                        item.setID(ds.getKey());
                        item.setName(ds.child("name").getValue(String.class));
                        item.setImageUrl(ds.child("image_url").getValue(String.class));

                        CategoryPlace.CATEGORIES.add(item);
                        CategoryPlace.CATEGORIES_MAP.put(item.getID(), item);

                        Log.d(TAG, "onDataChange: CATEGORIA " + item.toMap().toString());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }
}
