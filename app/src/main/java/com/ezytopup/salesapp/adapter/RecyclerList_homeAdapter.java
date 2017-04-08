package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ProductResponse;
import com.ezytopup.salesapp.fragment.HomeFragment;
import com.ezytopup.salesapp.utility.Helper;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class RecyclerList_homeAdapter extends RecyclerView.Adapter<RecyclerList_homeAdapter.ItemRowHolder> {

    private ArrayList<ProductResponse.Result> dataList;
    private Context mContext;
    private static final String TAG = "RecyclerList_homeAdapter";

    public RecyclerList_homeAdapter(Context mContext, ArrayList<ProductResponse.Result> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home, null);
        return new RecyclerList_homeAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        String sectionName = dataList.get(position).getCategoryName();
        ArrayList singleSectionItems = (ArrayList) dataList.get(position).getProducts();

        SectionList_homeAdapter itemListDataAdapter = null;
        if (!singleSectionItems.isEmpty()){
            itemListDataAdapter = new SectionList_homeAdapter(mContext, singleSectionItems);
        }else {
            Helper.showToast(mContext, R.string.section_error, true);
        }

        holder.categoryTitle.setText(sectionName);
        holder.recycler_view_list.setHasFixedSize(true);
        holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recycler_view_list.setAdapter(itemListDataAdapter);
        holder.recycler_view_list.setNestedScrollingEnabled(false);
    }


    @Override
    public int getItemCount() {
        if (null != dataList) {
            return dataList.size();
        } else {
            return 0;
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
