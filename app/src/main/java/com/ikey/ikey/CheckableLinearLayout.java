package com.ikey.ikey;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Checkable;

/**
 * Created by fready on 2018-04-26.
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    final String NS = "http://schemas.android.com/apk/res/com.huewu.example.checkable";
    final String ATTR = "checkable";

    int checkableId;
    Checkable checkable;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        checkableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
    }

    @Override
    public void setChecked(boolean b) {
        checkable = (Checkable) findViewById(checkableId);
        if(checkable == null)
            return;
        checkable.setChecked(true);
    }

    @Override
    public boolean isChecked() {
        checkable = (Checkable) findViewById(checkableId);
        if(checkable == null)
            return false;
        return checkable.isChecked();
    }

    @Override
    public void toggle() {
        checkable = (Checkable) findViewById(checkableId);
        if(checkable == null)
            return;
        checkable.toggle();
    }
}
