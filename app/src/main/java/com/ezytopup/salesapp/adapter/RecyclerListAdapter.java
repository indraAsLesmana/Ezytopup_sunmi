package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ProductResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter <RecyclerListAdapter.ItemRowHolder>{

    private ArrayList<ProductResponse.Result> dataList;
    private Context mContext;

    public RecyclerListAdapter(Context mContext, ArrayList<ProductResponse.Result> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        final String sectionName = dataList.get(position).getCategoryName();
        ArrayList singleSectionItems = (ArrayList) dataList.get(position).getProducts();

        holder.categoryTitle.setText(sectionName);

        SectionListDataAdapter itemListDataAdapter =
                new SectionListDataAdapter(mContext, singleSectionItems);

        holder.recycler_view_list.setHasFixedSize(true);
        holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recycler_view_list.setAdapter(itemListDataAdapter);


        holder.recycler_view_list.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        if (null != dataList){
            return dataList.size();
        } else {
            return 0;
        }
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView categoryTitle;
        private RecyclerView recycler_view_list;
        private Button btnMore;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            this.recycler_view_list = (RecyclerView) itemView.findViewById(R.id.data_list);
            this.btnMore = (Button) itemView.findViewById(R.id.btn_more);
        }
    }
}
