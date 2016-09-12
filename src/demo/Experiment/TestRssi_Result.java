//**********6.16 早上測試4個State，但只用到四個裝置
//*********7.4開始用8顆寫互相定位(自+公) Front=>左 Behind=>右
package demo.Experiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import demo.Globalvariable;





public class TestRssi_Result {
	   static Channel channel;
       static Connection connection;
	   static Channel channel_publish;
	   String Prestate="", Currentstate="3";
	   String text_name="公轉270_n=10_自轉0",time_name="公轉270_n=10_自轉0時間";
	   double User1LeftRight_scale=0.0,User1FrontBehind_scale=0.0,User2LeftRight_scale=0.0,User2FrontBehind_scale=0.0;
	   static long currenttime,startAlgotime,endAlgotime,decisiontime,endslotime;
	   static int OK_times=0, fail_times;
	   int slide_windows=3, min_window=3,confidence_window=3;
	   double Real_final_User2DownRight_User2DownLeft=100;     //100CM
	   double final_User1DownLeft_User1DownRight,final_User1DownFront_User1DownBehind;
	   static int count=0;    //for realtime compute
	   static int end_slot=60000;
	   static int Distance_count=0;
	   static boolean if_first_time_slot=false;
	   static String distanceString0="",distanceString1="",distanceString2="",distanceString3="",
			         distanceString4="",distanceString5="",distanceString6="",distanceString7="";

	   static int rssi_count=1;
	   static HSSFWorkbook workbook_rssi = new HSSFWorkbook();
	   static HSSFSheet sheet_rssi = workbook_rssi.createSheet("twelve_rssi");
	   static FileOutputStream fileOut;
	   static String filename_rssi;;
	   static Row row;
	   static String finaljson;
	   
	   private static final String TOPIC_location = "wise.position";    
	   private static final String TOPIC_location2 = "wise.position2";  // 測試用
	   private static boolean if_init=false;
	   private static boolean if_restartime=false;
	   
	   //Tim=user1   , Jack=user2   //8個節點  56條線
	   static ArrayList<ArrayList<Double>> Totoalline = new ArrayList<ArrayList<Double>>();
	   static ArrayList<ArrayList<Integer>> TimeTotoalline = new ArrayList<ArrayList<Integer>>();
	   static ArrayList<Double> RealTotoalline = new ArrayList<Double>();

	   static ArrayList<Double> Meanlinejudge = new ArrayList<Double>();
	   static ArrayList<Double> StringMeanlinejudge = new ArrayList<Double>(); //為了output.txt用

	   static ArrayList<Double> User1DownLeft_User1DownRight = new ArrayList<Double>(); //static ArrayList<Double> User1DownRight_User1DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User1DownLeft_User2DownLeft = new ArrayList<Double>();  static ArrayList<Double> User2DownLeft_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownLeft_User2DownRight = new ArrayList<Double>(); static ArrayList<Double> User2DownRight_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownLeft_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2DownFront_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownLeft_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownLeft = new ArrayList<Double>(); static ArrayList<Double> User2DownLeft_User1DownRight  = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownRight = new ArrayList<Double>();static ArrayList<Double> User2DownRight_User1DownRight = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownFront = new ArrayList<Double>();static ArrayList<Double> User2DownFront_User1DownRight = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User1DownRight = new ArrayList<Double>();

	   static ArrayList<Double> User1DownFront_User1DownLeft = new ArrayList<Double>(); //static ArrayList<Double> User1DownLeft_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User1DownRight = new ArrayList<Double>();//static ArrayList<Double> User1DownRight_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User1DownBehind = new ArrayList<Double>();//static ArrayList<Double> User1DownBehind_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownLeft = new ArrayList<Double>(); static ArrayList<Double> User2DownLeft_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownRight = new ArrayList<Double>(); static ArrayList<Double> User2DownRight_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2DownFront_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User1DownFront = new ArrayList<Double>();	   
	   static ArrayList<Double> User1DownBehind_User1DownLeft = new ArrayList<Double>(); static ArrayList<Double> User1DownLeft_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User1DownRight = new ArrayList<Double>();//static ArrayList<Double> User1DownRight_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownLeft = new ArrayList<Double>(); static ArrayList<Double> User2DownLeft_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownRight = new ArrayList<Double>();static ArrayList<Double> User2DownRight_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownFront = new ArrayList<Double>();static ArrayList<Double> User2DownFront_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User2DownLeft_User2DownFront = new ArrayList<Double>();   static ArrayList<Double> User2DownFront_User2DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownLeft_User2DownBehind = new ArrayList<Double>();  //static ArrayList<Double> User2DownBehind_User2DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownFront = new ArrayList<Double>();  //static ArrayList<Double> User2DownFront_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownBehind = new ArrayList<Double>(); //static ArrayList<Double> User2DownBehind_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownLeft = new ArrayList<Double>();   //static ArrayList<Double> User2DownLeft_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownFront_User2DownBehind = new ArrayList<Double>(); //static ArrayList<Double> User2DownBehind_User2DownFront = new ArrayList<Double>();
	   
	   static ArrayList<Integer> Time_User1DownLeft_User1DownRight = new ArrayList<Integer>();//static ArrayList<Integer> Time_User1DownRight_User1DownLeft = new ArrayList<Integer>();       //校正用
	   static ArrayList<Integer> Time_User1DownLeft_User2DownLeft = new ArrayList<Integer>(); static ArrayList<Integer> Time_User2DownLeft_User1DownLeft = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownLeft_User2DownRight = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownRight_User1DownLeft = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownLeft_User2DownFront = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownFront_User1DownLeft = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownLeft_User2DownBehind = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownBehind_User1DownLeft = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownRight_User2DownLeft = new ArrayList<Integer>(); static ArrayList<Integer> Time_User2DownLeft_User1DownRight  = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownRight_User2DownRight = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownRight_User1DownRight = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownRight_User2DownFront = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownFront_User1DownRight = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownRight_User2DownBehind = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownBehind_User1DownRight = new ArrayList<Integer>();

	   static ArrayList<Integer> Time_User1DownFront_User1DownLeft = new ArrayList<Integer>();  //static ArrayList<Integer> Time_User1DownLeft_User1DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownFront_User1DownRight = new ArrayList<Integer>(); //static ArrayList<Integer> Time_User1DownRight_User1DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownFront_User1DownBehind = new ArrayList<Integer>();//static ArrayList<Integer> Time_User1DownBehind_User1DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownFront_User2DownLeft = new ArrayList<Integer>();  static ArrayList<Integer> Time_User2DownLeft_User1DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownFront_User2DownRight = new ArrayList<Integer>(); static ArrayList<Integer> Time_User2DownRight_User1DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownFront_User2DownFront = new ArrayList<Integer>(); static ArrayList<Integer> Time_User2DownFront_User1DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownFront_User2DownBehind = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownBehind_User1DownFront = new ArrayList<Integer>();	   
	   static ArrayList<Integer> Time_User1DownBehind_User1DownLeft = new ArrayList<Integer>(); //static ArrayList<Integer> Time_User1DownLeft_User1DownBehind = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownBehind_User1DownRight = new ArrayList<Integer>();//static ArrayList<Integer> Time_User1DownRight_User1DownBehind = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownBehind_User2DownLeft = new ArrayList<Integer>(); static ArrayList<Integer> Time_User2DownLeft_User1DownBehind = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownBehind_User2DownRight = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownRight_User1DownBehind = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownBehind_User2DownFront = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownFront_User1DownBehind = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User1DownBehind_User2DownBehind = new ArrayList<Integer>();static ArrayList<Integer> Time_User2DownBehind_User1DownBehind = new ArrayList<Integer>();
	   static ArrayList<Integer> Time_User2DownLeft_User2DownFront = new ArrayList<Integer>();   //static ArrayList<Integer> Time_User2DownFront_User2DownLeft = new ArrayList<Integer>();       //校正用
	   static ArrayList<Integer> Time_User2DownLeft_User2DownBehind = new ArrayList<Integer>();  //static ArrayList<Integer> Time_User2DownBehind_User2DownLeft = new ArrayList<Integer>();       //校正用
	   static ArrayList<Integer> Time_User2DownRight_User2DownFront = new ArrayList<Integer>();  //static ArrayList<Integer> Time_User2DownFront_User2DownRight = new ArrayList<Integer>();       //校正用
	   static ArrayList<Integer> Time_User2DownRight_User2DownBehind = new ArrayList<Integer>(); //static ArrayList<Integer> Time_User2DownBehind_User2DownRight = new ArrayList<Integer>();       //校正用
	   static ArrayList<Integer> Time_User2DownRight_User2DownLeft = new ArrayList<Integer>();   //static ArrayList<Integer> Time_User2DownLeft_User2DownRight = new ArrayList<Integer>();       //校正用
	   static ArrayList<Integer> Time_User2DownFront_User2DownBehind = new ArrayList<Integer>(); //static ArrayList<Integer> Time_User2DownBehind_User2DownFront = new ArrayList<Integer>();
	   static ArrayList<Integer> IndexList = new ArrayList<Integer>();
	   static ArrayList<Double> MaxavgConfindence = new ArrayList<Double>();
	   static double RealcC,RealcD,RealcA,RealcB;
	   static double RealdC,RealdD,RealdA,RealdB;
	   static double RealaC,RealaD,RealaA,RealaB;
	   static double RealbC,RealbD,RealbA,RealbB;
	   static double RealCc,RealCd,RealCa,RealCb;
	   static double RealDc,RealDd,RealDa,RealDb;
	   static double RealAc,RealAd,RealAa,RealAb;
	   static double RealBc,RealBd,RealBa,RealBb;


	   
	   //可以觀察用
	   static double Distance;
	    private String[] nodeNames={"Tim_Down_Front","Tim_Down_Behind","Tim_Down_Left","Tim_Down_Right",
                "Jack_Down_Front","Jack_Down_Behind","Jack_Down_Left","Jack_Down_Right"};       //Jack_Up_Left=>User2DownFront   Jack_Up_Right=>User2DownBehind
		static String nodeName;
	    String Clear_MAC;
	    String RSSI;
	    //**********************************為了擴大判斷與即時性********************************************//
	   static int User1Left_size,User1Right_size,User1Front_size,User1Behind_size;                  
	   static Double User1Left_Distance,User1Right_Distance,User1Front_Distance,User1Behind_Distance;
	   static int User1Left_count=0,User1Right_count=0,User1Front_count=0,User1Behind_count=0; 
	   
