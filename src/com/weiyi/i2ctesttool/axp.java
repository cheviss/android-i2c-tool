package com.weiyi.i2ctesttool;




public class axp
{
	private static String NameSpaceVal = new String(" ");
	private static String NewLine = new String("\r\n");
	private static String ReservePartName = new String("�������ɸ�");

	private static String reg00h(String hexval){	
		String res = new String("");
		String regNameStr = new String("");		
		int PARTTOTAL = 8;
		String[] PartNameStr = new String[PARTTOTAL];
		String[] PartValStr = new String[PARTTOTAL];
		
		regNameStr = "REG00H �����Դ״̬";
		PartNameStr[7] = "bit7 ACIN����(��/��)";
		PartNameStr[6] = "bit6 ACIN����(��/��)";
		PartNameStr[5] = "bit5 VBUS����(��/��)";
		PartNameStr[4] = "bit4 VBUS����(��/��)";
		PartNameStr[3] = "bit3 VBUS����ǰ����Vhold(��/��)";
		PartNameStr[2] = "bit2 ��س�� or ��طŵ�";
		PartNameStr[1] = "bit1 ACIN��VBUS��PCB�϶̽�(��/��)";
		PartNameStr[0] = "bit0 ������Դ��ACIN/VBUS(��/��)";
		
		for (int bitindex=0; bitindex<PARTTOTAL; bitindex++){
			PartValStr[bitindex] = PublicFunc.HexStrToBinStr(hexval, bitindex, bitindex);
		}
		
		res = regNameStr + NameSpaceVal + "0x" + hexval + NewLine;
		for (int i=PARTTOTAL-1; i>=0; i--){
			res += "    " + PartNameStr[i] + NameSpaceVal + "[" + PartValStr[i] + "]" + NewLine;
		}
		
		return res;
	}
	

	private static String reg01h(String hexval){	
		String res = new String("");
		String regNameStr = new String("");		
		int PARTTOTAL = 8;
		String[] PartNameStr = new String[PARTTOTAL];
		String[] PartValStr = new String[PARTTOTAL];
		
		regNameStr = "REG01H ��Դ����ģʽ�Լ����״ָ̬ʾ";
		PartNameStr[7] = "bit7 AXP209����(��/��)";
		PartNameStr[6] = "bit6 ���ڳ�� or δ�����������";
		PartNameStr[5] = "bit5 ��������ӵ�AXP209(��/��)";
		PartNameStr[4] = "bit4 " + ReservePartName;
		PartNameStr[3] = "bit3 ����ѽ��뼤��ģʽ(��/��)";
		PartNameStr[2] = "bit2 ������С����������(��/��)";
		PartNameStr[1] = "bit1 " + ReservePartName;
		PartNameStr[0] = "bit0 " + ReservePartName;
		
		for (int bitindex=0; bitindex<PARTTOTAL; bitindex++){
			PartValStr[bitindex] = PublicFunc.HexStrToBinStr(hexval, bitindex, bitindex);
		}

		res = regNameStr + NameSpaceVal + "0x" + hexval + NewLine;
		for (int i=PARTTOTAL-1; i>=0; i--){
			res += "    " + PartNameStr[i] + NameSpaceVal + "[" + PartValStr[i] + "]" + NewLine;
		}
		
		return res;
	}
	
	
	private static String reg02h(String hexval){	
		String res = new String("");
		String regNameStr = new String("");		
		int PARTTOTAL = 8;
		String[] PartNameStr = new String[PARTTOTAL];
		String[] PartValStr = new String[PARTTOTAL];
		
		regNameStr = "REG02H USB OTG VBUS ״ָ̬ʾ";
		PartNameStr[7] = "bit7 " + ReservePartName;
		PartNameStr[6] = "bit6 " + ReservePartName;
		PartNameStr[5] = "bit5 " + ReservePartName;
		PartNameStr[4] = "bit4 " + ReservePartName;
		PartNameStr[3] = "bit3 " + ReservePartName;
		PartNameStr[2] = "bit2 VBUS��Ч(��/��)";
		PartNameStr[1] = "bit1 VBUS Session A/B��Ч(��/��)";
		PartNameStr[0] = "bit1 Session End״̬��Ч(��/��)";
		
		for (int bitindex=0; bitindex<PARTTOTAL; bitindex++){
			PartValStr[bitindex] = PublicFunc.HexStrToBinStr(hexval, bitindex, bitindex);
		}

		res = regNameStr + NameSpaceVal + "0x" + hexval + NewLine;
		for (int i=PARTTOTAL-1; i>=0; i--){
			res += "    " + PartNameStr[i] + NameSpaceVal + "[" + PartValStr[i] + "]" + NewLine;
		}
		
		return res;
	}
	

