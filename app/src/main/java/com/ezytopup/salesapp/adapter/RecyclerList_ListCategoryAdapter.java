package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ListCategoryResponse;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/13/17.
 */

public class RecyclerList_ListCategoryAdapter extends RecyclerView.Adapter
        <RecyclerList_ListCategoryAdapter.ItemProductHolder> {

    private ArrayList<ListCategoryResponse.Category> itemList;
    private Context mContext;
    private static final String TAG = "RecyclerList_CategoryAdapter";
    private RecyclerList_ListCategoryAdapterlistener mListener;
    private ListCategoryResponse.Category singleItem;

    public RecyclerList_ListCategoryAdapter(ArrayList<ListCategoryResponse.Category> itemList,
                                            RecyclerList_ListCategoryAdapterlistener mListener,
                                            Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public ItemProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_listcategoryitem, parent, false);
        return new ItemProductHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemProductHolder holder, int position) {
        singleItem = itemList.get(position);
        if (singleItem != null){
            holder.card_categoryname.setText(singleItem.getName());

            if (singleItem.getImage() != null){
                Glide.with(mContext)
                        .load(singleItem.getImage()).centerCrop()
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .into(holder.itemImage);
            }

            holder.singleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCardClick(singleItem);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (null != itemList) return itemList.size();
        else return 0;
    }

    public class ItemProductHolder extends RecyclerView.ViewHolder {
        private TextView card_categoryname;
        private ImageView itemImage;
        private LinearLayout singleCard;

        public ItemProductHolder(View itemView) {
            super(itemView);
            card_categoryname = (TextView) itemView.findViewById(R.id.listcat_title);
            singleCard = (LinearLayout) itemView.findViewById(R.id.listcat_container);
            itemImage = (ImageView) itemView.findViewById(R.id.listcat_image);
        }
    }

    public interface RecyclerList_ListCategoryAdapterlistener{
        void onCardClick(ListCategoryResponse.Category singleItem);
    }
}
