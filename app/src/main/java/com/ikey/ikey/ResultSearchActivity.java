package com.ikey.ikey;

import android.content.DialogInterface;
import android.database.Cursor;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        try {
            openDatabase("ikey");
            //자녀정보조회

            humanList = selectHumanData();
            ListView listTopView = (ListView) findViewById(R.id.listTopView);
            humanAdapter = new HumanAdapter();

            if (humanList != null && humanList.size() > 0) {
                for (int i = 0; i < humanList.size(); i++) {
                    humanAdapter.addItem(humanList.get(i));
                }
                BodyInfoVO item = (BodyInfoVO) humanAdapter.getItem(0);
                searchHistory(item.getName(), item.getBirth(), item.getSex());
            } else {
                showMessage("등록된 자녀정보가 없습니다. 자녀등록후 이용해주세요. ");
            }
            listTopView.setAdapter(humanAdapter);
            listTopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    BodyInfoVO item = (BodyInfoVO) humanAdapter.getItem(i);

                    //선택된 자녀정보 글자색 바꾸기

                    searchHistory(item.getName(), item.getBirth(), item.getSex());
                }
            });

        }catch(Exception e ){
            showMessage("등록된 자녀정보가 없습니다. 자녀등록후 이용해주세요. ");
            return;
        }



       /* //기존데이터 삭제
        ImageView deleteImg = (ImageView)findViewById(R.id.deleteImg);
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultList!=null){
                    for (int i=0; i<resultList.size() ; i++){

                        deleteData( ((BodyInfoVO)resultList.get(i)).getId() );
                    }
                }

            }
        });
*/

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //성장이력조회
    private void searchHistory(String strName, String strBirth, String strSex){
        //성장이력조회
        TextView historyTitle = (TextView)findViewById(R.id.historyTitle);
        historyTitle.setText(strName+" 성장이력");
        historyList  = selectHistoryData(strName, strBirth,strSex);
        historylistView=(ListView)findViewById(R.id.listView);
        historyAdapter = new HistoryAdapter();
        if(historyList!=null && historyList.size()>0){
            for (int i=0; i<historyList.size() ; i++){
                historyAdapter.addItem(historyList.get(i));
            }
        }else{
            //showMessage("이전결과내역이 없습니다. ");
        }
        historylistView.setAdapter(historyAdapter);
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
            view.setName(item.getName());
            view.setBirth(item.getBirth());
            view.setSex(item.getSex());


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
            view.setMonth(item.getMonth()+"개월("+item.getBirth()+")");
            view.setInsertDate(item.getInsertDate());
            view.setHeight(item.getHeight()+"cm ("+item.getHpercent()+"%)");
            view.setWeight(item.getWeight()+"kg ("+item.getWpercent()+"%)");
            view.getDeleteIcon().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ReDrawList(i);
                }
            });
            return view;
        }

        public void addItem(BodyInfoVO item){

            items.add(item);

        }
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
    private ArrayList<BodyInfoVO> selectHumanData(){
        println("selectHumanData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            String sql = "select name,birth,sex " +
                    "from body_info "+
                    " group by name, birth, sex order by name, birth, sex desc";
            Cursor cursor = db.rawQuery(sql , null);
            if(cursor!=null && cursor.getCount()>0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    String name= cursor.getString(0);
                    String birth = cursor.getString(1);
                    String sex = cursor.getString(2);

                    BodyInfoVO vo = new BodyInfoVO();
                    vo.setName(name);
                    vo.setBirth(birth);
                    vo.setSex( (sex.equals("1"))?"남자":"여자");
                    list.add(vo);
                }
                cursor.close();
            }

        }
        return list ;
    }
    //성장이력조회 호출
    private ArrayList<BodyInfoVO> selectHistoryData(String name, String birth, String sex){
        println("selectHistoryData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            sex=(sex.equals("남자")?"1":"2");
            String sql = "select _id, month, height, weight, hpercent, wpercent, insert_date " +
                    "from body_info where name ='"+name+"' and birth='"+birth+"' and sex='"+sex+"'" +
                    " order by insert_date desc";
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
                    preHeight= new BigDecimal(height);
                    preWeight= new BigDecimal(weight);

                    BodyInfoVO vo = new BodyInfoVO();
                    vo.setId(id);
                    vo.setBirth(birth);
                    vo.setMonth(month);
                    System.out.println(month+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    vo.setWeight(weight);
                    vo.setHeight(height);
                    vo.setHpercent(hpercent);
                    vo.setWpercent(wpercent);
                    vo.setInsertDate(insert_date);
                    list.add(vo);
                }
                cursor.close();
            }

        }
        return list ;
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
