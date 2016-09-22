package demo;

import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

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
public static double User1offset_x=0.01;//====================================acc offset調整地方
public static double User1offset_y=0.06;
public static int TimYawOffset=387+51+10;     //=======================TimYawOffset調整地方，只能用加 Ex:298+"62"=360
public static int User1YawThrshold=5;  //=======================調整User1Yaw

public static int first_time_slot=5000;
public static int time_slot=1000;
public static long starttime;
public static long endslotime;
public static int TimDownRight_yaw=0;
public static double User1Rad_yaw=0;
public static boolean if_statrtacc=false;//=====================與RSSI時間做同步
public static int CurrentState=5;      //=======================目前State
public static ArrayList<Double> AccStaterate = new ArrayList<Double>(); //目前此區間State的ACC機率，從index=1開始
public static ArrayList<Integer> RecordState = new ArrayList<Integer>();//從一開始到結束，記錄總共State變化
public static ArrayList<Double> User1TotalRad_yaw = new ArrayList<Double>();

public static ArrayList<Integer> RecordUser1Totalyaw = new ArrayList<Integer>();//從一開始到結束，記錄總共User1Yaw變化
public static ArrayList<Integer> RecordUser2Totalyaw = new ArrayList<Integer>();//從一開始到結束，記錄總共User2Yaw變化

public static ArrayList<Integer> User1Totalyaw = new ArrayList<Integer>();//記錄此區間User1Yaw變化
public static ArrayList<Integer> User2Totalyaw = new ArrayList<Integer>();//記錄此區間User2Yaw變化
public static ArrayList<Double> User1Totalacc_x = new ArrayList<Double>();
public static ArrayList<Double> User1Totalacc_y = new ArrayList<Double>();
public static ArrayList<Double> User1Postiveacc_x = new ArrayList<Double>();
public static ArrayList<Double> User1Postiveacc_y = new ArrayList<Double>();
public static ArrayList<Double> User1MinMaxacc = new ArrayList<Double>();//只有超出threshold才能算
public static ArrayList<Double> User2MinMaxacc = new ArrayList<Double>();//只有超出threshold才能算
public static ArrayList<Double> User1Work_array_x = new ArrayList<Double>();
public static ArrayList<Double> User1Work_array_y = new ArrayList<Double>();

public static double User1Work_x=0.0,User1Work_y=0.0;                    //得到可行的x,y在做方向性
public static int AvgUser1Totalyaw;                                      //得到Avg angle的Yaw
public static double AvgUser1Rad_yaw=0;                                  //得到Avg Rad yaw
public static boolean if_initTx=false, if_initTy=false;                  //為了得到目前這一秒方向性的x,y
public static boolean if_U1acccompute_x=true;                               //是否要算加速度的方向
public static boolean if_U1acccompute_y=true;                               //是否要算加速度的方向
public static boolean if_rotation=false;
public static boolean if_firstx=true, if_first_y=true;
public static boolean if_operateACC=false;
public static String AccX_PNegative,AccY_PNegative;                      //為了再某段時間一開始就確定


}
