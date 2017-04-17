package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.PaymentResponse;
import com.ezytopup.salesapp.utility.Constant;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/17/17.
 */

public class Grid_PaymentAdapter extends ArrayAdapter<PaymentResponse.PaymentMethod>{

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<PaymentResponse.PaymentMethod> mGridData;

    public Grid_PaymentAdapter(Context mContext, int layoutResourceId,
                               ArrayList<PaymentResponse.PaymentMethod> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PaymentResponse.PaymentMethod bankItem = mGridData.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_itemcard, null);
        }
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_image);
        final TextView textView = (TextView) convertView.findViewById(R.id.grid_item_title);
        if (bankItem != null){
            textView.setText(bankItem.getPaymentMethod());
            if (mGridData.get(position).getPaymentLogo() != null){
                Glide.with(mContext)
                        .load(bankItem.getPaymentLogo())
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .crossFade(Constant.ITEM_CROSSFADEDURATION)
                        .into(imageView);
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }
}
