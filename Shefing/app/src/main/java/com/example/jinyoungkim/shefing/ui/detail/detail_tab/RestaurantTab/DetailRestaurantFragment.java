package com.example.jinyoungkim.shefing.ui.detail.detail_tab.RestaurantTab;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailShopResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.util.GlobalApplication;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailRestaurantFragment extends Fragment {

    ImageView arrow;
    ScrollView scrollView;
    LinearLayout openmap;
    TextView shopName, shopLocationName;
    ImageView image1, image2, image3, image4, image5;

    NetworkService networkService;
    RequestManager requestManager;

    int shop_id=0;
    double la, lo;
    String shop_name, shop_location_name, shop_image1, shop_image2, shop_image3, shop_image4, shop_image5;

    public DetailRestaurantFragment() {
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_detail_restaurant, container, false);

        scrollView = (ScrollView)v.findViewById(R.id.scroll_shop);
        openmap = (LinearLayout)v.findViewById(R.id.openmap);
        shopName = (TextView)v.findViewById(R.id.shop_name);
        shopLocationName = (TextView)v.findViewById(R.id.shop_location_name);
        image1 = (ImageView)v.findViewById(R.id.shop_image1);
        image2 = (ImageView)v.findViewById(R.id.shop_image2);
        image3 = (ImageView)v.findViewById(R.id.shop_image3);
        image4 = (ImageView)v.findViewById(R.id.shop_image4);
        image5 = (ImageView)v.findViewById(R.id.shop_image5);

        // 1. scroll
        arrow = (ImageView)v.findViewById(R.id.arrow);
        final Animation traslation = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
        arrow.startAnimation(traslation);

//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                arrow.setVisibility(View.GONE);
//            }
//        });

        // 2. networking
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        requestManager = Glide.with(this);
        shop_id = getArguments().getInt("shop_id");

        Call<DetailShopResult> detailShopResultCall = networkService.detailshop(shop_id);
        detailShopResultCall.enqueue(new Callback<DetailShopResult>() {
            @Override
            public void onResponse(Call<DetailShopResult> call, Response<DetailShopResult> response) {
                if(response.isSuccessful()){
                    shop_name = response.body().data.get(0).shop_name;
                    shopName.setText(shop_name);

                    shop_location_name = response.body().data.get(0).shop_location_name;
                    shopLocationName.setText(shop_location_name);

                    la = response.body().data.get(0).shop_location_la;
                    lo = response.body().data.get(0).shop_location_lo;

                    // 3. map
                    MapView mapView = new MapView(getActivity());
                    ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map);
                    mapViewContainer.addView(mapView);

                    // 중심점 변경 - 예제 좌표는 서울 남산
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(la, lo), true);

                    // 줌 레벨 변경
                    mapView.setZoomLevel(3, true);

                    //마커 찍기
                    MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(la, lo);
                    MapPOIItem marker = new MapPOIItem();
                    marker.setItemName(shop_location_name); // 레스토랑 주소
                    marker.setTag(0);
                    marker.setMapPoint(MARKER_POINT);
                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // custom pin
                    marker.setCustomImageResourceId(R.drawable.pin);

                    mapView.addPOIItem(marker);

                    Log.e("la",String.valueOf(la));
                    Log.e("lo",String.valueOf(lo));

                    shop_image1 = response.body().data.get(0).shop_image1;
                    shop_image2 = response.body().data.get(0).shop_image2;
                    shop_image3 = response.body().data.get(0).shop_image3;
                    shop_image4 = response.body().data.get(0).shop_image4;
                    shop_image5 = response.body().data.get(0).shop_image5;

                    requestManager.load(shop_image1).into(image1);
                    requestManager.load(shop_image2).into(image2);
                    requestManager.load(shop_image3).into(image3);
                    requestManager.load(shop_image4).into(image4);
                    requestManager.load(shop_image5).into(image5);

                }
            }

            @Override
            public void onFailure(Call<DetailShopResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
            }
        });




        // open map
        openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://map.daum.net/link/map/"+String.valueOf(la)+","+String.valueOf(lo)));
                startActivity(i);
                // 위도 경도 넘겨 주기 ( 추후에 수정 )

            }
        });


        return v;
    }

}
