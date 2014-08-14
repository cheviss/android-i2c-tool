package com.weiyi.i2ctesttool;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_axpmfd extends Activity {

    //logfile
    private static File File_axp;	
    private static File File_axpWarn;	
    public static File File_battery;

    //pe30, smartGPS V2 board i2c chip axp address.
    public static String DevAddr = new String("0 0x34");
    
	/*
	 *���������б�ѡ��ĳһ�result textview�о���ʾ�����Ӧ�ļĴ���
	 */
    private Spinner Spinner_RegDisp;   
    private ArrayAdapter<String> Adapter_RegDisp;   
    private List<String> ListStr_RegDisp = new ArrayList<String>();   
    //0��ʾstatus��1��ʾctrl
    private long flag_RegDisp = 0;
    //
    
	private EditText EditText_ReadCycleTime;
	
	private Button Button_Read;
	private Button Button_BattGraph;
	private TextView TextView_Result;
	private TextView TextView_Warn;

	private String[] I2CDumpValHexStr = new String[256];
	
	/*
	 *���̣߳�ִ������������̴߳��������ݺ������̷߳��͵���Ϣ��
	 */
	private Process Process_ExecCmd;
    private boolean boolean_CaptureOutputThreadRunnig = true;
    private Handler Handler_MsgOperate;
   
	private Handler Handler_batt = new Handler();
	public static String String_battPer = new String("");
    

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
        setContentView(R.layout.layout_axpmfd);   
    
        /**********************************************************************
         * log �½�activity-basic logfile, ���û��д�룬��sdcard����Ȼ������
         */
    	File_axp = WriteReadSDCard.CreateFile(Activity_TableLayout.I2CLogDirFile,
    			"i2clog-axp-" + PublicFunc.DateGet() + ".txt");

    	File_axpWarn = WriteReadSDCard.CreateFile(Activity_TableLayout.I2CLogDirFile,
    			"i2clog-axpwarn.txt");
    	
    	File_battery = WriteReadSDCard.CreateFile(Activity_TableLayout.I2CLogDirFile,
    			"i2clog-battery.txt");
    	
    	WriteReadSDCard.writeToSDFile(File_axp, axp.statusItemLogNames());
 
    	//��ʼ��i2cdump output�洢���飬��ʽΪ16�����ַ���
    	for (int i=0; i<I2CDumpValHexStr.length; i++){
    		I2CDumpValHexStr[i] = "00";
    	}
    	
    	/**********************************************************************
    	 * ��ʼ��activity-basic UI
    	 */
    	/*
    	 *����spinner���洢i2c����ģʽ���洢��ʽ i2cbus address������ 0 0x34 
    	 *���������б����������ѡ�������б�����������ʽ������������������б�
    	 *���������б�ѡ��ʱ����Ӧ
    	 */
		ListStr_RegDisp.add("Status Reg");		
		ListStr_RegDisp.add("Ctrl Reg");
        Spinner_RegDisp = (Spinner)findViewById(R.id.idSpinnerRegDisp);   
        Adapter_RegDisp = new ArrayAdapter<String>(this,
        					android.R.layout.simple_spinner_item, ListStr_RegDisp);   
        Adapter_RegDisp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
        Spinner_RegDisp.setAdapter(Adapter_RegDisp);   
        Spinner_RegDisp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){   
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {   
                parent.setVisibility(View.VISIBLE); 
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(24);
                flag_RegDisp = id;
            }   
			public void onNothingSelected(AdapterView parent) {   
                parent.setVisibility(View.VISIBLE);   
            }   
        }); 
             
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
        do_exec("ls " + File_axp.getPath());       
        

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


    	/**********************************************************************
         *��ȡϵͳ���� settings->battery������ε�Դʱ��֪ͨ����ʾ
         *����axp.java�У��ѽ��д��textview��logfile��
         */
    	BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
    	    @Override
    	    public void onReceive(final Context context, Intent intent) {
    	        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
    	        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
    	        int percent = (level*100)/scale;

    	        String_battPer = String.valueOf(percent);
    	        Handler_batt.post( new Runnable() {
    	            public void run() {
    	                Toast.makeText(context, "System Battery="+String_battPer+"%", Toast.LENGTH_SHORT).show();
    	            }
    	        });
    	    }
    	};
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatInfoReceiver, filter);

        
    	/**********************************************************************
         *�������̣߳�ÿ2min��¼axp������ϵͳ����
         */
        new Thread(new Runnable() {
	        public void run() {
			while (true) {
                try {
		    		Thread.currentThread();
					do_exec(read_cmd_get());
					//��ʱ2sȷ�����ص������ѱ�����
					Thread.sleep(2000);							    		
		    		WriteReadSDCard.writeToSDFile(File_battery, statusBatteryPercent());
					Thread.sleep(2*60*1000);
                } catch (InterruptedException e) {
					e.printStackTrace();
				}
            }}
	    }).start(); //�����߳�   
        
    	/**********************************************************************
    	 *��ť�¼���������, ���°�ť�󣬻�����ͼ 
    	 */
        Button_BattGraph = (Button) findViewById(R.id.idButtonBattGraph);          
        Button_BattGraph.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {
            	if (File_battery.exists() == true){
                	drawGraph();            		
            	}
             }//end of onClick()             
        });//end of Button_Read.setOnClickListener()        	
 
    } /*end of onCreate()*****************************************************/

    
    private String read_cmd_get(){
        //Usage: i2cdump [-f] [-y] [-r first-last] I2CBUS ADDRESS [MODE [BANK [BANKREG]]]
    	//i2cdump -f -y -r first-last i2cbus address mode 
    	String dev = DevAddr;
    	String mode = "b";
    	String cmd = Activity_TableLayout.I2CToolDir 
    			+ "i2cdump -f -y" + " " + dev + " " + mode;
    	return cmd;    	
    }
    
    public static String screen_ctrl(boolean val){
    	String dev = DevAddr;
    	String mode = "b";
    	String value = new String("");
    	if (true == val){
    		value = "0x01 ";
    	} else {
    		value = "0x00 ";	
    	}
    	String cmd = Activity_TableLayout.I2CToolDir 
    			+ "i2cset -f -y" + " " + dev + " " + "0x90 " + value + mode;

    	System.out.println("screen_ctrl" + cmd);
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
//    	WriteReadSDCard.writeToSDFile(File_axp, PublicFunc.DateGet() + "\r\n" + cmd + "\r\n");
//    	WriteReadSDCard.writeToSDFile(File_axp, "--------------------------------------------\r\n");
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
    		axp.statusItemValCalc(I2CDumpValHexStr);
    		if (flag_RegDisp == 0){
    			String result = axp.statusItemTextviewRes();
    			String warn = axp.statusItemTextviewWarn();
    			TextView_Result.setText(result);
        		TextView_Warn.setText(warn);
        		WriteReadSDCard.writeToSDFile(File_axp, axp.statusItemLogVal());
        		WriteReadSDCard.writeToSDFile(File_axpWarn, warn);        		
    		} else if (flag_RegDisp == 1){
    			String ctrlReg = axp.CtrlReg(I2CDumpValHexStr);
    			TextView_Result.setText(ctrlReg);
    			TextView_Warn.setText("");
	    		WriteReadSDCard.writeToSDFile(File_axp, ctrlReg);
    		}
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
		        Handler_MsgOperate.sendMessage(msg);	 
	    	}
    	}		
    }/*end of exec_output_parse()*********************************************/

	
	//��������ͼ
    private void drawGraph()
    {
        Intent intent = new Intent(this, Activity_axpDrawGrap.class);
        startActivity(intent);
    }
    //��������ͼ��Ҫ�����ݣ�������ַ�������������������Ϊʱ�������	
	private static String statusBatteryPercent(){
		String batt = new String("");
		batt = String.format("%s,%s,%s,%s\r\n", 
					PublicFunc.DateGet(),
					axp.StatusBattPercentClass.itemStrVal, 
					String_battPer,
					axp.StatusItemBattTClass.itemStrVal
		);
		return batt;
	}
	
	
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