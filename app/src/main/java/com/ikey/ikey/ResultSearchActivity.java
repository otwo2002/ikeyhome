package com.ikey.ikey;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        openDatabase("ikey");
        List<BodyInfoVO> list  = selectData("20130814","1");
    }

    private List<BodyInfoVO> selectData(String birth, String sex){
        println("selectData()호출됨");
        List<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            String sql = "select month, height, weight, hpercent, wpercent, insert_date from body_info where birth='"+birth+"' and sex='"+sex+"'";
            Cursor cursor = db.rawQuery(sql , null);
            if(cursor!=null ) {

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    String month = cursor.getString(0);
                    String height = cursor.getString(1);
                    String weight = cursor.getString(2);
                    String hpercent = cursor.getString(3);
                    String wpercent = cursor.getString(4);
                    String insert_date = cursor.getString(5);
                    BodyInfoVO vo = new BodyInfoVO();
                    vo.setBirth(birth);
                    vo.setMonth(month);
                    vo.setWeight(weight);
                    vo.setHeight(height);
                    vo.setHpercent(hpercent);
                    vo.setWpercent(wpercent);
                    vo.setInsertDate(insert_date);
                    list.add(vo);
                    println(vo.toString());
                }
                cursor.close();
            }

        }
        return list ;
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
