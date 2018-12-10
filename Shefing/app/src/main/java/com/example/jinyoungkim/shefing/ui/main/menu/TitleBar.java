package com.example.jinyoungkim.shefing.ui.main.menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;

/******* 앱 전체 타이틀 바 ********/

public class TitleBar extends RelativeLayout{

    TextView logo; // 상단 로고
    ImageView menuicon; // 메뉴 버튼
    Context context;

    public TitleBar(Context context, View v){
        super(context);
        this.context = context;
        init(context,v);
    }


    private void init(final Context context, View v) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.titlebar, (ViewGroup) v);
        this.context = context;
        logo = (TextView) v.findViewById(R.id.logo);
        menuicon = (ImageView) v.findViewById(R.id.menuicon);

        // 1. 로고 리스너 - 메인 페이지로 이동
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(),MainActivity.class);
                context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

}
