package com.ikey.ikey;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by fready on 2018-04-24.
 */

public class HumanView extends LinearLayout implements Checkable{

    TextView nameView;
    TextView birthView;
    TextView sexView;
    TextView nidView ;
    RadioButton rb;
    public HumanView(Context context) {
        super(context);
        init(context);
    }

    public HumanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.human_item, this, true);

        nameView = (TextView)findViewById(R.id.name);
        birthView = (TextView)findViewById(R.id.birth);
        sexView = (TextView)findViewById(R.id.sex);
        rb = (RadioButton) findViewById(R.id.checkChild);
        nidView = (TextView)findViewById(R.id.nid);
    }
    public String getNid(){
        return nidView.getText().toString();
    }
    public void setNid(String nid) {
        nidView.setText(nid);
    }
    public void setName(String name) {
        nameView.setText(name);
    }
    public void setBirth(String birth) {
        birthView.setText(birth);
    }
    public void setSex(String sex) {  sexView.setText(sex); }
    @Override
    public void  setChecked(boolean checked){
        System.out.println("rb.isChecked()==>"+rb.isChecked()+"   checked-->"+checked);
        if (rb.isChecked() != checked) {
            rb.setChecked(checked) ;
        }

    }
    @Override
    public boolean isChecked() {
        return rb.isChecked() ;
    }

    @Override
    public void toggle() {
        setChecked(rb.isChecked() ? false : true) ;
    }


}