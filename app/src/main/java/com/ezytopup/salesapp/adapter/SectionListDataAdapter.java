package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ProductResponse;
import com.ezytopup.salesapp.fragment.HomeFragment;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_singlecard_home, parent, false);
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
        holder.container_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.setSingleItem(singleItem);
                Toast.makeText(mContext, HomeFragment.getSingleItem().getProductName(),
                        Toast.LENGTH_SHORT).show();
                //TODO just jump to detail activity
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
        private ConstraintLayout container_card;

        public SingleItemHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            container_card = (ConstraintLayout) itemView.findViewById(R.id.cn_cardview);
        }
    }
}
