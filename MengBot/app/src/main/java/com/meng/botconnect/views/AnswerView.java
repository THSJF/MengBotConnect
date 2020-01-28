package com.meng.botconnect.views;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;

public class AnswerView extends LinearLayout {
	private EditText et;
	private CheckBox cb;
	private int id;

    public AnswerView(Context context, int id, String Ans, boolean isTrue) {  
        super(context);
		LayoutInflater.from(context).inflate(R.layout.answer_view, this);
        et = (EditText)findViewById(R.id.answer_viewEditText);
        cb = (CheckBox)findViewById(R.id.answer_viewCheckBox);   		
		this.id = id;
		setAnswer(Ans);
		setTrueAnswer(isTrue);
	}

	public boolean isTrueAnswer() {
		return cb.isChecked();
	}

	public void setTrueAnswer(boolean t) {
		cb.setChecked(t);
	}

	public String getAnswer() {
		return et.getText().toString();
	}

	public void setAnswer(String s) {
		et.setText(s);
	}

	public int getId() {
		return id;
	}

	public void clean() {
		cb.setChecked(false);
		et.setText("");
	}
} 

