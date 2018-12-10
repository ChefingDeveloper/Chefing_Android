package com.example.jinyoungkim.shefing.ui.detail.detail_tab.ChefTab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailChefResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.util.GlobalApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailChefFragment extends Fragment {

    private NetworkService networkService;
    private RequestManager requestManager;
    ImageView chef_career;
    int chef_id=0;

    public DetailChefFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_detail_chef, container, false);
       chef_career = (ImageView)view.findViewById(R.id.chef_career);

       // 1. chef_id
        chef_id = getArguments().getInt("chef_id");

       // 2. Networking & requestManager
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);

        Call<DetailChefResult> detailChefResultCall = networkService.detailchef(chef_id);
        detailChefResultCall.enqueue(new Callback<DetailChefResult>() {
            @Override
            public void onResponse(Call<DetailChefResult> call, Response<DetailChefResult> response) {
                if(response.isSuccessful()){
                    requestManager.load(response.body().data.get(0).chef_info_image).into(chef_career);
                }
            }

            @Override
            public void onFailure(Call<DetailChefResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }
        });

        return view;
    }

}
