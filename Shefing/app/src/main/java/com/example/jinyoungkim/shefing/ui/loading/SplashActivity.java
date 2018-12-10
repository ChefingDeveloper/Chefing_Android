package com.example.jinyoungkim.shefing.ui.loading;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.ui.mypage.LoginActivity;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;

public class SplashActivity extends AppCompatActivity {

    LinearLayout layout_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        layout_splash = (LinearLayout)findViewById(R.id.layout_splash);

        final Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        layout_splash.startAnimation(fade);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("from","splash");
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();

            }
        }, 3000);// 2 초

    }

}
