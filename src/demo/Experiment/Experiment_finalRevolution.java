//**********6.16 早上測試4個State，但只用到四個裝置
//*********7.4開始用8顆寫互相定位(自+公) Front=>左 Behind=>右
package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import apple.laf.JRSUIConstants.Size;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import demo.Experiment.Experiment_Globalvariable;





public class Experiment_finalRevolution {
	   static Channel channel;
       static Connection connection;
	   static Channel channel_publish;
	   String Prestate="", Currentstate="3";
	   String text_name="公轉270_n=10_自轉0",time_name="公轉270_n=10_自轉0時間";
	   double User1LeftRight_scale=0.0,User1FrontBehind_scale=0.0,User2LeftRight_scale=0.0,User2FrontBehind_scale=0.0;
	   static long starttime,currenttime;
	   static int OK_times=0, fail_times;
	   int slide_windows=3, min_window=3;
	   double Real_User1DownLeft_User1DownRight=30,Real_User1DownFront_User1DownBehind=22;    //30,22CM
	   double Real_final_User2DownRight_User2DownLeft=100;     //100CM
	   double final_User1DownLeft_User1DownRight,final_User1DownFront_User1DownBehind;
	   static int count=0;    //for realtime compute
	   static int Distance_count=0;
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
	   static ArrayList<Double> User2DownRight_User2DownFront = new ArrayList<Double>(); static ArrayList<Double> User2Downront_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownRight_User2DownBehind = new ArrayList<Double>(); static ArrayList<Double> User2DownBehund_User2DownRight = new ArrayList<Double>();       //校正用
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
	 //static ArrayList<Double> DistanceRank = new ArrayList<Double>();//大到小

	   
	   
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
    	Totoalline.add(User2DownRight_User2DownFront);Totoalline.add(User2Downront_User2DownRight);     //24
    	Totoalline.add(User2DownRight_User2DownBehind);Totoalline.add(User2DownBehund_User2DownRight);  //25
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
    	
    	


        
        if(nodeName.equals(nodeNames[0])){      //Tim_Down_Front
        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Front.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Front[i].equals(Clear_MAC)){
        			try {
        				

    				int intrssi=Integer.valueOf(RSSI);
    				//intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Front[0].equals(Clear_MAC)){
    					User1DownFront_User1DownBehind.add(intRssi_to_distance(intrssi));   					
    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[1].equals(Clear_MAC)){
    					User1DownFront_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Tim_Down_Front[2].equals(Clear_MAC)){
    					User1DownFront_User2DownRight.add(intRssi_to_distance(intrssi));
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Front[3].equals(Clear_MAC)){
    					User1DownFront_User2DownFront.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Front[4].equals(Clear_MAC)){
    					User1DownFront_User2DownBehind.add(intRssi_to_distance(intrssi));
    				}
    				
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
    					User1DownBehind_User1DownFront.add(intRssi_to_distance(intrssi));
    					
    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[1].equals(Clear_MAC)){
    					User1DownBehind_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Tim_Down_Behind[2].equals(Clear_MAC)){
    					User1DownBehind_User2DownRight.add(intRssi_to_distance(intrssi));
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Behind[3].equals(Clear_MAC)){
    					User1DownBehind_User2DownFront.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Behind[4].equals(Clear_MAC)){
    					User1DownBehind_User2DownBehind.add(intRssi_to_distance(intrssi));
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
    					User1DownLeft_User1DownRight.add(intRssi_to_distance(intrssi));
    					
    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[1].equals(Clear_MAC)){
    					User1DownLeft_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[2].equals(Clear_MAC)){
    					User1DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));
    				} 
    				if(Experiment_Globalvariable.Tim_Down_Left[3].equals(Clear_MAC)){
    					User1DownLeft_User2DownFront.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Tim_Down_Left[4].equals(Clear_MAC)){
    					User1DownLeft_User2DownBehind.add(intRssi_to_distance(intrssi));
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
    			        User1DownRight_User1DownLeft.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[1].equals(Clear_MAC)){
    			        User1DownRight_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[2].equals(Clear_MAC)){
    			        User1DownRight_User2DownRight.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[3].equals(Clear_MAC)){
    			        User1DownRight_User2DownFront.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[4].equals(Clear_MAC)){
    			        User1DownRight_User2DownBehind.add(intRssi_to_distance(intrssi));
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
    					User2DownFront_User2DownBehind.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Front[1].equals(Clear_MAC)){
    					User2DownFront_User1DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Jack_Down_Front[2].equals(Clear_MAC)){
    					User2DownFront_User1DownRight.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Front[3].equals(Clear_MAC)){
    					User2DownFront_User1DownFront.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Front[4].equals(Clear_MAC)){
    					User2DownFront_User1DownBehind.add(intRssi_to_distance(intrssi));
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
    					User2DownBehind_User2DownFront.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Behind[1].equals(Clear_MAC)){
    					User2DownBehind_User1DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Jack_Down_Behind[2].equals(Clear_MAC)){
    					User2DownBehind_User1DownRight.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[3].equals(Clear_MAC)){
    					User2DownBehind_User1DownFront.add(intRssi_to_distance(intrssi));
    				}  	
    				if(Experiment_Globalvariable.Jack_Down_Behind[4].equals(Clear_MAC)){
    					User2DownBehind_User1DownBehind.add(intRssi_to_distance(intrssi));
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
    					User2DownLeft_User1DownLeft.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[1].equals(Clear_MAC)){
    					User2DownLeft_User1DownRight.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[2].equals(Clear_MAC)){
    					User2DownLeft_User1DownFront.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[3].equals(Clear_MAC)){
    					User2DownLeft_User1DownBehind.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Left[4].equals(Clear_MAC)){
    					User2DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));
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
    					User2DownRight_User1DownLeft.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[1].equals(Clear_MAC)){
    					User2DownRight_User1DownRight.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[2].equals(Clear_MAC)){
    					User2DownRight_User1DownFront.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[3].equals(Clear_MAC)){
    					User2DownRight_User1DownBehind.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Jack_Down_Right[4].equals(Clear_MAC)){
    					User2DownRight_User2DownLeft.add(intRssi_to_distance(intrssi));
    				}
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
    	
      // *********************************以下判斷公轉 ****************************//
 	    
 	   /* System.out.println("判斷size windows:"+User1DownLeft_User1DownRight.size()+" "+User1DownRight_User1DownLeft.size()+" "+
 	                                          User2DownRight_User2DownLeft.size()+" "+User2DownLeft_User2DownRight.size()+" "+
 	                                          User1DownLeft_User2DownLeft.size()+" "+User2DownLeft_User1DownLeft.size()+" "+
 	                                          User1DownLeft_User2DownRight.size()+" "+User2DownRight_User1DownLeft.size()+" "+
 	                                          User1DownRight_User2DownLeft.size()+" "+User2DownLeft_User1DownRight.size()+" "+
 	                                          User1DownRight_User2DownRight.size()+" "+User2DownRight_User1DownRight.size()+" "+
 	                                          User2DownBehind_User2DownFront.size()+" "+User2DownBehind_User1DownLeft.size()+" "+
 	                                          User2DownBehind_User1DownRight.size()+" "+User2DownBehind_User1DownFront.size()+" "+
 	                                         User2DownBehind_User1DownBehind.size());
 	                                         */
        System.out.println("判斷size windows:"+User1DownLeft_User2DownLeft.size()+
        		" "+User1DownLeft_User2DownRight.size()+" "+User1DownRight_User2DownLeft.size()+
        		" "+User1DownRight_User2DownRight.size()+" "+User1DownFront_User2DownLeft.size()+
        		" "+User1DownFront_User2DownRight.size()+" "+User1DownBehind_User2DownLeft.size()+
        		" "+User1DownBehind_User2DownRight.size()+" "+User1DownLeft_User2DownRight.size()+
        		
        		" "+User2DownLeft_User1DownLeft.size()+" "+User2DownLeft_User1DownRight.size()+
        		" "+User2DownLeft_User1DownFront.size()+" "+User2DownLeft_User1DownBehind.size()+
        		" "+User2DownRight_User1DownLeft.size()+" "+User2DownRight_User1DownRight.size()+
        		" "+User2DownRight_User1DownFront.size()+" "+User2DownRight_User1DownBehind.size()+
        		" "+User2DownRight_User1DownLeft.size());
		
 	    currenttime =System.currentTimeMillis();
 	   //if(currenttime - starttime >=   1000){     //每隔1s進行偵測是否要算
 	    //之後1S判斷一次//
 	    //********************************以下會有16種相互自轉判斷組合******************************************//			
 			LimitSize();
 			System.out.println("LimitSize: "+User1Left_size+" "+User1Right_size+" "+User1Front_size+" "+User1Behind_size+" "+
 					User1DownLeft_User1DownRight.size()+" "+User1DownRight_User1DownLeft.size()+" "+User1DownFront_User1DownBehind.size()+" "+User1DownBehind_User1DownFront.size());

 			if(User1Left_size>=slide_windows &&User1Right_size>=slide_windows&&User1Front_size>=slide_windows&&User1Behind_size>=slide_windows){
 				String Mindirection=Precompute();    //做資料前運算，目前版本就直接平均，並算出User1距離User2哪一個點最小
    		
     //****************************用機率估算目前已知距離***********************************//
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
			//*****************根據User2自轉之後會有，16種判斷******************//
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
    	  	   			currenttime=System.currentTimeMillis();
    	   			   try {
    	   				 
    	 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
    	  							+ "/"+ time_name+".txt",true);
    	   				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
    	   				bufferedWriter.write((currenttime-starttime)+"\n");
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
			    	
			    	
			    
    			
			    
 			currenttime=System.currentTimeMillis();
    		System.out.println("wiseserver執行時間:"+(currenttime-starttime));	
            System.exit(1);

    		
    		
    
 		  }//Rssi過總數門檻
    	//} //Rssi個數要過門檻
       /*if_restartime=false;
 	   DistanceRank.clear();
 	   User1DownLeft_User1DownRight.clear();User1DownRight_User1DownLeft.clear();
 	   User2DownRight_User2DownLeft.clear();User2DownLeft_User2DownRight.clear();
 	   User1DownLeft_User2DownLeft.clear();User2DownLeft_User1DownLeft.clear();
 	   User1DownLeft_User2DownRight.clear();User2DownRight_User1DownLeft.clear();
 	   User1DownRight_User2DownLeft.clear();User2DownLeft_User1DownRight.clear();
 	   User1DownRight_User2DownRight.clear();User2DownRight_User1DownRight.clear();
 	   User1DownLeft_User2DownFront.clear();User1DownLeft_User2DownBehind.clear();
 	   User1DownRight_User2DownFront.clear();User1DownLeft_User2DownBehind.clear();
 	   User1DownRight_User2DownRight.clear();User1DownRight_User2DownBehind.clear();
  	   final_User1DownLeft_User1DownRight=0;    //四個節點 6條線
 	   final_User2DownRight_User2DownLeft=0;
 	   final_User1DownLeft_User2DownLeft=0;
 	   final_User1DownLeft_User2DownRight=0;
 	   final_User1DownRight_User2DownLeft=0;
 	   final_User1DownRight_User2DownRight=0;
 	   */
 	    
 	    //}  //time slot做判斷
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
			for(int i=0;i<Totoalline.size();i++){
					System.out.println(i+": "+Totoalline.get(i));
			}
			//*****************************  先由固定長度做機率判斷  ********************************//
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
			
			
			if((Totoalline.get(i).size() + Totoalline.get(i+1).size())>=3){      //雙條線變成一條線
			
			for(int j=0;j<Totoalline.get(i+1).size();j++){
				Totoalline.get(i).add(Totoalline.get(i+1).get(j));
			}			
			Collections.sort(Totoalline.get(i));
			Statistics statistics5 =new Statistics(Totoalline.get(i).subList(1, Totoalline.get(i).size()-1));   //去頭去尾，如果size=10，就是取(1,9)

			DistanceLine[i/2]=statistics5.getMean();
			}
			else if(Totoalline.get(i).size()!=0 || Totoalline.get(i+1).size()!=0){                             //雙條線變成一條線
				for(int j=0;j<Totoalline.get(i+1).size();j++){
					Totoalline.get(i).add(Totoalline.get(i+1).get(j));
				}			
				Statistics statistics6 =new Statistics(Totoalline.get(i).subList(0, Totoalline.get(i).size()));
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
		User1Left_Distance=(DistanceLine[1]+DistanceLine[2]+DistanceLine[3]+DistanceLine[4])/Distance_count;
		Distance_count=0;
		
		for(int i=5;i<=8;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		User1Right_Distance=(DistanceLine[5]+DistanceLine[6]+DistanceLine[7]+DistanceLine[8])/Distance_count;
		Distance_count=0;

		for(int i=12;i<=15;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		User1Front_Distance=(DistanceLine[12]+DistanceLine[13]+DistanceLine[14]+DistanceLine[15])/Distance_count;
		Distance_count=0;
		
		for(int i=18;i<=21;i++){
			if(DistanceLine[i]>0.0){
				Distance_count+=1;
			}
		}
		User1Behind_Distance=(DistanceLine[18]+DistanceLine[19]+DistanceLine[20]+DistanceLine[21])/Distance_count;
		Distance_count=0;
		

		
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		//**************** 比較距離大小*******************//
		Distance_compare.add(User1Left_Distance);Distance_compare.add(User1Right_Distance);
		Distance_compare.add(User1Front_Distance);Distance_compare.add(User1Behind_Distance);
		System.out.println("\n");
		System.out.println("Distance_compare: "+Distance_compare);
		Collections.sort(Distance_compare);
		System.out.println("Distance_compare(sort後): "+Distance_compare);
		if(Distance_compare.get(0).equals(User1Left_Distance)){
			return "MinUser1Left";			
		}else if(Distance_compare.get(0).equals(User1Right_Distance)){
			return "MinUser1Right";			
		}else if(Distance_compare.get(0).equals(User1Front_Distance)){
			return "MinUser1Front";			
		}else if(Distance_compare.get(0).equals(User1Behind_Distance)){
			return "MinUser1Behind";				
		}
		else{
			return "No_decision";
		}
		
	}
	
	public double intRssi_to_distance(int intrssi){
	   	   if(intrssi<=40){
	   			Distance=(Math.pow(12,1.5*(intrssi/33.68361333)-1));
	   			//Distance=Distance/100;
	   			return Distance;
	   			//System.out.println("5CM"+Math.pow(12,1.5*(intrssi/33.68361333)-1));  
	   			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.569897)-1)+"\n");
	   		}
	   		else if(intrssi>=41 && intrssi<=57){
	   			Distance=(Math.pow(12,1.5*(intrssi/36.95969358)-1));
	   			//Distance=Distance/100;
	   			return Distance;
	   			//System.out.println("30CM"+Math.pow(12,1.5*(intrssi/36.95969358)-1));
	   			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.1784927)-1)+"\n");
	   			
	   		}
	   		else if(intrssi >=58 && intrssi<=80){     // intrssi=76 distance=109.66
	   			Distance=(Math.pow(12,1.5*(intrssi/39.4413766)-1));
	   			//Distance=Distance/100;
	   			return Distance;
	   			//System.out.println("50CM"+Math.pow(12,1.5*(intrssi/39.4413766)-1));
	   			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.5186138)-1)+"\n");
	   			
	   		}
	   		else {
	   			System.out.println("OUT of range"+intrssi);
	   			return 160.04;    //回傳最高的值
	   		}
		
		
	}
	
	

}

