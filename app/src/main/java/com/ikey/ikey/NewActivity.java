package com.ikey.ikey;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class NewActivity extends AppCompatActivity {
    String birth = "" ;
    String sex = "";
    String weight="";
    String height="";
    RadioButton m ;
    RadioButton w ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Button button = findViewById(R.id.btnShow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birth= ((TextView) (findViewById(R.id.birth))).getText().toString();

                //입력값 체크
                if(birth ==null || birth.trim().equals("") || birth.length() != 8 ){
                    /*AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder( );
                    dialog = builder.setMessage("생년월일을 정확히 입력해주세요")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();*/
                    showMessage("생년월일을 정확히 입력해주세요");
                    return;
                }
                boolean checkedM= ((RadioButton) (findViewById(R.id.radioM))).isChecked();
                boolean checkedW= ((RadioButton) (findViewById(R.id.radioW))).isChecked();
                //입력값 체크
                if(sex ==null || ( !checkedM && !checkedW )  ){
                    showMessage("성별을 입력해주세요");
                    return;
                }
                if(checkedM){
                    sex="1";
                }else if(checkedW){
                    sex="2";
                }

                height= ((TextView) (findViewById(R.id.currHeight))).getText().toString();
                if(height ==null || height.trim().equals("")  ){
                    showMessage("신장을 정확히 입력해주세요");
                    return;
                }
                weight= ((TextView) (findViewById(R.id.currWeight))).getText().toString();
                if(weight ==null || weight.trim().equals("")  ){
                    showMessage("체중을 정확히 입력해주세요");
                    return;
                }
                Intent intent = new Intent(NewActivity.this, ResultActivity.class);
                intent.putExtra("birth",birth);// 생년월일
                intent.putExtra("sex",sex);//성별
                intent.putExtra("height", height);
                intent.putExtra("weight", weight);
                startActivity(intent);
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
}