	private static String reg30h(String hexval){	
		String res = new String("");
		String regNameStr = new String("");		
		int PARTTOTAL = 5;
		String[] PartNameStr = new String[PARTTOTAL];
		String[] PartValStr = new String[PARTTOTAL];
		
		regNameStr = "REG30H VBUS-IPSOUTͨ·����";
		PartNameStr[4] = "bit7 VBUS����ʱVBUS-IPSOUTͨ·ѡ������ź�";
		PartNameStr[3] = "bit6 VBUS Vhold��ѹ����(��/��)";
		PartNameStr[2] = "bit5:3 Vhold/V=4.0+bit*0.1";
		PartNameStr[1] = "bit2 " + ReservePartName;
		PartNameStr[0] = "bit1:0 VBUS�������ƴ�ʱ";
		
		PartValStr[4] = PublicFunc.HexStrToBinStr(hexval, 7, 7);
		PartValStr[3] = PublicFunc.HexStrToBinStr(hexval, 6, 6);
		PartValStr[2] = PublicFunc.HexStrToBinStr(hexval, 5, 3);
		PartValStr[2] = PartValStr[2] + " " + String.valueOf(Integer.valueOf(PartValStr[2], 2) * 0.1 + 4);
		PartValStr[1] = PublicFunc.HexStrToBinStr(hexval, 2, 2);
		PartValStr[0] = PublicFunc.HexStrToBinStr(hexval, 1, 0);

		if (Integer.valueOf(PartValStr[4], 2) == 1){
			PartNameStr[4] = "bit7 VBUS-IPSOUT���Ա�ѡ��򿪣�����N_VBUSEN pin��״̬ ";
		} else if (Integer.valueOf(PartValStr[4], 2) == 0){
			PartNameStr[4] = "bit7 VBUS-IPSOUT��N_VBUSEN pin�� ";			
		}
		
		if (Integer.valueOf(PartValStr[0], 2) == 0){
			PartNameStr[0] += " 900mA";
		} else if (Integer.valueOf(PartValStr[0], 2) == 1){
			PartNameStr[0] += " 500mA";
		} else if (Integer.valueOf(PartValStr[0], 2) == 2){
			PartNameStr[0] += " 100mA";			
		} else if (Integer.valueOf(PartValStr[0], 2) == 3){
			PartNameStr[0] += " not limit";
		}
		
		res = regNameStr + NameSpaceVal + "0x" + hexval + NewLine;
		for (int i=PARTTOTAL-1; i>=0; i--){
			res += "    " + PartNameStr[i] + NameSpaceVal + "[" + PartValStr[i] + "]" + NewLine;
		}
		
		return res;
	}
	