	  public  class Coordinate{
		   ArrayList<Double> xList = new ArrayList<Double>();
		   ArrayList<Double> zList = new ArrayList<Double>();
		   
	   };




	   
	   static ArrayList<Double> CoeffVarRank = new ArrayList<Double>();//大到小
	   double [] DistanceLine= new double[28];
	   static ArrayList<Double> Distance_compare= new ArrayList<Double>();
	   static ArrayList<Double> Measurepower = new ArrayList<Double>();//大到小
	   static ArrayList<Double> AVGConfindence = new ArrayList<Double>();
	   static ArrayList<Double> MeansquareState = new ArrayList<Double>();
	   static ArrayList<Double> RssiStaterate = new ArrayList<Double>();
	   static ArrayList<Double> FinalRssiStaterate = new ArrayList<Double>();
	   static ArrayList<Double> TotalStaterate = new ArrayList<Double>();
	   static ArrayList<Double> SortTotalStaterate = new ArrayList<Double>();
	   //static ArrayList<Double> SortMeansquareState = new ArrayList<Double>();
	   static ArrayList<Integer> U1Ox = new ArrayList<Integer>();
	   static ArrayList<Integer> U1Oz = new ArrayList<Integer>();
	   static int R1=15, R2=10; //=====================================人體裝置長度
	   static int compare_count=0;
	   static double TotalMeansquare=0.0,Rssirate=0.0;
	   static double[] Measurepower_state1357 = new double[32]; 		
	   //Coordinate coo_a;Coordinate coo_b;Coordinate coo_c;Coordinate coo_d;
	   //Coordinate coo_A;Coordinate coo_B;Coordinate coo_C;Coordinate coo_D;
		 
		 
		 
		

    public void Revolution_processing(String posedata)
    
