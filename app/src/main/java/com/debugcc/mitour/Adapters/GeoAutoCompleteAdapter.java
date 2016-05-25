package com.debugcc.mitour.Adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.debugcc.mitour.Models.GeoSearchResult;
import com.debugcc.mitour.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dubgcc on 22/05/16.
 */
public class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 15;
    private Context mContext;
    private List resultList = new ArrayList<>();

    public GeoAutoCompleteAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.geo_search_result_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.geo_search_result_text);
        //textView.setText(getItem(position).getAddress());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List locations = findLocations(mContext, constraint.toString());

                    filterResults.values = locations;
                    filterResults.count = locations.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<GeoSearchResult> findLocations(Context context, String query_text) {
        List<GeoSearchResult> geo_search_result = new ArrayList<>();
        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(query_text, MAX_RESULTS);
            for (int i = 0; i < addresses.size(); i++) {
                Address address = addresses.get(i);
                if (address.getMaxAddressLineIndex() != -1) {
                    geo_search_result.add(new GeoSearchResult(address));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geo_search_result;
    }

}
