package com.xiang.adapter;

import java.util.List;

import com.xiang.ppnote.R;
import com.xiang.model.Note;
import com.xiang.model.NoteItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
	
	private Context context;
	private List<Note> notes;
	private ListView lv_notes;
	
	public NoteAdapter(Context context, List<Note> notes, ListView lv_notes) {
		super();
		this.context = context;
		this.notes = notes;
		this.lv_notes = lv_notes;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return notes.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != position){
			holder = new ViewHolder(position);
			Note note = notes.get(position);
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_note, null, false);
			TextView tv_li_notename = (TextView) convertView.findViewById(R.id.tv_li_notename);
			TextView tv_li_notecontent = (TextView) convertView.findViewById(R.id.tv_li_notecontent);
			LinearLayout ll_li_iv = (LinearLayout) convertView.findViewById(R.id.ll_li_iv);
			ImageView iv_li_sound = (ImageView) convertView.findViewById(R.id.iv_li_sound);
			
			if(note.noteItems != null && note.noteItems.size() > 0){
				if(note.noteItems.get(0).getType() != NoteItem.BITMAP){
					ll_li_iv.setVisibility(View.GONE);
					iv_li_sound.setVisibility(View.GONE);
				}
			}
			else{
				ll_li_iv.setVisibility(View.GONE);
				iv_li_sound.setVisibility(View.GONE);
			}
			tv_li_notename.setText(note.noteName);
			Log.d("note","0");
			if(note.noteItems != null && note.noteItems.size() > 0){
				Log.d("note","1");
				if(note.noteItems.get(0).getType().equals(NoteItem.TEXT))
					Log.d("note","2");
					tv_li_notecontent.setText(note.noteItems.get(0).getContent());
			}
			
			convertView.setTag(holder);
		}
		return convertView;
	}
	
	class ViewHolder{
		public ViewHolder(int flag) {
			super();
			this.flag = flag;
		}

		int flag = -1;
	}

}
