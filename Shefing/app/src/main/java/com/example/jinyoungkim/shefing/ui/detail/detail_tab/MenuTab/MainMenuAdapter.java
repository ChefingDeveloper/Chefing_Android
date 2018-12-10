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
import com.example.jinyoungkim.shefing.model.detail_tab.DetailMainMenuData;
import com.example.jinyoungkim.shefing.ui.detail.detail_menu.MenuDetailActivity;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 1. ViewHolder
    public static class ViewHolder extends  RecyclerView.ViewHolder {

        ImageView image_main;
        LinearLayout layout_main;
        TextView name_main;
        TextView price_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_main = itemView.findViewById(R.id.image_mainmenu);
            name_main = itemView.findViewById(R.id.name_mainmenu);
            price_main = itemView.findViewById(R.id.price_mainmenu);
            layout_main = itemView.findViewById(R.id.layout_mainmenu);
        }
    }

    // 2. 생성자로 ArrayList, context 등록
    private ArrayList<DetailMainMenuData> mainMenuItem;
    private Context context;
    private RequestManager requestManager;

    MainMenuAdapter(ArrayList<DetailMainMenuData> mainMenuItem, Context context, RequestManager requestManager){
        this.mainMenuItem = mainMenuItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    // 3. Adapter랑 ViewHolder 연결

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.maindish_recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder)holder;


        requestManager.load(mainMenuItem.get(position).menu_image).into(viewHolder.image_main);
        viewHolder.name_main.setText(mainMenuItem.get(position).menu_name);
        viewHolder.price_main.setText(String.valueOf(mainMenuItem.get(position).menu_price));

        viewHolder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MenuDetailActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("menu_id",mainMenuItem.get(position).menu_id);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mainMenuItem.size();
    }
}
