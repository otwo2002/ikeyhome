package com.ikey.ikey;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class NewActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String name="";
    String birth = "" ;
    String sex = "";
    String weight="";
    String height="";
    RadioButton m ;
    RadioButton w ;
    String[] spinneritems ={"성별","남자", "여자"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

            Button button = (Button)findViewById(R.id.btnShow);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                height= ((TextView) (findViewById(R.id.currHeight))).getText().toString();
                if(height ==null || height.trim().equals("")  ){
                    showMessage("신장을 입력해주세요");
                    return;
                }else{
                    try{
                        double heightD = Double.parseDouble(height);
                        if(heightD>230){
                            showMessage("신장을 정확히 입력해주세요!");
                            return;
                        }
                    }catch (Exception e){
                        showMessage("신장은 숫자만 입력해주세요");
                        return;
                    }

                }
                weight= ((TextView) (findViewById(R.id.currWeight))).getText().toString();
                if(weight ==null || weight.trim().equals("")  ){
                    showMessage("체중을 정확히 입력해주세요");
                    return;
                }else{
                    try{
                        double weightD = Double.parseDouble(weight);
                        if(weightD>300){
                            showMessage("체중을 정확히 입력해주세요!");
                            return;
                        }
                    }catch (Exception e){
                        showMessage("체중은 숫자만 입력해주세요");
                        return;
                    }
                }

                Intent intent = new Intent(NewActivity.this, ResultActivity.class);
                intent.putExtra("name",name);// 생년월일
                intent.putExtra("birth",birth);// 생년월일
                intent.putExtra("sex",sex);//성별
                intent.putExtra("height", height);
                intent.putExtra("weight", weight);
                startActivity(intent);
                finish();
            }
        });

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    //자녀정보조회 호출
    private ArrayList<BodyInfoVO> selectHumanData(){
        openDatabase("ikey");
        //println("selectHumanData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            String sql = "select name,birth,sex " +
                    "from child "+
                    " order by birth , name, sex desc";
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

    public void createTable(String tableName) throws Exception{
        println("createTable 호출");
        if(db !=null){
            String sql = "create table IF NOT EXISTS " +tableName+ " (_id integer PRIMARY KEY autoincrement,name text, birth text,sex text)";
            db.execSQL(sql);
            println("테이블생성됨.");
        }else{
            println("먼저데이터베이스를 오픈하세요");
        }
    }

    private void insertData(String name, String birth, String sex) throws Exception{
        println("insertData() 호출됨");
        if (db!=null){
            String sql ="insert into child(name, birth, sex) values(?,?,? )";
            Object[] params = {name, birth, sex};
            db.execSQL(sql, params);
        }else{
            println("db open 호출됨");
            openDatabase("ikey");
            String sql ="insert into child(name, birth, sex) values(?,?,? )";
            Object[] params = {name, birth, sex};
            db.execSQL(sql, params);
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
