package com.debugcc.mitour.Fragments.home;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityDetailFragment extends Fragment {

    private static final String TAG = "CityDetailFragment";

    private static City mCity;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCity = City.CURRENT_CITY;

        Log.d(TAG, "onCreateView: ONCREATE");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_detail, container, false);

        if (mCity != null) {
            ((TextView) rootView.findViewById(R.id.city_detail)).setText(
                    Html.fromHtml(mCity.getDetails())
            );
        }

        Log.d(TAG, "onCreateView: ONCREATEVIEW");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onCreateView: ONVIEWCREATED");

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.city_detail_toolbar_layout);
        ImageView imageView = (ImageView) activity.findViewById(R.id.city_detail_image_view);

        if (appBarLayout != null) {
            appBarLayout.setTitle(mCity.getCity());
            //appBarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        }

        Glide.with(getContext())
                .load(mCity.getImageUrl())
                .placeholder(R.drawable.img_placeholder_dark)
                .centerCrop()
                .crossFade()
                .error(R.drawable.img_placeholder_dark)
                .into(imageView);




    }
}
