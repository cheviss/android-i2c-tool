package com.weiyi.i2ctesttool;


import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_alc5625 extends Activity {

    //logfile
	private static File logfile;	

    //pe30, smartGPS V2 board i2c chip alc address.
    private String DevAddr = new String("1 0x1e");
    private String DevMode = new String("W");
   
	private EditText EditText_ReadCycleTime;
	
	private Button Button_Read;
	private TextView TextView_Result;
	private TextView TextView_Warn;

	private String[] I2CDumpValHexStr = new String[256];
	
	/*
	 *���̣߳�ִ������������̴߳��������ݺ������̷߳��͵���Ϣ��
	 */
	private Process Process_ExecCmd;
    private boolean boolean_CaptureOutputThreadRunnig = true;
    private Handler Handler_MsgOperate;
    

    /*
     *ѭ����ȡ������Ҫ����ı��� 
     */
    //ѭ��ʱ����Ϊ0��������ťֻ��һ��
    private boolean boolean_LoopReadEnable = false;
    //ѭ����ȡ�̣߳������ť��ʼѭ����ȡ���ٴε����ť����ѭ����ȡ
    private Thread ThreadReadCycle;
    private boolean boolean_LoopReadState = false;
    
  
    
	/************************************************************************** 
	 * Called when the activity is first created. 
	 */  
    @SuppressLint("HandlerLeak")
	public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.layout_alc5625);   
    
        /**********************************************************************
         * log �½�activity-basic logfile
         */
    	logfile = WriteReadSDCard.CreateFile(Activity_TableLayout.I2CLogDirFile,
    			"i2clog-alc-" + PublicFunc.DateGet() + ".txt");
    	
    	alc5625.ItemArrayInit();
    	WriteReadSDCard.writeToSDFile(logfile, alc5625.ItemLogNames());
 
    	//��ʼ��i2cdump output�洢���飬��ʽΪ16�����ַ���
    	for (int i=0; i<I2CDumpValHexStr.length; i++){
    		I2CDumpValHexStr[i] = "00";
    	}
    	
    	/**********************************************************************
    	 * ��ʼ��activity-basic UI
    	 */             
        /*
         *����textview, ��ʾ�������
         */
    	TextView_Result = (TextView) findViewById(R.id.idtextViewResult); 
    	TextView_Result.setMovementMethod(new ScrollingMovementMethod());	
    	TextView_Warn = (TextView) findViewById(R.id.idtextViewWarn); 
    	TextView_Warn.setMovementMethod(new ScrollingMovementMethod());	
    	 
    	/*
    	 *����edittext, ��ȡread/set�Ĳ�������
    	 */
    	EditText_ReadCycleTime = (EditText) findViewById(R.id.ideditTextCycleTime);


    	/**********************************************************************
    	 *read button ѭ��ʱ���Ƿ�Ϊ0����read��2��ģʽ��ֻ��1�Ρ�ѭ����ȡ
    	 *ʱ��Ϊ0�ı�׼�����Ϊ�ջ���ȫ������0��
    	 *ѭ����ȡʱ����һ�ο�ʼ���ٰ�������
    	 *������Ҫ�����û������ѭ��ʱ�䣬
    	 *���ڻس��������쳣�˳�����Ҫ������������Ϊ���֣��������볤��5λ�� xml����ɡ�
    	 */
    	//edittext change listener
    	EditText_ReadCycleTime.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable s) {
	        	String Str_cycletime = EditText_ReadCycleTime.getText().toString();
	        	if ((Str_cycletime.compareTo("") == 0) || (Integer.valueOf(Str_cycletime) == 0)){
	        		//System.out.println("boolean_LoopReadEnable = false");
	        		//bug �������cycle read�� boolean_LoopReadState = true��
	        		//��ʱ����cycletime=0������stop��Ч
	        		//if(==true)���ڵ������ǣ����û��cycle����д���0ֵ��ʹboolean_LoopReadEnable=true��
	        		//�����Ÿ�Ϊ0��preformClick()�������else����ִ��thread_read_cycle()��ȡedittextֵ��
	        		//�յ�edittext ""ת int ʱ�ᵼ��
	        		//FATAL EXCEPTION: Thread-387
	        		//java.lang.NumberFormatException: Invalid int: ""
	        		//���ԣ�ȫ�ֱ�־λ���׳����Ⱑ������
	        		//TODO ���ȫ�ֱ�־�ķ��� �ǳ���Ҫ
	        		if (boolean_LoopReadState == true){
		        		Button_Read.performClick();	        			
	        		}
	        		boolean_LoopReadEnable = false;
	        	} else {        		
	        		//System.out.println(Integer.valueOf(EditText_ReadCycleTime.getText().toString()));
	        		boolean_LoopReadEnable = true;
	        	}	
	        }
	
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
	
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
    	});//endof EditText_ReadCycleTime.addTextChangedListener()
        
    	/**********************************************************************
    	 *read button listener ��ť�¼��������� 
    	 */
        Button_Read = (Button) findViewById(R.id.idButtonRead);          
        Button_Read.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {
            	if (boolean_LoopReadEnable == false){
            		//ֻ��һ��
                	do_exec(read_cmd_get());
            	} else {
            		//ѭ����ȡ�������ť��ʼ���ٴε����ť����
                	if (false == boolean_LoopReadState){
                		boolean_LoopReadState = true;
                		Button_Read.setText("StopRead"); 
                		Button_Read.setTextColor(Color.RED);
                		thread_read_cycle();
                	} else {
                		boolean_LoopReadState = false;
                		ThreadReadCycle.interrupt();
                		Button_Read.setText("read");
                		Button_Read.setTextColor(Color.BLACK);
                	}
            	}
             }//end of onClick()             
        });//end of Button_Read.setOnClickListener()        	
        
 
        /**********************************************************************
    	 *call do_exec() first to init Process_ExecCmd before creating child thread.
    	 */
		do_exec("ls " + logfile.getPath());       
        

    	/**********************************************************************
         *����Hander��
         *���� ��activity-basic�� �� �����������ݵ����̡߳� ֮���ͨ�š�
         *��Ϊ��Ҫ���ݷ������ݸ���ui����ui����activity�н�����������activity����, ������Ҫͨ�š�
         *FATAL EXCEPTION: Thread-374
         *android.view.ViewRootImpl$CalledFromWrongThreadException:
         *Only the original thread that created a view hierarchy can touch its views.
         */
        Handler_MsgOperate = new Handler() {
	        public void handleMessage(Message msg) {
	        	hanle_exec_output_parse_msg(msg);
	        }
        };        		
        

    	/**********************************************************************
         *�������̣߳����ڴ�stdout&stderr�ж�ȡ�ı����ݣ�������Ȼ����ݽ����������֪ͨactivity
         */
        new Thread(new Runnable() {
	        public void run() {
            while (boolean_CaptureOutputThreadRunnig) {
                try {
                	//polling space 200ms
                    Thread.currentThread();
					Thread.sleep(200);
					//
					exec_output_parse(Process_ExecCmd);	
                    //Log.d(TAG, "lost  time " + timer);
                } catch (InterruptedException e) {
					e.printStackTrace();
				}
            }}
	    }).start(); //�����߳�

    } /*end of onCreate()*****************************************************/

    
    private String read_cmd_get(){
        //Usage: i2cdump [-f] [-y] [-r first-last] I2CBUS ADDRESS [MODE [BANK [BANKREG]]]
    	//i2cdump -f -y -r first-last i2cbus address mode 
    	String dev = DevAddr;
		String mode = DevMode;
    	String cmd = Activity_TableLayout.I2CToolDir 
    			+ "i2cdump -f -y" + " " + dev + " " + mode;
    	return cmd;    	
    }
    
    
    private void thread_read_cycle(){
    	ThreadReadCycle = new Thread(new Runnable() {
        public void run() {
           	int readcycletime;
           	//Integer.valueOf("")�ᵼ�� 
    		//FATAL EXCEPTION: Thread-387
    		//java.lang.NumberFormatException: Invalid int: ""
        	String str_readcycletime = EditText_ReadCycleTime.getText().toString();
        	if (str_readcycletime.compareTo("") == 0){
        		readcycletime = 0;
        	} else {
        		readcycletime = Integer.valueOf(str_readcycletime);
        	}
        	if (readcycletime < 100){
        		readcycletime = 100;            		
        	}

        	String cmd = read_cmd_get();
        	while (boolean_LoopReadState){
				try {
					Thread.sleep(readcycletime);
                	do_exec(cmd);               		
				} catch (InterruptedException e) {
					//����thread.interrupt()�����catch (InterruptedException e)
					//ִ��e.printStackTrace(); ��System.err��Ϣ���߳�˯�ߡ�
					e.printStackTrace();
				}	
           	}//end of while(loop)        	        	
        }//end of run()
        });//end of new Thread()
    	ThreadReadCycle.start();    	
    }
    
    
    private void do_exec(String cmd) {
//    	WriteReadSDCard.writeToSDFile(logfile, PublicFunc.DateGet() + "\r\n" + cmd + "\r\n");
//    	WriteReadSDCard.writeToSDFile(logfile, "--------------------------------------------\r\n");
    	try {  
    		Process_ExecCmd = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }   
    }/*end of do_exec()*******************************************************/
    

	private void hanle_exec_output_parse_msg(Message msg){
		switch (msg.what) {
	    case 0:
	    	alc5625.ItemValCalc(I2CDumpValHexStr);
			TextView_Result.setText(alc5625.ItemTextviewRes());
    		WriteReadSDCard.writeToSDFile(logfile, alc5625.ItemLogVal());
    		break;
				    	
		default:
			break;
	    }		
	}/*end of hanle_exec_output_parse_msg()***********************************/
    
	
	private void exec_output_parse(Process p){
    	if (p == null){
        	System.out.println("p == null");
        	return;
        }

        //��ȡstdout, Ȼ�����
    	String stdoutStr = new String("");
		stdoutStr = ExecOutputParse.StdoutGet(p);
		if (stdoutStr.length() > 1){
	    	//i2cdump output parse
	    	if (true == ExecOutputParse.StdoutMatch_I2CDump(stdoutStr, I2CDumpValHexStr)){
		    	Message msg = new Message();
	            msg.what = 0;    		
	            System.out.println(stdoutStr);
		        Handler_MsgOperate.sendMessage(msg);	 
	    	}
    	}		
    }/*end of exec_output_parse()*********************************************/

	
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

    
/*    
   //TODO activity�����ڵ�ǰ״̬ʱ��˯�ߣ���2013��8��25�� 02:21
    //��ʱͨ��ģ������ť��������ٵ�ǰ״̬��readloop����ִ��
    protected void onPause(){
        super.onPause();
		if (boolean_LoopReadState == true){
    		Button_Read.performClick();	        			
		}
    }
*/    

}