package com.ikey.ikey;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    private AdView mAdView;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        try {
            String name = intent.getExtras().getString("name");  //이름
            String birth = intent.getExtras().getString("birth");  //생년월일
            String sex = intent.getExtras().getString("sex");   //성별
            String height= intent.getExtras().getString("height");  //키
            String weight= intent.getExtras().getString("weight"); //몸무게
            String id= intent.getExtras().getString("id"); //자녀정보ID
            String month= intent.getExtras().getString("month"); //개월수
            String sexText = "";
            if(sex.equals("1")){
                sexText= "남자";
            }else if(sex.equals("2")){
                sexText= "여자";
            }
            //상세보기이면 앞에서 받은 개월수로 계산해줌.
            if(month ==null || month.equals("")){
                month = calMonth(birth);
            }

            TextView pMonth = findViewById(R.id.pMonth);
            TextView textiWeight = findViewById(R.id.iWeight);
            TextView textiHeight = findViewById(R.id.iHeight);
            textiHeight.setText("신장 : "+height+"cm");
            textiWeight.setText("체중 : "+weight+"kg");
            TextView textpWeight = findViewById(R.id.pWeight);
            TextView textpHeight = findViewById(R.id.pHeight);
            TextView tableNote =  findViewById(R.id.tableNote);
            pMonth.setText(name + " : "+month+"개월");
            tableNote.setText( ""+month+"개월 "+sexText+"아이 백분위수");
            //광고--------------------------------------------------------
            // Sample AdMob app ID: ca-app-pub-3940256099942544/6300978111  - 테스트 아이디
            MobileAds.initialize(this,
                    "ca-app-pub-4987131221778488/1324054764");
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    //Toast.makeText(rootView.getContext(), "광고로드", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the user is about to return
                    // to the app after tapping on an ad.
                }
            });
            //-------------------------------------광고끝 ---------------------------------------------

            //키목록 가져오기
            Object[] heightDataArray = parseJson( "height" ,sex, month);
            //키퍼센트
            String heightPercent= callPercent(height,heightDataArray);

            //몸무게퍼센트
            Object [] weightDataArray = parseJson( "weight" ,sex, month);
            String weightPercent= callPercent(weight,weightDataArray);
            //체질량지수 퍼센트
            //rawDateArray = parseJson( "bodymass" ,sex, month);
           // String wieghtPercent= callPercent(weight,rawDateArray);
           //resultBtn.setText("신장 : "+(new BigDecimal(heightPercent) ).setScale(1, RoundingMode.HALF_UP) +"%");
            heightPercent = (new BigDecimal(heightPercent) ).setScale(1, RoundingMode.HALF_UP)+"";
            weightPercent =(new BigDecimal(weightPercent) ).setScale(1, RoundingMode.HALF_UP)+"";
            textpHeight.setText("신장 : "+heightPercent +"%");
            textpWeight.setText("체중 : "+weightPercent +"%");
            TextView oneHeight = findViewById(R.id.oneHeight);
            TextView twoHeight = findViewById(R.id.twoHeight);
            TextView threeHeight = findViewById(R.id.threeHeight);
            TextView fourHeight = findViewById(R.id.fourHeight);
            TextView fiveHeight = findViewById(R.id.fiveHeight);
            TextView sixHeight = findViewById(R.id.sixHeight);
            TextView sevenHeight = findViewById(R.id.sevenHeight);
            TextView eightHeight = findViewById(R.id.eightHeight);
            TextView nineHeight = findViewById(R.id.nineHeight);
            TextView tenHeight = findViewById(R.id.tenHeight);
            TextView elevenHeight = findViewById(R.id.elevenHeight);
            TextView twelveHeight = findViewById(R.id.twelveHeight);
            TextView thirteenHeight = findViewById(R.id.thirteenHeight);
            oneHeight.setText(heightDataArray[4].toString());
            twoHeight.setText(heightDataArray[5].toString());
            threeHeight.setText(heightDataArray[6].toString());
            fourHeight.setText(heightDataArray[7].toString());
            fiveHeight.setText(heightDataArray[8].toString());
            sixHeight.setText(heightDataArray[9].toString());
            sevenHeight.setText(heightDataArray[10].toString());
            eightHeight.setText(heightDataArray[11].toString());
            nineHeight.setText(heightDataArray[12].toString());
            tenHeight.setText(heightDataArray[13].toString());
            elevenHeight.setText(heightDataArray[14].toString());
            twelveHeight.setText(heightDataArray[15].toString());
            thirteenHeight.setText(heightDataArray[16].toString());

            TextView oneWeight = findViewById(R.id.oneWeight);
            TextView twoWeight = findViewById(R.id.twoWeight);
            TextView threeWeight = findViewById(R.id.threeWeight);
            TextView fourWeight = findViewById(R.id.fourWeight);
            TextView fiveWeight = findViewById(R.id.fiveWeight);
            TextView sixWeight = findViewById(R.id.sixWeight);
            TextView sevenWeight = findViewById(R.id.sevenWeight);
            TextView eightWeight = findViewById(R.id.eightWeight);
            TextView nineWeight = findViewById(R.id.nineWeight);
            TextView tenWeight = findViewById(R.id.tenWeight);
            TextView elevenWeight = findViewById(R.id.elevenWeight);
            TextView twelveWeight = findViewById(R.id.twelveWeight);
            TextView thirteenWeight = findViewById(R.id.thirteenWeight);
            oneWeight.setText(weightDataArray[4].toString());
            twoWeight.setText(weightDataArray[5].toString());
            threeWeight.setText(weightDataArray[6].toString());
            fourWeight.setText(weightDataArray[7].toString());
            fiveWeight.setText(weightDataArray[8].toString());
            sixWeight.setText(weightDataArray[9].toString());
            sevenWeight.setText(weightDataArray[10].toString());
            eightWeight.setText(weightDataArray[11].toString());
            nineWeight.setText(weightDataArray[12].toString());
            tenWeight.setText(weightDataArray[13].toString());
            elevenWeight.setText(weightDataArray[14].toString());
            twelveWeight.setText(weightDataArray[15].toString());
            thirteenWeight.setText(weightDataArray[16].toString());
            if(!id.equals("")){
                //입력받은 값을 저장한다.
                //DB오픈
                openDatabase("ikey");
                createTable("body_info");
                insertData(name, birth, sex, month, height, weight, heightPercent, weightPercent, id);
            }
            ImageView back = findViewById(R.id.backBtn);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            TextView historyTv= findViewById(R.id.historyBtn);
            historyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, ResultSearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            //-----------------------------------그래프 -------------------------------
             //최대값 최소값 셋팅
            String minWeight;
            String maxWeight;
            String minHeight;
            String maxHeight;
            if( ( new BigDecimal(height)).compareTo(new BigDecimal(heightDataArray[16].toString()))>0){
                maxHeight=(Double.parseDouble(height)+3)+"";
            }else{
                maxHeight=(Double.parseDouble(heightDataArray[16].toString())+3)+"";
            }
            if( ( new BigDecimal(weight)).compareTo(new BigDecimal(weightDataArray[16].toString()))>0){
                maxWeight= (Double.parseDouble(weight)+3)+"";
            }else{
                maxWeight=(Double.parseDouble(weightDataArray[16].toString())+3)+"";
            }

            if( ( new BigDecimal(height)).compareTo(new BigDecimal(heightDataArray[4].toString()))<0){
                minHeight=height;
            }else{
                minHeight=heightDataArray[4].toString();
            }
            if( ( new BigDecimal(weight)).compareTo(new BigDecimal(weightDataArray[4].toString()))<0){
                minWeight=weight;
            }else{
                minWeight=weightDataArray[4].toString();
            }
            //X축 라벨 추가
            BarChart chart1 = (BarChart) findViewById(R.id.chart1);
            Legend L1;
            L1 = chart1.getLegend();
            //L1.setEnabled(false);
            chart1.setDescription("신장(cm)");
            //chart1.setDrawGridBackground(false);
            ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
            entries1.add(new BarEntry(Float.parseFloat(height), 0));

            ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
            entries2.add(new BarEntry(Float.parseFloat(heightDataArray[16].toString()), 0));

            BarDataSet set1 = new BarDataSet(entries1, "우리아이");
            BarDataSet set2 = new BarDataSet(entries2, "신장 100%");
            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            set1.setColors(ColorTemplate.COLORFUL_COLORS); //
            set1.setValueTextColor(Color.rgb(155,155,155));
            set1.setValueTextSize(11f);
            set2.setValueTextSize(11f);
            ArrayList<String> labels = new ArrayList<String>();
            labels.add("");
            BarData data = new BarData(labels, dataSets);

            chart1.setData(data);
            YAxis leftAxis = chart1.getAxisLeft();
            leftAxis.setStartAtZero(false);
            leftAxis.setAxisMinValue(Float.parseFloat(minHeight));
            leftAxis.setAxisMaxValue(Float.parseFloat(maxHeight));
            YAxis rightAxis = chart1.getAxisRight();
            rightAxis.setStartAtZero(false);
            rightAxis.setDrawLabels(false);//오른쪽 범례 없애기
            rightAxis.setAxisMinValue(Float.parseFloat(minHeight));
            rightAxis.setAxisMaxValue(Float.parseFloat(maxHeight));

            //체중 차트
            //X축 라벨 추가
            BarChart chart2 = (BarChart) findViewById(R.id.chart2);
            Legend L2;
            chart2.setDescription("체중(kg)");
            L2 = chart2.getLegend();
            ArrayList<BarEntry> entriesW1 = new ArrayList<BarEntry>();
            entriesW1.add(new BarEntry(Float.parseFloat(weight), 0));

            ArrayList<BarEntry> entriesW2 = new ArrayList<BarEntry>();
            entriesW2.add(new BarEntry(Float.parseFloat(weightDataArray[16].toString()), 0));

            BarDataSet setW1 = new BarDataSet(entriesW1, "우리아이");
            BarDataSet setW2 = new BarDataSet(entriesW2, "체중100%");
            ArrayList<BarDataSet> dataSetsW = new ArrayList<BarDataSet>();
            dataSetsW.add(setW1);
            dataSetsW.add(setW2);
            setW1.setColors(ColorTemplate.COLORFUL_COLORS);
            setW1.setValueTextColor(Color.rgb(155,155,155));
            setW1.setValueTextSize(11f);
            setW2.setValueTextSize(11f);
            ArrayList<String> labelsW = new ArrayList<String>();
            labelsW.add("");
            BarData dataW = new BarData(labelsW, dataSetsW);

            chart2.setData(dataW);
            YAxis weightLeftAxis = chart2.getAxisLeft();
            weightLeftAxis.setStartAtZero(false);
            weightLeftAxis.setAxisMinValue(Float.parseFloat(minWeight));
            weightLeftAxis.setAxisMaxValue(Float.parseFloat(maxWeight));
            YAxis weightRightAxis = chart2.getAxisRight();
            weightRightAxis.setStartAtZero(false);
            weightRightAxis.setDrawLabels(false);
            weightRightAxis.setAxisMinValue(Float.parseFloat(minWeight));
            weightRightAxis.setAxisMaxValue(Float.parseFloat(maxWeight));

        }catch(Exception e){
            showMessage("정보처리도중 오류가 발생했습니다."+e.getMessage());
            e.printStackTrace();
        }
    }
    //백분위 구하기
    private String callPercent(String height, Object[] rawDateArray ){
        double percent = 0 ;
        if(height!=null){
            //백분위 구하는공식
            //Z=((x/M)^L-1)/LS, L!=0
            //NORMSDIST(Z)*100 = 백분위수
            //만약에 값이 100%를 넘어가는 값이거나 0%보다 작을경우 처리해줌.
            if( new BigDecimal(height).compareTo(new BigDecimal(rawDateArray[4].toString())) <0 ){
                percent=0;
            }else if(new BigDecimal(height).compareTo( new BigDecimal(rawDateArray[16].toString()) )> 0){
                percent=100;
            }else {
                BigDecimal Z = (new BigDecimal(height)).divide(new BigDecimal(rawDateArray[2].toString()), MathContext.DECIMAL128);
                double middleValue = Math.pow(Z.doubleValue(), (new BigDecimal(rawDateArray[1].toString())).doubleValue()) - 1;
                Z = (new BigDecimal(middleValue)).divide(
                        (new BigDecimal(rawDateArray[1].toString())).multiply((new BigDecimal(rawDateArray[3].toString())), MathContext.DECIMAL128)
                        , MathContext.DECIMAL128
                );
                System.out.println("Z------------------>" + Z);
                percent = NORMSDIST(Z.doubleValue()) * 100;
                System.out.println("double------------------>" + percent);
            }
        }
        return percent+"";
    }
    //-----------------------------------정규분포 ------------------------------
    private static double erf(double x)
    {
        //A&S formula 7.1.26
        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;
        x = Math.abs(x);
        double t = 1 / (1 + p * x);
        //Direct calculation using formula 7.1.26 is absolutely correct
        //But calculation of nth order polynomial takes O(n^2) operations
        //return 1 - (a1 * t + a2 * t * t + a3 * t * t * t + a4 * t * t * t * t + a5 * t * t * t * t * t) * Math.Exp(-1 * x * x);

        //Horner's method, takes O(n) operations for nth order polynomial
        return 1 - ((((((a5 * t + a4) * t) + a3) * t + a2) * t) + a1) * t * Math.exp(-1 * x * x);
    }

    public static double NORMSDIST(double z)
    {
        double sign = 1;
        if (z < 0) sign = -1;
        return 0.5 * (1.0 + sign * erf(Math.abs(z)/Math.sqrt(2)));
    }
    //-----------------------------------정규분포표 -----------------------------
    //개월수 계산하기
    //만나이(개월) = ((측정년도-출생년도)×12))+(측정월-출생월)+((측정일-출생일)÷30.42))  소수점 버림
    private String calMonth(String birth) {
        //현재날짜 구함.
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String currdate = sdf.format(date);
        int middleValue=((Integer.parseInt(currdate.substring(0,4))-Integer.parseInt(birth.substring(0,4)))*12)
                +(Integer.parseInt(currdate.substring(4,6))-Integer.parseInt(birth.substring(4,6)));

         BigDecimal dayValue = new BigDecimal(Integer.parseInt(currdate.substring(6,8))-Integer.parseInt(birth.substring(6,8)));
         dayValue = dayValue.divide(new BigDecimal("30.42"),0, BigDecimal.ROUND_FLOOR);
         BigDecimal finalValue = dayValue.add(new BigDecimal(middleValue));
        return finalValue.toString();
    }

    //json파일 공통으로 불러오는 메서드
    private Object[] parseJson(String gubun, String sex ,String month) throws Exception{
        // myJson.json
        JSONParser parser = new JSONParser();
        InputStream inputStream = null;
        if(gubun.equals("height")){
            inputStream =  getResources().openRawResource(R.raw.height);
        }else if(gubun.equals("weight")){
            inputStream =  getResources().openRawResource(R.raw.weight);
        }else if(gubun.equals("bodymass")){
            inputStream =  getResources().openRawResource(R.raw.bodymass);
        }

        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        String line = null;
        Object []  mp =null;
        try {
            Object obj = parser.parse(reader);
            JSONArray jsonArray =(JSONArray) obj;
            System.out.println("4-----------------------------------");
            //System.out.println(jsonObject.keys());
            JSONObject jObj =null;
            Collection<String> values = null;
            for (int i =0; i<jsonArray.size(); i++) {
                jObj = (JSONObject) jsonArray.get(i);
                values = jObj.values();
               // System.out.println("!!!!!!!!!!"+values);
                if(jObj.get("month").equals(sex+month)){
                    System.out.println("!!!!!!!!!!"+jObj.get(gubun));
                    JSONArray jarray= (JSONArray)jObj.get(gubun);
                    mp =  jarray.toArray();
                }
            }
        }catch (Exception e){
            throw e;
        }
        return mp;
    }


    private void openDatabase(String databseName) {
        println("openDatabase 호출");
        db= openOrCreateDatabase(databseName, MODE_PRIVATE, null);
        if(db!=null){
            println("db open");
        }
    }

    public void println(String data){
        System.out.println(data + "\n");
    }

    public void createTable(String tableName) throws Exception{
        println("createTable 호출");
        // String sql = "drop table  " +tableName;
         //db.execSQL(sql);
        if(db !=null){
           // String sql = "drop table  " +tableName;
            //db.execSQL(sql);
            String sql = "create table IF NOT EXISTS " +tableName+ " (_id integer PRIMARY KEY autoincrement,child_id integer, name text, birth text,sex text, month integer,height text, weight text, hpercent text, wpercent text, insert_date text)";
            db.execSQL(sql);
            println("테이블생성됨.");
        }else{
            println("먼저데이터베이스를 오픈하세요");
        }
    }

    private void insertData(String name, String birth, String sex, String month, String height, String weight, String hpercent, String wpercent, String childId) throws Exception{
        println("insertData() 호출됨");
        if (db!=null){
            String sql ="insert into body_info(child_id , name, birth, sex, month, height, weight, hpercent, wpercent, insert_date) values(?,?,?,?,?,?,?,?,?,date('now') )";
            Object[] params = {childId, name, birth, sex, month, height, weight, hpercent, wpercent};
            db.execSQL(sql, params);
        }else{
            println("db open 호출됨");
            openDatabase("ikey");
            String sql ="insert into body_info(child_id, name, birth, sex, month, height, weight, hpercent, wpercent, insert_date) values(?,?,?,?,?,?,?,?,?,date('now') )";
            Object[] params = {childId,name, birth, sex, month, height, weight, hpercent, wpercent};
            db.execSQL(sql, params);
        }
    }
    //메시지 출력
    public void showMessage(String message){

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {    }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}