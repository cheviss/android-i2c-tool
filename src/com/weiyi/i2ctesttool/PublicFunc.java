package com.weiyi.i2ctesttool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class PublicFunc
{
    //Limit edittext length, and restrict the chars to 0-9, a-f, A-F only
	public static void  setEditTextFilters(EditText eidttext){
	    InputFilter[] FilterArray = new InputFilter[2];

	    FilterArray[0] = new InputFilter.LengthFilter(2);
	    FilterArray[1] = new InputFilter() { 
	        public CharSequence filter(CharSequence source, int start, int end, 
	        							Spanned dest, int dstart, int dend) { 
				char input_c;
				for (int i = start; i < end; i++) { 
					input_c = source.charAt(i);
					//������ĸ�����ߣ��������֣����ؿգ��൱��û������
					if (!Character.isLetterOrDigit(input_c)) { 
				        return ""; 
					}
					//������� 'f' < c <= 'z' ��Χ�ڵ��ַ���Ҳ���ؿ�
					if (input_c > 'f' && input_c <= 'z'){
				    	return "";
				    }
				} 
				return null; 
	        } 
	    };
	    
	    eidttext.setFilters(FilterArray);
	}//end of setEditTextFilters();

	/**************************************************************************
	 *��ȡʱ�䲢ת�����ַ��� 
	 */
    public static String DateGet() {
        String DATE_FORMAT_NOW = "yyyyMMdd-HHmmss";
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    	return sdf.format(cal.getTime());
    }
 
	/**************************************************************************
	 *��ȡʱ�䲢ת�����ַ��� 
	 */
    public static String TimeGet() {
        String DATE_FORMAT_NOW = "HH:mm:ss";
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    	return sdf.format(cal.getTime());
    }
 
    
	/**************************************************************************
	 *16�����ַ��� ת��Ϊ 2�����ַ���
	 *���� "31" to "00110001"
	 *Ȼ�󷵻ؽ�ȡ���ַ���������("31", 6, 2) ���� "01100"
	 */
	public static String HexStrToBinStr(String str, int bitH, int bitL) {
		//�����ڽ���һ���ֽ�
		if ((str.length() > 2) || (bitH < bitL) || (bitL < 0) || (bitH > 8))
			return "";
		
		//convert hexadecimal string to int
		int temp = Integer.parseInt(str, 16);

		//convert int to a fixed-length 8-bit binary string
		String bin = Integer.toBinaryString(0x100 | temp).substring(1);
		
		//�ַ�����charAt(7) ��  �����Ƶ�bit0
		return bin.subSequence(7-bitH, (7-bitL)+1).toString();
	}
    

	/**************************************************************************
     * ��ȡ����SD��·��
     * http://my.eoe.cn/1028320/archive/4718.html
     * @return ����ʱ�������ÿ���������ʱ�������ÿ�
     */
    public static String getExternalSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// �����뵱ǰ Java Ӧ�ó�����ص�����ʱ����
        try {
            Process p = run.exec(cmd);// ������һ��������ִ������
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // �������ִ�к��ڿ���̨�������Ϣ
                //System.out.println("CommonUtil:getSDCardPath"+lineStr);
                if (lineStr.contains("external_sd") && lineStr.contains("/mnt/")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        //System.out.println("getSDCardPath.restult return");
                        return result;
                    }
                }
                // ��������Ƿ�ִ��ʧ�ܡ�
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0��ʾ����������1������������
                	System.out.println("CommonUtil:getSDCardPath"+ "����ִ��ʧ��!");
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
        	System.out.println("CommonUtil:getSDCardPath"+ e.toString());
            return Environment.getExternalStorageDirectory().getPath();
        }
        
        return Environment.getExternalStorageDirectory().getPath();
    }
	
}//end of PublicFunc.java