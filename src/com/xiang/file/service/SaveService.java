package com.xiang.file.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.os.Environment;

import com.xiang.exception.NoSDCardException;
import com.xiang.model.Book;
import com.xiang.model.Note;
import com.xiang.model.NoteItem;

public interface SaveService {
	/**
	 * @return 获取分类的个数
	 * @throws NoSDCardException 
	 */
	public int getBooksCount() throws NoSDCardException;
	/**
	 * @return 获取所有分类名，返回<code>List<code>
	 * @throws IOException 
	 */
	public List<String> getBooksName() throws NoSDCardException, IOException;
	/**
	 * @return 获取分类对象<code>List<code> 
	 * @throws IOException 
	 */
	public List<Book> getBooks() throws NoSDCardException, IOException;
	/**
	 * @param bookName 分类名
	 * @return 该分类对象
	 * @throws IOException 
	 */
	public Book getBookByName(String bookName) throws IOException;
	/**
	 * @param <code>bookName<code> 分类名
	 * @return <code>boolean<code> 是否存在该分类
	 */
	public boolean isBookExists(String bookName);
	/**
	 * 
	 * @param bookName 分类名
	 * @return
	 * @throws IOException 
	 */
	public boolean createBook(String bookName) throws IOException;
	
	/**
	 * @param bookName 分类名
	 * @return int 该分类下笔记的个数
	 * @throws NoSDCardException 
	 */
	public int getNotesCount(String bookName) throws NoSDCardException;
	/**
	 * 
	 * @param bookName 分类名
	 * @return 返回该分类下的笔记名称列表
	 * @throws NoSDCardException 
	 */
	public List<String> getNotesName(String bookName) throws NoSDCardException;
	/**
	 * 
	 * @param bookName 分类名
	 * @return 返回该分类下笔记对象列表List Note
	 * @throws NoSDCardException 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public List<Note> getNotes(String bookName) throws NoSDCardException, IOException, JSONException;
	/**
	 * 
	 * @param bookName 分类名
	 * @param noteName 笔记名
	 * @return 返回笔记对象
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public Note getNoteByName(String bookName, String noteName) throws IOException, JSONException;
	/**
	 * 
	 * @param bookName 分类名
	 * @param noteName 笔记名
	 * @return 判断笔记是否存在
	 */
	public boolean isNoteExists(String bookName, String noteName);
	/**
	 * 创建note
	 * @param bookName 分类名
	 * @param noteName note名
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public boolean createNote(String bookName, String noteName) throws IOException, JSONException;
	
	/**
	 * 
	 * @param bookName 分类名
	 * @param noteName 笔记名
	 * @return 返回
	 */
	public int getNoteitemsCount(String bookName, String noteName);
	/**
	 * 
	 * @param bookName
	 * @param noteName
	 * @return 返回纸条个数
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public List<NoteItem> getNoteItems(String bookName, String noteName) throws IOException, JSONException;
	
	public boolean addItem(String bookName, String noteName, NoteItem item) throws IOException, JSONException;
}
