package com.meng.botconnect.views;

import android.content.*;
import android.content.res.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;

/**
 * Created by Administrator on 2018/8/9.
 */
public class mengEdittext extends LinearLayout {
    Context c;

    private EditText et;
    private TextView tv;

    public mengEdittext(Context c, AttributeSet a) {
        super(c, a);
        this.c = c;
        LayoutInflater.from(c).inflate(R.layout.meng_textview, this);
        tv = (TextView) findViewById(R.id.test_view_textview);
        et = (EditText)findViewById(R.id.test_view_edittext);
        TypedArray typedArray = c.obtainStyledAttributes(a, R.styleable.mengViews);
        tv.setText(typedArray.getString(R.styleable.mengViews_textviewText));
        et.setHint(typedArray.getString(R.styleable.mengViews_edittextHint));
        typedArray.recycle();
    }

    public String getString() {
        return isEmpty() ?et.getHint().toString(): et.getText().toString();
    }

    public void setString(String s) {
        et.setText(s);
    }

    public void addTextChangedListener(TextWatcher twColor) {
        et.addTextChangedListener(twColor);
    }

    public void setTextColor(int textColor) {
        tv.setTextColor(textColor);
    }

    private boolean isEmpty() {
        if (et.getText().toString() == null || et.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
