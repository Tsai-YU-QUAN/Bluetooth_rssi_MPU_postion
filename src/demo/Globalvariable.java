package demo;

import java.util.ArrayList;

public class Globalvariable {
//************************ For 展場自轉公轉
static String Tim_Down_Left[]={"89:73:ED","87:C1:F1","8B:C6:26"};
static String Tim_Down_Right[]={"89:73:ED","87:C1:F1","8B:C6:26"};




//************************* For 兩人自轉與公轉
//              *********** 以下為 Tim      *******************************      //
//static String Tim_Down_Left[]={"93:BE:A7","93:C3:E7","93:BE:9B",
//        "73:48:CC","75:EF:66","4E:C6:F9","55:B9:8A"};
//static String Tim_Down_Right[]={"93:C2:FB","93:C3:E7","93:BE:9B",
//         "73:48:CC","75:EF:66","4E:C6:F9","55:B9:8A"};

//static String Tim_Up_Left[]   ={"76:1E:30","74:56:6B","20:38:6E",
//         "73:48:CC","75:EF:66","4E:C6:F9","55:B9:8A"};
//static String Tim_Up_Right[]  ={"76:1E:30","74:56:6B","27:04:45",
//         "73:48:CC","75:EF:66","4E:C6:F9","55:B9:8A"};
//              ***************  以下為Jack     **************************       //
static String Jack_Down_Left[]={"93:C2:FB","93:BE:A7","93:BE:9B","20:38:6E",
         "75:EF:66","4E:C6:F9","55:B9:8A"};
static String Jack_Down_Right[]={"93:C2:FB","93:BE:A7","93:C3:E7","20:38:6E",
          "73:48:CC","4E:C6:F9","55:B9:8A"};
//static String Jack_Up_Left[]=  {"76:1E:30","74:56:6B","27:04:45","20:38:6E",
//        "73:48:CC","75:EF:66","55:B9:8A"};
//static String Jack_Up_Right[]= {"76:1E:30","74:56:6B","27:04:45","20:38:6E",
//         "73:48:CC","75:EF:66","4E:C6:F9"};


static boolean if_all_connect[]={false,false,false,false,false,false,false,false};
public static double User1offset_x=0.02;//====================================acc offset調整地方
public static double User1offset_y=0.08;
public static int TimYawOffset=471;     //=======================TimYawOffset調整地方

public static int first_time_slot=5000;
public static int time_slot=1000;
public static long starttime;
public static long endslotime;
public static int TimDownRight_yaw=0;
public static double FinalTim_yaw=0;
public static boolean if_statrtacc=false;
public static int CurrentState=1;      //=======================目前State
public static ArrayList<Double> AccStaterate = new ArrayList<Double>(); //目前此區間ACC的值，從index=1開始
public static ArrayList<Integer> RecordState = new ArrayList<Integer>();//記錄總共State變化
public static ArrayList<Double> User1Totalacc_x = new ArrayList<Double>();
public static ArrayList<Double> User1Totalacc_y = new ArrayList<Double>();
}
