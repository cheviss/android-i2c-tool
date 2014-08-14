package com.weiyi.i2ctesttool;


import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class Activity_ScreenCtrl extends Activity {

	private Button Button_ScreenCtrl;	
	private boolean Flag_ScreenIsOn = true;
    
    public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.layout_screen_ctrl); 
       
    	/**********************************************************************
    	 *read button listener ��ť�¼��������� 
    	 */
        Button_ScreenCtrl = (Button) findViewById(R.id.buttonScreenCtrl);          
        Button_ScreenCtrl.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {
            	if (Flag_ScreenIsOn == true){
            		Flag_ScreenIsOn = false;
            		screen_brightness_adjust(0.004f);
            		Button_ScreenCtrl.setText("ON");    	    		
            	} else {
            		Flag_ScreenIsOn = true;
            		screen_brightness_adjust(-1);
            		Button_ScreenCtrl.setText("OFF");    	    		
            	}
             }          
        });   	
        
 
        /**********************************************************************
         *�������̣߳�����ʱ
         */
/*
        new Thread(new Runnable() {
	        public void run() {
            while (true) {
                try {
                    Thread.currentThread();
					Thread.sleep(200);
                } catch (InterruptedException e) {
					e.printStackTrace();
				}
            }}
	    }).start(); //�����߳�        
*/       
    } /*end of onCreate()*****************************************************/

    
    //http://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#screenBrightness
    //http://www.apkbus.com/android-89276-1-1.html
    //������������Activity��Ϊ��ActivityǱ�뵽TabActivity ���� ViewGroup ������ʱ��ͨ������ķ��������޷�ȡ�óɹ�
    //��Ҫͨ��getParent����������ȡ��Parent��Ȼ������
    //screenBrightness ��Ϊ -1�ָ���ԭ�ȵ����ȣ���ϵͳ���ã�
    //����Ļ���õ��������ֵ��0.004������0.001������ʱ��Ļ����ȫ�ڣ������ܿ��ơ�����0.004������0.001��ʱ����Ļ��ʧȥ����
    private void screen_brightness_adjust(float val){
        WindowManager.LayoutParams params = getParent().getWindow().getAttributes();    
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = val;
        getParent().getWindow().setAttributes(params); 
    }

    
    private void do_exec(String cmd) {
    	try {  
    		Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {  
            e.printStackTrace();  
        }   
    }/*end of do_exec()*******************************************************/

    
    //catch the Home button click in android, then close app.
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME){
	        finish();
	        System.exit(0);    	
		}
		if (keyCode==KeyEvent.KEYCODE_BACK){
	        finish();
	        System.exit(0);    	
		}
		return false;
	};
    
}