package com.xiang.model;

public class Book extends LocalModel{
	public String bookName;
	public String tag;
	public Book(String bookName, String tag) {
		super();
		this.bookName = bookName;
		this.tag = tag;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
