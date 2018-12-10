package com.example.jinyoungkim.shefing.ui.main.mainlist;

/******  메인 페이지  ******/

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.main.MainListData;
import com.example.jinyoungkim.shefing.model.main.MainListResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageView menuIcon;
    ArrayList<MainListData> mainItem;
    ArrayList<MainListData> mainlist;
    MainAdapter adapter;
    OpenMenu openMenu;
    RequestManager requestManager;

    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("token : ", SharedPreferenceController.getTokenHeader(getApplicationContext()));
        Log.e("aaa","aaaa");

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_main);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);
        // 0. 메뉴
        menuIcon = (ImageView)findViewById(R.id.menuicon);
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.show();

            }
        });

        // 1. recyclerview
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // 2. requestManager
        requestManager=Glide.with(this);

        // hash key 발급
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.jinyoungkim.shefing", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        //  Networking
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();

        getmainlist();



    }
    public void getmainlist(){
        Call<MainListResult> mainListResultCall = networkService.mainlist();
        mainListResultCall.enqueue(new Callback<MainListResult>() {
            @Override
            public void onResponse(Call<MainListResult> call, Response<MainListResult> response) {
                if(response.isSuccessful()){
                    mainItem = new ArrayList<>();
                    mainlist = new ArrayList<>();
                    mainItem = response.body().data;
                    for(int i=0;i<mainItem.size();i++){
                       mainlist.add(new MainListData(mainItem.get(i).menu_id,mainItem.get(i).menu_name,mainItem.get(i).menu_price,
                                                     mainItem.get(i).menu_image, mainItem.get(i).shop_location_name, mainItem.get(i).chef_image,
                                                     mainItem.get(i).chef_name, mainItem.get(i).chef_id, mainItem.get(i).shop_id));

                    }
                    //  Adapter
                    adapter = new MainAdapter(mainlist, getApplicationContext(),requestManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<MainListResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }
        });
    }

}
