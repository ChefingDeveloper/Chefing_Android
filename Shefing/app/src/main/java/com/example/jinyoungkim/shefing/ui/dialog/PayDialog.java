package com.example.jinyoungkim.shefing.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.example.jinyoungkim.shefing.R;

public class PayDialog extends Dialog {

    private Context context;

    public PayDialog(Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_pay);

        LottieAnimationView lottie = (LottieAnimationView)findViewById(R.id.lottie_paydialog);
        lottie.playAnimation();
        lottie.loop(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                dismiss();
            }
        }, 3000);// 2 초




    }


}
