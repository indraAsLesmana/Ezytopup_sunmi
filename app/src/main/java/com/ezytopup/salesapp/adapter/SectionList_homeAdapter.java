package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ProductResponse;
import com.ezytopup.salesapp.utility.Helper;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class SectionList_homeAdapter extends RecyclerView.Adapter
        <SectionList_homeAdapter.SingleItemHolder> {

    private ArrayList<ProductResponse.Product> itemsList;
    private Context mContext;
    private SectionListDataAdapterListener mListener;
    private static final String TAG = "SectionList_homeAdapter";

    public SectionList_homeAdapter(Context mContext, ArrayList<ProductResponse.Product> itemsList) {
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
        final ProductResponse.Product singleItem = itemsList.get(position);

        holder.itemTitle.setText(singleItem.getProductName());
        holder.itemPrice.setText(singleItem.getHargaToko());

        if (singleItem.getImageUrl() != null){
            Glide.with(mContext)
                    .load(singleItem.getImageUrl()).centerCrop()
                    .error(R.drawable.com_facebook_profile_picture_blank_square)
                    .crossFade()
                    .into(holder.itemImage);
        }

        holder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onCardClick(singleItem);
                }else{
                    Helper.showToast(mContext, "listener null", false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != itemsList) return itemsList.size();
        else return 0;
    }

    class SingleItemHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle, itemPrice;
        private ConstraintLayout cardContainer;

        public SingleItemHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            cardContainer = (ConstraintLayout) itemView.findViewById(R.id.card_container);
            
        }
    }

    public interface SectionListDataAdapterListener {
        void onCardClick(ProductResponse.Product singleProduct);
    }
}
