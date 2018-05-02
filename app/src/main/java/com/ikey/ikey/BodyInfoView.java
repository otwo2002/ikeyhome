package com.ikey.ikey;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fready on 2018-04-19.
 */

public class BodyInfoView extends LinearLayout {
    TextView insertDateView;
    TextView heightView;
    TextView weightView;
    TextView monthView;
    TextView hincreView;
    TextView wincreView;
    TextView deleteIcon;
    TextView nidView ;
    public BodyInfoView(Context context) {
        super(context);
        init(context);
    }

    public BodyInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.body_info_item, this, true);

        insertDateView = (TextView)findViewById(R.id.insertDate);
        monthView = (TextView)findViewById(R.id.imonth);
        weightView = (TextView)findViewById(R.id.weight);
        heightView = (TextView)findViewById(R.id.height);
        hincreView = (TextView)findViewById(R.id.hincre);
        wincreView = (TextView)findViewById(R.id.wincre);
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

    public void setInsertDate(String insertDate) {
        insertDateView.setText(insertDate);
    }

    public void setHeight(String height) {
        heightView.setText(height);
    }

    public void setWeight(String weight) {
        weightView.setText(weight);
    }

    public void setHincre(String hincre) {
        hincreView.setText(hincre);
    }

    public void setWincre(String wincre) {
        wincreView.setText(wincre);
    }

    public void setMonth(String month) {
        monthView.setText(month);
    }

    public void setNid(String nid) {
        nidView.setText(nid);
    }


}
