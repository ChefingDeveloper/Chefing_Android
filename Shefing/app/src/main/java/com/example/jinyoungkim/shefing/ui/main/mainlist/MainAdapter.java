package com.example.jinyoungkim.shefing.ui.main.mainlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.main.MainListData;
import com.example.jinyoungkim.shefing.ui.detail.DetailActivity;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 1. ViewHolder로 아이템 뷰 연결
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView menuImage;
        ImageView chefImage;
        TextView chefName;
        TextView menuPrice;
        TextView menuInfo;
        TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            chefImage = itemView.findViewById(R.id.chefImage);
            chefName = itemView.findViewById(R.id.chefName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            menuInfo = itemView.findViewById(R.id.menuInfo);
            location = itemView.findViewById(R.id.location);
        }

    }

    // 2. 생성자로 ArrayList, context 등록
    private ArrayList<MainListData> mainItem;
    private Context context;
    private RequestManager requestManager;

    MainAdapter(ArrayList<MainListData> mainItem, Context context, RequestManager requestManager){
        this.mainItem = mainItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    // 3. Adapter 랑 ViewHolder 연결
        // 3-1. ViewHolder 생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item,parent,false);

        return new ViewHolder(v);
    }

        // 3-2. ViewHolder에 아이템 바인딩
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder)holder;

        requestManager.load(mainItem.get(position).menu_image).into(viewHolder.menuImage);
        requestManager.load(mainItem.get(position).chef_image).into(viewHolder.chefImage);
        viewHolder.chefName.setText(mainItem.get(position).chef_name);
        viewHolder.menuInfo.setText(mainItem.get(position).menu_name);
        viewHolder.menuPrice.setText(String.valueOf(mainItem.get(position).menu_price));
        viewHolder.location.setText(mainItem.get(position).shop_location_name);


        // 3-3. 메뉴 상세 페이지로 전환
        viewHolder.menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context.getApplicationContext(),DetailActivity.class);
                i.putExtra("chef_id",mainItem.get(position).chef_id);
                i.putExtra("shop_id",mainItem.get(position).shop_id);
                context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainItem.size();
    }

}