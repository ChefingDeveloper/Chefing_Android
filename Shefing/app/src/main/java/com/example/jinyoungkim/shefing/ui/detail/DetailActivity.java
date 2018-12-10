package com.example.jinyoungkim.shefing.ui.detail;

/******  메뉴 상세 페이지  ******/

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.ui.cart.CartActivity;
import com.example.jinyoungkim.shefing.ui.detail.detail_tab.ChefTab.DetailChefFragment;
import com.example.jinyoungkim.shefing.ui.detail.detail_tab.MenuTab.DetailMenuFragment;
import com.example.jinyoungkim.shefing.ui.detail.detail_tab.RestaurantTab.DetailRestaurantFragment;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;

public class DetailActivity extends AppCompatActivity {

    private ImageView menuIcon_detail;
    private OpenMenu openMenu;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout btn_gocart;

    DetailMenuFragment detailMenuFragment;
    DetailRestaurantFragment detailRestaurantFragment;
    DetailChefFragment detailChefFragment;

    private int chef_id=0;
    private int shop_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        menuIcon_detail = (ImageView)findViewById(R.id.menuicon_detail);
        tabLayout = (TabLayout)findViewById(R.id.tab_detail);
        viewPager = (ViewPager)findViewById(R.id.viewpager_detail);
        btn_gocart = (RelativeLayout)findViewById(R.id.btn_gocart);

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_detail);
        TitleBar titleBar = new TitleBar(getApplicationContext(), titlebar);

        // 1. 메뉴
        openMenu = new OpenMenu(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.show();

            }
        });

        // 2. 탭 레이아웃 초기화
        tabLayout.addTab(tabLayout.newTab().setText("메뉴"));
        tabLayout.addTab(tabLayout.newTab().setText("매장"));
        tabLayout.addTab(tabLayout.newTab().setText("셰프"));
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

        // 3. 장바구니 가기
        btn_gocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
                overridePendingTransition(0,0);
                finish();
            }
        });

        // 4. chef_id, shop_id
        Intent i = getIntent();
        chef_id = i.getIntExtra("chef_id",0);
        shop_id = i.getIntExtra("shop_id",0);

        changeTabsFont();


    }



    // 2-3. 탭 폰트 변경
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
    public class  TabPagerAdapter extends FragmentStatePagerAdapter{

        private int tabCount; // 탭 개수

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    detailMenuFragment= new DetailMenuFragment();
                    Bundle bundle = new Bundle(1);
                    bundle.putInt("chef_id",chef_id);
                    detailMenuFragment.setArguments(bundle);

                    return detailMenuFragment;

                case 1:
                    detailRestaurantFragment = new DetailRestaurantFragment();
                    Bundle bundle2 = new Bundle(1);
                    bundle2.putInt("shop_id",shop_id);
                    detailRestaurantFragment.setArguments(bundle2);

                    return detailRestaurantFragment;

                case 2:
                    detailChefFragment = new DetailChefFragment();
                    Bundle bundle3 = new Bundle(1);
                    bundle3.putInt("chef_id",chef_id);
                    detailChefFragment.setArguments(bundle3);

                    return detailChefFragment;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }


}
