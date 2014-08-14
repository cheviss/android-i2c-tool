package com.weiyi.i2ctesttool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;


public class WriteReadSDCard
{		    
	/** Method to check whether external media available and writable. This is adapted from
	   http://developer.android.com/guide/topics/data/data-storage.html#filesExternal */
	//��ֹ2013-08-28���������û���õ�
	 public static void checkExternalMedia(){
	      boolean mExternalStorageAvailable = false;
	    boolean mExternalStorageWriteable = false;
	    String state = Environment.getExternalStorageState();
	
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // Can read and write the media
	        mExternalStorageAvailable = mExternalStorageWriteable = true;
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // Can only read the media
	        mExternalStorageAvailable = true;
	        mExternalStorageWriteable = false;
	    } else {
	        // Can't read or write
	        mExternalStorageAvailable = mExternalStorageWriteable = false;
	    }   
	    System.out.println("\n\nExternal Media: readable="
	            +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
	}

	 //�½��ļ������� 10s �ӽ����� 1000���ļ�
	 //�����ļ���д���ļ����ƺͽ���ʱ��
	 public static File CreateFile(File dir, String filename){
		    File file = new File(dir, filename);
		    //writeToSDFile(file, filename + "\r\n");
		    //writeToSDFile(file, PublicFunc.TimeGet() + "\r\n\r\n");
		    return file;
	 }
	 
	 /** Method to write ascii text characters to file on SD card. Note that you must add a 
	   WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
	   a FileNotFound Exception because you won't have write permission. */
	 public static  void writeToSDFile(File file, String content){
		 //д��ĺ���������飬����д��֮ǰ��Ҫȷ��·�����ļ��Ѿ�����
		 
		 //����ʱҲ��ӡ��Ϣ��logcat
		 if (false){
			 System.out.println(content);
		 }
		
		try {
			//���ļ�ĩβ�������ݣ�������ȫ�����ǣ���Ҫ�ڲ���������true, 
		    FileWriter out = new FileWriter(file, true);
		    out.write(content);
		    //out.write("\r\n");
		    out.flush();
		    out.close();
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		    //TODO �ļ���飬����ʧ��ʱ��ʾ
		    System.out.println("******* File not found. Did you" +
		            " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
		} catch (IOException e) {
		    e.printStackTrace();
		}   
	}

	
}