package com.example.jinyoungkim.shefing.ui.detail.detail_menu;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.jinyoungkim.shefing.model.cart.AddCartPost;
import com.example.jinyoungkim.shefing.model.cart.AddCartResult;
import com.example.jinyoungkim.shefing.model.cart.DuplicateCartPost;
import com.example.jinyoungkim.shefing.model.cart.DuplicateCartResult;
import com.example.jinyoungkim.shefing.model.detail_menu.MenuResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.ui.detail.DetailActivity;
import com.example.jinyoungkim.shefing.ui.dialog.CartDialog;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;
import com.example.jinyoungkim.shefing.ui.mypage.LoginActivity;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuDetailActivity extends AppCompatActivity {

    private RelativeLayout btn_putcart;
    private ImageView menuicon_menu_detail, menu_image;
    private OpenMenu openMenu;
    private ImageView minus_button, plus_button;
    private TextView count, total_price, name_menu_detail, price_menu_detail,explain_menu_detail;
    int count_i=1;
    int total_price_i, price_i;

    int menu_id=0;
    int shop_id;
    int chef_id=0;
    String str_menu_image;
    String str_menu_name;

    AddCartPost addCartPost;
    DuplicateCartPost duplicateCartPost;
    NetworkService networkService;
    RequestManager requestManager;


    CartDialog cartDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        menuicon_menu_detail = (ImageView)findViewById(R.id.menuicon_menu_detail);

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_menu_detail);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        // 1. MenuIcon
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuicon_menu_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu.show();
            }
        });


        // 2. view init
            // plus, minus
            minus_button = (ImageView)findViewById(R.id.minus_button);
            plus_button = (ImageView)findViewById(R.id.plus_button);

            // 수량, 총 가격
            count = (TextView)findViewById(R.id.count);
            total_price = (TextView)findViewById(R.id.total_price);

            //메뉴명, 가격, 설명
            name_menu_detail = (TextView)findViewById(R.id.name_menu_detail);
            price_menu_detail = (TextView)findViewById(R.id.price_menu_detail);
            explain_menu_detail = (TextView)findViewById(R.id.explain_menu_detail);
            menu_image = (ImageView)findViewById(R.id.menu_image);


        // 3. networking
        Intent i = getIntent();
        menu_id = i.getIntExtra("menu_id",menu_id);
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);

        Call<MenuResult> menuResultCall = networkService.detailmenu(menu_id);
        menuResultCall.enqueue(new Callback<MenuResult>() {
            @Override
            public void onResponse(Call<MenuResult> call, Response<MenuResult> response) {
                if(response.isSuccessful()){
                    name_menu_detail.setText(response.body().data.get(0).menu_name);
                    str_menu_name = response.body().data.get(0).menu_name;
                    price_i = response.body().data.get(0).menu_price;
                    price_menu_detail.setText(String.valueOf(price_i));
                    total_price_i = response.body().data.get(0).menu_price;
                    total_price.setText(String.valueOf(total_price_i));
                    explain_menu_detail.setText(response.body().data.get(0).menu_info);
                    requestManager.load(response.body().data.get(0).menu_image).into(menu_image);
                    str_menu_image=response.body().data.get(0).menu_image;
                    shop_id = response.body().data.get(0).shop_id;
                    chef_id = response.body().data.get(0).chef_id;

                }
            }

            @Override
            public void onFailure(Call<MenuResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }
        });


        // 4. total price ( 총 가격 )
        // plus, minus button ( 메뉴 카운팅 )
        count.setText(String.valueOf(count_i));
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_i++;
                count.setText(String.valueOf(count_i));

                total_price_i = price_i * count_i;
                total_price.setText(String.valueOf(total_price_i));
            }
        });

        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count_i>1){
                    count_i--;
                    count.setText(String.valueOf(count_i));

                    total_price_i = price_i * count_i;
                    total_price.setText(String.valueOf(total_price_i));
                }
            }
        });

        // 5. 장바구니
        btn_putcart = (RelativeLayout)findViewById(R.id.btn_putcart);
        btn_putcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("menu_id: ",String.valueOf(menu_id));
                Log.e("user_id: ",String.valueOf(SharedPreferenceController.getUserID(getApplicationContext())));

                if(SharedPreferenceController.getShopId(getApplicationContext())==0){
                    SharedPreferenceController.setShopId(getApplicationContext(),shop_id);
                    duplicatecart();
                }else if(SharedPreferenceController.getShopId(getApplicationContext())==shop_id){
                    duplicatecart();
                }else{
                    GlobalApplication.getGlobalApplicationContext().makeToast("동일한 셰프의 메뉴만 장바구니에 담을 수 있습니다 : (");
                }



            }
        });

    }

    // 장바구니 중복상품 체크
    public void duplicatecart(){
        duplicateCartPost = new DuplicateCartPost(menu_id, SharedPreferenceController.getUserID(getApplicationContext()));
        Call<DuplicateCartResult> duplicateCartResultCall = networkService.duplicatecart(duplicateCartPost);
        duplicateCartResultCall.enqueue(new Callback<DuplicateCartResult>() {
            @Override
            public void onResponse(Call<DuplicateCartResult> call, Response<DuplicateCartResult> response) {
                Log.e("duplicatecart","RESPONSE");
                if(response.isSuccessful()){
                    if(response.body().duplicate_id==1){
                        GlobalApplication.getGlobalApplicationContext().makeToast("동일한 상품이 장바구니에 담겨있습니다 :)");
                    }else{
                        addcart();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuplicateCartResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 : (");
            }
        });
    }

    // 장바구니 담기
    public void addcart(){
        long userID = SharedPreferenceController.getUserID(MenuDetailActivity.this);
    addCartPost= new AddCartPost(str_menu_name,count_i,total_price_i,menu_id,SharedPreferenceController.getUserID(getApplicationContext()),str_menu_image);
    Call<AddCartResult> addCartResultCall = networkService.addcart(addCartPost);
    addCartResultCall.enqueue(new Callback<AddCartResult>() {
        @Override
        public void onResponse(Call<AddCartResult> call, Response<AddCartResult> response) {
            if(response.isSuccessful()){
                cartDialog = new CartDialog(MenuDetailActivity.this);
                if (SharedPreferenceController.getLogin(getApplicationContext()).equals("yes")){
                    cartDialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){

                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                            intent.putExtra("chef_id",chef_id);
                            intent.putExtra("shop_id",shop_id);
                            finish();
                        }
                    }, 2000);// 1 초
                } else {
                    GlobalApplication.getGlobalApplicationContext().makeToast("로그인이 필요한 기능입니다.");
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("from","menu");
                    intent.putExtra("menu_id",menu_id);
                    startActivity(intent);
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<AddCartResult> call, Throwable t) {
            GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
        }
    });

    }
}
