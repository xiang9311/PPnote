package com.xiang.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.entity.SerializableEntity;

import android.util.Log;

public class Note implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3069378792709629232L;
	public String noteName;
	public String createTime;
	public String updateTime;
	public String tag;
	public String bookName;
	public List<NoteItem> noteItems;
	
	public Note(Note note){
		this.noteName = note.noteName;
		this.createTime = note.createTime;
		this.updateTime = note.updateTime;
		this.tag = note.tag;
		this.bookName = note.bookName;
		this.noteItems = new ArrayList<NoteItem>();
		this.noteItems.addAll(note.noteItems);
	}
	
	public Note(String noteName, String createTime, String updateTime, String tag) {
		super();
		this.noteName = noteName;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.tag = tag;
	}
	
	public Note(String noteName, String createTime, String updateTime, String tag,
			String bookName) {
		super();
		this.noteName = noteName;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.tag = tag;
		this.bookName = bookName;
	}
	
	public Note(String noteName, String createTime, String updateTime, String tag,
			String bookName, List<NoteItem> noteItems) {
		super();
		this.noteName = noteName;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.tag = tag;
		this.bookName = bookName;
		this.noteItems = noteItems;
	}

	public String getNoteName() {
		return noteName;
	}
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
