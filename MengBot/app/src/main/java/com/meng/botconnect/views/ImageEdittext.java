package com.meng.botconnect.views;

import android.content.*;
import android.graphics.drawable.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.widget.*;

public class ImageEdittext extends EditText {  
    public ImageEdittext(Context context) {  
        super(context);  
    }  
    public ImageEdittext(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    public void insertDrawable(int id) {  
        final SpannableString ss = new SpannableString("easy");  
        //得到drawable对象，即所要插入的图片   
        Drawable d = getResources().getDrawable(id);  
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());  
        //用这个drawable对象代替字符串easy   
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);  
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。   
        ss.setSpan(span, 0, "easy".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  
        append(ss);  
    }  
} 
