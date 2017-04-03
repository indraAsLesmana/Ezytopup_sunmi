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
import com.ezytopup.salesapp.api.ProductResponse;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter
        <SectionListDataAdapter.SingleItemHolde> {

    private ArrayList<ProductResponse.Product> itemsList;
    private Context mContext;

    public SectionListDataAdapter(ArrayList<ProductResponse.Product> itemsList, Context mContext) {
        this.itemsList = itemsList;
        this.mContext = mContext;
    }

    @Override
    public SingleItemHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemHolde mh = new SingleItemHolde(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemHolde holder, int position) {
        ProductResponse.Product singleItem = itemsList.get(position);

        holder.itemTitle.setText(singleItem.getProductName());
        holder.itemPrice.setText(singleItem.getHargaToko());

        Glide.with(mContext)
                .load(singleItem.getImageUrl())
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .crossFade()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SingleItemHolde extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle, itemPrice;

        public SingleItemHolde(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
        }
    }
}
