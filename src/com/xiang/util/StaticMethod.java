package com.xiang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StaticMethod {
	/**
	 * 
	 * @param t 对象数组
	 * @return 对象List集合
	 */
	public static <T> List<T> Array2List(T[] t){
		List<T> c = new ArrayList<T>(); 
		for(T e : t){
			c.add(e);
		}
		return c;
	}
	
	public static String getFileContent(File file) throws IOException{
		if(!file.exists()){
			file.createNewFile();
		}
		String content = "";
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		String line;

		while ((line = buffer.readLine()) != null) {
			content = content + line;
		}
		buffer.close();
		return content;
	}
	
	public static boolean setFileContent(File file, String content) throws IOException{
		if(!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.close();
		return true;
	}
}
