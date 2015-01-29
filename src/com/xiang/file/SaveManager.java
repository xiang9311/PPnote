package com.xiang.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.xiang.exception.NoSDCardException;
import com.xiang.file.service.SaveService;
import com.xiang.model.Book;
import com.xiang.model.Note;
import com.xiang.model.NoteItem;
import com.xiang.util.StaticMethod;

public class SaveManager implements SaveService{
	public static String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/papernote";
	/**
	 * 文件信息名
	 */
	public static String INFO = "_info";
	/**
	 * 文件的扩展名
	 */
	public static String EXT = ".xjson";
	
	public static boolean haveSDCard = false;
	static{
		if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED))
			haveSDCard = true;
	}
	
	@Override
	public int getBooksCount() throws NoSDCardException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		File rootFolder = new File(rootPath);
		if(!rootFolder.exists()){
			rootFolder.mkdir();
			String defaultBookPath = rootPath + "/默认";
			new File(defaultBookPath).mkdir();
			return 1;
		}else{
			return rootFolder.list().length;
		}
	}

	@Override
	public List<String> getBooksName() throws NoSDCardException, IOException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		File rootFolder = new File(rootPath);
		if(!rootFolder.exists()){
			rootFolder.mkdir();
			String defaultBookPath = rootPath + "/默认";
			String infoPath = defaultBookPath + "/_info";
			new File(defaultBookPath).mkdir();
			new File(infoPath).createNewFile();
			return getBooksName();
		}else{
			return StaticMethod.Array2List((String[]) rootFolder.list());
		}
	}

	@Override
	public List<Book> getBooks() throws NoSDCardException, IOException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		List<Book> books = new ArrayList<Book>();
		File file = new File(rootPath);
		String[] filenames = file.list();
		for(String s : filenames){
			String infopath = rootPath + "/" + s + "/_info";
			File infoFile = new File(infopath);
			if(!infoFile.exists())
				infoFile.createNewFile();
			Book book = new Book(s, StaticMethod.getFileContent(infoFile));
			books.add(book);
		}
		return books;
	}

	@Override
	public Book getBookByName(String bookName) throws IOException {
		File infofile = new File(rootPath + "/" + bookName + "/_info");
		return new Book(bookName, StaticMethod.getFileContent(infofile));
	}

	@Override
	public boolean isBookExists(String bookName) {
		File file = new File(rootPath + "/" + bookName);
		return file.exists();
	}

	@Override
	public int getNotesCount(String bookName) throws NoSDCardException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		File file = new File(rootPath + "/" + bookName);
		String[] filenames = file.list();
		for(String s : filenames){
			if(s.equals("/_info"))
				return filenames.length - 1;
		}
		return filenames.length;
	}

	@Override
	public List<String> getNotesName(String bookName) throws NoSDCardException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		File file = new File(rootPath + "/" + bookName);
		String[] ss = file.list();
		List<String> l = new ArrayList<String>();
		for(String s : ss){
			if(!s.equals(INFO)){
				l.add(s);
			}
		}
		return l;
	}

	@Override
	public List<Note> getNotes(String bookName) throws NoSDCardException, IOException, JSONException {
		List<String> noteNames = this.getNotesName(bookName);
		List<Note> notes = new ArrayList<Note>();
		for(int i = 0 ; i < noteNames.size() ; i ++){
			File InfoFile = new File(rootPath + "/" + bookName + "/" + noteNames.get(i) + "/_info");
			if(!InfoFile.exists())
				continue;
			String s = StaticMethod.getFileContent(InfoFile);
			JSONObject infor = new JSONObject(s);
			Note note = new Note(noteNames.get(i),
					infor.getString("createTime"), 
					infor.getString("updateTime"), 
					infor.getString("tag"), 
					bookName,
					getNoteItems(bookName, noteNames.get(i)));
			notes.add(note);
		}
		return notes;
	}

	@Override
	public Note getNoteByName(String bookName, String noteName) throws IOException, JSONException {
		File InfoFile = new File(rootPath + "/" + bookName + "/" + noteName + "/_info");
		if(!InfoFile.exists())
			return new Note(noteName, "", "", "", noteName);
		String s = StaticMethod.getFileContent(InfoFile);
		JSONObject infor = new JSONObject(s);
		Note note = new Note(noteName,
				infor.getString("createTime"), 
				infor.getString("updateTime"), 
				infor.getString("tag"), 
				bookName,
				getNoteItems(bookName, noteName));
		return note;
	}

	@Override
	public boolean isNoteExists(String bookName, String noteName) {
		File InfoFile = new File(rootPath + "/" + bookName + "/" + noteName + "/_info");
		return InfoFile.exists();
	}

	@Override
	public int getNoteitemsCount(String bookName, String noteName) {
		File file = new File(rootPath + "/" + bookName + "/" + noteName);
		String[] l = file.list();
		int count = 0;
		for(String s : l){
			if(s.contains(EXT))
				count++;
		}
		return count;
	}

	@Override
	public List<NoteItem> getNoteItems(String bookName, String noteName) throws IOException, JSONException {
		List<NoteItem> noteitems = new ArrayList<NoteItem>();
		String notePath = rootPath + "/" + bookName + "/" + noteName;
		File file = new File(notePath);
		String[] list = file.list();
		for(String name : list){
			if(name.contains(EXT)){
				File itemFile = new File(notePath + "/" + name);
				String content = StaticMethod.getFileContent(itemFile);
				JSONObject json = new JSONObject(content);
				NoteItem item = new NoteItem(json.getString("type"),
						json.getString("path"),
						json.getString("content"),
						json.getString("updateTime"),
						json.getInt("order"),
						noteName,
						bookName);
				noteitems.add(item);
			}
		}
		return noteitems;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean createNote(String bookName, String noteName) throws IOException, JSONException {
		File file = new File(rootPath + "/" + bookName + "/" + noteName + "/_info");
		File f = new File(rootPath + "/" + bookName + "/" + noteName);
		boolean b2 = false;
		if(!f.exists())
			f.mkdir();
		if(!file.exists()){
			if(b2 = file.createNewFile()){
				JSONObject json = new JSONObject();
				json.put("createTime", new SimpleDateFormat("%y/%m/%d %H:%M:%S").format(new Date()));
				json.put("updateTime", new SimpleDateFormat("%y/%m/%d %H:%M:%S").format(new Date()));
				json.put("tag","");
				return StaticMethod.setFileContent(file, json.toString());
			}
		}
		return b2;
	}

	@Override
	public boolean createBook(String bookName) throws IOException {
		File file = new File(rootPath + "/" + bookName);
		File infoFile = new File(rootPath + "/" + bookName + "/_info");
		if(!file.exists())
			file.mkdir();
		if(!infoFile.exists())
			return infoFile.createNewFile();
		return false;
	}

	@Override
	public boolean addItem(String bookName, String noteName, NoteItem item) throws IOException, JSONException {
		String path = rootPath + "/" + bookName + "/" + 
				noteName + "/" + item.getOrder() + ".xjson";
		File file = new File(path);
		if(file.exists())
			return false;
		file.createNewFile();
		JSONObject json = new JSONObject();
		json.put("type", item.getType());
		json.put("path", item.getPath());               //可能是图片、声音的路径
		json.put("content", item.getContent());
		json.put("updateTime", item.getUpdateTime());
		json.put("order", item.getOrder());
		return StaticMethod.setFileContent(file, json.toString());
	}
	
}
