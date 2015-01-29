package com.example.ppnote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import com.xiang.util.BaseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 返回对应的fragment
 * @author 祥祥
 *
 */

public class SectionFragment extends android.support.v4.app.Fragment
 {
	public static final String TAG = "sectionfragment";
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final int COUNT = 3;
	public static final Context context = SectionActivity.that;
	
	/**
	 * 
	 */
	@SuppressWarnings("null")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		
		switch (getArguments().getInt(ARG_SECTION_NUMBER)){
		case 0: 
			rootView = inflater.inflate(R.layout.fragment_mine, container,false);
			return rootView;
		case 1:
			rootView = inflater.inflate(R.layout.fragment_note, container,false);
			initFragmentNote(inflater,rootView,2);
			return rootView;
		case 2:
			rootView = inflater.inflate(R.layout.fragment_shop, container,false);
			return rootView;
		default:
			rootView = inflater.inflate(R.layout.fragment_note, container,false);
			return rootView;
		}
		
	}
	
	public void initFragmentNote(LayoutInflater inflater, View root,int a){
		final ListView lv_note = (ListView) root.findViewById(R.id.lv_note);
		//v can't add into lv_note . why?
//		View v = inflater.inflate(R.layout.emptyview, null, false);
		TextView tv_empty = (TextView) root.findViewById(R.id.tv_em);
		lv_note.setEmptyView(tv_empty);
		
		
		mhandler = new MyHandler();
	}
	
	public static MyHandler mhandler;
	class MyHandler extends BaseHandler{

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
			case BaseHandler.INIT_LISTVIEW_0:
				break;
			}
		}
	}
}