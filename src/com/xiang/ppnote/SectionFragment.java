package com.xiang.ppnote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;











import org.json.JSONException;

import com.xiang.adapter.NoteAdapter;
import com.xiang.exception.NoSDCardException;
import com.xiang.file.SaveManager;
import com.xiang.file.service.SaveService;
import com.xiang.model.Note;
import com.xiang.model.NoteItem;
import com.xiang.util.BaseHandler;

import android.annotation.SuppressLint;
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
	
	public MyHandler mHandler = new MyHandler();;
	public View[] views = new View[COUNT];
	public SaveService service = new SaveManager();
	
	private ListView lv_note;
	
	public List<Note> notes;
	public String book = "默认";
	public Note currentNote = null;
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
			views[1] = rootView;
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
		lv_note = (ListView) root.findViewById(R.id.lv_note);
		TextView tv_empty = (TextView) root.findViewById(R.id.tv_em);
		tv_empty.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ReadActivity.class);
				List<NoteItem> l = new ArrayList<NoteItem>();
				currentNote = new Note("默认", "", "", "", book, l);
				intent.putExtra("note", currentNote);
				intent.putExtra("add", true);
				startActivity(intent);
			}
		});
		lv_note.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				currentNote = notes.get(arg2);
				Intent intent = new Intent(context,ReadActivity.class);
				intent.putExtra("note", currentNote);
				intent.putExtra("add", false);
				startActivity(intent);
			}
		});
		initListView(book);
		lv_note.setEmptyView(tv_empty);
	}
	
	/**
	 * 根据分类名初始化列表
	 * @param bookname 分类名
	 */
	public void initListView(String bookname) {
		try {
			Log.d(TAG,bookname);
			notes = service.getNotes(bookname);
			SectionActivity.that.tv_notebook.setText(bookname);
			mHandler.sendEmptyMessage(BaseHandler.INIT_LISTVIEW_0);
		} catch (NoSDCardException e) {
			Log.d(TAG,e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG,e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d(TAG,e.getMessage());
			e.printStackTrace();
		}
	}


	@SuppressLint("HandlerLeak")
	class MyHandler extends BaseHandler{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
			case BaseHandler.INIT_LISTVIEW_0:
				Log.d(TAG,"个数："+notes.size());
				lv_note.setAdapter(new NoteAdapter(context,notes,lv_note));
				break;
			}
		}
	}
}