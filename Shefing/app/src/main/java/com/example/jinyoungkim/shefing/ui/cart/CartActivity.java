package com.example.jinyoungkim.shefing.ui.cart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.cart.GetCartData;
import com.example.jinyoungkim.shefing.model.cart.GetCartPost;
import com.example.jinyoungkim.shefing.model.cart.GetCartResult;
import com.example.jinyoungkim.shefing.model.cart.GetShopIdResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.ui.dialog.PayDialog;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;
import com.example.jinyoungkim.shefing.ui.reservation.ReservationActivity;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/******  장바구니 페이지  ******/

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageView menuIcon_cart;
    ArrayList<GetCartData> cartItem;
    ArrayList<GetCartData> cartlist;
    CartAdapter adapter;
    OpenMenu openMenu;
    RelativeLayout btn_goreservation;
    PayDialog payDialog;


    RequestManager requestManager;
    private NetworkService networkService;
    GetCartPost getCartPost;

    TextView cart_total_price;
    int total=0; // 총액
    int shop_id=0;

    int delete_item=9000;

    // reservation 에 넘길 데이터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btn_goreservation = (RelativeLayout)findViewById(R.id.btn_goreservation);
        cart_total_price = (TextView)findViewById(R.id.cart_total_price);

        // shop_id
        Intent i = getIntent();
        shop_id=i.getIntExtra("shop_id",shop_id);

        // delete_item
        delete_item = i.getIntExtra("delete_item",delete_item);

        //0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_cart);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        //0. MenuIcon
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;

        menuIcon_cart=(ImageView)findViewById(R.id.menuicon_cart);
        menuIcon_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.show();

            }
        });

        // networking
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager= Glide.with(this);

        getcartlist();

        // 1. recyclerview
        recyclerView = findViewById(R.id.recyclerview_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        menuIcon_cart= (ImageView)findViewById(R.id.menuicon_cart);


        // 4. go reservation
        payDialog = new PayDialog(this);
        btn_goreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(),ReservationActivity.class);
                    i.putExtra("shop_id",shop_id);
                    startActivity(i);
                    overridePendingTransition(0,0);
                    finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
    public void getcartlist(){
        getCartPost = new GetCartPost(SharedPreferenceController.getUserID(getApplicationContext()));
        Call<GetCartResult> getCartResultCall = networkService.getcart(getCartPost);

        getCartResultCall.enqueue(new Callback<GetCartResult>() {
            @Override
            public void onResponse(Call<GetCartResult> call, Response<GetCartResult> response) {
                if(response.isSuccessful()){
                    Log.e("장바구니","success");
                    cartItem = new ArrayList<>();
                    cartlist = new ArrayList<>();
                    cartItem = response.body().data;

                    if(cartItem==null){
                        GlobalApplication.getGlobalApplicationContext().makeToast("장바구니가 비었습니다 : )");
                        SharedPreferenceController.setShopId(getApplicationContext(),0);
                        cart_total_price.setText("0");
                    }else{
                        btn_goreservation.setVisibility(View.VISIBLE);
                        for(int i=0;i<cartItem.size();i++){
                            cartlist.add(new GetCartData(cartItem.get(i).cart_id, cartItem.get(i).cart_menu_image,
                                    cartItem.get(i).cart_menu_name,cartItem.get(i).cart_menu_count,
                                    cartItem.get(i).cart_menu_total_price, cartItem.get(i).menu_id,
                                    cartItem.get(i).user_id));

                            total+=cartItem.get(i).cart_menu_total_price;
                        }
                        adapter = new CartAdapter(cartlist, getApplicationContext(), requestManager);
                        recyclerView.setAdapter(adapter);

                        if(delete_item!=9000){
                            adapter.notifyItemRemoved(delete_item);
                        }

                        cart_total_price.setText(String.valueOf(total));
                    }

                }
            }

            @Override
            public void onFailure(Call<GetCartResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("장바구니가 비었습니다 : )");
                SharedPreferenceController.setShopId(getApplicationContext(),0);
                cart_total_price.setText("0");
                btn_goreservation.setVisibility(View.GONE);
            }
        });
    }

}
