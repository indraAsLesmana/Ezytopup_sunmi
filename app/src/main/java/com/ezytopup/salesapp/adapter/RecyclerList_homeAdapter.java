package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ProductResponse;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class RecyclerList_homeAdapter extends RecyclerView.Adapter<RecyclerList_homeAdapter.ItemRowHolder> {

    private ArrayList<ProductResponse.Result> dataList;
    private Context mContext;
    private static final String TAG = "RecyclerList_homeAdapter";
    private RecyclerList_homeAdapterListener mListener;
    private ArrayList singleSectionItems;


    public RecyclerList_homeAdapter(Context mContext, ArrayList<ProductResponse.Result> dataList,
                                    RecyclerList_homeAdapterListener listener) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home, null);
        return new RecyclerList_homeAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final String categoryName = dataList.get(position).getCategoryName();
        final String categoryId = dataList.get(position).getCategoryId();
        singleSectionItems = dataList.get(position).getProducts();
        holder.categoryTitle.setText(categoryName);
        SectionListDataAdapter itemListDataAdapter =
                new SectionListDataAdapter(mContext, singleSectionItems);
        holder.recycler_view_list.setHasFixedSize(true);
        holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recycler_view_list.setAdapter(itemListDataAdapter);
        holder.recycler_view_list.setNestedScrollingEnabled(false);

        holder.category_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoreClick(categoryName, categoryId);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (null != dataList) return dataList.size();
        else return 0;
    }


    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView categoryTitle;
        private RecyclerView recycler_view_list;
        private RelativeLayout category_more;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            this.recycler_view_list = (RecyclerView) itemView.findViewById(R.id.data_list);
            this.category_more = (RelativeLayout) itemView.findViewById(R.id.container_more);
        }
    }
    public interface RecyclerList_homeAdapterListener{
        void onMoreClick(String categoryName, String categoryId);
    }
}
