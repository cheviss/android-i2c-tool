package com.weiyi.i2ctesttool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Activity_BasicOperate extends Activity {

    private File File_basic;	

    /*
	 *���������б��洢����i2c�豸��Ϣ
	 */
    private Spinner Spinner_I2CDevOnBoard;   
    private ArrayAdapter<String> Adapter_I2CDevOnBoard;   
	private List<String> ListStr_I2CBusOnBoard = new ArrayList<String>();
	//����i2c�豸�б�����Ϊpublic��������activity����
	public static List<String> ListStr_I2CDevOnBoard = new ArrayList<String>();
    //pe30 chip address.
    private String DevAddr = new String("");


	/*
	 *���������б��洢i2c����ģʽ
	 */
    private Spinner Spinner_I2CModes;   
    private ArrayAdapter<String> Adapter_I2CModes;   
    private List<String> ListStr_I2CModes = new ArrayList<String>();   
    
	private EditText EditText_ByteFirst;
	private EditText EditText_ByteLast;
	private EditText EditText_ReadCycleTime;
	private EditText EditText_ByteNo; 
	private EditText EditText_ByteVal; 
	
	private EditText EditText_Bus; 
	private EditText EditText_Addr; 

	
	private Button Button_Read;
	private TextView TextView_Result;

	private String[] I2CDumpValHexStr = new String[256];
	
	/*
	 *���̣߳�ִ������������̴߳��������ݺ������̷߳��͵���Ϣ��
	 */
	private Process Process_ExecCmd;
    private boolean boolean_CaptureOutputThreadRunnig = true;
    private Handler Handler_MsgOperate;
    
    /*
     *i2cdetect ̽�����i2c���߼��豸��Ҫ����ı��� 
     */
    //i2c bus number detect now ��ǰ̽������ߺ�
	private String Str_I2CBusDetecting = new String("");  
    //if current i2c device detect finish ��ǰ̽���Ƿ����
    private boolean boolean_I2CDetectCurFinish = false;
    //if all i2c device detect finish ����̽���Ƿ����
    private boolean boolean_I2CDetectAllFinish = false;

    /*
     *ѭ����ȡ������Ҫ����ı��� 
     *		[TODO bug20130828ע��]
			bug �������cycle read�� boolean_LoopReadState = true��
			��ʱ����cycletime=0������stop��Ч
			if(==true)���ڵ������ǣ����û��cycle����д���0ֵ��ʹboolean_LoopReadEnable=true��
			�����Ÿ�Ϊ0��preformClick()�������else����ִ��thread_read_cycle()��ȡedittextֵ��
			�յ�edittext ""ת int ʱ�ᵼ��
			FATAL EXCEPTION: Thread-387
			java.lang.NumberFormatException: Invalid int: ""
			���ԣ�ȫ�ֱ�־λ���׳����Ⱑ������
     */
    //ѭ��ʱ����Ϊ0��������ťֻ��һ��
    private boolean boolean_LoopReadEnable = false;
    //ѭ����ȡ�̣߳������ť��ʼѭ����ȡ���ٴε����ť����ѭ����ȡ
    private Thread ThreadReadCycle;
    private boolean boolean_LoopReadState = false;
    
    private Thread tempButtonClick;
    
	/************************************************************************** 
	 * Called when the activity is first created. 
	 */  
    public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.layout_basic);   
    
        /**********************************************************************
         * log �½�activity-basic logfile
         */
