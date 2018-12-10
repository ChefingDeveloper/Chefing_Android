package com.example.jinyoungkim.shefing.ui.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.cart.DeleteCartResult;
import com.example.jinyoungkim.shefing.model.cart.GetCartData;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.util.GlobalApplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 1. ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cart_image_menu;
        TextView cart_name_menu;
        TextView cart_count_menu;
        TextView cart_price_menu;
        RelativeLayout cart_btn_delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_image_menu = itemView.findViewById(R.id.cart_image_menu);
            cart_name_menu = itemView.findViewById(R.id.cart_name_menu);
            cart_count_menu = itemView.findViewById(R.id.cart_count_menu);
            cart_price_menu = itemView.findViewById(R.id.cart_price_menu);
            cart_btn_delete = itemView.findViewById(R.id.cart_btn_delete);
        }
    }

    // 2. ArrayList, context
    private ArrayList<GetCartData> cartItem;
    private Context context;
    private RequestManager requestManager;

    // networking
    private NetworkService networkService= GlobalApplication.getGlobalApplicationContext().getNetworkService();

    public CartAdapter(ArrayList<GetCartData> cartItem, Context context, RequestManager requestManager) {
        this.cartItem = cartItem;
        this.context = context;
        this.requestManager = requestManager;
    }

    //3. Adapter - ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;

        requestManager.load(cartItem.get(position).cart_menu_image).into(viewHolder.cart_image_menu);
        viewHolder.cart_name_menu.setText(cartItem.get(position).cart_menu_name);
        viewHolder.cart_count_menu.setText(String.valueOf(cartItem.get(position).cart_menu_count));
        viewHolder.cart_price_menu.setText(String.valueOf(cartItem.get(position).cart_menu_total_price));

        // 장바구니 삭제

        viewHolder.cart_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DeleteCartResult> deleteCartResultCall = networkService.deletecart(cartItem.get(position).cart_id);
                deleteCartResultCall.enqueue(new Callback<DeleteCartResult>() {
                    @Override
                    public void onResponse(Call<DeleteCartResult> call, Response<DeleteCartResult> response) {
                        if(response.isSuccessful()){
                            GlobalApplication.getGlobalApplicationContext().makeToast("장바구니에서 삭제되었습니다 : )");
                            notifyItemRemoved(position);
                            Intent i = new Intent(context.getApplicationContext(),CartActivity.class);
                            i.putExtra("delete_item",position);
                            context.startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteCartResult> call, Throwable t) {
                        GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItem.size();
    }
}
