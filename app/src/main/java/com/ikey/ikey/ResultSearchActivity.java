package com.ikey.ikey;

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
    BodyInfoAdapter listAdapter;
    ArrayList<BodyInfoVO> resultList  =null;
    String[] items ={"남자", "여자"};
    int sex = 0;
    TextView birth ;
    Spinner spinner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        openDatabase("ikey");
        spinner = (Spinner)findViewById(R.id.spinnerSex);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, items
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        System.out.print(sex+"*************************");
        birth = (TextView)findViewById(R.id.birth);
        birth.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 sex = spinner.getSelectedItemPosition()+1;
                 resultList  = selectData(birth.getText().toString(),sex+"");
                 ListView listView=(ListView)findViewById(R.id.listView);
                 listAdapter = new BodyInfoAdapter();
                 if(resultList!=null){
                     for (int i=0; i<resultList.size() ; i++){
                         listAdapter.addIem(resultList.get(i));
                     }
                 }
                 System.out.println(listAdapter.getCount()+"cout()");
                 System.out.println(listAdapter.getItem(0)+"getItem()");

                 listView.setAdapter(listAdapter);
                 listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                         BodyInfoVO item = (BodyInfoVO)listAdapter.getItem(i);
                         // Toast.makeText(getApplicationContext(), "선택"+item.getName(), Toast.LENGTH_LONG).show();
                     }
                 });
             }
         });



        //기존데이터 삭제
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
    }
    //데이터를 관리하는 어뎁터
    class BodyInfoAdapter extends BaseAdapter {
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
            BodyInfoView view= null;
            if (converView==null) {
                view = new BodyInfoView(getApplicationContext());
            }else{
                view = (BodyInfoView)converView;
            }
            BodyInfoVO item = items.get(i);
            view.setIdBox(item.getId());
            view.setMonth(item.getMonth()+"개월("+item.getInsertDate()+")");
            view.setInsertDate(item.getInsertDate());
            view.setHeight(item.getHeight()+"cm ("+item.getHpercent()+"%)");
            view.setWeight(item.getWeight()+"kg ("+item.getWpercent()+"%)");
            return view;
        }

        public void addIem(BodyInfoVO item){
            items.add(item);
        }
    }
    //조회 호출
    private ArrayList<BodyInfoVO> selectData(String birth, String sex){
        println("selectData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            String sql = "select _id, month, height, weight, hpercent, wpercent, insert_date from body_info where birth='"+birth+"' and sex='"+sex+"' order by insert_date desc";
            Cursor cursor = db.rawQuery(sql , null);
            if(cursor!=null ) {
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
                    if(preHeight.compareTo(BigDecimal.ZERO)!= 0){

                    }
                    list.add(vo);
                   // println(vo.toString());
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
}
