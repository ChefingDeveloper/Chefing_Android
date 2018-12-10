package com.example.jinyoungkim.shefing.ui.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.account.LoginPost;
import com.example.jinyoungkim.shefing.model.account.LoginResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.ui.detail.DetailActivity;
import com.example.jinyoungkim.shefing.ui.detail.detail_menu.MenuDetailActivity;
import com.example.jinyoungkim.shefing.ui.detail.detail_tab.MenuTab.DetailMenuFragment;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SessionCallback;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ImageView logo_login;
    RelativeLayout goto_kakaologin, goto_main;
    TextView text_login;
    private SessionCallback sessionCallback;
    private String token;
    private String profileUrl;
    private String userName;
    private long userID;
    private String userEmail;
    private NetworkService networkService;
    private LoginPost loginPost;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1.  상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 2. View init
        logo_login= (ImageView)findViewById(R.id.logo_login);
        goto_kakaologin = (RelativeLayout)findViewById(R.id.goto_kakaologin);
//        goto_main = (RelativeLayout)findViewById(R.id.goto_main);
        text_login = (TextView)findViewById(R.id.text_login);

        from = getIntent().getStringExtra("from");

        // networkservice init & 자동로그인
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        if(!SharedPreferenceController.getLogin(getApplicationContext()).equals("")){

            GlobalApplication.getGlobalApplicationContext().makeToast("자동 로그인 되었습니다 : )");
            if(from.equals("splash")){
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }else if(from.equals("mypage")){
                Intent i = new Intent(getApplicationContext(),MyPageActivity.class);
                startActivity(i);
                finish();
            }else if(from.equals("menu")){ // 메뉴 상세화면에서 넘어왔을 때
                int menu_id = getIntent().getIntExtra("menu_id",0);
                Intent i = new Intent(getApplicationContext(),MenuDetailActivity.class);
                i.putExtra("menu_id",menu_id);
                startActivity(i);
                finish();
            }
        }

        final Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        text_login.startAnimation(fade);

        final  Animation trasition = AnimationUtils.loadAnimation(this, R.anim.fade);
        goto_kakaologin.startAnimation(trasition);
//        goto_main.startAnimation(trasition);


        // 4. 메인화면으로 가기
//        goto_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0,0);
//                finish();
//            }
//        });

        // 5. 카카오 로그인
        goto_kakaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /***** kakao Login ******/
                kakaoLogin();


            }
        });

    }

    private void kakaoLogin(){
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL,this);
        Log.e("kakaoLogin()","in");
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.e("TAG" , "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.e("TAG" , exception.getMessage());
            }
        }
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {

        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG" , "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("TAG" , "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {

                profileUrl = userProfile.getProfileImagePath();
                userName = userProfile.getNickname();
                token = Session.getCurrentSession().getAccessToken();
                userID = userProfile.getId();


                Log.e("유저 이름: ",userName);
                Log.e("유저 토큰( 카카오 토큰 ): ",token);
                Log.e("유저 아이디: ",String.valueOf(userID));
                if(profileUrl!=null){
                    Log.e("프로필 사진 )",profileUrl);
                }else{
                    profileUrl=""; // 기본 이미지
                }

                SharedPreferenceController.setTokenHeader(getApplicationContext(),token);
                SharedPreferenceController.setLogin(getApplicationContext(),"yes");
                SharedPreferenceController.setNickname(getApplicationContext(),userName);
                SharedPreferenceController.setProfile(getApplicationContext(),profileUrl);
                SharedPreferenceController.setUserID(getApplicationContext(),userID);
                Toast.makeText(getApplicationContext(),userName+"님 반갑습니다 : )",Toast.LENGTH_SHORT).show();


                if(from.equals("splash")){
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }else if(from.equals("mypage")){
                    Intent i = new Intent(getApplicationContext(),MyPageActivity.class);
                    startActivity(i);
                    finish();
                }else if(from.equals("menu")){ // 메뉴 상세화면에서 넘어왔을 때
                    int menu_id = getIntent().getIntExtra("menu_id",0);
                    Intent i = new Intent(getApplicationContext(),MenuDetailActivity.class);
                    i.putExtra("menu_id",menu_id);
                    startActivity(i);
                    finish();
                }

//                loginPost = new LoginPost(SharedPreferenceController.getTokenHeader(getApplicationContext()));
//                Call<LoginResult> loginResultCall = networkService.login(loginPost);
//                loginResultCall.enqueue(new Callback<LoginResult>() {
//                    @Override
//                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
//                        if(response.isSuccessful()){
//
//                            Log.e("message: ",response.body().message);
//
//                            SharedPreferenceController.setTokenHeader(getApplicationContext(),token);
//                            SharedPreferenceController.setLogin(getApplicationContext(),"yes");
//                            SharedPreferenceController.setNickname(getApplicationContext(),userName);
//                            SharedPreferenceController.setProfile(getApplicationContext(),profileUrl);
//                            SharedPreferenceController.setUserID(getApplicationContext(),userID);
//                            Toast.makeText(getApplicationContext(),userName+"님 반갑습니다 : )",Toast.LENGTH_SHORT).show();
//
//
//                            if(from.equals("splash")){
//                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
//                                startActivity(i);
//                                finish();
//                            }else if(from.equals("mypage")){
//                                Intent i = new Intent(getApplicationContext(),MyPageActivity.class);
//                                startActivity(i);
//                                finish();
//                            }else { // 메뉴 상세화면에서 넘어왔을 때
//                                Intent i = new Intent(getApplicationContext(),DetailActivity.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResult> call, Throwable t) {
//                        Log.e("로그인 실패 ) ","FAIL");
//                        GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 :(");
//                    }
//                });



            }


            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
                Log.e("here","here");

            }
        });
    }

}
