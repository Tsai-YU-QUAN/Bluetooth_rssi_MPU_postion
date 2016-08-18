//**********6.16 早上測試4個State，但只用到四個裝置
//*********7.4開始用8顆寫互相定位(自+公) Front=>左 Behind=>右
package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;





public class Experiment_OutputExcelRevolution2 {
	   static Channel channel;
       static Connection connection;
	   static Channel channel_publish;
	   String Prestate="", Currentstate="3";
	   String text_name="公轉270_n=10_自轉0",time_name="公轉270_n=10_自轉0時間";
	   double User1LeftRight_scale=0.0,User1FrontBehind_scale=0.0,User2LeftRight_scale=0.0,User2FrontBehind_scale=0.0;
	   static long starttime,currenttime,startAlgotime,endAlgotime,decisiontime,endslotime;
	   static int OK_times=0, fail_times;
	   int slide_windows=3, min_window=3;
	   double Real_final_User2DownRight_User2DownLeft=100;     //100CM
	   double final_User1DownLeft_User1DownRight,final_User1DownFront_User1DownBehind;
	   static int count=0;    //for realtime compute
	   static int time_slot=250;
	   static int Distance_count=0;
	   static String distanceString0="",distanceString1="",distanceString2="",distanceString3="",
			         distanceString4="",distanceString5="",distanceString6="",distanceString7="";

	   static int rssi_count=1;
	   static HSSFWorkbook workbook_rssi = new HSSFWorkbook();
	   static HSSFSheet sheet_rssi = workbook_rssi.createSheet("twelve_rssi");
	   static FileOutputStream fileOut;
	   static String filename_rssi;;
	   static Row row;
	   
	   private static final String TOPIC_location = "wise.position";    
	   private static final String TOPIC_location2 = "wise.position2";  // 測試用
	   private static boolean if_init=false;
	   private static boolean if_restartime=false;
	   
	   //Tim=user1   , Jack=user2   //8個節點  56條線
	   static ArrayList<ArrayList<Double>> Totoalline = new ArrayList<ArrayList<Double>>();
	   static ArrayList<Double> User1DownLeft_User1DownRight = new ArrayList<Double>(); static ArrayList<Double> User1DownRight_User1DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User1DownLeft_User2DownLeft = new ArrayList<Double>();  static ArrayList<Double> User2DownLeft_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownLeft_User2DownRight = new ArrayList<Double>(); static ArrayList<Double> User2DownRight_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownLeft_User2DownFront = new ArrayList<Double>();  static ArrayList<Double> User2DownFront_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownLeft_User2DownBehind = new ArrayList<Double>();  static ArrayList<Double> User2DownBehind_User1DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownLeft = new ArrayList<Double>(); static ArrayList<Double> User2DownLeft_User1DownRight  = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownRight = new ArrayList<Double>(); static ArrayList<Double> User2DownRight_User1DownRight = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownFront = new ArrayList<Double>();  static ArrayList<Double> User2DownFront_User1DownRight = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownBehind = new ArrayList<Double>();  static ArrayList<Double> User2DownBehind_User1DownRight = new ArrayList<Double>();

	   static ArrayList<Double> User1DownFront_User1DownLeft = new ArrayList<Double>();  static ArrayList<Double> User1DownLeft_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User1DownRight = new ArrayList<Double>(); static ArrayList<Double> User1DownRight_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User1DownBehind = new ArrayList<Double>();static ArrayList<Double> User1DownBehind_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownLeft = new ArrayList<Double>();  static ArrayList<Double> User2DownLeft_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownRight = new ArrayList<Double>(); static ArrayList<Double> User2DownRight_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2DownFront_User1DownFront = new ArrayList<Double>();
	   static ArrayList<Double> User1DownFront_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User1DownFront = new ArrayList<Double>();	   
	   static ArrayList<Double> User1DownBehind_User1DownLeft = new ArrayList<Double>();  static ArrayList<Double> User1DownLeft_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User1DownRight = new ArrayList<Double>(); static ArrayList<Double> User1DownRight_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownLeft = new ArrayList<Double>();  static ArrayList<Double> User2DownLeft_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownRight = new ArrayList<Double>(); static ArrayList<Double> User2DownRight_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2DownFront_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User1DownBehind_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User1DownBehind = new ArrayList<Double>();
	   static ArrayList<Double> User2DownLeft_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2DownFront_User2DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownLeft_User2DownBehind = new ArrayList<Double>(); static ArrayList<Double> User2DownBehind_User2DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2DownFront_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownBehind = new ArrayList<Double>(); static ArrayList<Double> User2DownBehind_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownLeft = new ArrayList<Double>(); static ArrayList<Double> User2DownLeft_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownFront_User2DownBehind = new ArrayList<Double>();static ArrayList<Double> User2DownBehind_User2DownFront = new ArrayList<Double>();

	   
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
	   
	   
	   
	   
	   