//    	File_basic = WriteReadSDCard.CreateFile(Activity_TableLayout.I2CLogDirFile,
//    			"i2clog-basic-" + PublicFunc.DateGet() + ".txt");

    	File_basic = WriteReadSDCard.CreateFile(Activity_TableLayout.I2CLogDirFile,
    			"i2clog-basic.txt");

    	
    	//��ʼ��i2cdump output�洢���飬��ʽΪ16�����ַ���
    	for (int i=0; i<I2CDumpValHexStr.length; i++){
    		I2CDumpValHexStr[i] = "00";
    	}
    	
    	
    	/**********************************************************************
    	 * ��ʼ��activity-basic UI
    	 */
    	/*
    	 *����spinner���洢����i2c�豸��Ϣ���洢��ʽ i2cbus address������ 0 0x34 
    	 *���������б����������ѡ�������б�����������ʽ������������������б����������б�ѡ��ʱ����Ӧ
    	 */
        Spinner_I2CDevOnBoard = (Spinner)findViewById(R.id.idSpinnerI2CDevOnBoard);   
        Adapter_I2CDevOnBoard = new ArrayAdapter<String>(this,
        					android.R.layout.simple_spinner_item, ListStr_I2CDevOnBoard);   
        Adapter_I2CDevOnBoard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
        Spinner_I2CDevOnBoard.setAdapter(Adapter_I2CDevOnBoard);     
        Spinner_I2CDevOnBoard.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){   
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {   
                parent.setVisibility(View.VISIBLE); 
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(24);
                DevAddr = Spinner_I2CDevOnBoard.getSelectedItem().toString();
            }   
			public void onNothingSelected(AdapterView parent) {   
                parent.setVisibility(View.VISIBLE);   
            }   
        });  
        
    	/*
    	 *����spinner���洢i2c����ģʽ���洢��ʽ i2cbus address������ 0 0x34 
    	 *���������б����������ѡ�������б�����������ʽ������������������б����������б�ѡ��ʱ����Ӧ
    	 */
		ListStr_I2CModes.add("b (byte, default)");
		//ListStr_I2CModes.add("w (word)");
		ListStr_I2CModes.add("W (word on even register addresses)");
		ListStr_I2CModes.add("s (SMBus block)");
		ListStr_I2CModes.add("i (I2C block)");
		ListStr_I2CModes.add("c (consecutive byte)");		
        Spinner_I2CModes = (Spinner)findViewById(R.id.idSpinnerI2CModes);   
        Adapter_I2CModes = new ArrayAdapter<String>(this,
        					android.R.layout.simple_spinner_item, ListStr_I2CModes);   
        Adapter_I2CModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
        Spinner_I2CModes.setAdapter(Adapter_I2CModes);   
        Spinner_I2CModes.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){   
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {   
                parent.setVisibility(View.VISIBLE); 
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(24);
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
    	
    	/*
    	 *����edittext, ��ȡread/set�Ĳ�������
    	 */
    	EditText_ByteFirst = (EditText) findViewById(R.id.ideditTextByteFirst);
    	EditText_ByteLast = (EditText) findViewById(R.id.ideditTextByteLast);
    	EditText_ReadCycleTime = (EditText) findViewById(R.id.ideditTextCycleTime);
    	EditText_ByteNo = (EditText) findViewById(R.id.idEditTextByteNo);
    	EditText_ByteVal = (EditText) findViewById(R.id.idEditTextByteVal);   

    	EditText_Bus = (EditText) findViewById(R.id.idEditTextBus);   
    	EditText_Addr = (EditText) findViewById(R.id.idEditTextAddr);   

    	
    	//bus & addr
    	EditText_Bus.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable s) {
	        	DevAddr = EditText_Bus.getText().toString() + " 0x" + EditText_Addr.getText().toString();
	        }
	
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
	
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
    	});//endof EditText_Bus.addTextChangedListener()

    	EditText_Addr.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable s) {
	        	DevAddr = EditText_Bus.getText().toString() + " 0x" + EditText_Addr.getText().toString();
	        }
	
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
	
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
    	});//endof EditText_Bus.addTextChangedListener()
    	
    	
    	/*
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
	        		//TODO ���ȫ�ֱ�־�ķ��� �ǳ���Ҫ [bug20130828ע��]
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
        
    	/*
    	 *read button listener ��ť�¼��������� 
    	 */
        Button_Read = (Button) findViewById(R.id.idButtonRead);          
        Button_Read.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {
            	if (boolean_LoopReadEnable == false){
            		//ֻ��һ��
                	String cmd = new String("/system/xbin/i2cdump -f -y 0 0x34");
                	do_exec(cmd);
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
        
    	/*
    	 *set button listener ��ť�¼��������� 
    	 */
        Button ButtonSet_ls = (Button) findViewById(R.id.idbuttonSet);          
        ButtonSet_ls.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {
            	//String dev = Spinner_I2CDevOnBoard.getSelectedItem().toString();
            	String dev = DevAddr;
            	String mode = String.valueOf((Spinner_I2CModes.getSelectedItem().toString()).charAt(0)).toLowerCase();
            	String cmd = Activity_TableLayout.I2CToolDir + "i2cset -f -y " + dev + " "
            			+ "0x" + EditText_ByteNo.getText().toString() + " "           			
            			+ "0x" + EditText_ByteVal.getText().toString() + " "
            			+ mode;
            	do_exec(cmd);
            	//������ɺ�������
    			TextView_Result.setText("");            	
            }             
        });//end of Button_Set.setOnClickListener()           	        
 		
        
        /**********************************************************************
         *activity-basic �򿪽׶�̽�����i2c�豸 
         *call do_exec() first to init Process_ExecCmd before creating child thread.
         */
        do_exec(Activity_TableLayout.I2CToolDir + "i2cdetect -l");      

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

        //test
        String hexstr = new String("ff");
        int byteint = Integer.valueOf(hexstr, 16);        
        System.out.println(String.format("%s 0x%2x %d", "String(ff) to int", byteint, byteint));
        
    } /*end of onCreate()*****************************************************/

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
    
    private String read_cmd_get(){
        //Usage: i2cdump [-f] [-y] [-r first-last] I2CBUS ADDRESS [MODE [BANK [BANKREG]]]
    	//i2cdump -f -y -r first-last i2cbus address mode 
    	//first��lastδ��дʱĬ�� 00 �� ff
    	String fisrtbyte = new String("");
    	fisrtbyte = EditText_ByteFirst.getText().toString();
    	if (fisrtbyte.compareTo("") == 0){
    		fisrtbyte = "00";        		
    	}       	
    	
    	String lastbyte = new String("");
    	lastbyte = EditText_ByteLast.getText().toString();
    	if (lastbyte.compareTo("") == 0){
    		lastbyte = "FF";        		
    	}      	
    	
    	//String dev = Spinner_I2CDevOnBoard.getSelectedItem().toString();
    	String dev = DevAddr;
    	
    	String mode = String.valueOf((Spinner_I2CModes.getSelectedItem().toString()).charAt(0));

    	//Range parameter not compatible with i&s mode
    	String range = new String("");
    	if ((mode.compareTo("i") != 0) && (mode.compareTo("s") != 0)){
    		range = " -r" + " " + "0x" + fisrtbyte + "-" + "0x" + lastbyte;
    	}
    	
    	String cmd = Activity_TableLayout.I2CToolDir + "i2cdump -f -y" + range + " " + dev + " " + mode;
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
//    	WriteReadSDCard.writeToSDFile(File_basic, "\r\n" + PublicFunc.DateGet() + "\r\n" + cmd + "\r\n");
    	try {  
    		Process_ExecCmd = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {  
            e.printStackTrace();  
        }   
    }/*end of do_exec()*******************************************************/
    

	private void hanle_exec_output_parse_msg(Message msg){
		switch (msg.what) {
	    case 0:
	    	//display stderr & stdout message.
			TextView_Result.setText((String) msg.obj);
			break;
	    	
	    case 1:
	    	if (true == i2c_dev_detect()){
	        	//̽�����������ߺ��������û�д������˳��������������б�spinner;	
	    		Adapter_I2CDevOnBoard.notifyDataSetChanged();
	    		
//	    		WriteReadSDCard.writeToSDFile(File_basic, "i2cdev list:\r\n");
	        	String i2cdevall = new String("");		
	    		for (int i=0; i<ListStr_I2CDevOnBoard.size(); i++){
//	    			WriteReadSDCard.writeToSDFile(File_basic, ListStr_I2CDevOnBoard.get(i) + "\r\n");
	    			i2cdevall += ListStr_I2CDevOnBoard.get(i) + "\r\n";
	    		}
				TextView_Result.setText(i2cdevall);
				//���̽��ɹ������ֹbus/addr eiittext������
				EditText_Bus.setFocusable(false);
				EditText_Addr.setFocusable(false);
	    	} else {
	    		//̽��ʧ�ܣ������б�ѡ���豸�޷�ʹ�ã�ֻ��ͨ��edittext����
				TextView_Result.setText("detect failed!");
				//
				Spinner_I2CDevOnBoard.setFocusable(false);
				Spinner_I2CDevOnBoard.setFocusableInTouchMode(false);
	    	}
	    	
			//�ñ�־λ���ڷ���stdout����ʱ���ų�i2c detect������������͵ķ������Լӿ�����ٶȡ�
			boolean_I2CDetectAllFinish = true;	

	        Button_Read.performClick();	//2013-11-07 14:36		

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
        
        /* ��ȡstderr, Ȼ����� */
    	String stderrStr = new String("");		
    	stderrStr = ExecOutputParse.StderrGet(p);		
		if (stderrStr.length() > 1){
//			WriteReadSDCard.writeToSDFile(File_basic, stderrStr + "\r\n");			
			if (true == ExecOutputParse.StderrMatch(stderrStr)){
				//send message to inform main thread change UI.
				Message msg = new Message();
				msg.obj = stderrStr;
		        msg.what = 0;
		        Handler_MsgOperate.sendMessage(msg);				
			}
    	}/*end of stderr parse*/	

        /* ��ȡstdout, Ȼ����� */
    	String stdoutStr = new String("");
		stdoutStr = ExecOutputParse.StdoutGet(p);
		if (stdoutStr.length() > 1){
//			WriteReadSDCard.writeToSDFile(File_basic, stdoutStr + "\r\n");
	    	Message msg = new Message();
	    	// i2c�豸̽�������tool�򿪵ĳ�ʼ�׶���ɣ����Դ˺��ڷ���output����ʱ��
	        // �ų�i2c detect bus/dev ������������͵ķ������Լӿ�����ٶȡ�
	        if (boolean_I2CDetectAllFinish == false){
		    	// i2cdetect to identify id-0, id-1, id-2, ...;
	           	if (true == ExecOutputParse.StdoutMatch_I2CBusDetect(stdoutStr, ListStr_I2CBusOnBoard)){ 
	           		//send message to inform main thread change UI.
	        		msg.obj = stdoutStr;
	        		msg.what = 1;
	        		Handler_MsgOperate.sendMessage(msg);
	        	// i2cdetect to identify i2c devices address.
	        	} else if (true == ExecOutputParse.StdoutMatch_I2CDevDetect(stdoutStr, 
	        								ListStr_I2CDevOnBoard, Str_I2CBusDetecting)){
	           		boolean_I2CDetectCurFinish = true;
	        		//msg.obj = s;
	        		//msg.what = 3;   
	        	}  
	        } else {
				WriteReadSDCard.writeToSDFile(File_basic, stdoutStr + "\r\n");
				//todo
				
		    	//i2cdump output parse
		    	if (true == ExecOutputParse.StdoutMatch_I2CDump(stdoutStr, I2CDumpValHexStr)){
		        	msg.obj = stdoutStr;
		            msg.what = 0;    		
			        Handler_MsgOperate.sendMessage(msg);	 
		    	}
		    	//other stdout message to display.		        	
	        } 	
    	}/*end of stdout parse*/		
    }/*end of exec_output_parse()*********************************************/

    
    private boolean i2c_dev_detect(){
    	//we have detected several i2c bus, then execute i2c device detect command,
    	if (ListStr_I2CBusOnBoard.size() < 0){
    		return false;
    	}
    	
    	for (int i=0; i<ListStr_I2CBusOnBoard.size(); i++){
    		//200ms is short for i2cdetect -y bus
			Str_I2CBusDetecting = ListStr_I2CBusOnBoard.get(i);
			do_exec(Activity_TableLayout.I2CToolDir + "i2cdetect -y " + Str_I2CBusDetecting);
			//Ϊ�˱�֤�����뷵�����ݶ�Ӧ�����ñ�־λfalse�����ӳ��´������ִ�У�
			//�ϴ�����ķ���������ȷ�������ñ�־λtrueʹ���´��������ִ�С�
			boolean_I2CDetectCurFinish = false;
			int timeoutCtr = 0;
    		while (boolean_I2CDetectCurFinish == false){
            	//todo �����־λδ����true����������while�У���ʱ�˳���ӡ������Ϣ.
				try {
                    Thread.currentThread();
					Thread.sleep(100);
					timeoutCtr++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (timeoutCtr > 100){
					return false;
				}
        	}
    	}
    	
        return true;	
    }/*end of i2c_dev_detect()************************************************/
    	

	
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