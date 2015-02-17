package com.xiang.ppnote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.xiang.adapter.NoteItemAdapter;
import com.xiang.file.SaveManager;
import com.xiang.file.service.SaveService;
import com.xiang.model.Note;
import com.xiang.model.NoteItem;
import com.xiang.util.BaseHandler;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

public class ReadActivity extends Activity {
	public static final String TAG = "readActivity";
	public Note note;
	
	private TextView tv_notename;
	private TextView tv_note_bookname;
	private ListView lv_noteitem;
	private EditText et_content;
	private ImageButton ib_save;
	private LinearLayout ll_input;
	
	/**
	 * changeview
	 */
	private View change_view;
	private TextView tv_change_content;
	private EditText et_change_content;
	private Button b_cancle;
	private Button b_delete;
	private Button b_save;
	private AlertDialog al;
	
	private ButtonListener buttonListener;
	
	private List<NoteItem> items;
	
	public MyHandler mHandler;
	
	private boolean add = false;
	private int selected_item;
	
	public SaveService service = new SaveManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		// Show the Up button in the action bar.
//		setupActionBar();
		mHandler = new MyHandler();
		note = (Note) getIntent().getExtras().get("note");
		add = getIntent().getExtras().getBoolean("add");
		Init();
	}

	private void Init() {
		buttonListener = new ButtonListener();
		tv_notename = (TextView) findViewById(R.id.tv_notename);
		tv_note_bookname = (TextView) findViewById(R.id.tv_note_bookname);
		lv_noteitem = (ListView) findViewById(R.id.lv_noteitem);
		et_content = (EditText) findViewById(R.id.et_content);
		ib_save = (ImageButton) findViewById(R.id.ib_save);
		ll_input = (LinearLayout) findViewById(R.id.ll_input);
		
		change_view = LayoutInflater.from(getBaseContext()).inflate(R.layout.changeitemview, null);
		tv_change_content = (TextView) change_view.findViewById(R.id.tv_change_content);
		et_change_content = (EditText) change_view.findViewById(R.id.et_change_content);
		b_delete = (Button) change_view.findViewById(R.id.b_delete);
		b_cancle = (Button) change_view.findViewById(R.id.b_cancle);
		b_save = (Button) change_view.findViewById(R.id.b_save);
		
		tv_notename.setText(note.noteName);
		tv_note_bookname.setText(note.bookName);
		
		b_delete.setOnClickListener(buttonListener);
		b_cancle.setOnClickListener(buttonListener);
		b_save.setOnClickListener(buttonListener);
		
		/**
		 * 新增一个条目
		 */
		ib_save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(et_content.getText().toString().equals(""))
					return;
				NoteItem item = new NoteItem(NoteItem.TEXT, "", et_content.getText().toString(),
						"", getNextOrder(), note.noteName, note.bookName);
				try {
					service.addItem(note.bookName, note.noteName, item);
					items.add(item);
					note.noteItems.add(item);
					add = false;
					setListViewData();
					et_content.setText("");
					et_content.clearFocus();
					HideInputMethod(et_content);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		/**
		 * 长按对条目做出修改
		 */
		lv_noteitem.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selected_item = arg2;
				Builder builder = new Builder(ReadActivity.this);
				tv_change_content.setText("对“"+items.get(arg2).getContent()+"”做出修改");
				et_change_content.setText(items.get(arg2).getContent());
				if(al == null){
					al = builder.create();
					al.setView(change_view);
				}
				al.show();
				return true;
			}
		});
		
		/**
		 * 显示隐藏底部编辑栏
		 */
		
		setListViewData();
	}
	
	protected int getNextOrder() {
		int order , size;
		size = note.noteItems.size(); 
		if(size > 0)
			order = note.noteItems.get(size - 1).getOrder() + 1;
		else
			order = 0;
		return order;
	}

	private void HideInputMethod(EditText et){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}
	
	public class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.b_delete:
				NoteItem i = items.get(selected_item);
				note.noteItems.remove(selected_item);
				items.remove(selected_item);
				service.removeItem(i);
				setListViewData();
				HideInputMethod(et_change_content);
				al.cancel();
				break;
			case R.id.b_cancle:
				HideInputMethod(et_change_content);
				al.cancel();
				
				break;
			case R.id.b_save:
				NoteItem item = items.get(selected_item);
				String content = et_change_content.getText().toString();
				note.noteItems.get(selected_item).setContent(content);
				item.setContent(content);
				try {
					service.changeNoteItem(item);
					setListViewData();
					HideInputMethod(et_change_content);
					al.cancel();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		
	}

	private void setListViewData() {
		try {
			if(!add)
				items = service.getNoteItems(note.bookName, note.noteName);
			else{
				service.createNote(note.bookName, note.noteName);
				items = new ArrayList<NoteItem>();
			}
			mHandler.sendEmptyMessage(BaseHandler.INIT_LISTVIEW_0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends BaseHandler{

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case BaseHandler.INIT_LISTVIEW_0:
				lv_noteitem.setAdapter(new NoteItemAdapter(ReadActivity.this,items,lv_noteitem));
				break;
			}
			super.handleMessage(msg);
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
