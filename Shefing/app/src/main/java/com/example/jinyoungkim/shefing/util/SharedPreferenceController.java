package com.example.jinyoungkim.shefing.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceController {

    private static final String TOKEN_COMPARE = "token_compare"; // 이전 토큰값과 똑같은지 비교하기 위한 변수
    private static final String TOKEN_HEADER = "token_header"; // 서버로 부터 받은 토큰 값 (헤더에 넣어야 할)
    private static final String LOGIN = "login";
    private static final String NICKNAME = "nickname";
    private static final String PROFILE = "profile";
    private static final String USERID = "user_id";
    private static final String CART="cart";

    // 1. 토큰 값 비교
    public static void setTokenCompare(Context context, String tokenCompare){
        SharedPreferences pref = context.getSharedPreferences(TOKEN_COMPARE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN_COMPARE,tokenCompare);
        editor.commit();
    }

    public static String getTokenCompare(Context context){
        SharedPreferences pref = context.getSharedPreferences(TOKEN_COMPARE,context.MODE_PRIVATE);
        String tokenCompare = pref.getString(TOKEN_COMPARE,"");
        return tokenCompare;
    }

    // 2. 서버로 부터 받은 토큰 값
    public static void setTokenHeader(Context context,String tokenHeader){
        SharedPreferences pref = context.getSharedPreferences(TOKEN_HEADER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN_HEADER,tokenHeader);
        editor.commit();
    }

    public static String getTokenHeader(Context context){
        SharedPreferences pref = context.getSharedPreferences(TOKEN_HEADER,context.MODE_PRIVATE);
        String tokenHeader = pref.getString(TOKEN_HEADER,"");
        return tokenHeader;
    }

    public static void deleteUserInfo (Context context){
        SharedPreferences pref = context.getSharedPreferences(TOKEN_HEADER,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(TOKEN_HEADER);
        editor.remove(NICKNAME);
        editor.remove(PROFILE);
        Log.e("delete","delete");
    }

    // 3. 로그인 여부
    public static void setLogin(Context context, String login){
        SharedPreferences pref = context.getSharedPreferences(LOGIN,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LOGIN,login);
        editor.commit();
    }

    public static String getLogin(Context context){
        SharedPreferences pref = context.getSharedPreferences(LOGIN,context.MODE_PRIVATE);
        String login = pref.getString(LOGIN,"");
        return login;
    }

    // 4. 닉네임
    public static void setNickname(Context context, String nickname){
        SharedPreferences pref = context.getSharedPreferences(NICKNAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(NICKNAME,nickname);
        editor.commit();
    }

    public static String getNickname(Context context){
        SharedPreferences pref = context.getSharedPreferences(NICKNAME,context.MODE_PRIVATE);
        String nickname = pref.getString(NICKNAME,"");
        return nickname;
    }

    // 5. 프로필 사진
    public static void setProfile(Context context, String profile){
        SharedPreferences pref = context.getSharedPreferences(PROFILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PROFILE,profile);
        editor.commit();
    }

    public static String getProfile(Context context){
        SharedPreferences pref = context.getSharedPreferences(PROFILE,context.MODE_PRIVATE);
        String profile = pref.getString(PROFILE,"");
        return profile;
    }

    // 6. 유저 고유 아이디
    public static void setUserID(Context context, long user_id){
        SharedPreferences pref = context.getSharedPreferences(USERID,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(USERID,user_id);
        editor.commit();
    }

    public static long getUserID(Context context){
        SharedPreferences pref = context.getSharedPreferences(USERID,context.MODE_PRIVATE);
        long userID = pref.getLong(USERID,0);
        return userID;
    }

    // 7. 장바구니에 shop_id 넣기
    public static void setShopId(Context context, int shop_id){
        SharedPreferences pref = context.getSharedPreferences(CART, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CART,shop_id);
        editor.commit();
    }

    public static int getShopId(Context context){
        SharedPreferences pref = context.getSharedPreferences(CART, context.MODE_PRIVATE);
        int shop_id = pref.getInt(CART,0);
        return shop_id;
    }

}
