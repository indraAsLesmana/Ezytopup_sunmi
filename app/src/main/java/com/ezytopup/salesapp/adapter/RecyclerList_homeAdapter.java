package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ProductResponse;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class RecyclerList_homeAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{

    private ArrayList<ProductResponse.Result> dataList;
    private Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final String TAG = "RecyclerList_homeAdapter";

    public RecyclerList_homeAdapter(Context mContext, ArrayList<ProductResponse.Result> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER ){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, null);
            return new VHHeader(v);

        }else if (viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home, null);
            return new RecyclerList_homeAdapter.ItemRowHolder(v);
        }
        throw new RuntimeException("No match " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case TYPE_HEADER:

                VHHeader vhHeader = (VHHeader) holder;
                TextSliderView textSliderView = new TextSliderView(mContext);

                textSliderView
                        .image(R.drawable.header1)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                vhHeader.sliderLayout.addSlider(textSliderView);
                vhHeader.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                vhHeader.sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                vhHeader.sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

                break;

            case TYPE_ITEM:

                String sectionName = dataList.get(position).getCategoryName();
                ArrayList singleSectionItems = (ArrayList) dataList.get(position).getProducts();
                SectionListDataAdapter itemListDataAdapter =
                        new SectionListDataAdapter(mContext, singleSectionItems);
                ItemRowHolder itemRowHolder = (ItemRowHolder) holder;
                itemRowHolder.categoryTitle.setText(sectionName);

                itemRowHolder.recycler_view_list.setHasFixedSize(true);
                itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false));
                itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
                itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);

                break;

        }
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (null != dataList){
            return dataList.size();
        } else {
            return 0;
        }
    }


    class VHHeader extends RecyclerView.ViewHolder{
        private SliderLayout sliderLayout;

        public VHHeader(View itemView) {
            super(itemView);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
        }
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView categoryTitle, btnMore;
        private RecyclerView recycler_view_list;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            this.recycler_view_list = (RecyclerView) itemView.findViewById(R.id.data_list);
            this.btnMore = (TextView) itemView.findViewById(R.id.btn_more);
        }
    }
}
