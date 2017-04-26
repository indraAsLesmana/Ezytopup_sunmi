package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.PaymentResponse;
import com.ezytopup.salesapp.utility.Constant;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/26/17.
 */

public class RecyclerList_bankoption extends RecyclerView.Adapter<RecyclerList_bankoption.Cardbank> {

    private Context mContext;
    private ArrayList<PaymentResponse.PaymentMethod> itemList;
    private RecyclerList_bankoption.RecyclerList_bankoptionListener mListener;

    public RecyclerList_bankoption(Context mContext, ArrayList<PaymentResponse.PaymentMethod> itemList,
                                   RecyclerList_bankoptionListener mListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mListener = mListener;
    }

    @Override
    public Cardbank onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_singlecard_home, parent, false);
        return new RecyclerList_bankoption.Cardbank(v);
    }

    @Override
    public void onBindViewHolder(Cardbank holder, int position) {
        final PaymentResponse.PaymentMethod bankItem = itemList.get(position);
        if (bankItem == null) return;
        if (bankItem.getPaymentLogo() != null) {
            Glide.with(mContext)
                    .load(bankItem.getPaymentLogo())
                    .error(R.drawable.ic_error_loadimage)
                    .crossFade(Constant.ITEM_CROSSFADEDURATION)
                    .into(holder.itemImage);
        }
        holder.itemTitle.setText(bankItem.getPaymentMethod());
        holder.container_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCardClick(bankItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != itemList) return itemList.size();
        else return 0;
    }


    class Cardbank extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle, itemPrice;
        private ConstraintLayout container_card;

        public Cardbank(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            itemPrice.setVisibility(View.GONE);
            container_card = (ConstraintLayout) itemView.findViewById(R.id.cn_cardview);
        }
    }

    public interface RecyclerList_bankoptionListener {
        void onCardClick(PaymentResponse.PaymentMethod bankItem);
    }
}
