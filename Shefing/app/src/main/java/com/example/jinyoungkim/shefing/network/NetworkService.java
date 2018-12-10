package com.example.jinyoungkim.shefing.network;

import com.example.jinyoungkim.shefing.model.account.LoginPost;
import com.example.jinyoungkim.shefing.model.account.LoginResult;
import com.example.jinyoungkim.shefing.model.cart.AddCartPost;
import com.example.jinyoungkim.shefing.model.cart.AddCartResult;
import com.example.jinyoungkim.shefing.model.cart.DeleteCartResult;
import com.example.jinyoungkim.shefing.model.cart.DuplicateCartPost;
import com.example.jinyoungkim.shefing.model.cart.DuplicateCartResult;
import com.example.jinyoungkim.shefing.model.cart.GetCartPost;
import com.example.jinyoungkim.shefing.model.cart.GetCartResult;
import com.example.jinyoungkim.shefing.model.cart.GetShopIdData;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailChefResult;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailMainMenuResult;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailShopResult;
import com.example.jinyoungkim.shefing.model.detail_tab.DetailSideMenuResult;
import com.example.jinyoungkim.shefing.model.detail_menu.MenuResult;
import com.example.jinyoungkim.shefing.model.main.MainListResult;
import com.example.jinyoungkim.shefing.model.reservation.GetDatePost;
import com.example.jinyoungkim.shefing.model.reservation.GetDateResult;
import com.example.jinyoungkim.shefing.model.reservation.GetTimeResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

    // 0. 유저 등록 ( 로그인 )
    @POST("/account/signin")
    Call<LoginResult> login(@Body LoginPost loginPost);


    // 1. 메인리스트_메뉴
    @GET("/main")
    Call<MainListResult> mainlist();


    // 2. 상세보기 탭
    // 2-1. 메뉴 정보 ( 메인 메뉴 )
    @GET("/detail/mainmenu/{chef_id}")
    Call<DetailMainMenuResult> detailmainmenulist(@Path("chef_id")int chef_id);

    // 2-2. 메뉴 정보 ( 사이드 메뉴 )
    @GET("/detail/sidemenu/{chef_id}")
    Call<DetailSideMenuResult>detailsidemenulist(@Path("chef_id")int chef_id);

    // 2-3. 레스토랑( 숍 ) 정보
    @GET("detail/shopinfo/{shop_id}")
    Call<DetailShopResult>detailshop(@Path("shop_id")int shop_id);

    // 2-4. 셰프 정보
    @GET("detail/chefinfo/{chef_id}")
    Call<DetailChefResult>detailchef(@Path("chef_id")int chef_id);


    // 3. 메뉴 상세화면
    @GET("/menu/{menu_id}")
    Call<MenuResult>detailmenu(@Path("menu_id")int menu_id);

    // 4. 장바구니
    // 4-1. 장바구니 담기
    @POST("/cart/addcart")
    Call<AddCartResult>addcart(@Body AddCartPost addCartPost);

    // 4-1-2. 장바구니 중복체크
    @POST("/cart/doublecheck")
    Call<DuplicateCartResult>duplicatecart(@Body DuplicateCartPost duplicateCartPost);

    // 4-2. 장바구니 가져오기
    @POST("/cart/getcart")
    Call<GetCartResult>getcart(@Body GetCartPost getCartPost);

    // 4-3. 장바구니 삭제
    @DELETE("/cart/deletecart/{cart_id}")
    Call<DeleteCartResult>deletecart(@Path("cart_id")int cart_id);

    // 4-4. 레스토랑 고유 아이디 가져오기
    @GET("/cart/getshopid/{menu_id}")
    Call<GetShopIdData>getshopid(@Path("menu_id")int menu_id);



    // 5. 예약하기
    // 5-1. 좌석 수에 따른 날짜 가져오기
    @POST("/reservation/getdate")
    Call<GetDateResult>getdate(@Body GetDatePost getDatePost);

    @GET("/reservation/gettime/{date_id}")
    Call<GetTimeResult>gettime(@Path("date_id")int date_id);


}