    { 
    	try {
		   
    	if(if_init==false){   //先初始化，以後要判斷是哪一個state需要查表


    		//=================先讀取M.P
            try {
                FileReader fr=new FileReader("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/"+"AVGMeasurePower.txt");
                BufferedReader br=new BufferedReader(fr);
                String line;
                int i=0;
                while ((line=br.readLine()) != null) {
                	Measurepower_state1357[i]=Double.parseDouble(line);
                	i++;
                  }
                }
              catch (IOException e) {System.out.println(e);}
            //=================讀取C.V
            try {
                FileReader fr=new FileReader("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/"+"AVGConfindence.txt");
                BufferedReader br=new BufferedReader(fr);
                String line;
                while ((line=br.readLine()) != null) {
                	AVGConfindence.add(Double.parseDouble(line));
                  }
                }
              catch (IOException e) {System.out.println(e);}
          

    		/*Totoalline.add(User1DownFront_User2DownFront); //Totoalline.add(User2DownFront_User1DownFront);  //14
        	Totoalline.add(User1DownFront_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownFront);//15
        	Totoalline.add(User1DownFront_User2DownLeft);  //Totoalline.add(User2DownLeft_User1DownFront);    //12
        	Totoalline.add(User1DownFront_User2DownRight); //Totoalline.add(User2DownRight_User1DownFront);  //13
        	Totoalline.add(User1DownBehind_User2DownFront);//Totoalline.add(User2DownFront_User1DownBehind);//20
        	Totoalline.add(User1DownBehind_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownBehind);//21
        	Totoalline.add(User1DownBehind_User2DownLeft); //Totoalline.add(User2DownLeft_User1DownBehind);  //18
        	Totoalline.add(User1DownBehind_User2DownRight);//Totoalline.add(User2DownRight_User1DownBehind);//19
        	Totoalline.add(User1DownLeft_User2DownFront);//Totoalline.add(User2DownFront_User1DownLeft);   //3
        	Totoalline.add(User1DownLeft_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownLeft); //4
        	Totoalline.add(User1DownLeft_User2DownLeft);//Totoalline.add(User2DownLeft_User1DownLeft);     //1
        	Totoalline.add(User1DownLeft_User2DownRight);//Totoalline.add(User2DownRight_User1DownLeft);   //2
        	Totoalline.add(User1DownRight_User2DownFront);//Totoalline.add(User2DownFront_User1DownRight); //7
        	Totoalline.add(User1DownRight_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownRight);//8  
        	Totoalline.add(User1DownRight_User2DownLeft);//Totoalline.add(User2DownLeft_User1DownRight);   //5
        	Totoalline.add(User1DownRight_User2DownRight);//Totoalline.add(User2DownRight_User1DownRight); //6
      	
        //Totoalline.add(User1DownLeft_User1DownRight);//Totoalline.add(User1DownRight_User1DownLeft);   //0
    	//Totoalline.add(User1DownFront_User1DownLeft);  //Totoalline.add(User1DownLeft_User1DownFront);    //9
    	//Totoalline.add(User1DownFront_User1DownRight); //Totoalline.add(User1DownRight_User1DownFront);  //10
    	//Totoalline.add(User1DownFront_User1DownBehind);//Totoalline.add(User1DownBehind_User1DownFront);//11
    	//Totoalline.add(User1DownBehind_User1DownLeft); //Totoalline.add(User1DownLeft_User1DownBehind);  //16
    	//Totoalline.add(User1DownBehind_User1DownRight);//Totoalline.add(User1DownRight_User1DownBehind);//17
    	//Totoalline.add(User2DownLeft_User2DownFront);  //Totoalline.add(User2DownFront_User2DownLeft);      //22
    	//Totoalline.add(User2DownLeft_User2DownBehind); //Totoalline.add(User2DownBehind_User2DownLeft);    //23
    	//Totoalline.add(User2DownRight_User2DownFront); //Totoalline.add(User2DownFront_User2DownRight);     //24
    	//Totoalline.add(User2DownRight_User2DownBehind);//Totoalline.add(User2DownBehind_User2DownRight);  //25
    	//Totoalline.add(User2DownRight_User2DownLeft);  //Totoalline.add(User2DownLeft_User2DownRight);      //26
    	//Totoalline.add(User2DownFront_User2DownBehind);//Totoalline.add(User2DownBehind_User2DownFront);  //27
     
        	TimeTotoalline.add(Time_User1DownFront_User2DownFront); //Totoalline.add(User2DownFront_User1DownFront);  //14
        	TimeTotoalline.add(Time_User1DownFront_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownFront);//15
        	TimeTotoalline.add(Time_User1DownFront_User2DownLeft);  //Totoalline.add(User2DownLeft_User1DownFront);    //12
        	TimeTotoalline.add(Time_User1DownFront_User2DownRight); //Totoalline.add(User2DownRight_User1DownFront);  //13
        	TimeTotoalline.add(Time_User1DownBehind_User2DownFront);//Totoalline.add(User2DownFront_User1DownBehind);//20
        	TimeTotoalline.add(Time_User1DownBehind_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownBehind);//21
        	TimeTotoalline.add(Time_User1DownBehind_User2DownLeft); //Totoalline.add(User2DownLeft_User1DownBehind);  //18
        	TimeTotoalline.add(Time_User1DownBehind_User2DownRight);//Totoalline.add(User2DownRight_User1DownBehind);//19
           	TimeTotoalline.add(Time_User1DownLeft_User2DownFront);//Totoalline.add(User2DownFront_User1DownLeft);   //3
        	TimeTotoalline.add(Time_User1DownLeft_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownLeft); //4
           	TimeTotoalline.add(Time_User1DownLeft_User2DownLeft);//Totoalline.add(User2DownLeft_User1DownLeft);     //1
        	TimeTotoalline.add(Time_User1DownLeft_User2DownRight);//Totoalline.add(User2DownRight_User1DownLeft);   //2
        	TimeTotoalline.add(Time_User1DownRight_User2DownFront);//Totoalline.add(User2DownFront_User1DownRight); //7
        	TimeTotoalline.add(Time_User1DownRight_User2DownBehind);//Totoalline.add(User2DownBehind_User1DownRight);//8 
        	TimeTotoalline.add(Time_User1DownRight_User2DownLeft);//Totoalline.add(User2DownLeft_User1DownRight);   //5
        	TimeTotoalline.add(Time_User1DownRight_User2DownRight);//Totoalline.add(User2DownRight_User1DownRight); //6

    	//TimeTotoalline.add(Time_User1DownLeft_User1DownRight);//Totoalline.add(User1DownRight_User1DownLeft);   //0
 
    	//TimeTotoalline.add(Time_User1DownFront_User1DownLeft);  //Totoalline.add(User1DownLeft_User1DownFront);    //9
    	//TimeTotoalline.add(Time_User1DownFront_User1DownRight); //Totoalline.add(User1DownRight_User1DownFront);  //10
    	//TimeTotoalline.add(Time_User1DownFront_User1DownBehind);//Totoalline.add(User1DownBehind_User1DownFront);//11
    	//TimeTotoalline.add(Time_User1DownBehind_User1DownLeft); //Totoalline.add(User1DownLeft_User1DownBehind);  //16
    	//TimeTotoalline.add(Time_User1DownBehind_User1DownRight);//Totoalline.add(User1DownRight_User1DownBehind);//17
    	//TimeTotoalline.add(Time_User2DownLeft_User2DownFront);  //Totoalline.add(User2DownFront_User2DownLeft);      //22
    	//TimeTotoalline.add(Time_User2DownLeft_User2DownBehind); //Totoalline.add(User2DownBehind_User2DownLeft);    //23
    	//TimeTotoalline.add(Time_User2DownRight_User2DownFront); //Totoalline.add(User2DownFront_User2DownRight);     //24
    	//TimeTotoalline.add(Time_User2DownRight_User2DownBehind);//Totoalline.add(User2DownBehind_User2DownRight);  //25
    	//TimeTotoalline.add(Time_User2DownRight_User2DownLeft);  //Totoalline.add(User2DownLeft_User2DownRight);      //26
    	//TimeTotoalline.add(Time_User2DownFront_User2DownBehind);//Totoalline.add(User2DownBehind_User2DownFront);  //27
  */  	
        	Totoalline.add(User1DownFront_User2DownFront);Totoalline.add(User1DownFront_User2DownBehind);   //1
        	Totoalline.add(User1DownFront_User2DownLeft);Totoalline.add(User1DownFront_User2DownRight);     //2
        	Totoalline.add(User1DownBehind_User2DownFront);Totoalline.add(User1DownBehind_User2DownBehind); //3
        	Totoalline.add(User1DownBehind_User2DownLeft);Totoalline.add(User1DownBehind_User2DownRight);   //4
        	Totoalline.add(User1DownLeft_User2DownFront);Totoalline.add(User1DownLeft_User2DownBehind);     //5
        	Totoalline.add(User1DownLeft_User2DownLeft);Totoalline.add(User1DownLeft_User2DownRight);       //6
        	Totoalline.add(User1DownRight_User2DownFront);Totoalline.add(User1DownRight_User2DownBehind);   //7
        	Totoalline.add(User1DownRight_User2DownLeft);Totoalline.add(User1DownRight_User2DownRight);     //8
        	Totoalline.add(User2DownFront_User1DownFront);Totoalline.add(User2DownFront_User1DownBehind);   //9
        	Totoalline.add(User2DownFront_User1DownLeft);Totoalline.add(User2DownFront_User1DownRight);     //10
        	Totoalline.add(User2DownBehind_User1DownFront);Totoalline.add(User2DownBehind_User1DownBehind); //11
        	Totoalline.add(User2DownBehind_User1DownLeft);Totoalline.add(User2DownBehind_User1DownRight);   //12
        	Totoalline.add(User2DownLeft_User1DownFront);Totoalline.add(User2DownLeft_User1DownBehind);     //13
        	Totoalline.add(User2DownLeft_User1DownLeft);Totoalline.add(User2DownLeft_User1DownRight);       //14
        	Totoalline.add(User2DownRight_User1DownFront);Totoalline.add(User2DownRight_User1DownBehind);   //15
        	Totoalline.add(User2DownRight_User1DownLeft);Totoalline.add(User2DownRight_User1DownRight);    	//16
        	
        	TimeTotoalline.add(Time_User1DownFront_User2DownFront);TimeTotoalline.add(Time_User1DownFront_User2DownBehind);   //1
        	TimeTotoalline.add(Time_User1DownFront_User2DownLeft);TimeTotoalline.add(Time_User1DownFront_User2DownRight);     //2
        	TimeTotoalline.add(Time_User1DownBehind_User2DownFront);TimeTotoalline.add(Time_User1DownBehind_User2DownBehind); //3
        	TimeTotoalline.add(Time_User1DownBehind_User2DownLeft);TimeTotoalline.add(Time_User1DownBehind_User2DownRight);   //4
        	TimeTotoalline.add(Time_User1DownLeft_User2DownFront);TimeTotoalline.add(Time_User1DownLeft_User2DownBehind);     //5
        	TimeTotoalline.add(Time_User1DownLeft_User2DownLeft);TimeTotoalline.add(Time_User1DownLeft_User2DownRight);       //6
        	TimeTotoalline.add(Time_User1DownRight_User2DownFront);TimeTotoalline.add(Time_User1DownRight_User2DownBehind);   //7
        	TimeTotoalline.add(Time_User1DownRight_User2DownLeft);TimeTotoalline.add(Time_User1DownRight_User2DownRight);     //8
        	TimeTotoalline.add(Time_User2DownFront_User1DownFront);TimeTotoalline.add(Time_User2DownFront_User1DownBehind);   //9
        	TimeTotoalline.add(Time_User2DownFront_User1DownLeft);TimeTotoalline.add(Time_User2DownFront_User1DownRight);     //10
        	TimeTotoalline.add(Time_User2DownBehind_User1DownFront);TimeTotoalline.add(Time_User2DownBehind_User1DownBehind); //11
        	TimeTotoalline.add(Time_User2DownBehind_User1DownLeft);TimeTotoalline.add(Time_User2DownBehind_User1DownRight);   //12
        	TimeTotoalline.add(Time_User2DownLeft_User1DownFront);TimeTotoalline.add(Time_User2DownLeft_User1DownBehind);     //13
        	TimeTotoalline.add(Time_User2DownLeft_User1DownLeft);TimeTotoalline.add(Time_User2DownLeft_User1DownRight);       //14
        	TimeTotoalline.add(Time_User2DownRight_User1DownFront);TimeTotoalline.add(Time_User2DownRight_User1DownBehind);   //15
        	TimeTotoalline.add(Time_User2DownRight_User1DownLeft);TimeTotoalline.add(Time_User2DownRight_User1DownRight);    	//16

    	
    	
    	if_init=true;
    	}
    	try{
        ConnectionFactory factory = new ConnectionFactory();
        try {
			factory.setUri("amqp://admin:admin@127.0.0.1");
        	//factory.setUri("amqp://admin:admin@192.168.0.102");
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	    connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(TOPIC_location, "fanout");
        //****************************測試Java到Unity*********************//
		 //finaljson = "{" + "Position"+": { x:"+1+", y:"+0+", z:"+0+ "} }";
         //channel.basicPublish(TOPIC_location, "", null, finaljson.getBytes());
		 //finaljson = "{" + "Position"+": { x:"+-1+", y:"+0+", z:"+0+ "} }";
         //channel.basicPublish(TOPIC_location, "", null, finaljson.getBytes());
    	if(if_restartime==false){
        Globalvariable.starttime=System.currentTimeMillis();
        if_restartime=true;
    	}
    	
        
    	nodeName=posedata.substring(0, posedata.indexOf(":"));
    	Clear_MAC=posedata.substring(posedata.length()-11, posedata.length()-3);
    	RSSI=posedata.substring(posedata.length()-2, posedata.length());
    	System.out.println("posedata: "+nodeName+" "+Clear_MAC+" "+RSSI); 
    	//Globalvariable.starttime=1465827022898L;
    	
    	currenttime=System.currentTimeMillis();
    	if((currenttime-Globalvariable.starttime)<=(Globalvariable.time_slot*rssi_count)){
    	System.out.println("ExcelTime: "+(currenttime-Globalvariable.starttime));
		row = sheet_rssi.createRow((short)rssi_count);
		row.createCell(0).setCellValue(Globalvariable.time_slot*rssi_count);
    	}
    	else{
			row.createCell(1).setCellValue(distanceString0);row.createCell(2).setCellValue(distanceString1);
			row.createCell(3).setCellValue(distanceString2);row.createCell(4).setCellValue(distanceString3);
			row.createCell(5).setCellValue(distanceString4);row.createCell(6).setCellValue(distanceString5);
			row.createCell(7).setCellValue(distanceString6);row.createCell(8).setCellValue(distanceString7);
			

			distanceString0="";distanceString1="";distanceString2="";distanceString3="";
			distanceString4="";distanceString5="";distanceString6="";distanceString7="";
			try {
				filename_rssi = "/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/FullBleTable.xls";
				fileOut = new FileOutputStream(filename_rssi);
				workbook_rssi.write(fileOut);
            //fileOut.close();
	        } catch ( Exception ex ) {
	            System.out.println(ex);
	        }
			
			

    		rssi_count=rssi_count+1;
    	}
		currenttime=System.currentTimeMillis();
        if(nodeName.equals(nodeNames[0])){      //Tim_Down_Front
        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Front.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Front[i].equals(Clear_MAC)){
        			try {
        			
    				int intrssi=Integer.valueOf(RSSI);
    				//intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Front[0].equals(Clear_MAC)){
    					//User1DownFront_User1DownBehind.add(intRssi_to_distance(intrssi));
    					//distanceString0=distanceString0+intRssi_to_distance(intrssi)+"(cd),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[1].equals(Clear_MAC)){
    					Time_User1DownFront_User2DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User1DownFront_User2DownLeft.add(intRssi_to_distance(intrssi,2));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,2)+"(cA),";

    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[2].equals(Clear_MAC)){
    					Time_User1DownFront_User2DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User1DownFront_User2DownRight.add(intRssi_to_distance(intrssi,3));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,3)+"(cB),";
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Front[3].equals(Clear_MAC)){
    					Time_User1DownFront_User2DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User1DownFront_User2DownFront.add(intRssi_to_distance(intrssi,0));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,0)+"(cC),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Front[4].equals(Clear_MAC)){
    					Time_User1DownFront_User2DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User1DownFront_User2DownBehind.add(intRssi_to_distance(intrssi,1));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,1)+"(cD),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[5].equals(Clear_MAC)){
    					//User1DownFront_User1DownLeft.add(intRssi_to_distance(intrssi));
    					//distanceString0=distanceString0+intRssi_to_distance(intrssi)+"(ca),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[6].equals(Clear_MAC)){
    					//User1DownFront_User1DownRight.add(intRssi_to_distance(intrssi));
    					//distanceString0=distanceString0+intRssi_to_distance(intrssi)+"(cb),";
    				}
    				/*try {
    					filename_rssi = "/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/FullBleTable.xls";
    					fileOut = new FileOutputStream(filename_rssi);
    					workbook_rssi.write(fileOut);
    	            //fileOut.close();
    		        } catch ( Exception ex ) {
    		            System.out.println(ex);
    		        }*/
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        
        if(nodeName.equals(nodeNames[1])){      //Tim_Down_Behind
	        System.out.println("Tim_Down_Behind!!!");

        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Behind.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Behind[i].equals(Clear_MAC)){
        			try {
        				

    				int intrssi=Integer.valueOf(RSSI);
    				//intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Behind[0].equals(Clear_MAC)){
    					//User1DownBehind_User1DownFront.add(intRssi_to_distance(intrssi));
    					//distanceString1=distanceString1+intRssi_to_distance(intrssi)+"(dc),";    					
    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[1].equals(Clear_MAC)){
    					Time_User1DownBehind_User2DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User1DownBehind_User2DownLeft.add(intRssi_to_distance(intrssi,6));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,6)+"(dA),";

    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[2].equals(Clear_MAC)){
    					Time_User1DownBehind_User2DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User1DownBehind_User2DownRight.add(intRssi_to_distance(intrssi,7));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,7)+"(dB),";
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Behind[3].equals(Clear_MAC)){
    					Time_User1DownBehind_User2DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User1DownBehind_User2DownFront.add(intRssi_to_distance(intrssi,4));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,4)+"(dC),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Behind[4].equals(Clear_MAC)){
    					Time_User1DownBehind_User2DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User1DownBehind_User2DownBehind.add(intRssi_to_distance(intrssi,5));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,5)+"(dD),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[5].equals(Clear_MAC)){
    					//User1DownBehind_User1DownLeft.add(intRssi_to_distance(intrssi));
    					//distanceString1=distanceString1+intRssi_to_distance(intrssi)+"(da),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[6].equals(Clear_MAC)){
    					//User1DownBehind_User1DownRight.add(intRssi_to_distance(intrssi));
    					//distanceString1=distanceString1+intRssi_to_distance(intrssi)+"(db),";
    				}
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        
        if(nodeName.equals(nodeNames[2])){      //Tim_Down_Left
        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Left.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Left[i].equals(Clear_MAC)){
        			try {
        				

    				int intrssi=Integer.valueOf(RSSI);
    				//intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Left[0].equals(Clear_MAC)){
    			        //System.out.println("User1DownLeft_User1DownRight "+User1DownLeft_User1DownRight.size() +" "+intRssi_to_distance(intrssi));
    					//User1DownLeft_User1DownRight.add(intRssi_to_distance(intrssi));
    					//distanceString2=distanceString2+intRssi_to_distance(intrssi)+"(ab),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[1].equals(Clear_MAC)){
    					Time_User1DownLeft_User2DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User1DownLeft_User2DownLeft.add(intRssi_to_distance(intrssi,10));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,10)+"(aA),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[2].equals(Clear_MAC)){
    					Time_User1DownLeft_User2DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User1DownLeft_User2DownRight.add(intRssi_to_distance(intrssi,11));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,11)+"(aB),";
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Left[3].equals(Clear_MAC)){
    					Time_User1DownLeft_User2DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User1DownLeft_User2DownFront.add(intRssi_to_distance(intrssi,8));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,8)+"(aC),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Left[4].equals(Clear_MAC)){
    					Time_User1DownLeft_User2DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User1DownLeft_User2DownBehind.add(intRssi_to_distance(intrssi,9));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,9)+"(aD),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Left[5].equals(Clear_MAC)){
    					//User1DownLeft_User1DownFront.add(intRssi_to_distance(intrssi));
    					//distanceString2=distanceString2+intRssi_to_distance(intrssi)+"(ac),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Left[6].equals(Clear_MAC)){
    					//User1DownLeft_User1DownBehind.add(intRssi_to_distance(intrssi));
    					//distanceString2=distanceString2+intRssi_to_distance(intrssi)+"(ad),";
    				}
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        if(nodeName.equals(nodeNames[3])){      //Tim_Down_Right
        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Right.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Right[i].equals(Clear_MAC)){
        			try {
    				int intrssi=Integer.valueOf(RSSI);
    				
    				if(Experiment_Globalvariable.Tim_Down_Right[0].equals(Clear_MAC)){
    			        //System.out.println("User1DownRight_User1DownLeft "+User1DownRight_User1DownLeft.size() +" "+intRssi_to_distance(intrssi));
    					//System.out.println(User1DownRight_User1DownLeft.size());
    			        //User1DownRight_User1DownLeft.add(intRssi_to_distance(intrssi));
    					//distanceString3=distanceString3+intRssi_to_distance(intrssi)+"(ba),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[1].equals(Clear_MAC)){
    					Time_User1DownRight_User2DownLeft.add((int)(currenttime-Globalvariable.starttime));
    			        User1DownRight_User2DownLeft.add(intRssi_to_distance(intrssi,14));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,14)+"(bA),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[2].equals(Clear_MAC)){
    					Time_User1DownRight_User2DownRight.add((int)(currenttime-Globalvariable.starttime));
    			        User1DownRight_User2DownRight.add(intRssi_to_distance(intrssi,15));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,15)+"(bB),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[3].equals(Clear_MAC)){
    					Time_User1DownRight_User2DownFront.add((int)(currenttime-Globalvariable.starttime));
    			        User1DownRight_User2DownFront.add(intRssi_to_distance(intrssi,12));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,12)+"(bC),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[4].equals(Clear_MAC)){
    					Time_User1DownRight_User2DownBehind.add((int)(currenttime-Globalvariable.starttime));
    			        User1DownRight_User2DownBehind.add(intRssi_to_distance(intrssi,13));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,13)+"(bD),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[5].equals(Clear_MAC)){
    			        //User1DownRight_User1DownFront.add(intRssi_to_distance(intrssi));
    					//distanceString3=distanceString3+intRssi_to_distance(intrssi)+"(bc),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[5].equals(Clear_MAC)){
    			        //User1DownRight_User1DownBehind.add(intRssi_to_distance(intrssi));
    					//distanceString3=distanceString3+intRssi_to_distance(intrssi)+"(bd),";
    				}
 
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        if(nodeName.equals(nodeNames[4])){      //Jack_Down_Front
        	for(int i=0;i<Experiment_Globalvariable.Jack_Down_Front.length;i++){
        		if(Experiment_Globalvariable.Jack_Down_Front[i].equals(Clear_MAC)){
        			try {
    				int intrssi=Integer.valueOf(RSSI);
    				
    				if(Experiment_Globalvariable.Jack_Down_Front[0].equals(Clear_MAC)){
    					//User2DownFront_User2DownBehind.add(intRssi_to_distance(intrssi));
    					//distanceString4=distanceString4+intRssi_to_distance(intrssi)+"(CD),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Front[1].equals(Clear_MAC)){
    					Time_User2DownFront_User1DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User2DownFront_User1DownLeft.add(intRssi_to_distance(intrssi,18));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,18)+"(Ca),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Front[2].equals(Clear_MAC)){
    					Time_User2DownFront_User1DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User2DownFront_User1DownRight.add(intRssi_to_distance(intrssi,19));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,19)+"(Cb),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Front[3].equals(Clear_MAC)){
    					Time_User2DownFront_User1DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User2DownFront_User1DownFront.add(intRssi_to_distance(intrssi,16));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,16)+"(Cc),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Front[4].equals(Clear_MAC)){
    					Time_User2DownFront_User1DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User2DownFront_User1DownBehind.add(intRssi_to_distance(intrssi,17));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,17)+"(Cd),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Front[5].equals(Clear_MAC)){
    					//User2DownFront_User2DownLeft.add(intRssi_to_distance(intrssi));
    					//distanceString4=distanceString4+intRssi_to_distance(intrssi)+"(CA),";
    				} 
    				if(Experiment_Globalvariable.Jack_Down_Front[6].equals(Clear_MAC)){
    					//User2DownFront_User2DownRight.add(intRssi_to_distance(intrssi));
    					//distanceString4=distanceString4+intRssi_to_distance(intrssi)+"(CB),";
    				} 
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }     
        if(nodeName.equals(nodeNames[5])){      //Jack_Down_Behind
        	for(int i=0;i<Experiment_Globalvariable.Jack_Down_Behind.length;i++){
        		if(Experiment_Globalvariable.Jack_Down_Behind[i].equals(Clear_MAC)){
        			try {
    				int intrssi=Integer.valueOf(RSSI);
    				
    				if(Experiment_Globalvariable.Jack_Down_Behind[0].equals(Clear_MAC)){
    					//User2DownBehind_User2DownFront.add(intRssi_to_distance(intrssi));
    					//distanceString5=distanceString5+intRssi_to_distance(intrssi)+"(DC),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Behind[1].equals(Clear_MAC)){
    					Time_User2DownBehind_User1DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User2DownBehind_User1DownLeft.add(intRssi_to_distance(intrssi,22));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,22)+"(Da),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Behind[2].equals(Clear_MAC)){
    					Time_User2DownBehind_User1DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User2DownBehind_User1DownRight.add(intRssi_to_distance(intrssi,23));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,23)+"(Db),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[3].equals(Clear_MAC)){
    					Time_User2DownBehind_User1DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User2DownBehind_User1DownFront.add(intRssi_to_distance(intrssi,20));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,20)+"(Dc),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[4].equals(Clear_MAC)){
    					Time_User2DownBehind_User1DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User2DownBehind_User1DownBehind.add(intRssi_to_distance(intrssi,21));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,21)+"(Dd),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[5].equals(Clear_MAC)){
    					//User2DownBehind_User2DownLeft.add(intRssi_to_distance(intrssi));
    					//distanceString5=distanceString5+intRssi_to_distance(intrssi)+"(DA),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[6].equals(Clear_MAC)){
    					//User2DownBehind_User2DownRight.add(intRssi_to_distance(intrssi));
    					//distanceString5=distanceString5+intRssi_to_distance(intrssi)+"(DB),";
    				}  	
    				
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        
        
        if(nodeName.equals(nodeNames[6])){      //Jack_Down_Left
        	for(int i=0;i<Experiment_Globalvariable.Jack_Down_Left.length;i++){
        		if(Experiment_Globalvariable.Jack_Down_Left[i].equals(Clear_MAC)){
        			try {
    				int intrssi=Integer.valueOf(RSSI);
    				
    				if(Experiment_Globalvariable.Jack_Down_Left[0].equals(Clear_MAC)){
    					Time_User2DownLeft_User1DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User2DownLeft_User1DownLeft.add(intRssi_to_distance(intrssi,26));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,26)+"(Aa),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[1].equals(Clear_MAC)){
    					Time_User2DownLeft_User1DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User2DownLeft_User1DownRight.add(intRssi_to_distance(intrssi,27));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,27)+"(Ab),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[2].equals(Clear_MAC)){
    					Time_User2DownLeft_User1DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User2DownLeft_User1DownFront.add(intRssi_to_distance(intrssi,24));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,24)+"(Ac),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[3].equals(Clear_MAC)){
    					Time_User2DownLeft_User1DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User2DownLeft_User1DownBehind.add(intRssi_to_distance(intrssi,25));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,25)+"(Ad),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[4].equals(Clear_MAC)){
    					//User2DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));
    					//distanceString6=distanceString6+intRssi_to_distance(intrssi)+"(AB),";
    				}  
    				if(Experiment_Globalvariable.Jack_Down_Left[5].equals(Clear_MAC)){
    					//User2DownLeft_User2DownFront.add(intRssi_to_distance(intrssi));
    					//distanceString6=distanceString6+intRssi_to_distance(intrssi)+"(AC),";
    				}  
    				if(Experiment_Globalvariable.Jack_Down_Left[6].equals(Clear_MAC)){
    					//User2DownLeft_User2DownBehind.add(intRssi_to_distance(intrssi));
    					//distanceString6=distanceString6+intRssi_to_distance(intrssi)+"(AD),";
    				}  
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        
        if(nodeName.equals(nodeNames[7])){      //Jack_Down_Right
        	for(int i=0;i<Experiment_Globalvariable.Jack_Down_Right.length;i++){
        		if(Experiment_Globalvariable.Jack_Down_Right[i].equals(Clear_MAC)){
        			try {
    				int intrssi=Integer.valueOf(RSSI);
    				
    				if(Experiment_Globalvariable.Jack_Down_Right[0].equals(Clear_MAC)){
    					Time_User2DownRight_User1DownLeft.add((int)(currenttime-Globalvariable.starttime));
    					User2DownRight_User1DownLeft.add(intRssi_to_distance(intrssi,30));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,30)+"(Ba),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[1].equals(Clear_MAC)){
    					Time_User2DownRight_User1DownRight.add((int)(currenttime-Globalvariable.starttime));
    					User2DownRight_User1DownRight.add(intRssi_to_distance(intrssi,31));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,31)+"(Bb),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[2].equals(Clear_MAC)){
    					Time_User2DownRight_User1DownFront.add((int)(currenttime-Globalvariable.starttime));
    					User2DownRight_User1DownFront.add(intRssi_to_distance(intrssi,28));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,28)+"(Bc),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[3].equals(Clear_MAC)){
    					Time_User2DownRight_User1DownBehind.add((int)(currenttime-Globalvariable.starttime));
    					User2DownRight_User1DownBehind.add(intRssi_to_distance(intrssi,29));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,29)+"(Bd),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[4].equals(Clear_MAC)){
    					//User2DownRight_User2DownLeft.add(intRssi_to_distance(intrssi));
    					//distanceString7=distanceString7+intRssi_to_distance(intrssi)+"(BA),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[5].equals(Clear_MAC)){
    					//User2DownRight_User2DownFront.add(intRssi_to_distance(intrssi));
    					//distanceString7=distanceString7+intRssi_to_distance(intrssi)+"(BC),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[6].equals(Clear_MAC)){
    					//User2DownRight_User2DownBehind.add(intRssi_to_distance(intrssi));
    					//distanceString7=distanceString7+intRssi_to_distance(intrssi)+"(BD),";
    				}
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        
        System.out.print("判斷size windows:");
        for(int i=0;i<Totoalline.size();i++)
        	System.out.print(Totoalline.get(i).size()+" ");
        System.out.println("\n");
    	
      // ================================以下判斷公轉 ================================================//
		
 	    currenttime =System.currentTimeMillis();
 	    //System.out.println("currenttimeendslotime: "+(currenttime - endslotime));
 	   System.out.println("currenttimestarttime: "+(currenttime - Globalvariable.starttime));
 	   if(if_first_time_slot!=true &&(currenttime - Globalvariable.starttime >=1)){ //大於一毫秒
  		  if_first_time_slot=true;
 		/*   System.out.println("Begin current");
 		 		
 		
 	 		startAlgotime=System.currentTimeMillis();
 		    decisiontime=System.currentTimeMillis();
 		      //===============================================================================第一次時 5s內取10個data
 		       int count=0;double quality=0.0,mean=0.0;
 		       for(int i=0;i<Totoalline.size();i++){
 		    	   if(Totoalline.get(i).size()>=1){   //會有32條
 		    		   for(int j=Totoalline.get(i).size()-1;j>=0 && count<=10;j--,count++){  //第一次時 5s內取10個data
 		    			  quality=quality+Totoalline.get(i).get(j);	
 		    		   }
 		    		   mean=(quality/count);
  		    		  Meanlinejudge.add(mean);
    				  count=0;quality=0.0;mean=0.0;
 		    	   }else{                          //空值
   		    		  Meanlinejudge.add(-1.0);
 		    	   }
 		       }
 		      	FileWriter fw2;
 			   try {
 					fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/"
 							+String.valueOf(currenttime-Globalvariable.starttime)+".txt",true);
 				BufferedWriter bufferedWriter = new BufferedWriter(fw2);
 				for(int i=0;i<Meanlinejudge.size();i++){
 					bufferedWriter.write(Meanlinejudge.get(i)+"\n");
 					}
 				bufferedWriter.flush();
 				bufferedWriter.close();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} 		       
 		      Meanlinejudge.clear();
 		      */
 		     Globalvariable.endslotime=System.currentTimeMillis();		    	        
 	    }  //time slot做判斷
	   //===============================================================================1s內最多10個data
 	   else if(if_first_time_slot==true && (currenttime - Globalvariable.endslotime)>=Globalvariable.time_slot){ 		   
 		       Globalvariable.if_statrtacc=true;
		       int count=0,currentindex=0;double quality=0.0,mean=0.0;
		       for(int i=0;i<TimeTotoalline.size();i++){                       
		    	   //System.out.println("currentindex "+currentindex+" "+" "+currenttime+" "+Globalvariable.starttime);
		    	   if(Totoalline.get(i).size()>=1){                   //表示不為空才能進去判斷
		    		   for(int index=TimeTotoalline.get(i).size()-1;((currenttime-Globalvariable.starttime)-Globalvariable.time_slot)< TimeTotoalline.get(i).get(index)&& index>=0;index--){ //(6.1)-1 < 5.8(從最後一個算起)
		    			   //System.out.println("currentindex2 "+currentindex+" "+" "+(currenttime-Globalvariable.starttime)+" "+TimeTotoalline.get(i).get(index));
		    			   currentindex=currentindex+1;
		    			   if(index==0){
		    				   break;
		    				   }
		    			   }
		    		   if(Totoalline.get(i).size()>=1 && currentindex!=0){   //會有32條  currentindex!=0 代表在這個區間有值
		    			   //System.out.println("currentindex3 "+Totoalline.get(i)+" "+" "+currentindex+" ");
		    			   for(int j=(Totoalline.get(i).size()-currentindex);j<Totoalline.get(i).size()&& count<=10;j++,count++){  //1s內取10個data
		    				   //System.out.println("quality2 "+i+" "+j+" "+Totoalline.get(i).get(j)+" "+Totoalline.get(i).size());
		    				   quality=quality+Totoalline.get(i).get(j);	
		    				   StringMeanlinejudge.add(Totoalline.get(i).get(j));
		    				   }
		    			   mean=(quality/count);
		    			   //System.out.println("Meanlinejudge "+quality+" "+count+" "+mean);
		    			   Meanlinejudge.add(mean);
		    			   count=0;currentindex=0;quality=0.0;mean=0.0;
		    			   }else{                         //空值
		    				   StringMeanlinejudge.add(-1.0);
		    				   Meanlinejudge.add(-1.0);
		    				   }
		    		   }else{
	    				   StringMeanlinejudge.add(-1.0);//空值
		    			   Meanlinejudge.add(-1.0);
		    			   }
		    	   
	 		      /*	FileWriter fw2;
	  			   try {
	  					fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/"
	  							+String.valueOf(currenttime-Globalvariable.starttime)+".txt",true);
	  				BufferedWriter bufferedWriter = new BufferedWriter(fw2);
	  				for(int j=0;j<StringMeanlinejudge.size();j++){
	  					bufferedWriter.write(StringMeanlinejudge.get(j)+",");
	  					}
  					bufferedWriter.write("\n");
	  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
	  				bufferedWriter.flush();
	  				bufferedWriter.close();
	  				} catch (IOException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}*/
	  			 StringMeanlinejudge.clear();
		    	   }
		          ReturnIndex(Meanlinejudge,AVGConfindence);		          
		  		//=================初始化虛擬坐標,自轉角轉換坐標
		  		System.out.println("初始化虛擬坐標");
		          
		    	   Coordinate coo_a = new Coordinate(); Coordinate coo_b = new Coordinate();
		     	   Coordinate coo_c = new Coordinate(); Coordinate coo_d = new Coordinate();
		     	   Coordinate coo_A = new Coordinate(); Coordinate coo_B = new Coordinate();
		     	   Coordinate coo_C = new Coordinate(); Coordinate coo_D = new Coordinate();
		     	   
		    		coo_a.xList.add(-15.0);coo_a.xList.add(-30.0);coo_a.xList.add(-50.0);coo_a.xList.add(-30.0);//State1=>State8
		    		coo_a.xList.add(-15.0);coo_a.xList.add(0.0);coo_a.xList.add(20.0);coo_a.xList.add(0.0);
		    		coo_a.zList.add(25.0);coo_a.zList.add(25.0);coo_a.zList.add(0.0);coo_a.zList.add(-25.0);
		    		coo_a.zList.add(-25.0);coo_a.zList.add(-25.0);coo_a.zList.add(0.0);coo_a.zList.add(25.0);

		    		coo_b.xList.add(15.0);coo_b.xList.add(0.0);coo_b.xList.add(-20.0);coo_b.xList.add(0.0);
		    		coo_b.xList.add(15.0);coo_b.xList.add(30.0);coo_b.xList.add(50.0);coo_b.xList.add(30.0);
		    		coo_b.zList.add(25.0);coo_b.zList.add(25.0);coo_b.zList.add(0.0);coo_b.zList.add(-25.0);
		    		coo_b.zList.add(-25.0);coo_b.zList.add(-25.0);coo_b.zList.add(0.0);coo_b.zList.add(25.0);

		    		coo_c.xList.add(0.0);coo_c.xList.add(-15.0);coo_c.xList.add(-35.0);coo_c.xList.add(-35.0);
		    		coo_c.xList.add(0.0);coo_c.xList.add(15.0);coo_c.xList.add(35.0);coo_c.xList.add(15.0);
		    		coo_c.zList.add(35.0);coo_c.zList.add(35.0);coo_c.zList.add(10.0);coo_c.zList.add(-15.0);
		    		coo_c.zList.add(-15.0);coo_c.zList.add(-15.0);coo_c.zList.add(10.0);coo_c.zList.add(35.0);

		    		coo_d.xList.add(0.0);coo_d.xList.add(-15.0);coo_d.xList.add(-35.0);coo_d.xList.add(-35.0);
		    		coo_d.xList.add(0.0);coo_d.xList.add(15.0);coo_d.xList.add(35.0);coo_d.xList.add(15.0);
		    		coo_d.zList.add(15.0);coo_d.zList.add(15.0);coo_d.zList.add(-10.0);coo_d.zList.add(-35.0);
		    		coo_d.zList.add(-35.0);coo_d.zList.add(-35.0);coo_d.zList.add(-10.0);coo_d.zList.add(15.0);

		    		coo_A.xList.add(-15.0);coo_A.zList.add(0.0);

		    		coo_B.xList.add(15.0);coo_B.zList.add(0.0);

		    		coo_C.xList.add(0.0);coo_C.zList.add(10.0);

		    		coo_D.xList.add(0.0);coo_D.zList.add(-10.0);
		        //==============================================offsetX,offsetY
		    		U1Ox.add(0);U1Ox.add(-15);U1Ox.add(-35);U1Ox.add(-15);
		    		U1Ox.add(0);U1Ox.add(15);U1Ox.add(35);U1Ox.add(15);
		    		U1Oz.add(25);U1Oz.add(25);U1Oz.add(0);U1Oz.add(-25);
		    		U1Oz.add(-25);U1Oz.add(-25);U1Oz.add(0);U1Oz.add(25);
		    		int size=0;
		          for(int state=0;state<8;state++){
/*		        	  RealcC= sqrt_pow(U1Ox.get(state)+coo_c.xList.get(state),coo_C.xList.get(size),U1Oz.get(state)+coo_c.zList.get(state),coo_C.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealcD= sqrt_pow(U1Ox.get(state)+coo_c.xList.get(state),coo_D.xList.get(size),U1Oz.get(state)+coo_c.zList.get(state),coo_D.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealcA= sqrt_pow(U1Ox.get(state)+coo_c.xList.get(state),coo_A.xList.get(size),U1Oz.get(state)+coo_c.zList.get(state),coo_A.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealcB= sqrt_pow(U1Ox.get(state)+coo_c.xList.get(state),coo_B.xList.get(size),U1Oz.get(state)+coo_c.zList.get(state),coo_B.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  
		        	  RealdC= sqrt_pow(U1Ox.get(state)+coo_d.xList.get(state),coo_C.xList.get(size),U1Oz.get(state)+coo_d.zList.get(state),coo_C.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealdD= sqrt_pow(U1Ox.get(state)+coo_d.xList.get(state),coo_D.xList.get(size),U1Oz.get(state)+coo_d.zList.get(state),coo_D.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealdA= sqrt_pow(U1Ox.get(state)+coo_d.xList.get(state),coo_A.xList.get(size),U1Oz.get(state)+coo_d.zList.get(state),coo_A.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealdB= sqrt_pow(U1Ox.get(state)+coo_d.xList.get(state),coo_B.xList.get(size),U1Oz.get(state)+coo_d.zList.get(state),coo_B.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  
		        	  RealaC= sqrt_pow(U1Ox.get(state)+coo_a.xList.get(state),coo_C.xList.get(size),U1Oz.get(state)+coo_a.zList.get(state),coo_C.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealaD= sqrt_pow(U1Ox.get(state)+coo_a.xList.get(state),coo_D.xList.get(size),U1Oz.get(state)+coo_a.zList.get(state),coo_D.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealaA= sqrt_pow(U1Ox.get(state)+coo_a.xList.get(state),coo_A.xList.get(size),U1Oz.get(state)+coo_a.zList.get(state),coo_A.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealaB= sqrt_pow(U1Ox.get(state)+coo_a.xList.get(state),coo_B.xList.get(size),U1Oz.get(state)+coo_a.zList.get(state),coo_B.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  
		        	  RealbC= sqrt_pow(U1Ox.get(state)+coo_b.xList.get(state),coo_C.xList.get(size),U1Oz.get(state)+coo_b.zList.get(state),coo_C.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealbD= sqrt_pow(U1Ox.get(state)+coo_b.xList.get(state),coo_D.xList.get(size),U1Oz.get(state)+coo_b.zList.get(state),coo_D.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealbA= sqrt_pow(U1Ox.get(state)+coo_b.xList.get(state),coo_A.xList.get(size),U1Oz.get(state)+coo_b.zList.get(state),coo_A.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  RealbB= sqrt_pow(U1Ox.get(state)+coo_b.xList.get(state),coo_B.xList.get(size),U1Oz.get(state)+coo_b.zList.get(state),coo_B.zList.get(size),Globalvariable.FinalTim_yaw);
		        	  
		        	  RealCc= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)+coo_c.xList.get(state),coo_C.zList.get(size),U1Oz.get(state)+coo_c.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealCd= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)+coo_d.xList.get(state),coo_C.zList.get(size),U1Oz.get(state)+coo_d.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealCa= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)+coo_a.xList.get(state),coo_C.zList.get(size),U1Oz.get(state)+coo_a.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealCb= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)+coo_b.xList.get(state),coo_C.zList.get(size),U1Oz.get(state)+coo_b.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  
		        	  RealDc= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)+coo_c.xList.get(state),coo_D.zList.get(size),U1Oz.get(state)+coo_c.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealDd= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)+coo_d.xList.get(state),coo_D.zList.get(size),U1Oz.get(state)+coo_d.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealDa= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)+coo_a.xList.get(state),coo_D.zList.get(size),U1Oz.get(state)+coo_a.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealDb= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)+coo_b.xList.get(state),coo_D.zList.get(size),U1Oz.get(state)+coo_b.zList.get(state),Globalvariable.FinalTim_yaw);
		        	 
		        	  RealAc= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)+coo_c.xList.get(state),coo_A.zList.get(size),U1Oz.get(state)+coo_c.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealAd= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)+coo_d.xList.get(state),coo_A.zList.get(size),U1Oz.get(state)+coo_d.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealAa= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)+coo_a.xList.get(state),coo_A.zList.get(size),U1Oz.get(state)+coo_a.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealAb= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)+coo_b.xList.get(state),coo_A.zList.get(size),U1Oz.get(state)+coo_b.zList.get(state),Globalvariable.FinalTim_yaw);

		        	  RealBc= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)+coo_c.xList.get(state),coo_B.zList.get(size),U1Oz.get(state)+coo_c.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealBd= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)+coo_d.xList.get(state),coo_B.zList.get(size),U1Oz.get(state)+coo_d.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealBa= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)+coo_a.xList.get(state),coo_B.zList.get(size),U1Oz.get(state)+coo_a.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  RealBb= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)+coo_b.xList.get(state),coo_B.zList.get(size),U1Oz.get(state)+coo_b.zList.get(state),Globalvariable.FinalTim_yaw);
		        	  */
		        	  System.out.println("U1ox: "+(U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw))+" "+(U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw)));
		        	  RealcC= sqrt_pow(U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_C.xList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw),coo_C.zList.get(size));
		        	  RealcD= sqrt_pow(U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_D.xList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw),coo_D.zList.get(size));
		        	  RealcA= sqrt_pow(U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_A.xList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw),coo_A.zList.get(size));
		        	  RealcB= sqrt_pow(U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_B.xList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw),coo_B.zList.get(size));
		        	  
		        	  RealdC= sqrt_pow(U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_C.xList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw),coo_C.zList.get(size));
		        	  RealdD= sqrt_pow(U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_D.xList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw),coo_D.zList.get(size));
		        	  RealdA= sqrt_pow(U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_A.xList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw),coo_A.zList.get(size));
		        	  RealdB= sqrt_pow(U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_B.xList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw),coo_B.zList.get(size));
		        	  
		        	  RealaC= sqrt_pow(U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_C.xList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw),coo_C.zList.get(size));
		        	  RealaD= sqrt_pow(U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_D.xList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw),coo_D.zList.get(size));
		        	  RealaA= sqrt_pow(U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_A.xList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw),coo_A.zList.get(size));
		        	  RealaB= sqrt_pow(U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_B.xList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw),coo_B.zList.get(size));
		        	  
		        	  RealbC= sqrt_pow(U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_C.xList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw),coo_C.zList.get(size));
		        	  RealbD= sqrt_pow(U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_D.xList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw),coo_D.zList.get(size));
		        	  RealbA= sqrt_pow(U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_A.xList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw),coo_A.zList.get(size));
		        	  RealbB= sqrt_pow(U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_B.xList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw),coo_B.zList.get(size));
		        	  
		        	  RealCc= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_C.zList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealCd= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_C.zList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealCa= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_C.zList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw));
		        	  RealCb= sqrt_pow(coo_C.xList.get(size),U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_C.zList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw));
		        	  
		        	  RealDc= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_D.zList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealDd= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_D.zList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealDa= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_D.zList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw));
		        	  RealDb= sqrt_pow(coo_D.xList.get(size),U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_D.zList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw));
		        	 
		        	  RealAc= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_A.zList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealAd= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_A.zList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealAa= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_A.zList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw));
		        	  RealAb= sqrt_pow(coo_A.xList.get(size),U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_A.zList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw));

		        	  RealBc= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)-R2*Math.sin(Globalvariable.FinalTim_yaw),coo_B.zList.get(size),U1Oz.get(state)+R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealBd= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)+R2*Math.sin(Globalvariable.FinalTim_yaw),coo_B.zList.get(size),U1Oz.get(state)-R2*Math.cos(Globalvariable.FinalTim_yaw));
		        	  RealBa= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)-R1*Math.cos(Globalvariable.FinalTim_yaw),coo_B.zList.get(size),U1Oz.get(state)-R1*Math.sin(Globalvariable.FinalTim_yaw));
		        	  RealBb= sqrt_pow(coo_B.xList.get(size),U1Ox.get(state)+R1*Math.cos(Globalvariable.FinalTim_yaw),coo_B.zList.get(size),U1Oz.get(state)+R1*Math.sin(Globalvariable.FinalTim_yaw));

		        	  
				  		RealTotoalline.add(RealcC);RealTotoalline.add(RealcD);RealTotoalline.add(RealcA);RealTotoalline.add(RealcB);
				  		RealTotoalline.add(RealdC);RealTotoalline.add(RealdD);RealTotoalline.add(RealdA);RealTotoalline.add(RealdB);
				  		RealTotoalline.add(RealaC);RealTotoalline.add(RealaD);RealTotoalline.add(RealaA);RealTotoalline.add(RealaB);
				  		RealTotoalline.add(RealbC);RealTotoalline.add(RealbD);RealTotoalline.add(RealbA);RealTotoalline.add(RealbB);
				  		RealTotoalline.add(RealCc);RealTotoalline.add(RealCd);RealTotoalline.add(RealCa);RealTotoalline.add(RealCb);
				  		RealTotoalline.add(RealDc);RealTotoalline.add(RealDd);RealTotoalline.add(RealDa);RealTotoalline.add(RealDb);
				  		RealTotoalline.add(RealAc);RealTotoalline.add(RealAd);RealTotoalline.add(RealAa);RealTotoalline.add(RealAb);
				  		RealTotoalline.add(RealBc);RealTotoalline.add(RealBd);RealTotoalline.add(RealBa);RealTotoalline.add(RealBb);
		        	  
		   	       
		        	  for(int i=0;i<RealTotoalline.size();i++){
		        		  System.out.print(RealTotoalline.get(i)+" ");
		        	  }
		        	 
		        	  //======================================================運算RSSI與陀螺儀之間的mean square error
		        	  System.out.println("運算RSSI與陀螺儀之間的mean square error");
		        	  if(IndexList.size()==3){
		        	  MeansquareState.add((AVGConfindence.get(IndexList.get(0))/(AVGConfindence.get(IndexList.get(0))+AVGConfindence.get(IndexList.get(1))+AVGConfindence.get(IndexList.get(2))))*Math.pow(RealTotoalline.get(IndexList.get(0))-Meanlinejudge.get(0), 2)+
		        			              (AVGConfindence.get(IndexList.get(1))/(AVGConfindence.get(IndexList.get(0))+AVGConfindence.get(IndexList.get(1))+AVGConfindence.get(IndexList.get(2))))*Math.pow(RealTotoalline.get(IndexList.get(1))-Meanlinejudge.get(1), 2)+
		        			              (AVGConfindence.get(IndexList.get(2))/(AVGConfindence.get(IndexList.get(0))+AVGConfindence.get(IndexList.get(1))+AVGConfindence.get(IndexList.get(2))))*Math.pow(RealTotoalline.get(IndexList.get(2))-Meanlinejudge.get(2), 2)); //avg*(Realline-RSSIline)
		        	  }else if(IndexList.size()==2){
			         MeansquareState.add((AVGConfindence.get(IndexList.get(0))/(AVGConfindence.get(IndexList.get(0))+AVGConfindence.get(IndexList.get(1))))*Math.pow(RealTotoalline.get(IndexList.get(0))-Meanlinejudge.get(0), 2)+
			        		             (AVGConfindence.get(IndexList.get(1))/(AVGConfindence.get(IndexList.get(0))+AVGConfindence.get(IndexList.get(1))))*Math.pow(RealTotoalline.get(IndexList.get(1))-Meanlinejudge.get(1), 2)); //avg*(Realline-RSSIline)
		        		  
		        	  }else if(IndexList.size()==1){
			        	  MeansquareState.add((AVGConfindence.get(IndexList.get(0))/(AVGConfindence.get(IndexList.get(0))))*Math.pow(RealTotoalline.get(IndexList.get(0))-Meanlinejudge.get(0), 2)); //avg*(Realline-RSSIline) ,avg=1		  
		        	  }else{
		        		  System.out.println("Currently, System is not work!");
			        	  System.out.println("\n");

		        	  }

		 		 	  RealTotoalline.clear();
		          }
		          
	   	          //======================================================比較8 state mean square error(1.RSSI)
		          if(IndexList.size()!=0){
		          
		          for(int i=0;i<MeansquareState.size();i++){
		        	  TotalMeansquare=TotalMeansquare+MeansquareState.get(i);
		        	  System.out.println("MeansquareState ");
		        	  System.out.print(MeansquareState.get(i)+" ");
		        	  System.out.println("\n");
		          }
		          System.out.println("TotalMeansquare "+TotalMeansquare);
		          for (int i = 0; i <MeansquareState.size(); i++) {//先算出目前最高的比例
		        	  RssiStaterate.add(1-(MeansquareState.get(i)/TotalMeansquare));
			          System.out.println("Rssirate "+(1-(MeansquareState.get(i)/TotalMeansquare)));
		        	  Rssirate=Rssirate+(1-(MeansquareState.get(i)/TotalMeansquare));

				   }
		          System.out.println("Rssirate2 "+Rssirate);
		          FinalRssiStaterate.add(0,0.0);
		          for (int i = 1;    i <=          8        ; i++) {      //在做normalize index從一開始，i=1目的要跟ACC一致
			          System.out.println("Rssirate3 "+(RssiStaterate.get(i-1)/Rssirate));
			          FinalRssiStaterate.add(i,(RssiStaterate.get(i-1)/Rssirate));      	  
		          }
		          //======================================================比較8 state mean square error(2.ACC) 回傳acc各state機率
		          System.out.println("FinalRssiStateratesize: "+FinalRssiStaterate.size());
		          TotalStaterate.add(0,0.0);//先亂塞值
		          for (int i = 1; i < FinalRssiStaterate.size(); i++) {       //把RSSI機率與加速度相乘
		        	  //System.out.println("RSSI+ACC: "+RssiStaterate.get(i)+" "+Globalvariable.AccStaterate.get(i));
		        	  if(Globalvariable.AccStaterate.size()!=9){
			          //TotalStaterate.add(i,FinalRssiStaterate.get(i));
		        		  Thread.sleep(50);
		        	  }
		        	  if(Globalvariable.AccStaterate.size()==9){
			        	  TotalStaterate.add(i,FinalRssiStaterate.get(i)*Globalvariable.AccStaterate.get(i));
					}
				}
		          SortTotalStaterate.add(0,0.0);
		          for (int i = 1; i < TotalStaterate.size(); i++) {
		        	  SortTotalStaterate.add(i,TotalStaterate.get(i));
		        	  System.out.println("TotalStaterate ");
		        	  System.out.println(TotalStaterate.get(i));
		          }
		          Collections.sort(SortTotalStaterate);
		          System.out.println("SortTotalStaterate "+SortTotalStaterate);
				

		          for (int i = 1; i < TotalStaterate.size(); i++) {        //OutPut結果
		        	  if(SortTotalStaterate.get(8).equals(TotalStaterate.get(i))){
		        		  Globalvariable.RecordState.add(i);              //記錄目前State軌跡還沒寫好 有可能前面第一名第二名機率也要算進去

		        		  Globalvariable.CurrentState=i;
			        	  System.out.println("State: "+i);
			        	  
			   		      	FileWriter fw2;
				  			   try {
				  					fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/OutputState.txt",true);
				  				    BufferedWriter bufferedWriter = new BufferedWriter(fw2);
				  					bufferedWriter.write(Globalvariable.CurrentState+"\n");
				  					
				  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				  				bufferedWriter.flush();
				  				bufferedWriter.close();
				  				} catch (IOException e) {
				  					// TODO Auto-generated catch block
				  					e.printStackTrace();
				  				}
			        	  break;
		        	  }
		          }
		          }else{//=============================================表示目前RSSI在此區間沒收到，所以就參照上一筆資料丟出
		        	  Globalvariable.CurrentState=Globalvariable.RecordState.get(Globalvariable.RecordState.size()-2);
		        	  System.out.println("參照上一筆狀態: "+Globalvariable.CurrentState);
		   		      	FileWriter fw2;
			  			   try {
			  					fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/OutputState.txt",true);
			  				    BufferedWriter bufferedWriter = new BufferedWriter(fw2);
			  					bufferedWriter.write(Globalvariable.CurrentState+"\n");
			  					
			  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
			  				bufferedWriter.flush();
			  				bufferedWriter.close();
			  				} catch (IOException e) {
			  					// TODO Auto-generated catch block
			  					e.printStackTrace();
			  				}
		        	  
		          }
		          
		          
		          //======================================================Unity呈現
		 		 /*finaljson = "{" + "Position"+": { x:"+1+", y:"+0+", z:"+0+ "} }";
		          channel.basicPublish(TOPIC_location, "", null, finaljson.getBytes());
		 		 finaljson = "{" + "Position"+": { x:"+-1+", y:"+0+", z:"+0+ "} }";
		         channel.basicPublish(TOPIC_location, "", null, finaljson.getBytes());
		 		 finaljson = "{" + "Position"+": { x:"+1+", y:"+0+", z:"+1+ "} }";
		         channel.basicPublish(TOPIC_location, "", null, finaljson.getBytes());
		         */

		       
 		       //System.out.println(Meanlinejudge.get(i));
		      MeansquareState.clear();
		      RssiStaterate.clear();
		      TotalStaterate.clear();
		      SortTotalStaterate.clear();
		      FinalRssiStaterate.clear();
	 		  IndexList.clear();
 		      Meanlinejudge.clear();
 		     TotalMeansquare=0.0;Rssirate=0.0;
 		     Globalvariable.AccStaterate.clear();
 		 	 Globalvariable.User1Totalacc_x.clear();
 		 	 Globalvariable.User1Totalacc_y.clear();
 		     Globalvariable.endslotime=System.currentTimeMillis();
 		 	  coo_a.xList.clear();coo_a.zList.clear();coo_b.xList.clear();coo_b.zList.clear();
 		   	  coo_c.xList.clear();coo_c.zList.clear();coo_d.xList.clear();coo_d.zList.clear();
 		   	  coo_A.xList.clear();coo_A.zList.clear();coo_B.xList.clear();coo_B.zList.clear();
 		   	  coo_C.xList.clear();coo_C.zList.clear();coo_D.xList.clear();coo_D.zList.clear();

 		   
 	   }
    	
        channel.close();
        connection.close();
        
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}


	} catch (Exception e) {
		// TODO: handle exception
	}

    }

	public class Statistics 
    {
        List<Double> data;
        int size;   

        public Statistics(List<Double> data) 
        {
            this.data = data;
            size = data.size();
        }   

        public double getMean()   //平均值
        {
            double sum = 0.0;
            for(double a : data)
                sum += a;
            return sum/size;
        }

        public double getVariance()
        {
            double mean = getMean();
            double temp = 0;
            for(double a :data)
                temp += (mean-a)*(mean-a);
            return temp/size;
        }

        public double getStdDev()    //標準差
        {
            return Math.abs(Math.sqrt(getVariance()));
        }
        public double getCoeffVar()
        {
        	return 1-(getStdDev()/getMean());
        }
    }
	public double sqrt_pow(double x1,double x2 ,double z1,double z2){
		return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(z1-z2,2));		
	}
	
	public void ReturnIndex(ArrayList<Double> meanlinejudge,ArrayList<Double> avgConfindence){
		System.out.println("ReturnIndex "+avgConfindence.size());
		for (int i = 0; i < avgConfindence.size(); i++) {                            //把RSSI有值的index抓取
			if(!meanlinejudge.get(i).equals(-1.0) && !meanlinejudge.get(i).equals(100)){
				MaxavgConfindence.add(avgConfindence.get(i));                   
			}	
		}
		int getSize=0;
		Collections.sort(MaxavgConfindence);
		System.out.println("MaxavgConfindence: "+MaxavgConfindence);
		if(MaxavgConfindence.size()>=3){                                   
			getSize=3;                                                         //抓取目前3個最高的C.V
		}
		else{
			getSize=MaxavgConfindence.size();
		}
		if(getSize > 0){
		for(int i=MaxavgConfindence.size()-1; i>MaxavgConfindence.size()-getSize-1 ;i--){                                     
			for(int j=0;j<avgConfindence.size();j++){
				if(MaxavgConfindence.get(i).equals(avgConfindence.get(j))){
					IndexList.add(j);                                      //IndexList由高到低
					break;
				}
				
			}
		}}
		for (int i = 0; i < IndexList.size(); i++) {
			System.out.println("IndexList "+IndexList.get(i)+" "+avgConfindence.get(IndexList.get(i)));
			
		}
		MaxavgConfindence.clear();
				
	}

	public void LimitSize(){
		User1Left_size=Totoalline.get(2).size()+Totoalline.get(3).size()+Totoalline.get(4).size()+Totoalline.get(5).size()+
				       Totoalline.get(6).size()+Totoalline.get(7).size()+Totoalline.get(8).size()+Totoalline.get(9).size();
		
		User1Right_size=Totoalline.get(10).size()+Totoalline.get(11).size()+Totoalline.get(12).size()+Totoalline.get(13).size()+
			            Totoalline.get(14).size()+Totoalline.get(15).size()+Totoalline.get(16).size()+Totoalline.get(17).size();
		
		User1Front_size=Totoalline.get(24).size()+Totoalline.get(25).size()+Totoalline.get(26).size()+Totoalline.get(27).size()+
	                    Totoalline.get(28).size()+Totoalline.get(29).size()+Totoalline.get(30).size()+Totoalline.get(31).size();
		
		User1Behind_size=Totoalline.get(36).size()+Totoalline.get(37).size()+Totoalline.get(38).size()+Totoalline.get(39).size()+
	                    Totoalline.get(40).size()+Totoalline.get(41).size()+Totoalline.get(42).size()+Totoalline.get(43).size();
		
	}
    
	public String Precompute(){         //做資料前運算
		
		try{
			//for(int i=0;i<Totoalline.size();i++){
					//System.out.println(i+": "+Totoalline.get(i));
			//}
			//==========================  先由固定長度做機率判斷  ==========================//
			//First scale
    		/*Collections.sort(User1DownLeft_User1DownRight);
			Statistics statistics1 =new Statistics(User1DownLeft_User1DownRight.
					subList(User1DownLeft_User1DownRight.size()-slide_windows+1, User1DownLeft_User1DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			
			//System.out.println("statistics1: "+statistics1.getMean()+" "+statistics1.getStdDev()+" "+statistics1.getCoeffVar());        //可靠度					
    		Collections.sort(User1DownRight_User1DownLeft);
			Statistics statistics2 =new Statistics(User1DownRight_User1DownLeft.
					subList(User1DownRight_User1DownLeft.size()-slide_windows+1, User1DownRight_User1DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)

			final_User1DownLeft_User1DownRight=(statistics1.getMean()*statistics1.getCoeffVar()+statistics2.getMean()*statistics2.getCoeffVar())
			/(statistics1.getCoeffVar()+statistics2.getCoeffVar());
			User1LeftRight_scale=Real_User1DownLeft_User1DownRight/final_User1DownLeft_User1DownRight;
			System.out.println("statistics2與User1可靠度 "+statistics2.getMean()+" "+statistics2.getStdDev()+" "+statistics2.getCoeffVar()
					+" "+final_User1DownLeft_User1DownRight+" "+User1LeftRight_scale);        //可靠度
			
			//Second scale
    		Collections.sort(User1DownFront_User1DownBehind);
			Statistics statistics3 =new Statistics(User1DownFront_User1DownBehind.
					subList(User1DownFront_User1DownBehind.size()-slide_windows+1, User1DownFront_User1DownBehind.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			
			//System.out.println("statistics1: "+statistics1.getMean()+" "+statistics1.getStdDev()+" "+statistics1.getCoeffVar());        //可靠度					
    		Collections.sort(User1DownBehind_User1DownFront);
			Statistics statistics4 =new Statistics(User1DownBehind_User1DownFront.
					subList(User1DownBehind_User1DownFront.size()-slide_windows+1, User1DownBehind_User1DownFront.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		

			final_User1DownFront_User1DownBehind=(statistics3.getMean()*statistics3.getCoeffVar()+statistics4.getMean()*statistics4.getCoeffVar())
			/(statistics3.getCoeffVar()+statistics4.getCoeffVar());
			User1FrontBehind_scale=Real_User1DownFront_User1DownBehind/final_User1DownFront_User1DownBehind;
			System.out.println("statistics4與User1可靠度 "+statistics4.getMean()+" "+statistics4.getStdDev()+" "+statistics4.getCoeffVar()
					+" "+final_User1DownFront_User1DownBehind+" "+User1FrontBehind_scale);        //可靠度
		    /////調整原本的長度//////////
			for(int i=2;i<=16;i=i+2){    //看查表
				for(int j=0;j<Totoalline.get(i).size();j++){
					Totoalline.get(i).set(j,Totoalline.get(i).get(j)*User1LeftRight_scale);
			}
				}
		for(int i=24;i<=30;i=i+2){    //看查表 12~15
			for(int j=0;j<Totoalline.get(i).size();j++){
			Totoalline.get(i).set(j,Totoalline.get(i).get(j)*User1FrontBehind_scale);
			}
			}
		for(int i=18;i<=42;i=i+2){    //看查表 18~21
			for(int j=0;j<Totoalline.get(i).size();j++){
			Totoalline.get(i).set(j,Totoalline.get(i).get(j)*User1FrontBehind_scale);
			}
			}
			*/
			
		for(int i=0;i<Totoalline.size();i=i+2){    //兩條雙線變成一條有唯一的值
			
			
			if((Totoalline.get(i).size() + Totoalline.get(i+1).size())>=2){      //雙條線變成一條線
			
			for(int j=0;j<Totoalline.get(i+1).size();j++){
				Totoalline.get(i).add(Totoalline.get(i+1).get(j));
			}			
			//Collections.sort(Totoalline.get(i));
			Statistics statistics5 =new Statistics(Totoalline.get(i));   //不做去頭去尾，如果size=10，就是取(0,9s)

			DistanceLine[i/2]=statistics5.getMean();
			}
			else if(Totoalline.get(i).size()!=0 || Totoalline.get(i+1).size()!=0){  //雙條線變成一條線
				for(int j=0;j<Totoalline.get(i+1).size();j++){
					Totoalline.get(i).add(Totoalline.get(i+1).get(j));
				}			
				Statistics statistics6 =new Statistics(Totoalline.get(i));
				DistanceLine[i/2]=statistics6.getMean();			
			}
		}
		
		
		System.out.println("DistanceLine: ");
		for(int i=0;i<DistanceLine.length;i++)
			System.out.print(i+":"+DistanceLine[i]+" ");
		
		for(int i=1;i<=4;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		if(Distance_count!=0){
		User1Left_Distance=(DistanceLine[1]+DistanceLine[2]+DistanceLine[3]+DistanceLine[4])/Distance_count;
		Distance_count=0;
		}else{
			Distance_count=0;
			User1Left_Distance=null;
		}
		
		for(int i=5;i<=8;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		if(Distance_count!=0){
		User1Right_Distance=(DistanceLine[5]+DistanceLine[6]+DistanceLine[7]+DistanceLine[8])/Distance_count;
		Distance_count=0;
		}else{
			Distance_count=0;
			User1Right_Distance=null;
		}

		for(int i=12;i<=15;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		if(Distance_count!=0){
		User1Front_Distance=(DistanceLine[12]+DistanceLine[13]+DistanceLine[14]+DistanceLine[15])/Distance_count;
		Distance_count=0;
		}else{
			Distance_count=0;
			User1Front_Distance=null;
		}
		
		for(int i=18;i<=21;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		if(Distance_count!=0){
		User1Behind_Distance=(DistanceLine[18]+DistanceLine[19]+DistanceLine[20]+DistanceLine[21])/Distance_count;
		Distance_count=0;
		}else{
			Distance_count=0;
			User1Behind_Distance=null;
		}
		

		
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Distance: "+User1Left_Distance+" "+User1Right_Distance+" "+User1Front_Distance+" "+User1Behind_Distance);
		//==================== 比較距離大小 ==================//
		
		Distance_compare.add(User1Left_Distance);Distance_compare.add(User1Right_Distance);
		Distance_compare.add(User1Front_Distance);Distance_compare.add(User1Behind_Distance);
		System.out.println("\n");
		System.out.println("Distance_compare: "+Distance_compare);
		Collections.sort(Distance_compare);
		System.out.println("Distance_compare(sort後): "+Distance_compare);
		/*compare_count=compare_count+1;
    	FileWriter fw1;
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
						+ "/"+"compare_2.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw1);
			bufferedWriter.write(compare_count+":");
			//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(User1Left_Distance<User1Right_Distance){
			User1Left_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("L"+User1Left_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Left_Distance<User1Front_Distance){
			User1Left_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("L"+User1Left_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Left_Distance<User1Behind_Distance){
			User1Left_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("L"+User1Left_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Right_Distance < User1Left_Distance){
			User1Right_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("R"+User1Right_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Left_Distance < User1Front_Distance){
			User1Right_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("R"+User1Right_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Right_Distance < User1Behind_Distance){
			User1Right_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("R"+User1Right_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Front_Distance < User1Left_Distance){
			User1Front_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("F"+User1Front_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Front_Distance < User1Right_Distance){
			User1Front_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("F"+User1Front_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Front_Distance < User1Behind_Distance){
			User1Front_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("F"+User1Front_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Behind_Distance < User1Left_Distance){
			User1Behind_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("B"+User1Behind_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Behind_Distance < User1Right_Distance){
			User1Behind_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("B"+User1Behind_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if(User1Behind_Distance < User1Front_Distance){
			User1Behind_count++;
	    	//FileWriter fw1;
			   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/"+"compare_2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write("B"+User1Behind_count);
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
						+ "/"+"compare_2.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw1);
			bufferedWriter.write("\n");
			//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(User1Left_count >User1Right_count &&User1Left_count >User1Front_count &&User1Left_count >User1Behind_count){
			User1Left_count=0;User1Right_count=0;User1Front_count=0;User1Behind_count=0;
			return "MinUser1Left";
		}
		else if(User1Right_count >User1Left_count&&User1Right_count >User1Front_count &&User1Right_count >User1Behind_count){
			User1Left_count=0;User1Right_count=0;User1Front_count=0;User1Behind_count=0;
			return "MinUser1Right";
		}
		else if(User1Behind_count >User1Left_count&&User1Behind_count >User1Right_count &&User1Behind_count >User1Front_count){
			User1Left_count=0;User1Right_count=0;User1Front_count=0;User1Behind_count=0;
			return "MinUser1Behind";
		}
		else if(User1Front_count >User1Left_count&&User1Front_count >User1Right_count &&User1Front_count >User1Behind_count){
			User1Left_count=0;User1Right_count=0;User1Front_count=0;User1Behind_count=0;
			return "MinUser1Front";
		}
		else {
			User1Left_count=0;User1Right_count=0;User1Front_count=0;User1Behind_count=0;
			return "No_decision";
		}*/
		
		if(Distance_compare.get(0).equals(User1Left_Distance)){
			Distance_compare.clear();
			return "MinUser1Left";
		}else if(Distance_compare.get(0).equals(User1Right_Distance)){
			Distance_compare.clear();
			return "MinUser1Right";	
		}else if(Distance_compare.get(0).equals(User1Front_Distance)){
			Distance_compare.clear();
			return "MinUser1Front";	
		}else if(Distance_compare.get(0).equals(User1Behind_Distance)){
			Distance_compare.clear();
			return "MinUser1Behind";
		}
		else{
			return "No_decision";
		}
		
	}
	public double intRssi_to_distance(int intrssi,int index){
		
		
		Distance=Math.pow(10, 1.5*(intrssi/Measurepower_state1357[index])-1);
		if(Distance>=100.0){
			return 100.0;
		}
		else{
		return  new BigDecimal(Distance).setScale(2,RoundingMode.HALF_UP).doubleValue(); // 運用General purpose method 75.64代表1m量測到的RSSI，回傳公分
		}	
	}
	
	

}

