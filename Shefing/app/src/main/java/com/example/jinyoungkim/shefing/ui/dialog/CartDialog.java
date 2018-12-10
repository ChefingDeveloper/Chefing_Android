package com.example.jinyoungkim.shefing.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import com.airbnb.lottie.LottieAnimationView;
import com.example.jinyoungkim.shefing.R;

public class CartDialog extends Dialog {
    public CartDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_cart);

        LottieAnimationView lottie = (LottieAnimationView)findViewById(R.id.lottie_cart_loading);
        lottie.playAnimation();
        lottie.loop(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
            }
        }, 3000);// 3 초


    }
}
