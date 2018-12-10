package com.example.jinyoungkim.shefing.ui.detail.detail_tab.MenuTab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailSideMenuData;
import com.example.jinyoungkim.shefing.ui.detail.detail_menu.MenuDetailActivity;

import java.util.ArrayList;

public class SideMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 1. ViewHolder
    public static class ViewHolder extends  RecyclerView.ViewHolder {

        ImageView image_side;
        LinearLayout layout_side;
        TextView name_side;
        TextView price_side;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_side = itemView.findViewById(R.id.image_sidemenu);
            name_side = itemView.findViewById(R.id.name_sidemenu);
            price_side = itemView.findViewById(R.id.price_sidemenu);
            layout_side = itemView.findViewById(R.id.layout_sidemenu);
        }
    }

    // 2. 생성자로 ArrayList, context 등록
    private ArrayList<DetailSideMenuData> sideMenuItem;
    private Context context;
    private RequestManager requestManager;

    SideMenuAdapter(ArrayList<DetailSideMenuData> sideMenuItem, Context context, RequestManager requestManager){
        this.sideMenuItem = sideMenuItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    // 3. Adapter랑 ViewHolder 연결

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sidedish_recycler_item, parent, false);

        return new SideMenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SideMenuAdapter.ViewHolder viewHolder = (SideMenuAdapter.ViewHolder)holder;

        requestManager.load(sideMenuItem.get(position).menu_image).into(viewHolder.image_side);
        viewHolder.name_side.setText(sideMenuItem.get(position).menu_name);
        viewHolder.price_side.setText(String.valueOf(sideMenuItem.get(position).menu_price));

        viewHolder.layout_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(),MenuDetailActivity.class);
                i.putExtra("menu_id",sideMenuItem.get(position).menu_id);
                context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return sideMenuItem.size();
    }
}