	private static String reg31h(String hexval){	
		String res = new String("");
		String regNameStr = new String("");		
		int PARTTOTAL = 6;
		String[] PartNameStr = new String[PARTTOTAL];
		String[] PartValStr = new String[PARTTOTAL];
		
		regNameStr = "REG31H Voff�ػ���ѹ���� ";
		PartNameStr[5] = "bit7 " + ReservePartName;
		PartNameStr[4] = "bit6 " + ReservePartName;
		PartNameStr[3] = "bit5 " + ReservePartName;
		PartNameStr[2] = "bit4 " + ReservePartName;
		PartNameStr[1] = "bit3 Sleep��PEK��GPIO����ʹ��(��/��)";
		PartNameStr[0] = "bit2:0 Voff/V=2.6+bit*0.1";
		
		PartValStr[5] = PublicFunc.HexStrToBinStr(hexval, 7, 7);
		PartValStr[4] = PublicFunc.HexStrToBinStr(hexval, 6, 6);
		PartValStr[3] = PublicFunc.HexStrToBinStr(hexval, 5, 5);
		PartValStr[2] = PublicFunc.HexStrToBinStr(hexval, 4, 4);		
		PartValStr[1] = PublicFunc.HexStrToBinStr(hexval, 3, 3);
		PartValStr[0] = PublicFunc.HexStrToBinStr(hexval, 2, 0);
		PartValStr[0] = PartValStr[0] + " "+ String.valueOf(Integer.valueOf(PartValStr[0], 2) * 0.1 + 2.6);
		
		res = regNameStr + NameSpaceVal + "0x" + hexval + NewLine;
		for (int i=PARTTOTAL-1; i>=0; i--){
			res += "    " + PartNameStr[i] + NameSpaceVal + "[" + PartValStr[i] + "]" + NewLine;
		}
		
		return res;
	}
	
	
	private static String reg32h(String hexval){	
		String res = new String("");
		String regNameStr = new String("");		
		int PARTTOTAL = 6;
		String[] PartNameStr = new String[PARTTOTAL];
		String[] PartValStr = new String[PARTTOTAL];
		
		regNameStr = "REG32H�ػ����á���ؼ���Լ�CHGLED�ܽſ��� ";
		PartNameStr[5] = "bit7 �ر�AXP209�����(��/��)";
		PartNameStr[4] = "bit6 ʹ�ܵ�ؼ�⹦��(��/��)";
		PartNameStr[3] = "bit5:4 CHGLED�ܽŹ�������";
		PartNameStr[2] = "bit3 CHGLED�ܽſ�������";
		PartNameStr[1] = "bit2 ����ر�ʱ�����";
		PartNameStr[0] = "bit1:0 N_OE�ͱ�ߺ�AXP209�ػ��ӳ�ʱ��";
		
		PartValStr[5] = PublicFunc.HexStrToBinStr(hexval, 7, 7);
		PartValStr[4] = PublicFunc.HexStrToBinStr(hexval, 6, 6);
		PartValStr[3] = PublicFunc.HexStrToBinStr(hexval, 5, 4);
		PartValStr[2] = PublicFunc.HexStrToBinStr(hexval, 3, 3);
		PartValStr[1] = PublicFunc.HexStrToBinStr(hexval, 2, 2);
		PartValStr[0] = PublicFunc.HexStrToBinStr(hexval, 1, 0);

		int index = 3;
		if (Integer.valueOf(PartValStr[index], 2) == 0){
			PartNameStr[index] += " ����";
		} else if (Integer.valueOf(PartValStr[index], 2) == 1){
			PartNameStr[index] += " 25% 1Hz��˸";
		} else if (Integer.valueOf(PartValStr[index], 2) == 2){
			PartNameStr[index] += " 25% 4Hz��˸";			
		} else if (Integer.valueOf(PartValStr[index], 2) == 3){
			PartNameStr[index] += " ����͵�ƽ";
		}

		index = 2;
		if (Integer.valueOf(PartValStr[index], 2) == 1){
			PartNameStr[index] += " �ɼĴ��� REG32H[5:4] ����";
		} else if (Integer.valueOf(PartValStr[index], 2) == 0){
			PartNameStr[index] += " �ɳ�繦�ܿ���";			
		}
		
		index = 1;
		if (Integer.valueOf(PartValStr[index], 2) == 1){
			PartNameStr[index] += " ������ʱ���෴";
		} else if (Integer.valueOf(PartValStr[index], 2) == 0){
			PartNameStr[index] += " ͬʱ�ر�";			
		}
		
		index = 0;
		if (Integer.valueOf(PartValStr[index], 2) == 0){
			PartNameStr[index] += " 128ms";
		} else if (Integer.valueOf(PartValStr[index], 2) == 1){
			PartNameStr[index] += " 1s";
		} else if (Integer.valueOf(PartValStr[index], 2) == 2){
			PartNameStr[index] += " 2s";			
		} else if (Integer.valueOf(PartValStr[index], 2) == 3){
			PartNameStr[index] += " 3s";
		}
		
		res = regNameStr + NameSpaceVal + "0x" + hexval + NewLine;
		for (int i=PARTTOTAL-1; i>=0; i--){
			res += "    " + PartNameStr[i] + NameSpaceVal + "[" + PartValStr[i] + "]" + NewLine;
		}
		
		return res;
	}
	
