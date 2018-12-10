package com.example.jinyoungkim.shefing.ui.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;
import com.example.jinyoungkim.shefing.ui.mypage.LoginActivity;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class SettingActivity extends AppCompatActivity {

    private ImageView menuIcon_setting;
    private OpenMenu openMenu;
    private Switch switch_setting;
    private TextView license, logout, secession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        menuIcon_setting = (ImageView)findViewById(R.id.menuicon_setting);
        switch_setting = (Switch)findViewById(R.id.switch_setting);
        license = (TextView)findViewById(R.id.license);
        logout = (TextView)findViewById(R.id.logout);
        secession = (TextView)findViewById(R.id.secesstion);

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_setting);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        // 1. 메뉴
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.show();

            }
        });

        switch_setting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getApplicationContext(),"서비스 준비중입니다 :)",Toast.LENGTH_SHORT).show();
            }
        });

        // 2. 로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SharedPreferenceController.getLogin(getApplicationContext()).equals("")){
                    GlobalApplication.getGlobalApplicationContext().makeToast("로그아웃 되었습니다 :)");

                    UserManagement.requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Log.e("로그아웃","로그아웃");
                            SharedPreferenceController.setLogin(getApplicationContext(),""); // 로그인 삭제
                            SharedPreferenceController.setTokenHeader(getApplicationContext(),""); //헤더 삭제
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                    });
                }
            }
        });

        // 3. 탈퇴하기

        // 4. 라이센스
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
