package com.example.jinyoungkim.shefing.ui.mypage.reservation_tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinyoungkim.shefing.R;

import java.util.ArrayList;


public class ReservationFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ReservationItem> reservationItem;
    ReservationAdapter adapter;

    public ReservationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_reservation, container, false);


        // 1. recyclerview
        recyclerView = v.findViewById(R.id.recyclerview_reservation);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // 2. Item Add
        reservationItem = new ArrayList<>();
        reservationItem.add(new ReservationItem(R.drawable.pork,"스테이크만찬","홍길동",0,"2018-10-11","15:00:00"));
        reservationItem.add(new ReservationItem(R.drawable.spaghetti,"유기농스파게티","해리포터",1,"2018-10-11","15:00:00"));
        reservationItem.add(new ReservationItem(R.drawable.curry,"정통 인도 커리","볼드모트",0,"2018-10-11","15:00:00"));

        // 3. Adapter
        adapter = new ReservationAdapter(reservationItem,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);


        return  v;
    }

}
