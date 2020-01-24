package com.meng.botconnect.fragment;

import android.app.*;
import android.os.*;
import android.view.*;
import com.meng.botconnect.*;

public class MessageFragment extends Fragment {

		
		@Override
		public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
			// TODO: Implement this method
			return inflater.inflate(R.layout.msg_view,container,false);
		}

		@Override
		public void onViewCreated(View view,Bundle savedInstanceState){
			// TODO: Implement this method
			super.onViewCreated(view,savedInstanceState);
			
		}
}
