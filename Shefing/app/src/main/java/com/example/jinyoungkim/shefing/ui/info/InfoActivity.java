package com.example.jinyoungkim.shefing.ui.info;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;

public class InfoActivity extends AppCompatActivity {

    private ImageView menuIcon_info;
    private OpenMenu openMenu;
    private ImageView logo_info;
    private RelativeLayout btn_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        menuIcon_info = (ImageView)findViewById(R.id.menuicon_info);
        logo_info = (ImageView)findViewById(R.id.logo_info);
        btn_info = (RelativeLayout)findViewById(R.id.btn_info);

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_info);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        // 1. 메뉴
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.show();
            }
        });

        // 2. 카카오 페이지로 이동
        logo_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_sWbnj"));
                startActivity(i);
            }
        });

        // 3. 문의 전화 버튼
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "07086776822";
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+tel));
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
