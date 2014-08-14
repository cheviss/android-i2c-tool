//http://stackoverflow.com/questions/7358072/android-tabs-without-icons?rq=1
//http://joshclemm.com/blog/?p=136
//https://code.google.com/p/android-custom-tabs/

package com.weiyi.i2ctesttool;

import java.io.File;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;



public class Activity_TableLayout extends TabActivity {

	private TabHost mTabHost;

	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
	}
		
	public static String I2CToolDir = new String("/system/xbin/");
//	public static String I2CTestToolDir = new String("/mnt/external_sd/i2ctools/");

	public static String SDCardPath = new String("");
	public static File I2CLogDirFile;

	
	private String releaseNote = "update note (2013-10-22):\r\n"
							+ "svn84-117\r\n"
							+ "A ����alc5625 3���Ĵ���\r\n"
							+ "A ��Ļ���ȵ���\r\n"
							+ "A �����¶�����ͼ\r\n"
							+ "A 2min�������Ƶ�������ͼ������axp��ǩҳ����ã�\r\n"
							+ "A ��ȡϵͳ��������д��log;\r\n"
							+ "A ����alc5625\r\n"
							+ "A ��axp״̬�Ĵ���������Χʱ����ʾ��д��log;\r\n"
							+ "A ͨ�������ʾNTC�¶�;\r\n"
							+ "A ����appͼ��;\r\n"
							+ "M ɾ��si4709/axp��ַѡ���б�;\r\n"
							+ "M �޸�axp logfile�еļĴ�������;\r\n"
							+ "A ˯�ߺ�app��Ȼ����;\r\n"
							+ "A �������Ͻǲ˵�About;\r\n"
							+ "M ���/system/xbin/i2cdetect,i2cdump,i2cset������ʱ�˳�app."
	;
	
	
	
	/** Called when the activity is first created. */		  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// construct the tabhost
		setContentView(R.layout.layout_table);

		//����Ƿ���ڣ��������ڣ�ֱ���˳���
		if (false == checki2ctoolExist()){
			i2ctoolNoExist();
		}
		
		//��ʼ��app log�ļ��洢·��;
		SDCardPath = PublicFunc.getExternalSDCardPath();
		I2CLogDirFile = new File(SDCardPath + "/" +"i2clog");
		I2CLogDirFile.mkdirs();				
		
		setupTabHost();
		//mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		setupTab(new TextView(this), "basic", Activity_BasicOperate.class);
		setupTab(new TextView(this), "si4709", Activity_si4709.class);
		setupTab(new TextView(this), "axp", Activity_axpmfd.class);
		setupTab(new TextView(this), "alc5625", Activity_alc5625.class);
		setupTab(new TextView(this), "Display", Activity_ScreenCtrl.class);
	}

    
	private void setupTab(final View view, final String tag, Class cls) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		Intent intent = new Intent(this, cls);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	
	/**************************************************************************
	 * ���i2ctool�Ƿ���ڣ�
	 * ��һ�������ڣ�����Ȩ�޲��ԣ������Ի�����ʾ�û����û�ȷ�����˳�����
	 * Ȩ�޼���أ� TODO
	 */
	private static boolean checki2ctoolExist(){
		boolean i2cdetect = false;
		boolean i2cdump = false;
		boolean i2cset = false;
		
		String name = new String("");		
		File[] i2ctools = new File("/system/xbin/").listFiles();

		for (int i=0; i<i2ctools.length; i++){
			name = i2ctools[i].getName();
			if (name.compareTo("i2cdetect") == 0){
				i2cdetect = true;
			} else if (name.compareTo("i2cdump") == 0){
				i2cdump = true;
			} else if (name.compareTo("i2cset") == 0){
				i2cset = true;
			}
		}
		
		return (i2cdetect && i2cdump && i2cset);
	}
	
    private void i2ctoolNoExist(){
        new AlertDialog.Builder(this)
		.setTitle("Error: APP Will Closed!")
		.setMessage("Please check files exist or not:\r\n" +
				"	/system/xbin/i2cdetect\r\n" +
				"	/system/xbin/i2cdump\r\n" + 
				"	/system/xbin/i2cset")
		.setPositiveButton("Yes, I know.", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) { 
	        finish();
	        System.exit(0);    	
		}
		})
		.show();     	
    }
	
	
	/**************************************************************************
	 * �������ϽǵĲ˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.clear();
//��ʱ����log�ļ�·��ѡ��logɾ�����ڳ����й̶�Ϊ/mnt/external_sd/i2clog/
//		menu.add(0, MenuItemIDType.SelLogDir.getNumericType(), 0, "Select Logfile Directory");
//		menu.add(0, MenuItemIDType.DelLog.getNumericType(), 0, "Delete Logfile");
		menu.add(0, MenuItemIDType.About.getNumericType(), 0, "About");
		menu.add(0, MenuItemIDType.Close.getNumericType(), 0, "Close");
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	int itemid = item.getItemId();
    	System.out.println(itemid);
    	
    	if (itemid == MenuItemIDType.DelLog.getNumericType()){			//ɾ��logfile��ʡ�ռ�
        	MenuItem_DelLog();           	    		
			return true;
    	} else if (itemid == MenuItemIDType.SelLogDir.getNumericType()){//ѡ��logfile�洢λ��
    		MenuItem_SelLogDir();
			return true;
    	} else if (itemid == MenuItemIDType.Close.getNumericType()){	//�ر�app
    		MenuItem_CloseApp();
			return true;
    	} else if (itemid == MenuItemIDType.About.getNumericType()){	//���ڣ���ʾ�洢·���汾��
    		MenuItem_About();
    		return true;
    	} else {
            return super.onOptionsItemSelected(item);	    		
    	}
    		
    }
    
    private enum MenuItemIDType
    {
        SelLogDir(1),
        DelLog(2),
        Close(3),
        About(4);

        MenuItemIDType (int i) { this.type = i; }

        private int type;
        public int getNumericType() { return type; }
    }
    
    private void MenuItem_SelLogDir(){
		CharSequence[] items = new CharSequence[2];
		items[0] = "internal SD, /mnt/sdcard/i2clog/";
		items[1] = "external SD, /mnt/external_sd/i2clog/";
    	
    	//����һ���б��û�ѡ��    	
		File[] mnt = new File("/mnt/").listFiles();
		for (int i=0; i<mnt.length; i++){
			if (mnt[i].getName().compareTo("sdcard") == 0){
				items[0] = "internal SD, /mnt/sdcard/i2clog/";
			} else if (mnt[i].getName().compareTo("external_sd") == 0){
				items[1] = "external SD, /mnt/external_sd/i2clog/";
			}
		}

		new AlertDialog.Builder(this)
		.setTitle("Select logfile directory")
		.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position
				// of the selected item
				System.out.println(which);
			}
		})
		.show();
    }
    
    private void MenuItem_DelLog(){
        new AlertDialog.Builder(this)
		.setTitle("Warn!")
		.setMessage("Are you sure want to delete logfile?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) { 
			// continue with delete
			if (I2CLogDirFile.isDirectory()) {
		        String[] children = I2CLogDirFile.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(I2CLogDirFile, children[i]).delete();
		        }
		    }
		}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) { 
			// do nothing
			}
		})
		.show();     	
    }
    
    private void MenuItem_CloseApp(){
		//ֱ�ӹرղ�����ʾ
        finish();
        System.exit(0);    	
    }
    
    private void MenuItem_About(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("About");
		builder.setMessage("logfile path:" + I2CLogDirFile.getPath() + "\r\n\r\n" + releaseNote);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();     	
		alertDialog.getWindow().setLayout(600, 400);
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


}
