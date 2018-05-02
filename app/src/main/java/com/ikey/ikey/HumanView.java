package com.ikey.ikey;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fready on 2018-04-24.
 */

public class HumanView extends LinearLayout {

    TextView nameView;
    TextView birthView;
    TextView sexView;
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
    }

    public void setName(String name) {
        nameView.setText(name);
    }
    public void setBirth(String birth) {
        birthView.setText(birth);
    }
    public void setSex(String sex) {  sexView.setText(sex);
    }

    public void setColorChange(){

    }

}
