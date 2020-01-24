package com.meng.botconnect.fragment;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;

/**
 * Created by Administrator on 2018/7/19.
 */

public class TextFragment extends Fragment{

    private TextView tv;
    private int flag=0;

	public TextFragment(int i){
		flag=i;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        // TODO: Implement this method
        return inflater.inflate(R.layout.text_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        // TODO: Implement this method
        super.onViewCreated(view,savedInstanceState);
        tv=(TextView) view.findViewById(R.id.aboutTextView);
        switch(flag){
            case 0:
                welcome();
                break;
            case 1:
                about();
                break;
        }
    }
    private void welcome(){
        tv.setText("选择想要使用的功能吧");
    }

    private void about(){
        tv.setText("此生无悔入东方");
    }
}
