package com.meng.botconnect.fragment;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;

public class StatusFragment extends Fragment {

    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        return inflater.inflate(R.layout.text_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView) view.findViewById(R.id.aboutTextView);
        tv.setText("未填坑");
	}
}
