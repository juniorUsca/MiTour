package com.debugcc.mitour.Adapters;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.debugcc.mitour.Fragments.main.MapsFragment;
import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dubgcc on 28/05/16.
 */
public class CategoryPlaceAdapter extends RecyclerView.Adapter<CategoryPlaceAdapter.ViewHolder> {

    private List<CategoryPlace> mDataset;
    private OnItemClickListener mListener;

    public CategoryPlaceAdapter(List<CategoryPlace> dataset) {
        mDataset = dataset;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

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
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * VIEWHOLDER of CategoriesPlaces
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView mImage;
        private TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.categoryPlace_image);
            mName  = (TextView)  itemView.findViewById(R.id.categoryPlace_name);
        }

        public void bind(final CategoryPlace category) {
            Glide.with(itemView.getContext())
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.ic_home)
                    .fitCenter()
                    .placeholder(R.drawable.ic_home)
                    .into(mImage);

            mName.setText(category.getName());

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition(), category);
                }
            });
        }

        public void setSelected(boolean selected) {
            if(selected)
                mName.setTextColor(Color.YELLOW);
            else
                mName.setTextColor(Color.WHITE);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, CategoryPlace category);
    }
}