	   static int User1DownLeft_User1DownRight_size=-1;//User1DownLeft_User1DownRight_presize=-2;    //六個節點  30條線
	   static int User1DownRight_User1DownLeft_size=-1;//User1DownRight_User1DownLeft_presize=-2;
	   static int User2DownRight_User2DownLeft_size=-1;//User2DownRight_User2DownLeft_presize=-2;
	   static int User2DownLeft_User2DownRight_size=-1;//User2DownLeft_User2DownRight_presize=-2;	   
	   static int User1DownLeft_User2DownLeft_size=-1;//User1DownLeft_User2DownLeft_presize=-2;   
	   static int User2DownLeft_User1DownLeft_size=-1;//User2DownLeft_User1DownLeft_presize=-2;	   
	   static int User1DownLeft_User2DownRight_size=-1;//User1DownLeft_User2DownRight_presize=-2;
	   static int User2DownRight_User1DownLeft_size=-1;//User2DownRight_User1DownLeft_presize=-2;	  
	   static int User2DownLeft_User1DownRight_size=-1;//User2DownLeft_User1DownRight_presize=-2;
	   static int User1DownRight_User2DownLeft_size=-1;//User1DownRight_User2DownLeft_presize=-2;
	   static int User1DownRight_User2DownRight_size=-1;//User1DownRight_User2DownRight_presize=-2;
	   static int User2DownRight_User1DownRight_size=-1;//User2DownRight_User1DownRight_presize=-2;
	   
	   




	   
	   static ArrayList<Double> CoeffVarRank = new ArrayList<Double>();//大到小
	   double [] DistanceLine= new double[28];
	   static ArrayList<Double> Distance_compare= new ArrayList<Double>();
	   static ArrayList<Double> Measurepower = new ArrayList<Double>();//大到小
	   static int compare_count=0;
	   double  [] Measurepower_state3 ={35.67121571,35.11919496,38.62570303,33.79520093,38.28567975,33.9023951,40.54102715,34.34922062,
			                            35.18025015,33.79520093,45.39972909,35.46145485,35.11427548,36.39115823,29.57674348,34.19719854,
			                            32.42837792,35.40705721,37.67333874,39.58336509,31.66484791,28.89073669,31.85613203,36.39115823,
			                            35.11427548,35.11427548,36.85042946,33.54973887,39.88941749, 35.18025015,32.79518005,38.0296432};
	   double  [] Measurepower_state7 ={38.32444663,36.55850623,38.78137812,31.12401691,36.27064397,31.24916418,36.01127968,35.11427548,
			                            37.66804097,42.77557195,37.73483976,36.86056837,30.47108281,31.57912218, 32.52855257, 31.54396761,
			                            32.13357449,31.66484791,34.15661343,32.13314187,36.27064397,33.31278823,31.60284794, 37.11931906,
			                            40.44343718,34.90324031,32.42837792,37.32784721,35.43349617,35.75271686,36.19840247,42.74649817};
	   double [] Measurepower_state3_7={36.99783117,35.83885059,38.70354058,32.45960892,37.27816186,32.57577964,38.27615342,34.73174805,
			                            36.42414556,38.28538644,41.56728443,36.16101161,32.79267915,33.98514021,31.05264803,32.87058307,
			                            32.2809762,33.53595256,35.91497608,35.85825348,33.96774594,31.10176246,31.72948998,36.75523864,
			                            37.77885633,35.0087579,34.63940369,35.43879304,37.66145683,35.4664835,34.49679126,40.38807068};
	   double [] Measurepower_state1_5={38.93967308,37.58452768,34.95250855,36.07692452,37.35604134,30.4949247,34.42594091,31.95485754,
			                            36.73653476,40.70747278,40.03436268,37.04245281,38.72993364,32.93598186,30.38638707,39.09605731,
			                            34.2481462,34.99005393,33.27222839,36.82310258,33.82258898,36.28114118,30.52101373,34.08069741,
			                            38.98883332,39.23187324,35.03006735,37.18714989,35.8396017,36.00334289,34.43790534,38.15775193};
	   double [] Measurepower_state1357={37.96875213,36.71168914,36.82802456,34.26826672,37.3171016,31.53535217,36.35104716,33.3433028,
			                             36.58034016,39.49642961,40.80082355,36.60173221,35.76130639,33.46056103,30.71951755,35.98332019,
			                             33.2645612,34.26300324,34.59360224,36.34067803,33.89516746,33.69145182,31.12525185,35.41796803,
			                             38.38384482,37.12031557,34.83473552,36.31297147,36.75052927,35.73491319,34.4673483,39.27291131};
	   
	   
	   private static FileWriter fw1,fw2,fw3,fw4;
		private static String finaljson;
		



