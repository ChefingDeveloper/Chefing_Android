package com.example.jinyoungkim.shefing.ui.detail.detail_tab.MenuTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailMainMenuData;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailMainMenuResult;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailSideMenuData;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailSideMenuResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.util.GlobalApplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailMenuFragment extends Fragment {

    RecyclerView recycler_main, recycler_side;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager2;

    ArrayList<DetailMainMenuData> mainMenuItem;
    ArrayList<DetailMainMenuData> detailmainlist;

    ArrayList<DetailSideMenuData> sideMenuItem;
    ArrayList<DetailSideMenuData> detailsidelist;

    MainMenuAdapter mainMenuAdapter;
    SideMenuAdapter sideMenuAdapter;
    RequestManager requestManager;
    NetworkService networkService;

    int chef_id=0;

    public DetailMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_detail_menu, container, false);

        // 1. recyclerview - main
        recycler_main = v.findViewById(R.id.recyclerview_mainmenu);
        recycler_main.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getActivity());
        recycler_main.setLayoutManager(layoutManager);

        // 2. requestManager & chef_id
        requestManager=Glide.with(this);
        chef_id = getArguments().getInt("chef_id");
        Log.e("chef_id: ",String.valueOf(chef_id));

        // 3. Networking
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        getdetailmainlist();


        // 4. recyclerview - side
        recycler_side = v.findViewById(R.id.recyclerview_sidemenu);
        recycler_side.setHasFixedSize(true);
        layoutManager2= new LinearLayoutManager(getActivity());
        recycler_side.setLayoutManager(layoutManager2);

        // 5. Networking - side
        getdetailsidelist();


        return v;
    }
    public void getdetailmainlist(){
        Call<DetailMainMenuResult> detailMainMenuResultCall = networkService.detailmainmenulist(chef_id);
        detailMainMenuResultCall.enqueue(new Callback<DetailMainMenuResult>() {
            @Override
            public void onResponse(Call<DetailMainMenuResult> call, Response<DetailMainMenuResult> response) {
                if(response.isSuccessful()){
                    mainMenuItem = new ArrayList<>();
                    detailmainlist = new ArrayList<>();
                    mainMenuItem = response.body().data;
                    for(int i=0;i<mainMenuItem.size();i++){
                        detailmainlist.add(new DetailMainMenuData(mainMenuItem.get(i).menu_id,
                                                                  mainMenuItem.get(i).menu_name,
                                                                  mainMenuItem.get(i).menu_image,
                                                                  mainMenuItem.get(i).menu_price));
                    }
                    // Adapter
                    mainMenuAdapter = new MainMenuAdapter(detailmainlist,getActivity().getApplicationContext(),requestManager);
                    recycler_main.setAdapter(mainMenuAdapter);


                }
            }

            @Override
            public void onFailure(Call<DetailMainMenuResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }
        });
    }

    public void getdetailsidelist(){
        Call<DetailSideMenuResult> detailSideMenuResultCall = networkService.detailsidemenulist(chef_id);
        detailSideMenuResultCall.enqueue(new Callback<DetailSideMenuResult>() {
            @Override
            public void onResponse(Call<DetailSideMenuResult> call, Response<DetailSideMenuResult> response) {
                if(response.isSuccessful()){
                    sideMenuItem = new ArrayList<>();
                    detailsidelist = new ArrayList<>();
                    sideMenuItem = response.body().data;
                    for(int i=0;i<sideMenuItem.size();i++){
                        detailsidelist.add(new DetailSideMenuData(sideMenuItem.get(i).menu_id,
                                sideMenuItem.get(i).menu_name,
                                sideMenuItem.get(i).menu_image,
                                sideMenuItem.get(i).menu_price));
                    }
                    // Adapter
                    sideMenuAdapter = new SideMenuAdapter(detailsidelist,getActivity().getApplicationContext(),requestManager);
                    recycler_side.setAdapter(sideMenuAdapter);
                }
            }

            @Override
            public void onFailure(Call<DetailSideMenuResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }
        });
    }


}
