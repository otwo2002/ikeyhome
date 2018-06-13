package com.ikey.ikey;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

                    //입력한 날짜가 오늘보다 크면
                    Calendar cal = Calendar.getInstance();
                    Date d = new Date(cal.getTimeInMillis());
                    DateFormat df = new SimpleDateFormat( "yyyyMMdd HH:mm:ss");
                    int today = Integer.parseInt(df.format(d).substring(0,8));
                    if( Integer.parseInt(birth) > today){
                        showMessage("생년월일은 오늘보다 클수없습니다. ");
                        return ;
                    }
                    //달 말일 확인

                    int daysInMonth=0;
                    boolean leapYear;
                    leapYear = checkLeap(Integer.parseInt(birth.substring(0,4)));
                    int month= Integer.parseInt(birth.substring(4,6));
                    int day= Integer.parseInt(birth.substring(6,8));
                    if (month == 4 || month == 6 || month == 9 || month == 11)
                        daysInMonth = 30;
                    else if (month == 2)
                        daysInMonth = (leapYear) ? 29 : 28;
                    else
                        daysInMonth = 31;
                    if(month==0|| month > 12 || day==0 ||day > daysInMonth){
                        showMessage("생년월일을 확인하세요");
                        return ;
                    }
                    openDatabase("ikey");
                    createTable("child");
                    insertData(name, birth, sex);
                    showMessage("자녀등록이 완료되었습니다.");
                }catch (Exception e){
                    showMessage("등록중 오류가 발생했습니다. "+e.getMessage());
                    e.printStackTrace();
                }
                searchChild();
            }//end onclick
        });//end listener

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//end onCreate
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
    //날짜 검사
    // to check a year is leap or not
    private boolean checkLeap(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

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
        public View getView(final int i, View converView, ViewGroup viewGroup) {
            ChildView view= null;
            if (converView==null) {
                view = new ChildView(getApplicationContext()
                );
            }else{
                view = (ChildView)converView;
            }
            BodyInfoVO item = items.get(i);
            view.setNid(item.getId());
            view.setName(item.getName());
            view.setBirth(item.getBirth());
            view.setSex(item.getSex());
            view.setTag(i);
            view.setMonthView("("+calMonth(item.getBirth().replaceAll("-",""))+"개월)");
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
        builder.setMessage("자녀정보를 삭제하면 해당자녀의 측정정보가 삭제됩니다. 삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BodyInfoVO vo = (BodyInfoVO)childAdapter.getItem(rownum);

                String id = vo.getId();
                deleteData(id);
                childAdapter.items.remove(rownum);

                // listview 선택 초기화.
                //childAdapter.clearChoices();

                // listview 갱신.
                childAdapter.notifyDataSetChanged();
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
    private void searchChild(){
        //조회
        try {
            childList = selectChildData();
            ListView listTopView = (ListView) findViewById(R.id.listTopView);
            childAdapter = new ChildAdapter();

            if (childList != null && childList.size() > 0) {
                for (int i = 0; i < childList.size(); i++) {
                    childAdapter.addItem(childList.get(i));
                }
                // listview 갱신.
                childAdapter.notifyDataSetChanged();
                listTopView.setAdapter(childAdapter);
            } else {
                showMessage("등록된 자녀정보가 없습니다. 자녀등록후 이용해주세요. ");
            }
        }catch (SQLException sqle){
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
            String sql = "select _id, name,birth,sex " +
                    "from child "+
                    "order by birth,name,  sex desc";
            Cursor cursor = db.rawQuery(sql , null);
            if(cursor!=null && cursor.getCount()>0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    String id= cursor.getString(0);
                    String name= cursor.getString(1);
                    String birth = cursor.getString(2);
                    String sex = cursor.getString(3);

                    BodyInfoVO vo = new BodyInfoVO();
                    vo.setId(id);
                    vo.setName(name);
                    vo.setBirth(birth.substring(0,4)+"-"+birth.substring(4,6)+"-"+birth.substring(6,8));
                    vo.setSex( (sex.equals("1"))?"남자":"여자");
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
            String sql ="delete from child where _id= "+strId;
            db.execSQL(sql);
            sql ="delete from body_info where child_id= "+strId;
            db.execSQL(sql);
        }else{
            println("db open 호출됨");
            openDatabase("ikey");
            String sql ="delete from child where _id= "+strId;
            db.execSQL(sql);
            sql ="delete from body_info where child_id= "+strId;
            db.execSQL(sql);
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