	/**************************************************************************
	 * ������Ĵ�������
	 */
	public static String CtrlReg(String[] valhexstr){
		String res = new String("");
		res += reg00h(valhexstr[0x00]) + "\r\n"
				+ reg01h(valhexstr[0x01]) + "\r\n"
				+ reg02h(valhexstr[0x02]) + "\r\n"
				+ reg30h(valhexstr[0x30]) + "\r\n"
				+ reg31h(valhexstr[0x31]) + "\r\n"
				+ reg32h(valhexstr[0x32]);
		return res;
	}
	
	
	

	//��һ���ֽں���һ���ֽڵĵ�4λ ��ϳ�12λ�Ķ������ַ�����
	//���� 56[7:0],57[3:0]��index��56
	//58[7:0],59[3:0]
	//7A[7:0],7B[3:0]
	public static String ByteH70_ByteL30(String[] valhexstr, int index){
		String res = new String("");
		String Hbyte = valhexstr[index];
		String Lbyte = valhexstr[index+1];
	
		//java.lang.NumberFormatException: Invalid int: "  "
		//if (Hbyte.compareTo("  ") == 0)
		//	Hbyte = "00";
		//if (Lbyte.compareTo("  ") == 0)
		//	Lbyte = "00";
		
		res = PublicFunc.HexStrToBinStr(Hbyte, 7, 0) +
				PublicFunc.HexStrToBinStr(Lbyte, 3, 0);
		return res;
	}
	
	/**************************************************************************
	 * ״̬��Ĵ�������
	 */
	private static String statusItemLogFormat = new String("%-14s");

 	private static StatusItemClass[] statusItemArray = {
		new StatusItemBatt(),
		
		new StatusItemAcinU(),
		new StatusItemAcinI(),
		
		new StatusItemVbusU(),
		new StatusItemVbusI(),
		
		new StatusItemAxp209T(),
		
		new StatusItemTsU(),
		new StatusItemNtcR(),
		new StatusItemBattT(),
		
		new StatusItemBattU(),
		new StatusItemBattCha(),
		new StatusItemBattDisCha(),
		
		new StatusItemIpsoutU(),
	};	
 	
 	//����StatusBattPercentClass��Ϊ���ⲿ���õ�ص������¶�
 	public static StatusItemClass StatusBattPercentClass = statusItemArray[0];
 	public static StatusItemClass StatusItemBattTClass = statusItemArray[8];

 	
 	
	//����״̬�Ĵ�����ֵ,
	public static void statusItemValCalc(String[] hexStrArray){
		for (int i=0; i<statusItemArray.length; i++){
			statusItemArray[i].valCalc(hexStrArray);
		}		
	}

