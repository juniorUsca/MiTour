package com.debugcc.mitour.Fragments.home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugcc.mitour.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CityMapFragment extends Fragment {

    public CityMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_map, container, false);
    }
}
