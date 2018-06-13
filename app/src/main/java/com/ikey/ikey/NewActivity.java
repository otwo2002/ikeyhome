package com.ikey.ikey;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class NewActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String weight="";
    String height="";
    String[] spinneritems ;
    Map<String, BodyInfoVO> childMap ;
    private AdView mAdView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        try {
            openDatabase("ikey");
            ArrayList<BodyInfoVO> childList = selectChildData();

            if (childList != null && childList.size() > 0) {
                println(childList.toString());
                spinneritems = new String[childList.size()];
                childMap = new HashMap<String, BodyInfoVO>();
                for (int i = 0; i < childList.size(); i++) {
                    println(childList.get(i).getName() + ":" + childList.get(i).getBirth() + ":" + childList.get(i).getSex());
                    spinneritems[i] = childList.get(i).getName() + " (" + childList.get(i).getBirth() + ") - "
                            + (childList.get(i).getSex().equals("1") ? "남자" : "여자");
                    childMap.put(spinneritems[i], childList.get(i));
                }
            } else {
                showMessage("자녀등록후 이용하세요");
                return;
            }

            final Spinner spinner = (Spinner) findViewById(R.id.childSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinneritems
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            println("--------------------------->>>>>" + spinner);
            spinner.setAdapter(adapter);

            Button button = (Button) findViewById(R.id.btnShow);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    height = ((TextView) (findViewById(R.id.currHeight))).getText().toString();
                    if (height == null || height.trim().equals("")) {
                        showMessage("신장을 입력해주세요");
                        return;
                    } else {
                        try {
                            double heightD = Double.parseDouble(height);
                            if (heightD > 230) {
                                showMessage("신장을 정확히 입력해주세요!");
                                return;
                            }
                        } catch (Exception e) {
                            showMessage("신장은 숫자만 입력해주세요");
                            return;
                        }

                    }
                    weight = ((TextView) (findViewById(R.id.currWeight))).getText().toString();
                    if (weight == null || weight.trim().equals("")) {
                        showMessage("체중을 정확히 입력해주세요");
                        return;
                    } else {
                        try {
                            double weightD = Double.parseDouble(weight);
                            if (weightD > 300) {
                                showMessage("체중을 정확히 입력해주세요!");
                                return;
                            }
                        } catch (Exception e) {
                            showMessage("체중은 숫자만 입력해주세요");
                            return;
                        }
                    }
                    String key = (String) spinner.getSelectedItem();
                    println("key===========================>" + key);

                    BodyInfoVO vo = (BodyInfoVO) childMap.get(key);
                    println("vo===========================>" + vo.toString());
                    Intent intent = new Intent(NewActivity.this, ResultActivity.class);
                    intent.putExtra("id", vo.getId());
                    intent.putExtra("name", vo.getName());// 생년월일
                    intent.putExtra("birth", vo.getBirth());// 생년월일
                    intent.putExtra("sex", vo.getSex());//성별
                    intent.putExtra("height", height);
                    intent.putExtra("weight", weight);
                    intent.putExtra("month", "");
                    startActivity(intent);
                    finish();
                }
            });
        }catch (SQLException ex){
            showMessage("등록된 자녀정보가 없습니다. 자녀등록후 이용해주세요. ");
            ex.printStackTrace();
        }catch (Exception e){
            showMessage("알수없는 오류가 발생하여 앱을 실행할수없습니다. ");
            e.printStackTrace();
        }
        //광고--------------------------------------------------------
        // Sample AdMob app ID: ca-app-pub-3940256099942544/6300978111  - 테스트 아이디

       /* mAdView = findViewById(R.id.adView);
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
        */
        //-------------------------------------광고끝 ---------------------------------------------


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


    private void openDatabase(String databseName) {
        println("openDatabase 호출");
        db= openOrCreateDatabase(databseName, MODE_PRIVATE, null);
        if(db!=null){
            println("db open");
        }

    }
    //자녀정보조회 호출
    private ArrayList<BodyInfoVO> selectChildData() throws SQLException{
        println("selectChildData()호출됨");
        ArrayList<BodyInfoVO> list = new ArrayList<BodyInfoVO>();
        if(db!=null){
            String sql = "select _id , name,birth,sex " +
                    "from child "+
                    "order by birth,name,  sex desc";
            Cursor cursor = db.rawQuery(sql , null);
            if(cursor!=null && cursor.getCount()>0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    String id = cursor.getString(0);
                    String name= cursor.getString(1);
                    String birth = cursor.getString(2);
                    String sex = cursor.getString(3);

                    BodyInfoVO vo = new BodyInfoVO();
                    vo.setId(id);
                    vo.setName(name);
                    vo.setBirth(birth);
                    vo.setSex( sex);
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



}
