package com.xiang.file;

/**
 * 文件结构
 * 根papernote
 * 	book
 * 		note
 * 			noteitem
 * 			note info
 * 		book info	
 * 		
 */

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
import android.util.Log;

import com.xiang.exception.NoSDCardException;
import com.xiang.file.service.SaveService;
import com.xiang.model.Book;
import com.xiang.model.Note;
import com.xiang.model.NoteItem;
import com.xiang.util.StaticMethod;

public class SaveManager implements SaveService{
	private static final String TAG = "savemanager";
	public static String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/papernote";
	/**
	 * 文件信息名
	 */
	public static String INFO = "info";
	/**
	 * 文件的扩展名
	 */
	public static String EXT = ".xjson";
	
	public static boolean haveSDCard = false;
	
	static{
		if(Environment.getExternalStorageDirectory() != null)
			haveSDCard = true;
	}
	static{
		if(haveSDCard){
			File file = new File(rootPath);
			if(!file.exists())
				file.mkdirs();
		}
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
			String infoPath = defaultBookPath + "/info";
			new File(defaultBookPath).mkdirs();
			new File(infoPath).createNewFile();
//			return getBooksName();
			if(rootFolder.exists())
				return getBooksName();
			else
				return new ArrayList<String>();
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
		Log.d(TAG,"getbooks"+filenames.length);
		for(String s : filenames){
			String infopath = rootPath + "/" + s + "/info";
			File infoFile = new File(infopath);
			if(!infoFile.exists())
				infoFile.createNewFile();
			Book book = new Book(s, StaticMethod.getFileContent(infoFile));
			books.add(book);
//			Log.d(TAG,"added"+s);
		}
		return books;
	}

	@Override
	public Book getBookByName(String bookName) throws IOException {
		File infofile = new File(rootPath + "/" + bookName + "/info");
		return new Book(bookName, StaticMethod.getFileContent(infofile));
	}

	@Override
	public boolean isBookExists(String bookName) {
		File file = new File(rootPath + "/" + bookName);
		Log.d(TAG,rootPath + "/" + bookName + "/");
		Log.d(TAG,bookName+" 存在吗？" + file.exists());
		return file.exists();
	}

	@Override
	public int getNotesCount(String bookName) throws NoSDCardException, IOException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		if(!isBookExists(bookName)){
			createBook(bookName);
		}
		File file = new File(rootPath + "/" + bookName);
		String[] filenames = file.list();
		for(String s : filenames){
			if(s.equals("/info"))
				return filenames.length - 1;
		}
		return filenames.length;
	}

	@Override
	public List<String> getNotesName(String bookName) throws NoSDCardException, IOException {
		if(!haveSDCard){
			throw new NoSDCardException();
		}
		if(!isBookExists(bookName)){
			createBook(bookName);
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
		
		if(!isBookExists(bookName)){
			Log.d(TAG,"createBook");
			createBook(bookName);
		}
		
		List<Note> notes = new ArrayList<Note>();
		for(int i = 0 ; i < noteNames.size() ; i ++){
			File InfoFile = new File(rootPath + "/" + bookName + "/" + noteNames.get(i) + "/info");
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
		File InfoFile = new File(rootPath + "/" + bookName + "/" + noteName + "/info");
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
		File InfoFile = new File(rootPath + "/" + bookName + "/" + noteName + "/info");
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
		if(list == null)
			return new ArrayList<NoteItem>();
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
		
		int count = noteitems.size();
		for(int i = 0 ;i < count ; i ++){
			Log.d("NoteItems",","+noteitems.get(i).getOrder());
		}
		if(count < 2)
			return noteitems;
		for(int i = 0; i < count ; i ++){
			for(int j = count - 1; j > i ; j --){
				NoteItem i1 , i2;
				i1 = noteitems.get(j);
				i2 = noteitems.get(j - 1);
				if(i1.getOrder() < i2.getOrder()){
					noteitems.set(j, i2);
					noteitems.set(j-1, i1);
				}
			}
		}
		for(int i = 0 ;i < count ; i ++){
			Log.d("NoteItems",","+noteitems.get(i).getOrder());
		}
		
		return noteitems;
	}


	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean createNote(String bookName, String noteName) throws IOException, JSONException {
		
		File f = new File(rootPath + "/" + bookName + "/" + noteName);
		
		if(!f.exists())
			f.mkdir();
		File file = new File(rootPath + "/" + bookName + "/" + noteName + "/info");
		boolean b2 = false;
		
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
		if(!file.exists())
			file.mkdir();
		File infoFile = new File(rootPath + "/" + bookName + "/info");
		if(!infoFile.exists())
			return infoFile.createNewFile();
		return false;
	}

	@Override
	public boolean addItem(String bookName, String noteName, NoteItem item) throws IOException, JSONException {
		String path = rootPath + "/" + bookName + "/" + 
				noteName + "/" + item.getOrder() + ".xjson";
		String pathfolder = rootPath + "/" + bookName + "/" + noteName;
		File folder = new File(pathfolder);
		if(!folder.exists())
			createNote(bookName, noteName);
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

	@Override
	public boolean saveNote(Note note) {
//		String bookname = note.bookName;
//		String notename = note.noteName;
//		note.setUpdateTime(getTime());
//		if(isNoteExists(bookname,notename)){
//			
//		}
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public String getTime() {
		return new SimpleDateFormat("%y/%m/%d %H:%M:%S").format(new Date());
	}

	@Override
	public boolean changeNoteItem(NoteItem item) throws IOException, JSONException {
		String bookName = item.getBookName();
		String noteName = item.getNoteName();
		String path = rootPath + "/" + bookName + "/" + 
				noteName + "/" + item.getOrder() + ".xjson";
		File file = new File(path);
		if(file.exists()){
			file.delete();
			file.createNewFile();
			item.setUpdateTime(getTime());
			JSONObject json = new JSONObject();
			json.put("type", item.getType());
			json.put("path", item.getPath());               //可能是图片、声音的路径
			json.put("content", item.getContent());
			json.put("updateTime", item.getUpdateTime());
			json.put("order", item.getOrder());
			return StaticMethod.setFileContent(file, json.toString());
		}
		else{
			file.createNewFile();
			item.setUpdateTime(getTime());
			JSONObject json = new JSONObject();
			json.put("type", item.getType());
			json.put("path", item.getPath());               //可能是图片、声音的路径
			json.put("content", item.getContent());
			json.put("updateTime", item.getUpdateTime());
			json.put("order", item.getOrder());
			return StaticMethod.setFileContent(file, json.toString());
		}
	}

	@Override
	public void removeItem(NoteItem item) {
		String bookName = item.getBookName();
		String noteName = item.getNoteName();
		String path = rootPath + "/" + bookName + "/" + 
				noteName + "/" + item.getOrder() + ".xjson";
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	
}
