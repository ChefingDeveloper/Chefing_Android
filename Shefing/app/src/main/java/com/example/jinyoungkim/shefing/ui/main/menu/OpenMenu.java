package com.example.jinyoungkim.shefing.ui.main.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.cart.CartActivity;
import com.example.jinyoungkim.shefing.ui.info.InfoActivity;
import com.example.jinyoungkim.shefing.ui.mypage.MyPageActivity;
import com.example.jinyoungkim.shefing.ui.setting.SettingActivity;

public class OpenMenu extends Dialog {

    private Context context;
    ImageView menu_my, menu_info, menu_setting, menu_cart;

    public OpenMenu(final Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.menu_btns);

        menu_my = (ImageView)findViewById(R.id.menu_my);
        menu_info = (ImageView)findViewById(R.id.menu_info);
        menu_setting = (ImageView)findViewById(R.id.menu_setting);
        menu_cart = (ImageView)findViewById(R.id.menu_cart);

        // 1. 마이 페이지
        menu_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(),MyPageActivity.class);
                context.startActivity(i);
                dismiss();
                ((Activity)context).finish();
            }
        });

        // 2. 장바구니
        menu_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(),CartActivity.class);
                context.startActivity(i);
                dismiss();
                ((Activity)context).finish();
            }
        });

        // 3. 문의 사항
        menu_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(),InfoActivity.class);
                context.startActivity(i);
                dismiss();
                ((Activity)context).finish();
            }
        });

        // 4. 환경설정
        menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(),SettingActivity.class);
                context.startActivity(i);
                dismiss();
                ((Activity)context).finish();
            }
        });

    }
}
