package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
        <SectionListDataAdapter.SingleItemHolder> {

    private ArrayList<ProductResponse.Product> itemsList;
    private Context mContext;

    private static final String TAG = "SectionListDataAdapter";

    public SectionListDataAdapter(Context mContext, ArrayList<ProductResponse.Product> itemsList) {
        this.itemsList = itemsList;
        this.mContext = mContext;
    }

    @Override
    public SingleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_singlecard_home, null);
        return new SingleItemHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemHolder holder, int position) {
        ProductResponse.Product singleItem = itemsList.get(position);

        holder.itemTitle.setText(singleItem.getProductName());
        holder.itemPrice.setText(singleItem.getHargaToko());

        if (singleItem.getImageUrl() != null){
            Glide.with(mContext)
                    .load(singleItem.getImageUrl()).centerCrop()
                    .error(R.drawable.com_facebook_profile_picture_blank_square)
                    .crossFade()
                    .into(holder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        if (null != itemsList) return itemsList.size();
        else return 0;
    }

    class SingleItemHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle, itemPrice;

        public SingleItemHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
        }
    }
}
