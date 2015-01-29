package com.xiang.model;

import java.util.Date;

public class NoteItem {
	
	public static final String TEXT = "text";
	public static final String BITMAP = "bitmap";
	public static final String SOUND = "sound";
	public static final String VIDEO = "video";
	
	private String type;
	private String path;
	private String content;
	private String updateTime;
	private int order;
	private String noteName;
	private String bookName;
	public NoteItem(String type, String path, String content, String updateTime,
			int order) {
		super();
		this.type = type;
		this.path = path;
		this.content = content;
		this.updateTime = updateTime;
		this.order = order;
	}
	
	public NoteItem(String type, String path, String content, String updateTime,
			int order, String noteName, String bookName) {
		super();
		this.type = type;
		this.path = path;
		this.content = content;
		this.updateTime = updateTime;
		this.order = order;
		this.noteName = noteName;
		this.bookName = bookName;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
}
