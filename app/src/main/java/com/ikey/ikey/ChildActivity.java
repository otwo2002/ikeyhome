package com.ikey.ikey;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ChildActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String name="";
    String birth = "" ;
    String sex = "";

    ChildAdapter childAdapter;
    ArrayList<BodyInfoVO> childList  =null;

    String[] spinneritems ={"성별","남자", "여자"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        openDatabase("ikey");
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, spinneritems
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView addChild =(TextView) findViewById(R.id.addChild);
        searchChild();
        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ((TextView) (findViewById(R.id.name))).getText().toString();
                birth = ((TextView) (findViewById(R.id.birth))).getText().toString();
                if (spinner.getSelectedItemPosition() != 0) {
                    sex = spinner.getSelectedItemPosition() + "";
                } else {
                    showMessage("성별을 입력해주세요");
                    return;
                }
                //입력값 체크
                if (name == null || name.trim().equals("")) {
                    showMessage("이름 또는 별칭을 정확히 입력해주세요");
                    return;
                }

                if (birth == null || birth.trim().equals("") || birth.length() != 8) {
                    showMessage("생년월일을 정확히 입력해주세요");
                    return;
                } else {
                    try {
                        int birthInt = Integer.parseInt(birth);
                    } catch (Exception e) {
                        showMessage("생년월일을 숫자만 입력해주세요");
                        return;
                    }//end try
                }//end if
                //입력받은 값을 저장한다.
                //DB오픈
                try {
                    openDatabase("ikey");
                    createTable("child");
                    insertData(name, birth, sex);
                    showMessage("자녀등록이 완료되었습니다.");
                }catch (Exception e){
                    showMessage("등록중 오류가 발생했습니다. ");
                    e.printStackTrace();
                }
                searchChild();
            }//end onclick
        });//end listener


    }//end onCreate


   //자녀정보를 관리하는 어뎁터
    class ChildAdapter extends BaseAdapter {
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

    private void searchChild(){
        //조회
        childList = selectChildData();
        ListView listTopView = (ListView) findViewById(R.id.listTopView);
        childAdapter = new ChildAdapter();

        if(childList != null && childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                childAdapter.addItem(childList.get(i));
            }
            // listview 갱신.
            childAdapter.notifyDataSetChanged();
            listTopView.setAdapter(childAdapter);
        } else {
            showMessage("등록된 자녀정보가 없습니다. 자녀등록후 이용해주세요. ");
        }
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

    //자녀정보조회 호출
    private ArrayList<BodyInfoVO> selectChildData(){
        println("selectChildData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            String sql = "select name,birth,sex " +
                    "from child "+
                    "order by birth,name,  sex desc";
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
