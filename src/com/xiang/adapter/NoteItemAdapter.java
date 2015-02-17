package com.xiang.adapter;

import java.util.List;

import com.xiang.ppnote.R;
import com.xiang.model.NoteItem;
import com.xiang.util.BaseHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NoteItemAdapter extends BaseAdapter {
	private Context context;
	private List<NoteItem> items;
	private ListView lv;
	
	public NoteItemAdapter(Context context, List<NoteItem> items, ListView lv) {
		super();
		this.context = context;
		this.items = items;
		this.lv = lv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != position ){
			holder = new ViewHolder(position);
			NoteItem item = items.get(position);
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_noteitem, parent, false);
			TextView tv_notecontent = (TextView) convertView.findViewById(R.id.tv_notecontent);
			ImageView iv_notecontent = (ImageView) convertView.findViewById(R.id.iv_notecontent);
			ImageView iv_sound = (ImageView) convertView.findViewById(R.id.iv_sound);
			
			if(item.getType().equals(NoteItem.TEXT)){
				tv_notecontent.setText("  ^"+item.getContent());
				iv_notecontent.setVisibility(View.GONE);
				iv_sound.setVisibility(View.GONE);
			}
			
			convertView.setTag(holder);
		}
		return convertView;
	}
	
	class ViewHolder extends BaseHolder{

		public ViewHolder(int flag) {
			super(flag);
		}
		
	}
}
