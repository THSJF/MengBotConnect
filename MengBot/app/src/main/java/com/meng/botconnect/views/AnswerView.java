package com.meng.botconnect.views;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;

public class AnswerView extends LinearLayout {
	private EditText et;
	private TextView tv;
	private CheckBox cb;
	private int id;

    public AnswerView(Context context, int id, String Ans, boolean isTrue) {  
        super(context);
		LayoutInflater.from(context).inflate(R.layout.answer_view, this);
        et = (EditText)findViewById(R.id.answer_viewEditText);
		tv = (TextView) findViewById(R.id.answer_viewTextView);
		cb = (CheckBox)findViewById(R.id.answer_viewCheckBox);   		
		this.id = id;
		tv.setText("答案" + (id+1)+":");
		setAnswer(Ans);
		setTrueAnswer(isTrue);
		if (id == 0) {
			et.setHint("是");
		} else if (id == 1) {
			et.setHint("否");
		}
	}

	public boolean isTrueAnswer() {
		return cb.isChecked();
	}

	public void setTrueAnswer(boolean t) {
		cb.setChecked(t);
	}

	public String getAnswer() {
		String userInput=et.getText().toString();
		return userInput.equals("") ?et.getHint().toString(): userInput;
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