	//��logfile��д��״̬�Ĵ���statusItem�����֣���������ĸ�ʽ������ַ���
	//���ֽ��ڳ�ʼ��ʱд��һ��
	public static String statusItemLogNames(){	
		String namelog = new String("");
		namelog += String.format(statusItemLogFormat, "Time");
		namelog += String.format(statusItemLogFormat, "SysBatt%");
		for (int i=0; i<statusItemArray.length; i++){
			namelog += String.format(statusItemLogFormat, statusItemArray[i].itemName);
		}
		namelog += "\r\n";		
		return namelog;
	}
	
	//��logfile��д��״̬�Ĵ�����ֵ�����������ʽ������ַ���
	public static String statusItemLogVal(){	
		String vallog = new String("");
		vallog += String.format(statusItemLogFormat, PublicFunc.TimeGet());
		vallog += String.format(statusItemLogFormat, Activity_axpmfd.String_battPer);
		for (int i=0; i<statusItemArray.length; i++){
			vallog += String.format(statusItemLogFormat, statusItemArray[i].itemStrVal);
		}
		vallog += "\r\n";		
		return vallog;
	}
	
/*
	//��textviewResult��д��״̬�Ĵ����Ľ����������һ�������ʽ������ַ���
	public static String statusItemTextviewRes(){	
		String textviewRes = new String("");
		for (int i=0; i<statusItemArray.length; i++){
			textviewRes += String.format("%-16s%s\r\n", 
					statusItemArray[i].itemName, statusItemArray[i].itemStrVal);
		}
		textviewRes += "\r\n";		
		return textviewRes;
	} 
 */	
	
