package com.example.jinyoungkim.shefing.ui.mypage.reservation_tab;

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

import com.example.jinyoungkim.shefing.R;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //1. ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout_reservation;
        ImageView image_reservation; // 예약 메뉴 사진
        TextView name_reservation; // 예약 메뉴 이름
        TextView chef_reservation; // 예약 메뉴 셰프 이름
        TextView date_reservation; // 예약 날짜
        TextView time_reservation; // 예약 시간
        TextView confirm_reservation; // 예약 완료 여부

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_reservation = itemView.findViewById(R.id.image_reservation);
            layout_reservation = itemView.findViewById(R.id.layout_reservation);
            name_reservation = itemView.findViewById(R.id.name_reservation);
            chef_reservation = itemView.findViewById(R.id.chef_reservation);
            date_reservation = itemView.findViewById(R.id.date_reservation);
            time_reservation = itemView.findViewById(R.id.time_reservation);
            confirm_reservation = itemView.findViewById(R.id.text_confirm_reservation);
        }
    }

    // 2. 생성자 ArrayList, context
    private ArrayList<ReservationItem> reservationItem;
    private Context context;

    public ReservationAdapter(ArrayList<ReservationItem> reservationItem, Context context) {
        this.reservationItem = reservationItem;
        this.context = context;
    }

    // Adapter - ViewHolder

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.image_reservation.setImageResource(reservationItem.get(position).image_reservation);
        if(reservationItem.get(position).confirm_reservation==0) {
            viewHolder.confirm_reservation.setText("예약완료");
        }else{
            viewHolder.confirm_reservation.setText("예약취소");
        }
        viewHolder.name_reservation.setText(reservationItem.get(position).name_reservation);
        viewHolder.date_reservation.setText(reservationItem.get(position).date_reservation);
        viewHolder.chef_reservation.setText(reservationItem.get(position).chef_reservation);
        viewHolder.time_reservation.setText(reservationItem.get(position).time_reservation);

        viewHolder.layout_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(),ReservationDetailActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationItem.size();
    }
}
