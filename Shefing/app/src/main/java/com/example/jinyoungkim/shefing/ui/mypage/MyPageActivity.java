package com.example.jinyoungkim.shefing.ui.mypage;

/******  마이 페이지  ******/

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;
import com.example.jinyoungkim.shefing.ui.mypage.like_tab.LikeFragment;
import com.example.jinyoungkim.shefing.ui.mypage.reservation_tab.ReservationFragment;
import com.example.jinyoungkim.shefing.ui.mypage.review_tab.ReviewFragment;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;

import java.net.Inet4Address;

public class MyPageActivity extends AppCompatActivity {

    private ImageView menuIcon_mypage;
    private OpenMenu openMenu;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        menuIcon_mypage = (ImageView)findViewById(R.id.menuicon_mypage);
        tabLayout = (TabLayout)findViewById(R.id.tab_mypage);
        viewPager = (ViewPager)findViewById(R.id.viewpager_mypage);

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_mypage);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        // 1. Menu
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu.show();
            }
        });

        /***** 로그인이 안되었을 경우 *****/
        if(!SharedPreferenceController.getLogin(this).equals("yes")){
            GlobalApplication.getGlobalApplicationContext().makeToast("로그인이 필요한 기능입니다 : )");
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            i.putExtra("from","mypage");
            startActivity(i);
        }


        // 2. TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("예약내역"));
        tabLayout.addTab(tabLayout.newTab().setText("찜"));
        tabLayout.addTab(tabLayout.newTab().setText("리뷰"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // 2-1. 탭 어댑터
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // 2-2. 탭 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        changeTabsFont();
    }

    private void changeTabsFont(){

        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/nanum_bold.ttf");

        ViewGroup vg = (ViewGroup)tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for(int j=0;j<tabsCount; j++){
            ViewGroup vgTab = (ViewGroup)vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();

            for(int i=0;i<tabChildsCount; i++){
                View tabViewChild = vgTab.getChildAt(i);
                if(tabViewChild instanceof TextView){
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

    /***** 탭 레이아웃 어댑터 클래스 ******/
    public class  TabPagerAdapter extends FragmentStatePagerAdapter {

        private int tabCount; // 탭 개수

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    ReservationFragment reservationFragment= new ReservationFragment();
                    return reservationFragment;

                case 1:
                    LikeFragment likeFragment = new LikeFragment();
                    return likeFragment;

                case 2:
                    ReviewFragment reviewFragment = new ReviewFragment();
                    return reviewFragment;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

}
