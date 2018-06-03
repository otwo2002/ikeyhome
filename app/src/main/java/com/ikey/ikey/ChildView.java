package com.ikey.ikey;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by fready on 2018-05-12.
 */

public class ChildView extends LinearLayout {

    TextView nameView;
    TextView birthView;
    TextView sexView;
    TextView deleteIcon;
    TextView nidView ;
    public ChildView(Context context) {
        super(context);
        init(context);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.child_item, this, true);

        nameView = (TextView)findViewById(R.id.name);
        birthView = (TextView)findViewById(R.id.birth);
        sexView = (TextView)findViewById(R.id.sex);
        deleteIcon = (TextView)findViewById(R.id.deleteIcon);
        nidView = (TextView)findViewById(R.id.nid);
    }
    public TextView getDeleteIcon(){
        return deleteIcon;
    }
    public String getNid(){
        return nidView.getText().toString();
    }
    public void setDeleteImgView(String id) {}
    public void setName(String name) {
        nameView.setText(name);
    }
    public void setBirth(String birth) {
        birthView.setText(birth);
    }
    public void setSex(String sex) {  sexView.setText(sex);
    }
    public void setNid(String nid) {
        nidView.setText(nid);
    }
}
