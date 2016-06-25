package com.debugcc.mitour.Adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugcc.mitour.Fragments.main.MapsFragment;
import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.R;

import java.util.ArrayList;

/**
 * Created by dubgcc on 28/05/16.
 */
public class CategoryPlaceAdapter extends RecyclerView.Adapter<CategoryPlaceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CategoryPlace item);
    }

    private ArrayList<CategoryPlace> mDataset;
    private OnItemClickListener mListener;

    public CategoryPlaceAdapter(ArrayList<CategoryPlace> dataset, OnItemClickListener listener) {
        mDataset = dataset;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.row_category_place,parent,false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //CategoryPlace item = mDataset.get(position);
        //holder.mImage.setImageResource(item.getImage());
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.categoryPlace_image);
            mName  = (TextView)  itemView.findViewById(R.id.categoryPlace_name);
        }

        public void bind(final CategoryPlace item, final OnItemClickListener listener) {
            mImage.setImageBitmap(item.getImage());
            mName.setText(item.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //mName.setTextColor( ContextCompat.getColor(v.getContext(), R.color.pink) );
                    listener.onItemClick(item);
                }
            });
        }
    }
}
