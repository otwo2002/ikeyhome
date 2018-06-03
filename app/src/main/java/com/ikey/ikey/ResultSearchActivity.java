package com.ikey.ikey;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HumanAdapter humanAdapter;
    HistoryAdapter historyAdapter;
    ArrayList<BodyInfoVO> humanList  =null;
    ArrayList<BodyInfoVO> historyList  =null;
    String[] items ={"남자", "여자"};
    int sex = 0;
    TextView birth ;
    Spinner spinner ;
    ListView historylistView ;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
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

        try {
            openDatabase("ikey");
            //자녀정보조회

            humanList = selectHumanData();
            ListView listTopView = (ListView) findViewById(R.id.listTopView);
            listTopView.setItemsCanFocus(false);
            listTopView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            humanAdapter = new HumanAdapter();

            if (humanList != null && humanList.size() > 0) {
                for (int i = 0; i < humanList.size(); i++) {
                    humanAdapter.addItem(humanList.get(i));
                }
                BodyInfoVO item = (BodyInfoVO) humanAdapter.getItem(0);
                try {
                    searchHistory(item.getId(), item.getName());
                }catch (SQLException e){
                    Toast.makeText(this, "등록된 성장이력정보가 없습니다. ", Toast.LENGTH_SHORT).show();
                }

            } else {
                showMessage("등록된 자녀정보가 없습니다. 자녀등록후 이용해주세요. ");
            }
            listTopView.setAdapter(humanAdapter);
            listTopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    BodyInfoVO item = (BodyInfoVO) humanAdapter.getItem(i);
                    searchHistory(item.getId(), item.getName());
                }
            });

            listTopView.setItemChecked(0,true);

            ImageView back = findViewById(R.id.backBtn);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


        }catch (SQLException ex){
            showMessage(ex.getMessage());
            ex.printStackTrace();
        }catch(Exception e ){
            showMessage("오류:"+e.getMessage());
            e.printStackTrace();
        }
    }

    //성장이력조회
    private void searchHistory(String nid, String strName){
        try {
            //성장이력조회
            TextView historyTitle = (TextView) findViewById(R.id.historyTitle);
            historyTitle.setText(strName + " 성장이력");
            historyList = selectHistoryData(nid);
            historylistView = (ListView) findViewById(R.id.listView);
            historyAdapter = new HistoryAdapter();
            if (historyList != null && historyList.size() > 0) {
                for (int i = 0; i < historyList.size(); i++) {
                    historyAdapter.addItem(historyList.get(i));
                }
            } else {
                //showMessage("이전결과내역이 없습니다. ");
            }
            historylistView.setAdapter(historyAdapter);
        }catch (SQLException e){
            throw new SQLException("등록된 성장정보가 없습니다.");
        }
    }
    //자녀정보를 관리하는 어뎁터
    class HumanAdapter extends BaseAdapter {
        ArrayList<BodyInfoVO> items = new ArrayList<BodyInfoVO>();
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //부분화면을 보내준다.
        @Override
        public View getView(int i, View converView, ViewGroup viewGroup) {
            HumanView view= null;
            if (converView==null) {
                view = new HumanView(getApplicationContext());

            }else{
                view = (HumanView)converView;
            }
            BodyInfoVO item = items.get(i);
            view.setNid(item.getId());
            view.setName(item.getName());
            view.setBirth(item.getBirth());
            view.setSex(item.getSex());
            System.out.print(" #####################");
            return view;
        }

        public void addItem(BodyInfoVO item){
            items.add(item);
        }


    }
    //성장이력 데이터를 관리하는 어뎁터
    class HistoryAdapter extends BaseAdapter {

        ArrayList<BodyInfoVO> items = new ArrayList<BodyInfoVO>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        //부분화면을 보내준다.
        @Override
        public View getView(final int i, View converView, ViewGroup viewGroup) {
            BodyInfoView view= null;
            if (converView==null) {
                view = new BodyInfoView(getApplicationContext());
            }else{
                view = (BodyInfoView)converView;
            }

            BodyInfoVO item  = items.get(i);
            view.setNid(i+"");
            view.setMonth(item.getMonth()+"개월");
            view.setInsertDate(item.getInsertDate());
            view.setHeight(item.getHeight()+"cm ("+item.getHpercent()+"%)");
            view.setWeight(item.getWeight()+"kg ("+item.getWpercent()+"%)");
            if(item.getHincre().compareTo(BigDecimal.ZERO) > 0){
                view.setHincre("▲"+item.getHincre().toString()+"cm");
                view.setRedHincre();
            }else if(item.getHincre().compareTo(BigDecimal.ZERO) < 0){
                view.setHincre("▼"+item.getHincre().toString()+"cm");
                view.setBlueHincre();
            }else if(item.getHincre().compareTo(BigDecimal.ZERO) == 0) {
                view.setHincre("--");
            }
            if(item.getWincre().compareTo(BigDecimal.ZERO) > 0){
                view.setWincre("▲"+item.getWincre().toString()+"kg");
                view.setRedWincre();
            }else if(item.getWincre().compareTo(BigDecimal.ZERO) < 0){
                view.setWincre("▼"+item.getWincre().toString()+"kg");
                view.setBlueWincre();
            }else if(item.getWincre().compareTo(BigDecimal.ZERO) == 0) {
                view.setWincre("--");
            }

            view.getDeleteIcon().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ReDrawList(i);
                }
            });
            view.getHeightView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goResultActivity(i);
                }
            });
            view.getWeightView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goResultActivity(i);
                }
            });
            view.getInsertDateView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goResultActivity(i);
                }
            });
            view.getMonthView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goResultActivity(i);
                }
            });
            return view;
        }

        public void addItem(BodyInfoVO item){

            items.add(item);

        }
    }
    //결과보기 화면으로 이동
    private void goResultActivity(int rownum){
        BodyInfoVO vo = (BodyInfoVO)historyAdapter.getItem(rownum);
        Intent intent = new Intent(ResultSearchActivity.this, ResultActivity.class);
        intent.putExtra("id", "");
        intent.putExtra("name",vo.getName());// 생년월일
        intent.putExtra("birth",vo.getBirth());// 생년월일
        intent.putExtra("sex",vo.getSex());//성별
        intent.putExtra("height", vo.getHeight());
        intent.putExtra("weight", vo.getWeight());
        startActivity(intent);
    }
    //리스트 다시 그리기
    public void ReDrawList(final int rownum){
         System.out.print("iiiiiiiiiiiiiiiiiiiiiiiiiiiii"+rownum);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("삭제");
        builder.setMessage("삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BodyInfoVO vo = (BodyInfoVO)historyAdapter.getItem(rownum);

                String id = vo.getId();
                deleteData(id);
                historyAdapter.items.remove(rownum);

                // listview 선택 초기화.
                historylistView.clearChoices();

                // listview 갱신.
                historyAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){

            // 취소 버튼 클릭시 설정
            public void onClick(DialogInterface dialog, int whichButton){
                dialog.cancel();
            }

        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();


    }
    //자녀정보조회 호출
    private ArrayList<BodyInfoVO> selectHumanData() throws SQLException{
        try {
            println("selectHumanData()호출됨");
            ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
            if (db != null) {
                String sql = "select _id, name,birth,sex " +
                        "from child " +
                        " order by name, birth, sex desc";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        String id = cursor.getString(0);
                        String name = cursor.getString(1);
                        String birth = cursor.getString(2);
                        String sex = cursor.getString(3);

                        BodyInfoVO vo = new BodyInfoVO();
                        vo.setId(id);
                        vo.setName(name);
                        vo.setBirth(birth);
                        vo.setSex((sex.equals("1")) ? "남자" : "여자");
                        list.add(vo);
                    }
                    cursor.close();
                }

            }
            return list ;
        }catch(SQLException e){
            throw new SQLException("등록된 자녀정보가 없습니다. 자녀등록후 이용하세요");
        }
    }
    //성장이력조회 호출
    private ArrayList<BodyInfoVO> selectHistoryData(String nid) throws SQLException {
        println("selectHistoryData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        ArrayList<BodyInfoVO> flist = new ArrayList<BodyInfoVO>();
        if(db!=null){

            String sql = "select _id, month, height, weight, hpercent, wpercent, insert_date, sex, birth , name " +
                    "from body_info where child_id ="+nid+" " +
                    " order by insert_date asc";
            Cursor cursor = db.rawQuery(sql , null);
            System.out.println(sql+"<<<<<<<<<<<<<<<<<<sql");
            if(cursor!=null && cursor.getCount()>0) {
                BigDecimal preHeight=BigDecimal.ZERO;
                BigDecimal preWeight=BigDecimal.ZERO;

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    String id= cursor.getString(0);
                    String month = cursor.getString(1);
                    String height = cursor.getString(2);
                    String weight = cursor.getString(3);
                    String hpercent = cursor.getString(4);
                    String wpercent = cursor.getString(5);
                    String insert_date = cursor.getString(6);
                    String sex = cursor.getString(7);
                    String birth = cursor.getString(8);
                    String name = cursor.getString(9);
                    BodyInfoVO vo = new BodyInfoVO();
                    vo.setId(id);
                    vo.setBirth(birth);
                    vo.setMonth(month);
                    vo.setSex(sex);
                   // System.out.println(month+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    vo.setWeight(weight);
                    vo.setHeight(height);
                    vo.setHpercent(hpercent);
                    vo.setWpercent(wpercent);
                    vo.setInsertDate(insert_date);
                    vo.setName(name);
                    if(i==0){
                        vo.setHincre(BigDecimal.ZERO);
                        vo.setWincre(BigDecimal.ZERO);
                    }else{
                        vo.setHincre( (new BigDecimal(height)).subtract(preHeight));
                        vo.setWincre(( new BigDecimal(weight)).subtract(preWeight));
                    }
                    preHeight= new BigDecimal(height);
                    preWeight= new BigDecimal(weight);

                    list.add(vo);
                }//end for
                cursor.close();
            }//end if

            //다시 뒤집기
            if(list!=null && list.size()>0){
             //   println("뒤집기"+list.size());

                int j= list.size()-1;
                for(int i=j; i>=0 ; i--){
                  //  println("뒤집기뒤집기뒤집기뒤집기>>>>>>>>>>>>>>>>>>>>>>>>>>"+i);
                    flist.add(list.get(i));
                }
            }

        }
        return flist ;
    }
    private void deleteData(String strId) {
        println("deleteData() 호출됨");
        //Integer id = Integer.parseInt(strId);
        if (db!=null){
            String sql ="delete from body_info where _id= "+strId;
            db.execSQL(sql);
        }else{
            println("db open 호출됨");
            openDatabase("ikey");
            String sql ="delete from body_info where _id= "+strId;
            db.execSQL(sql);
        }
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
