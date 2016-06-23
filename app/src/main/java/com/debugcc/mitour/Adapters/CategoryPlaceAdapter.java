package com.debugcc.mitour.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.R;

import java.util.ArrayList;

/**
 * Created by dubgcc on 28/05/16.
 */
public class CategoryPlaceAdapter extends RecyclerView.Adapter<CategoryPlaceAdapter.ViewHolder> {

    private ArrayList<CategoryPlace> mDataset;

    public CategoryPlaceAdapter(ArrayList<CategoryPlace> dataset) {
        mDataset = dataset;
    }

    @Override
    public CategoryPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.row_category_place,parent,false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryPlaceAdapter.ViewHolder holder, int position) {
        CategoryPlace item = mDataset.get(position);
        //holder.mImage.setImageResource(item.getImage());
        holder.mImage.setImageBitmap(item.getImage());
        holder.mName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.categoryPlace_image);
            mName  = (TextView)  itemView.findViewById(R.id.categoryPlace_name);
        }
    }
}
