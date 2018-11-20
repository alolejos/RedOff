package com.abcontenidos.www.redhost;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapterCategories extends RecyclerView.Adapter<MyRecyclerViewAdapterCategories.ViewHolder> {

    private ArrayList<Category> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapterCategories(Context context, ArrayList<Category> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_category, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageCategory.setImageURI(Uri.parse(mData.get(position).getImage()));
        holder.switchSelected.setText(mData.get(position).getName());
        holder.textDetails.setText(mData.get(position).getDetails());
        holder.switchSelected.setChecked(Boolean.parseBoolean(mData.get(position).getSelected()));
        //Picasso.with(context).load(android_versions.get(i).getAndroid_image_url()).resize(120, 60).into(viewHolder.img_android);
        Picasso.get().load(mData.get(position).getImage()).resize(120, 120).into(holder.imageCategory);
    }


    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textName, textDetails;
        ImageView imageCategory;
        Switch switchSelected;

        ViewHolder(View itemView) {
            super(itemView);
            textDetails = itemView.findViewById(R.id.textCategoryDetails);
            imageCategory = itemView.findViewById(R.id.imageCategory);
            switchSelected = itemView.findViewById(R.id.switchCategory);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Category getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
