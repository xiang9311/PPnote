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
	 * @return ��ȡ����ĸ���
	 * @throws NoSDCardException 
	 */
	public int getBooksCount() throws NoSDCardException;
	/**
	 * @return ��ȡ���з�����������<code>List<code>
	 * @throws IOException 
	 */
	public List<String> getBooksName() throws NoSDCardException, IOException;
	/**
	 * @return ��ȡ�������<code>List<code> 
	 * @throws IOException 
	 */
	public List<Book> getBooks() throws NoSDCardException, IOException;
	/**
	 * @param bookName ������
	 * @return �÷������
	 * @throws IOException 
	 */
	public Book getBookByName(String bookName) throws IOException;
	/**
	 * @param <code>bookName<code> ������
	 * @return <code>boolean<code> �Ƿ���ڸ÷���
	 */
	public boolean isBookExists(String bookName);
	/**
	 * 
	 * @param bookName ������
	 * @return
	 * @throws IOException 
	 */
	public boolean createBook(String bookName) throws IOException;
	
	/**
	 * @param bookName ������
	 * @return int �÷����±ʼǵĸ���
	 * @throws NoSDCardException 
	 */
	public int getNotesCount(String bookName) throws NoSDCardException;
	/**
	 * 
	 * @param bookName ������
	 * @return ���ظ÷����µıʼ������б�
	 * @throws NoSDCardException 
	 */
	public List<String> getNotesName(String bookName) throws NoSDCardException;
	/**
	 * 
	 * @param bookName ������
	 * @return ���ظ÷����±ʼǶ����б�List Note
	 * @throws NoSDCardException 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public List<Note> getNotes(String bookName) throws NoSDCardException, IOException, JSONException;
	/**
	 * 
	 * @param bookName ������
	 * @param noteName �ʼ���
	 * @return ���رʼǶ���
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public Note getNoteByName(String bookName, String noteName) throws IOException, JSONException;
	/**
	 * 
	 * @param bookName ������
	 * @param noteName �ʼ���
	 * @return �жϱʼ��Ƿ����
	 */
	public boolean isNoteExists(String bookName, String noteName);
	/**
	 * ����note
	 * @param bookName ������
	 * @param noteName note��
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public boolean createNote(String bookName, String noteName) throws IOException, JSONException;
	
	/**
	 * 
	 * @param bookName ������
	 * @param noteName �ʼ���
	 * @return ����
	 */
	public int getNoteitemsCount(String bookName, String noteName);
	/**
	 * 
	 * @param bookName
	 * @param noteName
	 * @return ����ֽ������
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public List<NoteItem> getNoteItems(String bookName, String noteName) throws IOException, JSONException;
	
	public boolean addItem(String bookName, String noteName, NoteItem item) throws IOException, JSONException;
}
