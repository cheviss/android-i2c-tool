package com.weiyi.i2ctesttool;



public class alc5625
{
	private static String ItemLogFormat = new String("%-10s");

	private static int ItemTotal = 20;
	
 	public static ItemClass[] ItemArray = new ItemClass[ItemTotal];	
 	
 	public static void ItemArrayInit(){
 		int index;
 		for (index=0; index<ItemTotal; index++){
 			ItemArray[index] = new ItemClass();
 		}
 		
 		index = 0;
 		ItemArray[index].itemName = "REG02";
 		ItemArray[index].index = 2;
 		index++;
 		ItemArray[index].itemName = "REG04";
 		ItemArray[index].index = 4;
 		index++;
 		ItemArray[index].itemName = "REG06";
 		ItemArray[index].index = 6;
 		index++;
 		ItemArray[index].itemName = "REG0A";
 		ItemArray[index].index = 0xa;
 		index++;
 		ItemArray[index].itemName = "REG0C";
 		ItemArray[index].index = 0xc;
 		index++;
 		ItemArray[index].itemName = "REG0E";
 		ItemArray[index].index = 0xe;
 		index++;
 		ItemArray[index].itemName = "REG10";
 		ItemArray[index].index = 0x10;
 		index++;
 		ItemArray[index].itemName = "REG12";
 		ItemArray[index].index = 0x12;
 		index++;
 		ItemArray[index].itemName = "REG14";
 		ItemArray[index].index = 0x14;
 		index++;
 		ItemArray[index].itemName = "REG18";
 		ItemArray[index].index = 0x18;
 		index++;
 		ItemArray[index].itemName = "REG1A";
 		ItemArray[index].index = 0x1a;
 		index++;
 		ItemArray[index].itemName = "REG1C";
 		ItemArray[index].index = 0x1c;
 		index++;
 		ItemArray[index].itemName = "REG1E";
 		ItemArray[index].index = 0x1e;
 		index++;
 		ItemArray[index].itemName = "REG22";
 		ItemArray[index].index = 0x22;
 		index++;
 		ItemArray[index].itemName = "REG2E";
 		ItemArray[index].index = 0x2e;
 		index++;
 		ItemArray[index].itemName = "REG34";
 		ItemArray[index].index = 0x34;
 		index++;
 		ItemArray[index].itemName = "REG36";
 		ItemArray[index].index = 0x36;
 		index++;
 		ItemArray[index].itemName = "REG3A";
 		ItemArray[index].index = 0x3a;
 		index++;
 		ItemArray[index].itemName = "REG3C";
 		ItemArray[index].index = 0x3c;
 		index++;
 		ItemArray[index].itemName = "REG3E";
 		ItemArray[index].index = 0x3e;
 	}
 	
	//����״̬�Ĵ�����ֵ,
	public static void ItemValCalc(String[] hexStrArray){
		for (int i=0; i<ItemArray.length; i++){
			ItemArray[i].valCalc(hexStrArray, ItemArray[i].index);
		}		
	}

	//��logfile��д��״̬�Ĵ���Item�����֣���������ĸ�ʽ������ַ���
	//���ֽ��ڳ�ʼ��ʱд��һ��
	public static String ItemLogNames(){	
		String namelog = new String("");
		namelog += String.format(ItemLogFormat, "Time");
		for (int i=0; i<ItemArray.length; i++){
			namelog += String.format(ItemLogFormat, ItemArray[i].itemName);
		}
		namelog += "\r\n";		
		return namelog;
	}
	
	//��logfile��д��״̬�Ĵ�����ֵ�����������ʽ������ַ���
	public static String ItemLogVal(){	
		String vallog = new String("");
		vallog += String.format(ItemLogFormat, PublicFunc.TimeGet());
		for (int i=0; i<ItemArray.length; i++){
			vallog += String.format(ItemLogFormat, ItemArray[i].itemStrVal);
		}
		vallog += "\r\n";		
		return vallog;
	}
	
	//��textviewResult��д��״̬�Ĵ����Ľ����������һ�������ʽ������ַ���
	public static String ItemTextviewRes(){	
		String textviewRes = new String("");
		for (int i=0; i<ItemArray.length; i++){
			textviewRes += String.format("%-10s%s\r\n", 
					ItemArray[i].itemName, ItemArray[i].itemStrVal);
		}
		textviewRes += "\r\n";		
		return textviewRes;
	}

}



/******************************************************************************
 * ������״̬�Ĵ������class����
 * ģ��c�еĽṹ�壬�Ա����������ͨ��forѭ������
 * ���ҽ�ϸ�ڣ������������Ƶȣ�������class�У�
 * �ο�think in java #9.1, 9.2
 */
class ItemClass {
	String itemName;	//����
	int index;			//���ֽ����е����
	String itemStrVal;	//ʮ�������ַ���ֵ
	String valCalc(String[] hexStrArray, int index){
		itemStrVal = hexStrArray[index] + hexStrArray[index+1];
		return itemStrVal;
	}
}
