package com.example.jinyoungkim.shefing.ui.loading;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.jinyoungkim.shefing.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //        상태바 색상변경
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff483025);
        }

        LottieAnimationView lottie = (LottieAnimationView)findViewById(R.id.lottie_loading);
        lottie.playAnimation();
        lottie.loop(true);

    }

}
