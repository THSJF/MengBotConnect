package com.addques;

import android.content.*;
import android.graphics.drawable.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import android.graphics.*;

public class ImageEdittext extends EditText {  
    public ImageEdittext(Context context) {  
        super(context);  
    }  
    public ImageEdittext(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }

	public void insertImage(File chooseFilePath) {
		Drawable d=new BitmapDrawable(BitmapFactory.decodeFile(chooseFilePath.getAbsolutePath()));
		SpannableString ss = new SpannableString("(image)");  
        d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, "(image)".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  

		String after=getText().toString().substring(getSelectionStart());
		setText(getText().toString().replace(after, ""));
		append(ss);
		append(after);
    }

	public void replaceDrawable(File f) {
		Drawable d=new BitmapDrawable(BitmapFactory.decodeFile(f.getAbsolutePath()));
		SpannableString ss = new SpannableString("(image)");  
        d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, "(image)".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  
		String[] sa=getText().toString().split("\\(image\\)");
		String ns=getText().toString();
		setText("");
		if (sa.length == 1) {
			if (ns.startsWith("(image)")) {
				append(ss);
				append(sa[0]);
			} else {
				append(sa[0]);
				append(ss);
			}
		} else {
			append(sa[0]);
			append(ss);
			append(sa[1]);
		}
	}
}
