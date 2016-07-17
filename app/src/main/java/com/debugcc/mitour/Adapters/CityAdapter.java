package com.debugcc.mitour.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dubgcc on 07/07/16.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<City> mDataset;
    private OnItemClickListener mListener;

    public CityAdapter(List<City> dataset) {
        this.mDataset = dataset;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_city, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    /**
     * VIEW HOLDER of City
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView mImage;
        private TextView mName;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.row_city_image);
            mName  = (TextView)  itemView.findViewById(R.id.row_city_name);
        }

        public void bind(final City city) {
            //mImage.setImageBitmap(city.getImage());
            Glide.with(itemView.getContext())
                    .load(city.getImageUrl())
                    .placeholder(R.drawable.img_placeholder_dark)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.img_placeholder_dark)
                    .into(mImage);

            mName.setText(city.getCity());

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition(), city);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, City city);
    }
}
