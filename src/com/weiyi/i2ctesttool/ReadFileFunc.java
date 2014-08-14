package com.weiyi.i2ctesttool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class ReadFileFunc
{
	/**
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     */
    public static void readFileByLines(String fileName, String[] s) {
        File file = new File(fileName);
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((line = reader.readLine()) != null) {
            	s[0] += line + "\r\n";
            }
            //System.out.println(s);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    } // end of class ReadFileFunc{}
        
}