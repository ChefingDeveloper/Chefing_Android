package com.example.jinyoungkim.shefing.ui.reservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinyoungkim.shefing.R;
import com.example.jinyoungkim.shefing.model.reservation.GetDateData;
import com.example.jinyoungkim.shefing.model.reservation.GetDatePost;
import com.example.jinyoungkim.shefing.model.reservation.GetDateResult;
import com.example.jinyoungkim.shefing.model.reservation.GetTimeData;
import com.example.jinyoungkim.shefing.model.reservation.GetTimeResult;
import com.example.jinyoungkim.shefing.network.NetworkService;
import com.example.jinyoungkim.shefing.ui.dialog.PayDialog;
import com.example.jinyoungkim.shefing.ui.main.mainlist.MainActivity;
import com.example.jinyoungkim.shefing.ui.main.menu.OpenMenu;
import com.example.jinyoungkim.shefing.ui.main.menu.TitleBar;
import com.example.jinyoungkim.shefing.ui.mypage.MyPageActivity;
import com.example.jinyoungkim.shefing.util.GlobalApplication;
import com.example.jinyoungkim.shefing.util.SharedPreferenceController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.CancelListener;
import kr.co.bootpay.CloseListener;
import kr.co.bootpay.ConfirmListener;
import kr.co.bootpay.DoneListener;
import kr.co.bootpay.ErrorListener;
import kr.co.bootpay.ReadyListener;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class ReservationActivity extends FragmentActivity implements View.OnClickListener{

    // 1. 좌석 수
    ImageView seat_one, seat_two, seat_four, seat_many;
    Boolean flag_seat_one, flag_seat_two, flag_seat_four, flag_seat_many, flag;

    // 2. 캘린더
    MCalendarView calendar;
    LinearLayout below_calendar;


    // 3. 점심 시간 버튼, 시간 텍스트, 좌석 텍스트
    ImageView lunch1, lunch2, lunch3;
    Boolean flag_lunch1, flag_lunch2, flag_lunch3;

    TextView text_lunch1, text_lunch2, text_lunch3;
    TextView seat_lunch1, seat_lunch2, seat_lunch3;

    // 4. 저녁 시간 버튼, 시간 텍스트, 좌석 텍스트
    ImageView dinner1, dinner2, dinner3;
    Boolean flag_dinner1, flag_dinner2, flag_dinner3;

    TextView text_dinner1, text_dinner2, text_dinner3;
    TextView seat_dinner1, seat_dinner2, seat_dinner3;

    LinearLayout layout_lunch, layout_dinner;

    // 5. 예약 정보 ( 날짜, 시간, 메뉴, 수량, 가격 )
    TextView reservation_date_menu, reservation_time_menu, reservation_name_menu, reservation_count_menu, reservation_price_menu, reservation_price2_menu;
    String str_date;

    // 6. 카카오 페이 버튼, 이용약관 체크박스, 결제하기 버튼
    ImageView pay_method;
    CheckBox reservation_checkbox1, reservation_checkbox2;
    RelativeLayout btn_gopayment;

    // 7. 메뉴 버튼
    OpenMenu openMenu;
    ImageView menuIcon;

    // 8. Dialog
    PayDialog payDialog;
    private int stuck = 10;

    // 9. Networking
    private NetworkService networkService;
    int seat_count=0;
    int shop_id=0;
    int date_id=0;

    int click_year=0;
    int click_month=0;
    int click_day=0;

    GetDatePost getDatePost;
    ArrayList<GetDateData> getDateData;
    ArrayList<GetTimeData> getTimeData;
    HashMap<String, Integer> datemap = new HashMap<>();


    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH);
    int day = now.get(Calendar.DAY_OF_MONTH);

    // 뒤로 가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        getCalendar();
        networkService = GlobalApplication.getGlobalApplicationContext().getNetworkService();
        shop_id=SharedPreferenceController.getShopId(getApplicationContext());

        // 0. Title Bar
        RelativeLayout titlebar = (RelativeLayout)findViewById(R.id.titlebar_reservation);
        TitleBar titleBar = new TitleBar(getApplicationContext(),titlebar);

        // 1. Menu / Dialog
        openMenu = new OpenMenu(this);
        payDialog = new PayDialog(this);
        WindowManager.LayoutParams params = openMenu.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        menuIcon = (ImageView)findViewById(R.id.menuicon_reservation);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu.show();
            }
        });

        // 2. view init

            // 좌석 수 버튼
            seat_one = (ImageView)findViewById(R.id.seat_one);
            seat_two = (ImageView)findViewById(R.id.seat_two);
            seat_four = (ImageView)findViewById(R.id.seat_four);
            seat_many = (ImageView)findViewById(R.id.seat_many);

            // Calendar
            calendar = (MCalendarView) findViewById(R.id.calendar);
            calendar.init(this);
            below_calendar = (LinearLayout)findViewById(R.id.below_calendar);
            // calendar 표기 초기화


            // Lunch
            lunch1 = (ImageView)findViewById(R.id.lunch1);
            lunch2 = (ImageView)findViewById(R.id.lunch2);
            lunch3 = (ImageView)findViewById(R.id.lunch3);

            text_lunch1 = (TextView)findViewById(R.id.text_lunch1);
            text_lunch2 = (TextView)findViewById(R.id.text_lunch2);
            text_lunch3 = (TextView)findViewById(R.id.text_lunch3);

            seat_lunch1 = (TextView)findViewById(R.id.seat_lunch1);
            seat_lunch2 = (TextView)findViewById(R.id.seat_lunch2);
            seat_lunch3 = (TextView)findViewById(R.id.seat_lunch3);

            layout_lunch = (LinearLayout)findViewById(R.id.layout_lunch);

            // Dinner
            dinner1 = (ImageView)findViewById(R.id.dinner1);
            dinner2 = (ImageView)findViewById(R.id.dinner2);
            dinner3 = (ImageView)findViewById(R.id.dinner3);

            text_dinner1 = (TextView)findViewById(R.id.text_dinner1);
            text_dinner2 = (TextView)findViewById(R.id.text_dinner2);
            text_dinner3 = (TextView)findViewById(R.id.text_dinner3);

            seat_dinner1 = (TextView)findViewById(R.id.seat_dinner1);
            seat_dinner2 = (TextView)findViewById(R.id.seat_dinner2);
            seat_dinner3 = (TextView)findViewById(R.id.seat_dinner3);

            layout_dinner = (LinearLayout)findViewById(R.id.layout_dinner);

            // 예약 정보
            reservation_date_menu = (TextView)findViewById(R.id.reservation_date_menu);
            reservation_time_menu = (TextView)findViewById(R.id.reservation_time_menu);
            reservation_name_menu = (TextView)findViewById(R.id.reservation_name_menu);
            reservation_count_menu = (TextView)findViewById(R.id.reservation_count_menu);
            reservation_price_menu = (TextView)findViewById(R.id.reservation_price_menu);
            reservation_price2_menu = (TextView)findViewById(R.id.reservation_price2_menu);



            // 카카오 페이 버튼, 이용약관 체크박스, 결제하기 버튼
            pay_method = (ImageView)findViewById(R.id.pay_method);
            reservation_checkbox1 = (CheckBox)findViewById(R.id.reservation_checkbox1);
            reservation_checkbox2 = (CheckBox)findViewById(R.id.reservation_checkbox2);
            btn_gopayment = (RelativeLayout)findViewById(R.id.btn_gopayment);

            // flag init
            flag_seat_one=false; flag_seat_two=false; flag_seat_four=false; flag_seat_many=false;
            flag_lunch1=false; flag_lunch2=false; flag_lunch3=false;
            flag_dinner1=false; flag_dinner2=false; flag_dinner3=false;


        // 3. 좌석 수 버튼 클릭
        seat_one.setOnClickListener(this);
        seat_two.setOnClickListener(this);
        seat_four.setOnClickListener(this);
        seat_many.setOnClickListener(this);

        // 4. 점심 시간 버튼 클릭
        lunch1.setOnClickListener(this);
        lunch2.setOnClickListener(this);
        lunch3.setOnClickListener(this);

        // 5. 저녁 시간 버튼 클릭
        dinner1.setOnClickListener(this);
        dinner2.setOnClickListener(this);
        dinner3.setOnClickListener(this);

        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                if(seat_count==0){
                    GlobalApplication.getGlobalApplicationContext().makeToast("좌석 테이블을 먼저 선택해주세요 : )");
                }else {
                    str_date = String.valueOf(date.getYear())+date.getMonthString()+date.getDayString();
                    Log.e("클릭한 날짜: ",str_date);
                    if(datemap.get(str_date)==null){
                        GlobalApplication.getGlobalApplicationContext().makeToast("운영되지 않는 날입니다 :(");
                    }else{
                        click_year=date.getYear();
                        click_month=date.getMonth();
                        click_day=date.getDay();

                        String str = String.valueOf(click_month)+"월 "+String.valueOf(click_day)+"일";
                        GlobalApplication.getGlobalApplicationContext().makeToast(str);

                        Log.e("클릭한 날짜의 date_id: ",String.valueOf(datemap.get(str_date)));
                        date_id=datemap.get(str_date);
                        gettime();
                    }
                }
            }
        });

        // 부트페이 초기 설정
        BootpayAnalytics.init(this,"5baa746eb6d49c5a2452ee7f");


        // 6. 결제하기 버튼
        btn_gopayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Bootpay.init(getFragmentManager())
                                .setApplicationId("5baa746eb6d49c5a2452ee7f") // 해당 프로젝트(안드로이드)의 application id 값
                                .setPG(PG.KAKAO) // 결제할 PG 사
                                .setUserPhone("010-1234-5678") // 구매자 전화번호
                                .setMethod(Method.EASY) // 결제수단
                                .setName("스테이크 만찬") // 결제할 상품명
                                .setOrderId("1234") //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
                                .setPrice(1000) // 결제할 금액
                                //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
                                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
                                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                                    @Override
                                    public void onConfirm(@Nullable String message) {
                                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
//                                else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                        Log.d("confirm", message);

                                    }
                                })
                                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                                    @Override
                                    public void onDone(@Nullable String message) {
                                        Log.d("done", message);
                                        Intent i = new Intent(getApplicationContext(), MyPageActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                                    @Override
                                    public void onReady(@Nullable String message) {
                                        Log.d("ready", message);
                                    }
                                })
                                .onCancel(new CancelListener() { // 결제 취소시 호출
                                    @Override
                                    public void onCancel(@Nullable String message) {
                                        Log.d("cancel", message);
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                                    @Override
                                    public void onError(@Nullable String message) {
                                        Log.d("error", message);
                                    }
                                })
                                .onClose(new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                    @Override
                                    public void onClose(String message) {
                                        Log.d("close", "close");
                                    }
                                })
                                .show();
                    }
                }, 3000);// 2 초


            }
        });


    }

    // networking 1) 좌석 수 에 따른 날짜 가져와서 달력에 표시하기
    public void getdate(){
        Log.e("post","shop_id"+String.valueOf(shop_id)+" seat_count"+String.valueOf(seat_count));
        getDatePost = new GetDatePost(shop_id, seat_count);
        Call<GetDateResult> getDateResultCall = networkService.getdate(getDatePost);
        getDateResultCall.enqueue(new Callback<GetDateResult>() {
            @Override
            public void onResponse(Call<GetDateResult> call, Response<GetDateResult> response) {
                Log.e("getDate","onResponse");
                if(response.isSuccessful()){
                    getDateData = new ArrayList<>();
                    getDateData = response.body().data;
                    Log.e("success","calendar");

                    for(int i=0;i<getDateData.size();i++){
                        Log.e("오늘 날짜 : ",String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
                        Log.e("date",String.valueOf(getDateData.get(i).date_month)+"/"+String.valueOf(getDateData.get(i).date_day));
                        String str_month;
                        String str_day;

                        if(!(getDateData.get(i).date_year < year || getDateData.get(i).date_month < month || getDateData.get(i).date_day < day)){
                            calendar.markDate(new DateData(getDateData.get(i).date_year,getDateData.get(i).date_month,getDateData.get(i).date_day).setMarkStyle(MarkStyle.DOT,Color.parseColor("#483025")));

                        }else{
                            getDateData.remove(i);
                        }
                            if (getDateData.get(i).date_month<10){
                                str_month="0"+String.valueOf(getDateData.get(i).date_month);
                            }else{
                                str_month=String.valueOf(getDateData.get(i).date_month);
                            }

                            if(getDateData.get(i).date_day<10){
                                str_day = "0"+String.valueOf(getDateData.get(i).date_day);
                            }else{
                                str_day=String.valueOf(getDateData.get(i).date_day);
                            }
                            String str_date = String.valueOf(getDateData.get(i).date_year)+str_month+str_day;
                            datemap.put(str_date,getDateData.get(i).date_id);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetDateResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("서버 상태를 확인해주세요 : (");
            }
        });
    }

    // networking 2) 날짜에 따른 시간 가져오기
    public void gettime(){
        Call<GetTimeResult> getTimeResultCall = networkService.gettime(date_id);
        getTimeResultCall.enqueue(new Callback<GetTimeResult>() {
            @Override
            public void onResponse(Call<GetTimeResult> call, Response<GetTimeResult> response) {
                if(response.isSuccessful()){

                    getTimeData = new ArrayList<>();
                    getTimeData = response.body().data;

                    if(getTimeData.size()==0){
                        GlobalApplication.getGlobalApplicationContext().makeToast("이런 지져스! 시간 등록이 안되어 있는 레스토랑입니다 :0 조금만 기다려 주세요 Coming Soon~");
                    } else if(getTimeData.size()==2){ // 점심 하나. 저녁 하나 ( 사실 상, 점심 하나 저녁 둘 이렇게 시간이 될 수도 있는건데,, 정해진 매뉴얼이 암것도 없어서 그저 회의내용 바탕으로 짜자.. )

                        layout_lunch.setVisibility(View.VISIBLE);
                        layout_dinner.setVisibility(View.VISIBLE);

                        lunch1.setVisibility(View.VISIBLE);
                        text_lunch1.setVisibility(View.VISIBLE);
                        seat_lunch1.setVisibility(View.VISIBLE);
                        lunch2.setVisibility(View.GONE);
                        text_lunch2.setVisibility(View.GONE);
                        seat_lunch2.setVisibility(View.GONE);
                        lunch3.setVisibility(View.GONE);
                        text_lunch3.setVisibility(View.GONE);
                        seat_lunch3.setVisibility(View.GONE);


                        dinner1.setVisibility(View.VISIBLE);
                        text_dinner1.setVisibility(View.VISIBLE);
                        seat_dinner1.setVisibility(View.VISIBLE);
                        dinner2.setVisibility(View.GONE);
                        text_dinner2.setVisibility(View.GONE);
                        seat_dinner2.setVisibility(View.GONE);
                        dinner3.setVisibility(View.GONE);
                        text_dinner3.setVisibility(View.GONE);
                        seat_dinner3.setVisibility(View.GONE);

                        text_lunch1.setText(getTimeData.get(0).time);
                        seat_lunch1.setText(String.valueOf(getTimeData.get(0).seat_remain)+" / "+String.valueOf(getTimeData.get(0).seat_all));

                        text_dinner1.setText(getTimeData.get(1).time);
                        seat_dinner1.setText(String.valueOf(getTimeData.get(1).seat_remain)+" / "+String.valueOf(getTimeData.get(1).seat_all));

                    } else if(getTimeData.size()==4){

                        layout_lunch.setVisibility(View.VISIBLE);
                        layout_dinner.setVisibility(View.VISIBLE);

                        lunch1.setVisibility(View.VISIBLE);
                        text_lunch1.setVisibility(View.VISIBLE);
                        seat_lunch1.setVisibility(View.VISIBLE);
                        lunch2.setVisibility(View.VISIBLE);
                        text_lunch2.setVisibility(View.VISIBLE);
                        seat_lunch2.setVisibility(View.VISIBLE);
                        lunch3.setVisibility(View.GONE);
                        text_lunch3.setVisibility(View.GONE);
                        seat_lunch3.setVisibility(View.GONE);


                        dinner1.setVisibility(View.VISIBLE);
                        text_dinner1.setVisibility(View.VISIBLE);
                        seat_dinner1.setVisibility(View.VISIBLE);
                        dinner2.setVisibility(View.VISIBLE);
                        text_dinner2.setVisibility(View.VISIBLE);
                        seat_dinner2.setVisibility(View.VISIBLE);
                        dinner3.setVisibility(View.GONE);
                        text_dinner3.setVisibility(View.GONE);
                        seat_dinner3.setVisibility(View.GONE);

                        text_lunch1.setText(getTimeData.get(0).time);
                        seat_lunch1.setText(String.valueOf(getTimeData.get(0).seat_remain)+" / "+String.valueOf(getTimeData.get(0).seat_all));
                        text_lunch2.setText(getTimeData.get(1).time);
                        seat_lunch2.setText(String.valueOf(getTimeData.get(1).seat_remain)+" / "+String.valueOf(getTimeData.get(1).seat_all));

                        text_dinner1.setText(getTimeData.get(2).time);
                        seat_dinner1.setText(String.valueOf(getTimeData.get(2).seat_remain)+" / "+String.valueOf(getTimeData.get(2).seat_all));
                        text_dinner2.setText(getTimeData.get(3).time);
                        seat_dinner2.setText(String.valueOf(getTimeData.get(3).seat_remain)+" / "+String.valueOf(getTimeData.get(3).seat_all));

                    } else if(getTimeData.size()==6){

                        layout_lunch.setVisibility(View.VISIBLE);
                        layout_dinner.setVisibility(View.VISIBLE);

                        lunch1.setVisibility(View.VISIBLE);
                        text_lunch1.setVisibility(View.VISIBLE);
                        seat_lunch1.setVisibility(View.VISIBLE);
                        lunch2.setVisibility(View.VISIBLE);
                        text_lunch2.setVisibility(View.VISIBLE);
                        seat_lunch2.setVisibility(View.VISIBLE);
                        lunch3.setVisibility(View.VISIBLE);
                        text_lunch3.setVisibility(View.VISIBLE);
                        seat_lunch3.setVisibility(View.VISIBLE);


                        dinner1.setVisibility(View.VISIBLE);
                        text_dinner1.setVisibility(View.VISIBLE);
                        seat_dinner1.setVisibility(View.VISIBLE);
                        dinner2.setVisibility(View.VISIBLE);
                        text_dinner2.setVisibility(View.VISIBLE);
                        seat_dinner2.setVisibility(View.VISIBLE);
                        dinner3.setVisibility(View.VISIBLE);
                        text_dinner3.setVisibility(View.VISIBLE);
                        seat_dinner3.setVisibility(View.VISIBLE);

                        text_lunch1.setText(getTimeData.get(0).time);
                        seat_lunch1.setText(String.valueOf(getTimeData.get(0).seat_remain)+" / "+String.valueOf(getTimeData.get(0).seat_all));
                        text_lunch2.setText(getTimeData.get(1).time);
                        seat_lunch2.setText(String.valueOf(getTimeData.get(1).seat_remain)+" / "+String.valueOf(getTimeData.get(1).seat_all));
                        text_lunch3.setText(getTimeData.get(2).time);
                        seat_lunch3.setText(String.valueOf(getTimeData.get(2).seat_remain)+" / "+String.valueOf(getTimeData.get(2).seat_all));

                        text_dinner1.setText(getTimeData.get(3).time);
                        seat_dinner1.setText(String.valueOf(getTimeData.get(3).seat_remain)+" / "+String.valueOf(getTimeData.get(3).seat_all));
                        text_dinner2.setText(getTimeData.get(4).time);
                        seat_dinner2.setText(String.valueOf(getTimeData.get(4).seat_remain)+" / "+String.valueOf(getTimeData.get(4).seat_all));
                        text_dinner3.setText(getTimeData.get(5).time);
                        seat_dinner3.setText(String.valueOf(getTimeData.get(5).seat_remain)+" / "+String.valueOf(getTimeData.get(5).seat_all));

                    }
                }
            }

            @Override
            public void onFailure(Call<GetTimeResult> call, Throwable t) {
                GlobalApplication.getGlobalApplicationContext().makeToast("이런 지져스! 시간 등록이 안되어 있는 레스토랑입니다 :0 조금만 기다려 주세요 Coming Soon~");

            }
        });
    }

    // 결제할 내역 정보
    public void getReservationInfo(){

    }

    public MCalendarView getCalendar() {
        return calendar;
    }

    /********************* Click Listener ************************/
    @Override
    public void onClick(View v) {

        below_calendar.setVisibility(View.GONE);
        calendar.setVisibility(View.VISIBLE);

        // seat_one
        if(v.getId()==R.id.seat_one){
            if(flag_seat_one==false){

                seat_count = 1;
                calendar.setVisibility(View.VISIBLE);
                calendar.hasTitle(false);

                seat_one.setImageResource(R.drawable.time_selected);
                seat_two.setImageResource(R.drawable.time_unselected);
                seat_four.setImageResource(R.drawable.time_unselected);
                seat_many.setImageResource(R.drawable.time_unselected);
                flag_seat_one=true;

                getdate();

            }else{
                seat_one.setImageResource(R.drawable.time_unselected);
                flag_seat_one=false;
            }
        }

        // seat_two
        else if(v.getId()==R.id.seat_two){
            if(flag_seat_two==false){

                seat_count = 2;
                calendar.setVisibility(View.VISIBLE);

                seat_one.setImageResource(R.drawable.time_unselected);
                seat_two.setImageResource(R.drawable.time_selected);
                seat_four.setImageResource(R.drawable.time_unselected);
                seat_many.setImageResource(R.drawable.time_unselected);
                flag_seat_two=true;

                getdate();

            }else{
                seat_two.setImageResource(R.drawable.time_unselected);
                flag_seat_two=false;
            }
        }

        // seat_four
        else if(v.getId()==R.id.seat_four){
            if(flag_seat_four==false){

                seat_count = 4;
                calendar.setVisibility(View.VISIBLE);

                seat_one.setImageResource(R.drawable.time_unselected);
                seat_two.setImageResource(R.drawable.time_unselected);
                seat_four.setImageResource(R.drawable.time_selected);
                seat_many.setImageResource(R.drawable.time_unselected);
                flag_seat_four=true;

                getdate();

            }else{
                seat_four.setImageResource(R.drawable.time_unselected);
                flag_seat_four=false;
            }
        }

        // seat_many
        else if(v.getId()==R.id.seat_many){
            if(flag_seat_many==false){

                seat_count = 6;
                calendar.setVisibility(View.VISIBLE);

                seat_one.setImageResource(R.drawable.time_unselected);
                seat_two.setImageResource(R.drawable.time_unselected);
                seat_four.setImageResource(R.drawable.time_unselected);
                seat_many.setImageResource(R.drawable.time_selected);
                flag_seat_many=true;

                getdate();

            }else{
                seat_many.setImageResource(R.drawable.time_unselected);
                flag_seat_many=false;
            }
        }

        // lunch1
        else if(v.getId()==R.id.lunch1){
            if(flag_lunch1==false){
                lunch1.setImageResource(R.drawable.time_selected);
                lunch2.setImageResource(R.drawable.time_unselected);
                lunch3.setImageResource(R.drawable.time_unselected);

                dinner1.setImageResource(R.drawable.time_unselected);
                dinner2.setImageResource(R.drawable.time_unselected);
                dinner3.setImageResource(R.drawable.time_unselected);

                flag_lunch1=true;

            }else{
                lunch1.setImageResource(R.drawable.time_unselected);
                flag_lunch1=false;
            }
        }

        // lunch2
        else if(v.getId()==R.id.lunch2){
            if(flag_lunch2==false){
                lunch1.setImageResource(R.drawable.time_unselected);
                lunch2.setImageResource(R.drawable.time_selected);
                lunch3.setImageResource(R.drawable.time_unselected);

                dinner1.setImageResource(R.drawable.time_unselected);
                dinner2.setImageResource(R.drawable.time_unselected);
                dinner3.setImageResource(R.drawable.time_unselected);

                flag_lunch2=true;

            }else{
                lunch2.setImageResource(R.drawable.time_unselected);
                flag_lunch2=false;
            }
        }

        // lunch3
        else if(v.getId()==R.id.lunch3){
            if(flag_lunch3==false){
                lunch1.setImageResource(R.drawable.time_unselected);
                lunch2.setImageResource(R.drawable.time_unselected);
                lunch3.setImageResource(R.drawable.time_selected);

                dinner1.setImageResource(R.drawable.time_unselected);
                dinner2.setImageResource(R.drawable.time_unselected);
                dinner3.setImageResource(R.drawable.time_unselected);

                flag_lunch3=true;

            }else{
                lunch3.setImageResource(R.drawable.time_unselected);
                flag_lunch3=false;
            }
        }

        // dinner1
        else if(v.getId()==R.id.dinner1){
            if(flag_dinner1==false){
                dinner1.setImageResource(R.drawable.time_selected);
                dinner2.setImageResource(R.drawable.time_unselected);
                dinner3.setImageResource(R.drawable.time_unselected);

                lunch1.setImageResource(R.drawable.time_unselected);
                lunch2.setImageResource(R.drawable.time_unselected);
                lunch3.setImageResource(R.drawable.time_unselected);

                flag_dinner1=true;
            }else{
                dinner1.setImageResource(R.drawable.time_unselected);
                flag_dinner1=false;
            }
        }

        // dinner2
        else if(v.getId()==R.id.dinner2){
            if(flag_dinner2==false){
                dinner1.setImageResource(R.drawable.time_unselected);
                dinner2.setImageResource(R.drawable.time_selected);
                dinner3.setImageResource(R.drawable.time_unselected);

                lunch1.setImageResource(R.drawable.time_unselected);
                lunch2.setImageResource(R.drawable.time_unselected);
                lunch3.setImageResource(R.drawable.time_unselected);

                flag_dinner2=true;
            }else{
                dinner2.setImageResource(R.drawable.time_unselected);
                flag_dinner2=false;
            }
        }

        // dinner3
        else if(v.getId()==R.id.dinner3){
            if(flag_dinner3==false){
                dinner1.setImageResource(R.drawable.time_unselected);
                dinner2.setImageResource(R.drawable.time_unselected);
                dinner3.setImageResource(R.drawable.time_selected);

                lunch1.setImageResource(R.drawable.time_unselected);
                lunch2.setImageResource(R.drawable.time_unselected);
                lunch3.setImageResource(R.drawable.time_unselected);

                flag_dinner3=true;
            }else{
                dinner3.setImageResource(R.drawable.time_unselected);
                flag_dinner3=false;
            }
        }





    }
}
