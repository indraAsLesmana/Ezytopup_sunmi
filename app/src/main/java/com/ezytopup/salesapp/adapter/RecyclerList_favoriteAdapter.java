package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.BestSellerResponse;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/5/17.
 */

public class RecyclerList_favoriteAdapter extends RecyclerView.Adapter
        <RecyclerList_favoriteAdapter.SingleItemFavHolder> {

    private ArrayList<BestSellerResponse.Product> itemList;
    private Context mContext;

    public RecyclerList_favoriteAdapter(Context mContext, ArrayList<BestSellerResponse.Product> itemList) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @Override
    public SingleItemFavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_singlecard_favorite, null);
        return new SingleItemFavHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemFavHolder holder, int position) {
        BestSellerResponse.Product singleItem = itemList.get(position);
        if (singleItem != null){
            holder.fav_title.setText(singleItem.getProductName());
            holder.fav_category_value.setText(singleItem.getCategoryName());
            holder.fav_price_value.setText(singleItem.getPrice());

            if (singleItem.getReviewUrl() != null){
                Glide.with(mContext)
                        .load(singleItem.getReviewUrl())
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .crossFade()
                        .into(holder.fav_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null != itemList) return itemList.size();
        else return 0;
    }

    public class SingleItemFavHolder extends RecyclerView.ViewHolder {
        private TextView fav_title, fav_category_value, fav_price_value;
        private ImageView fav_image;
        private RecyclerView recycler_view_list;

        public SingleItemFavHolder(View itemView) {
            super(itemView);
            fav_title = (TextView) itemView.findViewById(R.id.fav_itemtitle);
            fav_category_value = (TextView) itemView.findViewById(R.id.fav_category_value);
            fav_price_value = (TextView) itemView.findViewById(R.id.fav_price_value);
            fav_image = (ImageView) itemView.findViewById(R.id.fav_image);
            recycler_view_list = (RecyclerView) itemView.findViewById(R.id.rc_favorite_recycle);
        }
    }
}