    public void Revolution_processing(String posedata)
    
    { 
    	FileWriter fw1;
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
						+ "/"+"arduino有沒有資料測試.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw1);
			bufferedWriter.write(posedata+"\n");
			//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
    	if(if_init==false){   //先初始化，以後要判斷是哪一個state需要查表
    	Totoalline.add(User1DownLeft_User1DownRight);Totoalline.add(User1DownRight_User1DownLeft);   //0
    	Totoalline.add(User1DownLeft_User2DownLeft);Totoalline.add(User2DownLeft_User1DownLeft);     //1
    	Totoalline.add(User1DownLeft_User2DownRight);Totoalline.add(User2DownRight_User1DownLeft);   //2
    	Totoalline.add(User1DownLeft_User2DownFront);Totoalline.add(User2DownFront_User1DownLeft);   //3
    	Totoalline.add(User1DownLeft_User2DownBehind);Totoalline.add(User2DownBehind_User1DownLeft); //4
    	Totoalline.add(User1DownRight_User2DownLeft);Totoalline.add(User2DownLeft_User1DownRight);   //5
    	Totoalline.add(User1DownRight_User2DownRight);Totoalline.add(User2DownRight_User1DownRight); //6
    	Totoalline.add(User1DownRight_User2DownFront);Totoalline.add(User2DownFront_User1DownRight); //7
    	Totoalline.add(User1DownRight_User2DownBehind);Totoalline.add(User2DownBehind_User1DownRight);//8
    	Totoalline.add(User1DownFront_User1DownLeft);Totoalline.add(User1DownLeft_User1DownFront);    //9
    	Totoalline.add(User1DownFront_User1DownRight);Totoalline.add(User1DownRight_User1DownFront);  //10
    	Totoalline.add(User1DownFront_User1DownBehind);Totoalline.add(User1DownBehind_User1DownFront);//11
    	Totoalline.add(User1DownFront_User2DownLeft);Totoalline.add(User2DownLeft_User1DownFront);    //12
    	Totoalline.add(User1DownFront_User2DownRight);Totoalline.add(User2DownRight_User1DownFront);  //13
    	Totoalline.add(User1DownFront_User2DownFront);Totoalline.add(User2DownFront_User1DownFront);  //14
    	Totoalline.add(User1DownFront_User2DownBehind);Totoalline.add(User2DownBehind_User1DownFront);//15
    	Totoalline.add(User1DownBehind_User1DownLeft);Totoalline.add(User1DownLeft_User1DownBehind);  //16
    	Totoalline.add(User1DownBehind_User1DownRight);Totoalline.add(User1DownRight_User1DownBehind);//17
    	Totoalline.add(User1DownBehind_User2DownLeft);Totoalline.add(User2DownLeft_User1DownBehind);  //18
    	Totoalline.add(User1DownBehind_User2DownRight);Totoalline.add(User2DownRight_User1DownBehind);//19
    	Totoalline.add(User1DownBehind_User2DownFront);Totoalline.add(User2DownFront_User1DownBehind);//20
    	Totoalline.add(User1DownBehind_User2DownBehind);Totoalline.add(User2DownBehind_User1DownBehind);//21
    	Totoalline.add(User2DownLeft_User2DownFront);Totoalline.add(User2DownFront_User2DownLeft);      //22
    	Totoalline.add(User2DownLeft_User2DownBehind);Totoalline.add(User2DownBehind_User2DownLeft);    //23
    	Totoalline.add(User2DownRight_User2DownFront);Totoalline.add(User2DownFront_User2DownRight);     //24
    	Totoalline.add(User2DownRight_User2DownBehind);Totoalline.add(User2DownBehind_User2DownRight);  //25
    	Totoalline.add(User2DownRight_User2DownLeft);Totoalline.add(User2DownLeft_User2DownRight);      //26
    	Totoalline.add(User2DownFront_User2DownBehind);Totoalline.add(User2DownBehind_User2DownFront);  //27
    	

    	
    	
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
        starttime=System.currentTimeMillis();
        if_restartime=true;
    	}
    	
        
    	nodeName=posedata.substring(0, posedata.indexOf(":"));
    	Clear_MAC=posedata.substring(posedata.length()-11, posedata.length()-3);
    	RSSI=posedata.substring(posedata.length()-2, posedata.length());
    	System.out.println("posedata: "+nodeName+" "+Clear_MAC+" "+RSSI); 
    	//starttime=1465827022898L;
    	
    	currenttime=System.currentTimeMillis();
    	if((currenttime-starttime)<=(time_slot*rssi_count)){
    	System.out.println("ExcelTime: "+(currenttime-starttime));
		row = sheet_rssi.createRow((short)rssi_count);
		row.createCell(0).setCellValue(time_slot*rssi_count);
    	}
    	else{
			row.createCell(1).setCellValue(distanceString0);row.createCell(2).setCellValue(distanceString1);
			row.createCell(3).setCellValue(distanceString2);row.createCell(4).setCellValue(distanceString3);
			row.createCell(5).setCellValue(distanceString4);row.createCell(6).setCellValue(distanceString5);
			row.createCell(7).setCellValue(distanceString6);row.createCell(8).setCellValue(distanceString7);
			

			distanceString0="";distanceString1="";distanceString2="";distanceString3="";
			distanceString4="";distanceString5="";distanceString6="";distanceString7="";
			try {
				filename_rssi = "/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/FullBleTable_2.xls";
				fileOut = new FileOutputStream(filename_rssi);
				workbook_rssi.write(fileOut);
            //fileOut.close();
	        } catch ( Exception ex ) {
	            System.out.println(ex);
	        }
			
			

    		rssi_count=rssi_count+1;
    	}
		
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
    					User1DownFront_User2DownLeft.add(intRssi_to_distance(intrssi,2));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,2)+"(cA),";

    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[2].equals(Clear_MAC)){
    					User1DownFront_User2DownRight.add(intRssi_to_distance(intrssi,3));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,3)+"(cB),";
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Front[3].equals(Clear_MAC)){
    					User1DownFront_User2DownFront.add(intRssi_to_distance(intrssi,0));
    					distanceString0=distanceString0+intRssi_to_distance(intrssi,0)+"(cC),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Front[4].equals(Clear_MAC)){
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
    					User1DownBehind_User2DownLeft.add(intRssi_to_distance(intrssi,6));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,6)+"(dA),";

    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[2].equals(Clear_MAC)){
    					User1DownBehind_User2DownRight.add(intRssi_to_distance(intrssi,7));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,7)+"(dB),";
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Behind[3].equals(Clear_MAC)){
    					User1DownBehind_User2DownFront.add(intRssi_to_distance(intrssi,4));
    					distanceString1=distanceString1+intRssi_to_distance(intrssi,4)+"(dC),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Behind[4].equals(Clear_MAC)){
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
    					User1DownLeft_User2DownLeft.add(intRssi_to_distance(intrssi,10));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,10)+"(aA),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[2].equals(Clear_MAC)){
    					User1DownLeft_User2DownRight.add(intRssi_to_distance(intrssi,11));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,11)+"(aB),";
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Left[3].equals(Clear_MAC)){
    					User1DownLeft_User2DownFront.add(intRssi_to_distance(intrssi,8));
    					distanceString2=distanceString2+intRssi_to_distance(intrssi,8)+"(aC),";
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Left[4].equals(Clear_MAC)){
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
    			        User1DownRight_User2DownLeft.add(intRssi_to_distance(intrssi,14));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,14)+"(bA),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[2].equals(Clear_MAC)){
    			        User1DownRight_User2DownRight.add(intRssi_to_distance(intrssi,15));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,15)+"(bB),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[3].equals(Clear_MAC)){
    			        User1DownRight_User2DownFront.add(intRssi_to_distance(intrssi,12));
    					distanceString3=distanceString3+intRssi_to_distance(intrssi,12)+"(bC),";
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[4].equals(Clear_MAC)){
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
    					User2DownFront_User1DownLeft.add(intRssi_to_distance(intrssi,18));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,18)+"(Ca),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Front[2].equals(Clear_MAC)){
    					User2DownFront_User1DownRight.add(intRssi_to_distance(intrssi,19));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,19)+"(Cb),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Front[3].equals(Clear_MAC)){
    					User2DownFront_User1DownFront.add(intRssi_to_distance(intrssi,16));
    					distanceString4=distanceString4+intRssi_to_distance(intrssi,16)+"(Cc),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Front[4].equals(Clear_MAC)){
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
    					User2DownBehind_User1DownLeft.add(intRssi_to_distance(intrssi,22));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,22)+"(Da),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Behind[2].equals(Clear_MAC)){
    					User2DownBehind_User1DownRight.add(intRssi_to_distance(intrssi,23));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,23)+"(Db),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[3].equals(Clear_MAC)){
    					User2DownBehind_User1DownFront.add(intRssi_to_distance(intrssi,20));
    					distanceString5=distanceString5+intRssi_to_distance(intrssi,20)+"(Dc),";
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[4].equals(Clear_MAC)){
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
    					User2DownLeft_User1DownLeft.add(intRssi_to_distance(intrssi,26));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,26)+"(Aa),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[1].equals(Clear_MAC)){
    					User2DownLeft_User1DownRight.add(intRssi_to_distance(intrssi,27));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,27)+"(Ab),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[2].equals(Clear_MAC)){
    					User2DownLeft_User1DownFront.add(intRssi_to_distance(intrssi,24));
    					distanceString6=distanceString6+intRssi_to_distance(intrssi,24)+"(Ac),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[3].equals(Clear_MAC)){
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
    					User2DownRight_User1DownLeft.add(intRssi_to_distance(intrssi,30));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,30)+"(Ba),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[1].equals(Clear_MAC)){
    					User2DownRight_User1DownRight.add(intRssi_to_distance(intrssi,31));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,31)+"(Bb),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[2].equals(Clear_MAC)){
    					User2DownRight_User1DownFront.add(intRssi_to_distance(intrssi,28));
    					distanceString7=distanceString7+intRssi_to_distance(intrssi,28)+"(Bc),";
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[3].equals(Clear_MAC)){
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
 	    System.out.println("currenttimeendslotime: "+(currenttime - endslotime));
 	   if(currenttime - endslotime >=time_slot){     //每隔1s進行偵測是否要算

 	 //=================================以下會有16種相互自轉判斷組合====================================//			
 			LimitSize();
 	        System.out.print("LimitSize: "+User1Behind_size+" "+User1Front_size+" "+User1Left_size+" "+User1Right_size);
 			if(User1Behind_size>=1 &&User1Front_size>=1&&User1Left_size>=1&&User1Right_size>=1){
 	 		startAlgotime=System.currentTimeMillis();
 		    String Mindirection=Precompute();    //做資料前運算，目前版本就直接平均，並算出User1距離User2之間大小關係
    		
     //=================================用機率估算目前已知距離=========================================//
    	/*	
    		Collections.sort(User1DownLeft_User1DownRight);
			Statistics statistics1 =new Statistics(User1DownLeft_User1DownRight.
					subList(User1DownLeft_User1DownRight.size()-slide_windows+1, User1DownLeft_User1DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			

			//System.out.println("statistics1: "+statistics1.getMean()+" "+statistics1.getStdDev()+" "+statistics1.getCoeffVar());        //可靠度
			
			
    		Collections.sort(User1DownRight_User1DownLeft);
			Statistics statistics2 =new Statistics(User1DownRight_User1DownLeft.
					subList(User1DownRight_User1DownLeft.size()-slide_windows+1, User1DownRight_User1DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			

			final_User1DownLeft_User1DownRight=(statistics1.getMean()*statistics1.getCoeffVar()+statistics2.getMean()*statistics2.getCoeffVar())
			/(statistics1.getCoeffVar()+statistics2.getCoeffVar());
			User1adjsut_scale=Real_User1DownLeft_User1DownRight/final_User1DownLeft_User1DownRight;
			System.out.println("statistics2與User1可靠度 "+statistics2.getMean()+" "+statistics2.getStdDev()+" "+statistics2.getCoeffVar()
					+" "+final_User1DownLeft_User1DownRight+" "+User1adjsut_scale);        //可靠度
			//DistanceRank.add(final_User1DownLeft_User1DownRight*User1adjsut_scale);
			
			
			
    		Collections.sort(User2DownLeft_User2DownRight);
			Statistics statistics3 =new Statistics(User2DownLeft_User2DownRight.
					subList(User2DownLeft_User2DownRight.size()-slide_windows+1, User2DownLeft_User2DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			

			System.out.println("statistics3: "+statistics3.getMean()+" "+statistics3.getStdDev()+" "+statistics3.getCoeffVar());        //可靠度
			
			
    		Collections.sort(User2DownRight_User2DownLeft);
			Statistics statistics4 =new Statistics(User2DownRight_User2DownLeft.
					subList(User2DownRight_User2DownLeft.size()-slide_windows+1, User2DownRight_User2DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			

			final_User2DownRight_User2DownLeft=(statistics3.getMean()*statistics3.getCoeffVar()+statistics4.getMean()*statistics4.getCoeffVar())
			/(statistics3.getCoeffVar()+statistics4.getCoeffVar());
			User2adjsut_scale=Real_final_User2DownRight_User2DownLeft/final_User2DownRight_User2DownLeft;
			System.out.println("statistics4與User2可靠度 "+statistics4.getMean()+" "+statistics4.getStdDev()+" "+statistics4.getCoeffVar()
					+" "+final_User2DownRight_User2DownLeft+" "+User2adjsut_scale);        //可靠度
			//DistanceRank.add(final_User2DownRight_User2DownLeft*User2adjsut_scale);

			
			Collections.sort(User1DownLeft_User2DownLeft);
			Statistics statistics5 =new Statistics(User1DownLeft_User2DownLeft.
					subList(User1DownLeft_User2DownLeft.size()-slide_windows+1, User1DownLeft_User2DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics5: "+statistics5.getMean()+" "+statistics5.getStdDev()+" "+statistics5.getCoeffVar());        //可靠度
			
    		Collections.sort(User2DownLeft_User1DownLeft);
			Statistics statistics6 =new Statistics(User2DownLeft_User1DownLeft.
					subList(User2DownLeft_User1DownLeft.size()-slide_windows+1, User2DownLeft_User1DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownLeft_User2DownLeft=(User1adjsut_scale*statistics5.getMean()*statistics5.getCoeffVar()+User2adjsut_scale*statistics6.getMean()*statistics6.getCoeffVar())
			/(statistics5.getCoeffVar()+statistics6.getCoeffVar());
			System.out.println("statistics6 "+statistics6.getMean()+" "+statistics6.getStdDev()+" "+statistics6.getCoeffVar()
					+" "+final_User1DownLeft_User2DownLeft);        //可靠度
			DistanceRank.add(final_User1DownLeft_User2DownLeft);

			
			Collections.sort(User1DownLeft_User2DownRight);
			Statistics statistics7 =new Statistics(User1DownLeft_User2DownRight.
					subList(User1DownLeft_User2DownRight.size()-slide_windows+1, User1DownLeft_User2DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics7: "+statistics7.getMean()+" "+statistics7.getStdDev()+" "+statistics7.getCoeffVar());        //可靠度
			
    		Collections.sort(User2DownRight_User1DownLeft);
			Statistics statistics8 =new Statistics(User2DownRight_User1DownLeft.
					subList(User2DownRight_User1DownLeft.size()-slide_windows+1, User2DownRight_User1DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownLeft_User2DownRight=(User1adjsut_scale*statistics7.getMean()*statistics7.getCoeffVar()+User2adjsut_scale*statistics8.getMean()*statistics8.getCoeffVar())
			/(statistics7.getCoeffVar()+statistics8.getCoeffVar());
			System.out.println("statistics8 "+statistics8.getMean()+" "+statistics8.getStdDev()+" "+statistics8.getCoeffVar()
					+" "+final_User1DownLeft_User2DownRight);        //可靠度
			DistanceRank.add(final_User1DownLeft_User2DownRight);
			
			Collections.sort(User1DownRight_User2DownLeft);
			Statistics statistics9 =new Statistics(User1DownRight_User2DownLeft.
					subList(User1DownRight_User2DownLeft.size()-slide_windows+1, User1DownRight_User2DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics9: "+statistics9.getMean()+" "+statistics9.getStdDev()+" "+statistics9.getCoeffVar());        //可靠度
			
    		Collections.sort(User2DownLeft_User1DownRight);
			Statistics statistics10 =new Statistics(User2DownLeft_User1DownRight.
					subList(User2DownLeft_User1DownRight.size()-slide_windows+1, User2DownLeft_User1DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownRight_User2DownLeft=(User1adjsut_scale*statistics9.getMean()*statistics9.getCoeffVar()+User2adjsut_scale*statistics10.getMean()*statistics10.getCoeffVar())
			/(statistics9.getCoeffVar()+statistics10.getCoeffVar());
			System.out.println("statistics10 "+statistics10.getMean()+" "+statistics10.getStdDev()+" "+statistics10.getCoeffVar());        //可靠度
			DistanceRank.add(final_User1DownRight_User2DownLeft);
			for(double temp :User2DownRight_User1DownRight)
				System.out.println("User1DownRight_User2DownRight: "+temp);
	
			Collections.sort(User1DownRight_User2DownRight);
			Statistics statistics11 =new Statistics(User1DownRight_User2DownRight.
					subList(User1DownRight_User2DownRight.size()-slide_windows+1, User1DownRight_User2DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics11: "+statistics11.getMean()+" "+statistics11.getStdDev()+" "+statistics11.getCoeffVar());        //可靠度
			

			
    		Collections.sort(User2DownRight_User1DownRight);
			Statistics statistics12 =new Statistics(User2DownRight_User1DownRight.
					subList(User2DownRight_User1DownRight.size()-slide_windows+1, User2DownRight_User1DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownRight_User2DownRight=(User1adjsut_scale*statistics11.getMean()*statistics11.getCoeffVar()+User2adjsut_scale*statistics12.getMean()*statistics12.getCoeffVar())
			/(statistics11.getCoeffVar()+statistics12.getCoeffVar());
			System.out.println("statistics12 "+statistics12.getMean()+" "+statistics12.getStdDev()+" "+statistics12.getCoeffVar()
					+" "+final_User1DownRight_User2DownRight+" "+starttime);        //可靠度
			DistanceRank.add(final_User1DownRight_User2DownRight);
			
    		Collections.sort(User1DownLeft_User2DownFront);
			Statistics statistics13 =new Statistics(User1DownLeft_User2DownFront.
					subList(User1DownLeft_User2DownFront.size()-slide_windows+1, User1DownLeft_User2DownFront.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownLeft_User2DownFront=(User1adjsut_scale*statistics13.getMean());
			System.out.println("statistics13 "+statistics13.getMean()+" "+statistics13.getStdDev()+" "+statistics13.getCoeffVar()
					+" "+final_User1DownLeft_User2DownFront+" "+starttime);        //可靠度
			DistanceRank.add(final_User1DownLeft_User2DownFront);
			
    		Collections.sort(User1DownLeft_User2DownBehind);
			Statistics statistics14 =new Statistics(User1DownLeft_User2DownBehind.
					subList(User1DownLeft_User2DownBehind.size()-slide_windows+1, User1DownLeft_User2DownBehind.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownLeft_User2DownBehind=(User1adjsut_scale*statistics14.getMean());
			System.out.println("statistics14 "+statistics14.getMean()+" "+statistics14.getStdDev()+" "+statistics14.getCoeffVar()
					+" "+final_User1DownRight_User2DownRight+" "+starttime);        //可靠度
			DistanceRank.add(final_User1DownLeft_User2DownBehind);
			
    		Collections.sort(User1DownRight_User2DownFront);
			Statistics statistics15 =new Statistics(User1DownRight_User2DownFront.
					subList(User1DownRight_User2DownFront.size()-slide_windows+1, User1DownRight_User2DownFront.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownRight_User2DownFront=(User1adjsut_scale*statistics15.getMean());
			System.out.println("statistics15 "+statistics15.getMean()+" "+statistics15.getStdDev()+" "+statistics15.getCoeffVar()
					+" "+final_User1DownRight_User2DownFront+" "+starttime);        //可靠度
			DistanceRank.add(final_User1DownRight_User2DownFront);
			
    		Collections.sort(User1DownRight_User2DownBehind);
			Statistics statistics16 =new Statistics(User1DownRight_User2DownBehind.
					subList(User1DownRight_User2DownBehind.size()-slide_windows+1, User1DownRight_User2DownBehind.size()-1));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownRight_User2DownBehind=(User1adjsut_scale*statistics16.getMean());
			System.out.println("statistics16 "+statistics16.getMean()+" "+statistics16.getStdDev()+" "+statistics16.getCoeffVar()
					+" "+final_User1DownRight_User2DownRight+" "+starttime);        //可靠度
			DistanceRank.add(final_User1DownRight_User2DownBehind);
			*/
			//=========================根據User2自轉之後會有16種判斷==========================//
 		        decisiontime=System.currentTimeMillis();
 		        System.out.println("currenttimedecisiontime: "+(decisiontime-starttime));
	  			   try {
	  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
	  							+ "/"+"Decision_2.txt",true);
	  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
	  				bufferedWriter.write((Mindirection+" "+(decisiontime-starttime))+"\n");
	  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
	  				bufferedWriter.flush();
	  				bufferedWriter.close();
	  				} catch (IOException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}
 				if(Mindirection.equals("MinUser1Behind")){ 
 	   				Currentstate="state1";
    				System.out.println("state1");
 					
 				}else if(Mindirection.equals("MinUser1Right")){
    				Currentstate="state3";
    				System.out.println("state3");
    				
    				///******輸出到Unity******///
    				 //finaljson = "{" + "Position"+": { x:"+1+", y:"+0+", z:"+0+ "} }";
    		         //channel.basicPublish(TOPIC_location, "", null, finaljson.getBytes());
    	  			   try {
    	  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
    	  							+ "/"+ text_name+".txt",true);
    	  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
    	  				bufferedWriter.write(("1")+"\n");
    	  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
    	  				bufferedWriter.flush();
    	  				bufferedWriter.close();
    	  				} catch (IOException e) {
    	  					// TODO Auto-generated catch block
    	  					e.printStackTrace();
    	  				}
    	   			   
			    	}
			    	else if(Mindirection.equals("MinUser1Front")){
	    				Currentstate="states5";
	    				System.out.println("state5");
			    		
			    	}
			    	else if(Mindirection.equals("MinUser1Left")){
	    				Currentstate="states7";
	    				System.out.println("state7");
			    	}
			    	else{
	    				System.out.println("No_decision");
			    		
			    	}
			    	
			    	
			    
    			
			    
 			endAlgotime=System.currentTimeMillis();
    		System.out.println("algo執行時間:"+(endAlgotime-startAlgotime));	 //目前algo時間 0.02s

    		
       for(int i=0;i<Totoalline.size();i++){   //Rest Array
    	   Totoalline.get(i).clear();
       }
       //System.exit(1);
       } //Rssi個數要過門檻
 			endslotime=System.currentTimeMillis();
       //if_restartime=false;
 	    }  //time slot做判斷
        //}
    	
    	
        channel.close();
        connection.close();
        
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
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
		
		//===================== 清空資料 ====================//		
	}
	
	public double intRssi_to_distance(int intrssi,int index){
		
		
		Distance=Math.pow(10, 1.5*(intrssi/Measurepower_state3_7[index])-1);
		if(Distance>=100.0){
			return 100.0;
		}
		else{
		return  new BigDecimal(Distance).setScale(2,RoundingMode.HALF_UP).doubleValue(); // 運用General purpose method 75.64代表1m量測到的RSSI，回傳公分
		}	
	}
	
	

}

