package com.example.jinyoungkim.shefing.ui.mypage.reservation_tab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;

public class ReservationDetailActivity extends AppCompatActivity {

    ImageView menuIcon;
    TextView reservation_detail_shop; // 매장위치
    TextView reservation_detail_chef; // 셰프정보
    TextView reservation_detail_date; // 예약일시
    TextView reservation_detail_menu; // 메뉴이름
    TextView reservation_detail_count; // 메뉴수량
    TextView reservation_detail_paydate; // 결제일시
    TextView reservation_detail_paymethod; // 결제수단
    TextView reservation_detail_payprice; // 결제금액

    RelativeLayout btn_cancel; // 결제취소 버튼

    OpenMenu openMenu; // 오픈메뉴

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_reservation_detail);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        openMenu = new OpenMenu(this);
        menuIcon = (ImageView)findViewById(R.id.menuicon_reservation_detail);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.show();
            }
        });

        // 1. view init
        reservation_detail_shop = (TextView)findViewById(R.id.reservation_detail_shop);
        reservation_detail_chef = (TextView)findViewById(R.id.reservation_detail_chef);
        reservation_detail_date = (TextView)findViewById(R.id.reservation_detail_date);
        reservation_detail_menu = (TextView)findViewById(R.id.reservation_detail_menu);
        reservation_detail_count = (TextView)findViewById(R.id.reservation_detail_count);
        reservation_detail_paydate = (TextView)findViewById(R.id.reservation_detail_paydate);
        reservation_detail_paymethod = (TextView)findViewById(R.id.reservation_detail_paymethod);
        reservation_detail_payprice = (TextView)findViewById(R.id.reservation_detail_payprice);
        btn_cancel = (RelativeLayout)findViewById(R.id.btn_cancel);




    }
}