	//��textviewResult��д��״̬�Ĵ����Ľ����������һ�������ʽ������ַ���
	public static String statusItemTextviewRes(){	
		String textviewRes = new String("");

		textviewRes += String.format("%-14s%s\r\n",
				"SysBatt%", Activity_axpmfd.String_battPer);
		
		int i=0;
		textviewRes += String.format("%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal);
		i++;
	
		textviewRes += String.format("%-14s%-8s%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal,
				statusItemArray[i+1].itemName, statusItemArray[i+1].itemStrVal);
		i += 2;
		
		textviewRes += String.format("%-14s%-8s%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal,
				statusItemArray[i+1].itemName, statusItemArray[i+1].itemStrVal);
		i += 2;

		textviewRes += String.format("%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal);
		i++;

		textviewRes += String.format("%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal);
		i++;

		textviewRes += String.format("%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal);
		i++;
		
		textviewRes += String.format("%-14s%-8s%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal,
				statusItemArray[i+1].itemName, statusItemArray[i+1].itemStrVal);
		i += 2;

		textviewRes += String.format("%-14s%-8s%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal,
				statusItemArray[i+1].itemName, statusItemArray[i+1].itemStrVal);
		i += 2;

		textviewRes += String.format("%-14s%s\r\n", 
				statusItemArray[i].itemName, statusItemArray[i].itemStrVal);
		i++;
		
//		textviewRes += "\r\n";		
		return textviewRes;
	}

	
	//��textviewWarn��д��״̬�Ĵ����ĳ�����Χ�Ľ����������һ�������ʽ������ַ���
	public static String statusItemTextviewWarn(){	
		String textviewWarn = new String("");
		textviewWarn += PublicFunc.TimeGet() + "\r\n";
		for (int i=0; i<statusItemArray.length; i++){
			if ((statusItemArray[i].itemDoubleVal < statusItemArray[i].itemLowerBand) ||
				(statusItemArray[i].itemDoubleVal > statusItemArray[i].itemUpperBand)){
				textviewWarn += String.format("%-16s%s\r\n", 
						statusItemArray[i].itemName, statusItemArray[i].itemStrVal);				
				//textviewWarn += String.format("    [%s~%s]\r\n", 
				//		statusItemArray[i].itemLowerBand, statusItemArray[i].itemUpperBand);				

			}
		}
		textviewWarn += "\r\n";		
		return textviewWarn;
	}
}



/******************************************************************************
 * ������״̬�Ĵ������class����
 * ģ��c�еĽṹ�壬�Ա����������ͨ��forѭ������
 * ���ҽ�ϸ�ڣ������������Ƶȣ�������class�У�
 * �ο�think in java #9.1, 9.2
 */
abstract class StatusItemClass {
	//��Ŀ�����ƣ�ֵ��������(��Щ���ǹ̶�ֵ����ʼ����Ͳ�Ҫ����)
	String itemName;
	double itemLowerBand;
	double itemUpperBand;
	
	//�����������Ҫ��ʾ��ʮ������ʵֵ
	String itemStrVal;
	//����Ŀ�����ж��Ƿ񳬳���Χ
	double itemDoubleVal;
	
	//��i2c���������л�ȡ����Ŀ���ֽ�������
	//ĳЩ��Ŀ��һ���ֽ�, ĳЩ��Ŀ���ֽڵ�ƴ��, 
	//������ƴ�ӳɶ������ַ����洢�Ϻ���,
	abstract double valCalc(String[] hexStrArray);	
}


//name				range		address				val
//Batt % 			0~100 		B9					ԭʼֵ
class StatusItemBatt extends StatusItemClass{
	StatusItemBatt(){
		itemName = "Batt %";
		itemLowerBand = 0;
		itemUpperBand = 100;
	}
	double valCalc(String[] hexStrArray){
		int valInt = Integer.valueOf(hexStrArray[0xb9], 16);
		itemStrVal = String.valueOf(String.format("%d", valInt));
		itemDoubleVal = (double)valInt;
		return itemDoubleVal;		
	}
}

//ACIN U/V			4.4~5.25	56[7:0],57[3:0]		*1.7/1000			
class StatusItemAcinU extends StatusItemClass{
	StatusItemAcinU(){
		itemName = "ACIN U/V";
		itemLowerBand = 4.4;
		itemUpperBand = 5.25;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x56);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 1.7 / 1000;
		itemStrVal = String.valueOf(String.format("%.3f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//ACIN I/mA		0~800		58[7:0],59[3:0]		*0.625
class StatusItemAcinI extends StatusItemClass{
	StatusItemAcinI(){
		itemName = "ACIN I/mA";
		itemLowerBand = 0;
		itemUpperBand = 800;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x58);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 0.625;
		itemStrVal = String.valueOf(String.format("%.3f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//VBUS U/V			4.4~5.25	5A[7:0],5B[3:0]		*1.7/1000
class StatusItemVbusU extends StatusItemClass{
	StatusItemVbusU(){
		itemName = "VBUS U/V";
		itemLowerBand = 4.4;
		itemUpperBand = 5.25;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x5a);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 1.7 / 1000;
		itemStrVal = String.valueOf(String.format("%.3f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//VBUS I/mA		0~500		5C[7:0],5D[3:0]		*0.375
class StatusItemVbusI extends StatusItemClass{
	StatusItemVbusI(){
		itemName = "VBUS I/mA";
		itemLowerBand = 0;
		itemUpperBand = 500;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x5c);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 0.375;
		itemStrVal = String.valueOf(String.format("%.3f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//AXP209 T/��		-40~130		5E[7:0],5F[3:0]		*0.1-144.7	
class StatusItemAxp209T extends StatusItemClass{
	StatusItemAxp209T(){
		itemName = "AXP209 T/��C";
		itemLowerBand = -40;
		itemUpperBand = 130;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x5e);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 0.1 - 144.7;
		itemStrVal = String.valueOf(String.format("%.1f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//TS U/mV						62[7:0],63[3:0]		*0.8
class StatusItemTsU extends StatusItemClass{
	StatusItemTsU(){
		itemName = "TS U/mV";
		itemLowerBand = 0;
		itemUpperBand = 0xfff;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x62);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 0.8;
		itemStrVal = String.valueOf(String.format("%.1f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//NTC R/K��			0.4519~263						TS/40-10
class StatusItemNtcR extends StatusItemClass{
	StatusItemNtcR(){
		itemName = "NTC R/K��";
		itemLowerBand = 0.4519;
		itemUpperBand = 263;
	}
	double valCalc(String[] hexStrArray){
		//ͨ��TS U����
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x62);
		double ts = Integer.valueOf(valBinStr, 2) * 0.8;
		itemDoubleVal = ts / 40 - 10;		
		itemStrVal = String.valueOf(String.format("%.4f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//Battery T/��										���
class StatusItemBattT extends StatusItemClass{
	StatusItemBattT(){
		itemName = "Batt T/��C";
		itemLowerBand = -20;
		itemUpperBand = 45;
	}
	double valCalc(String[] hexStrArray){
		//ͨ��NTC���
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x62);
		double ntc = Integer.valueOf(valBinStr, 2) * 0.8 / 40 - 10;
		itemDoubleVal = NtcToBattT(ntc);
		itemStrVal = String.valueOf(String.format("%.0f", itemDoubleVal));
		return itemDoubleVal;		
	}

	double NtcToBattT(double ntc){
		int index;
		double battT;
		//130*2, [][0]��ʾ�¶�, [0][1]��ʾntc��ֵ, ��ֵ��������
		double lookuptab[][] = {
			{-40, 228.2376},	{-39, 214.8696},	{-38, 202.3826},	{-37, 190.7126},	{-36, 179.8005},
			{-35, 169.5919}, 	{-34, 160.0366}, 	{-33, 151.0884},	{-32, 142.7046}, 	{-31, 134.8459},
			{-30, 127.4759},	{-29, 120.5608}, 	{-28, 114.0696}, 	{-27, 107.9735}, 	{-26, 102.2459},
			{-25, 96.8620}, 	{-24, 91.7990},		{-23, 87.0357}, 	{-22, 82.5523}, 	{-21, 78.3306},			
			{-20, 74.3538},		{-19, 70.6058}, 	{-18, 67.0723}, 	{-17, 63.7394}, 	{-16, 60.5946},
			{-15, 57.6261}, 	{-14, 54.8228},		{-13, 52.1745}, 	{-12, 49.6717}, 	{-11, 47.3056},		
			{-10, 45.0676},		{-9, 42.9503}, 		{-8, 40.9462}, 		{-7, 39.0487}, 		{-6, 37.2514},
			{-5, 35.5484}, 		{-4, 33.9342},		{-3, 32.4037}, 		{-2, 30.9520}, 		{-1, 29.5745},		
			{0, 28.2671},		{1, 27.0257}, 		{2, 25.8466}, 		{3, 24.7264}, 		{4, 23.6617},
			{5, 22.6495}, 		{6, 21.6869},		{7, 20.7711}, 		{8, 19.8996}, 		{9, 19.0700},
			{10, 18.2801},		{11, 17.5276}, 		{12, 16.8108}, 		{13, 16.1275},		{14, 15.4762},
			{15, 14.8550}, 		{16, 14.2625},		{17, 13.6972}, 		{18, 13.1576}, 		{19, 12.6425},
			{20, 12.1505},		{21, 11.6806}, 		{22, 11.2316}, 		{23, 10.8025}, 		{24, 10.3923},
			{25, 10.0000}, 		{26, 9.6248},		{27, 9.2658}, 		{28, 8.9223}, 		{29, 8.5934},
			{30, 8.2786},		{31, 7.9770}, 		{32, 7.6882}, 		{33, 7.4114}, 		{34, 7.1461},
			{35, 6.8919}, 		{36, 6.6480},		{37, 6.4142}, 		{38, 6.1899}, 		{39, 5.9746},
			{40, 5.7680},		{41, 5.5697}, 		{42, 5.3793}, 		{43, 5.1964}, 		{44, 5.0208},
			{45, 4.8520}, 		{46, 4.6898},		{47, 4.5339}, 		{48, 4.3840}, 		{49, 4.2398},
			{50, 4.1012},		{51, 3.9678}, 		{52, 3.8395}, 		{53, 3.7160}, 		{54, 3.5971},
			{55, 3.4826}, 		{56, 3.3724},		{57, 3.2662}, 		{58, 3.1639}, 		{59, 3.0654},
			{60, 2.9704},		{61, 2.8788}, 		{62, 2.7905}, 		{63, 2.7054}, 		{64, 2.6233},
			{65, 2.5442},		{66, 2.4678},		{67, 2.3940}, 		{68, 2.3229}, 		{69, 2.2542},
			{70, 2.1879},		{71, 2.1239}, 		{72, 2.0620}, 		{73, 2.0023}, 		{74, 1.9446},
			{75, 1.8888}, 		{76, 1.8349},		{77, 1.7828}, 		{78, 1.7324}, 		{79, 1.6837},
			{80, 1.6366},		{81, 1.5910}, 		{82, 1.5469}, 		{83, 1.5043}, 		{84, 1.4630},
			{85, 1.4231}, 		{86, 1.3844},		{87, 1.3470}, 		{88, 1.3107}, 		{89, 1.2756},
		};

		index = 0;
		if (ntc > lookuptab[index][1]){
			battT = lookuptab[index][0];
			return battT;
		}

		index = lookuptab.length-1;
		if (ntc < lookuptab[index][1]){
			battT = lookuptab[index][0];
			return battT;
		}

		//TODO 2013-09-03��Ҫ �޸�Ϊ���ٲ��
		for (index=0; index<lookuptab.length; index++){
			if (ntc < lookuptab[index][1]){
				continue;
			}
			if (ntc < ((lookuptab[index-1][1] + lookuptab[index][1]) / 2)){
				battT = lookuptab[index][0];
			} else {
				battT = lookuptab[index-1][0];				
			}
			return battT;
		}
		
		return 0;
	}	
}

//Battery U/V		3~4.2		78[7:0],79[3:0]		*1.1/1000
class StatusItemBattU extends StatusItemClass{
	StatusItemBattU(){
		itemName = "Batt U/V";
		itemLowerBand = 3;
		itemUpperBand = 4.2;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x78);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 1.1 / 1000;
		itemStrVal = String.valueOf(String.format("%.2f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//Batt Cha I/mA	0~500		7A[7:0],7B[3:0]		*0.5
class StatusItemBattCha extends StatusItemClass{
	StatusItemBattCha(){
		itemName = "Batt Cha I/mA";
		itemLowerBand = 0;
		itemUpperBand = 500;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x7a);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 0.5;
		itemStrVal = String.valueOf(String.format("%.2f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//Batt DiscI/mA	0~1000		7C[7:0],7D[3:0]		*0.5
class StatusItemBattDisCha extends StatusItemClass{
	StatusItemBattDisCha(){
		itemName = "Batt Dis I/mA";
		itemLowerBand = 0;
		itemUpperBand = 1000;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x7c);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 0.5;
		itemStrVal = String.valueOf(String.format("%.2f", itemDoubleVal));
		return itemDoubleVal;		
	}
}

//IPSOUT U/V		3.4~5.25	7E[7:0],7F[3:0]		*1.4/1000
class StatusItemIpsoutU extends StatusItemClass{
	StatusItemIpsoutU(){
		itemName = "IPSOUT U/V";
		itemLowerBand = 3.4;
		itemUpperBand = 5.25;
	}
	double valCalc(String[] hexStrArray){
		String valBinStr = axp.ByteH70_ByteL30(hexStrArray, 0x7e);
		itemDoubleVal = Integer.valueOf(valBinStr, 2) * 1.4 / 1000;
		itemStrVal = String.valueOf(String.format("%.3f", itemDoubleVal));
		return itemDoubleVal;		
	}
}