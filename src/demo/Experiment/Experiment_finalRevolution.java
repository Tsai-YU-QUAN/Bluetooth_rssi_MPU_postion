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
	   double User1adjsut_scale=0.0,User2adjsut_scale=0.0;
	   static long starttime,currenttime;
	   static int OK_times=0, fail_times;
	   int slide_windows=5, min_window=3;
	   double Real_User1DownLeft_User1DownRight=20;    //20CM
	   double Real_final_User2DownRight_User2DownLeft=100;     //100CM
	   static int count=0;    //for realtime compute
	   private static final String TOPIC_location = "wise.position";    
	   private static final String TOPIC_location2 = "wise.position2";  // 測試用
	   private static boolean if_init=false;
	   
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
	   double [] DistanceRank= new double[28];
	 //static ArrayList<Double> DistanceRank = new ArrayList<Double>();//大到小

	   
	   
	   private static FileWriter fw1,fw2,fw3,fw4;
		private static String finaljson;
		



    public void Revolution_processing(String posedata)
    
    {  	
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
    	/*if(if_restartime==false){
        starttime=System.currentTimeMillis();
        if_restartime=true;
    	}
    	*/
        
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
		
       // if(User1DownLeft_User2DownLeft_size!=User1DownLeft_User2DownLeft_presize  ||
       //    User1DownLeft_User2DownRight_size!=User1DownLeft_User2DownRight_presize ||
       //    User1DownRight_User2DownLeft_size!=User1DownRight_User2DownLeft_presize ||
       //    User1DownRight_User2DownRight_size!=User1DownRight_User2DownRight_presize){    //判斷是否有refresh
 	    currenttime =System.currentTimeMillis();
 	   //if(currenttime - starttime >=   1000){     //每隔1s進行偵測是否要算
    	/*if(User1DownLeft_User1DownRight.size()>=min_window && User1DownRight_User1DownLeft.size() >=min_window &&
    	   User2DownRight_User2DownLeft.size()>=min_window && User2DownLeft_User2DownRight.size()>=min_window &&
    	   User1DownLeft_User2DownLeft.size()>=min_window &&  User2DownLeft_User1DownLeft.size()>=slide_windows &&
    	   User1DownLeft_User2DownRight.size()>=min_window && User2DownRight_User1DownLeft.size()>=min_window &&
    	   User1DownRight_User2DownLeft.size()>=min_window && User2DownLeft_User1DownRight.size()>=min_window &&
    	   User1DownRight_User2DownRight.size()>=min_window && User2DownRight_User1DownRight.size()>=min_window &&
    	   User1DownLeft_User2DownFront.size()>=min_window && User1DownLeft_User2DownBehind.size()>=min_window &&
    	   User1DownRight_User2DownFront.size()>=min_window && User1DownRight_User2DownBehind.size()>=min_window){   //判斷是否所有的線都超過min_window
        */
    	/*	if(User1DownLeft_User1DownRight.size()+User1DownRight_User1DownLeft.size()>=slide_windows &&
    		   User2DownRight_User2DownLeft.size()+User2DownLeft_User2DownRight.size()>=slide_windows &&
    		   User1DownLeft_User2DownLeft.size()+User2DownLeft_User1DownLeft.size()>=slide_windows &&
    		   User1DownLeft_User2DownRight.size()+User2DownRight_User1DownLeft.size()>=slide_windows&&
    		   User1DownRight_User2DownLeft.size()+User2DownLeft_User1DownRight.size()>=slide_windows &&
    		   User1DownRight_User2DownRight.size()+User2DownRight_User1DownRight.size()>=slide_windows&&
    		   User1DownLeft_User2DownFront.size()+User1DownLeft_User2DownBehind.size()>=slide_windows &&
    		   User1DownRight_User2DownFront.size()+User1DownRight_User2DownBehind.size()>=slide_windows){          //判斷是否雙向的線都超過slide_window
        */
 	     /* if(User1DownLeft_User2DownLeft.size()>=slide_windows && User1DownLeft_User2DownRight.size()>=slide_windows && 
 				User1DownRight_User2DownLeft.size()>=slide_windows && User1DownRight_User2DownRight.size()>=slide_windows &&
 				User1DownFront_User2DownLeft.size()>=slide_windows && User1DownFront_User2DownRight.size()>=slide_windows&&
 				User1DownBehind_User2DownLeft.size()>=slide_windows &&User1DownBehind_User2DownRight.size()>=slide_windows &&
 				User2DownLeft_User1DownLeft.size()>=slide_windows&& User2DownLeft_User1DownRight.size()>=slide_windows && 
 				User2DownLeft_User1DownFront.size()>=slide_windows && User2DownLeft_User1DownBehind.size()>=slide_windows && 
 				User2DownRight_User1DownLeft.size()>=slide_windows && User2DownRight_User1DownRight.size()>=slide_windows && 
 			    User2DownRight_User1DownFront.size()>=slide_windows &&User2DownRight_User1DownBehind.size()>=slide_windows &&
 			    User2DownRight_User1DownLeft .size()>=slide_windows &&User1DownLeft_User2DownRight.size()>=slide_windows) {          
 			    //判斷四個腳擺法(test)
 			     */

 		if(Totoalline.get(4).size()+Totoalline.get(5).size()>slide_windows &&
 		   Totoalline.get(10).size()+Totoalline.get(11).size()>slide_windows &&
 		   Totoalline.get(24).size()+Totoalline.get(25).size()>slide_windows &&
 		   Totoalline.get(36).size()+Totoalline.get(37).size()>slide_windows ) //判斷四個腳擺法(test)
 		{
 			
 			
 			Precompute();    //做資料前運算，目前版本就直接平均
    		
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
			
			
			
			//******************************沒有做運算，就以估算目前已知距離*********************************//
/*    		Collections.sort(User1DownLeft_User1DownRight);
			Statistics statistics1 =new Statistics(User1DownLeft_User1DownRight.
					subList(User1DownLeft_User1DownRight.size()-slide_windows, User1DownLeft_User1DownRight.size()));   //去頭去尾，如果size=10，就是取(1,9)
			
			for(double temp :User1DownLeft_User1DownRight.
					subList(User1DownLeft_User1DownRight.size()-slide_windows, User1DownLeft_User1DownRight.size())){
				System.out.println("statistics1: "+temp);
			}
			System.out.println("statistics1: "+statistics1.getMean());        //可靠度
			
			
    		Collections.sort(User1DownRight_User1DownLeft);
			Statistics statistics2 =new Statistics(User1DownRight_User1DownLeft.
					subList(User1DownRight_User1DownLeft.size()-slide_windows, User1DownRight_User1DownLeft.size()));   //去頭去尾，如果size=10，就是取(1,9)
			
			for(double temp :User1DownRight_User1DownLeft.
					subList(User1DownRight_User1DownLeft.size()-slide_windows, User1DownRight_User1DownLeft.size())){
				System.out.println("statistics2: "+temp);
			}
			final_User1DownLeft_User1DownRight=(statistics1.getMean()+statistics2.getMean())/2;
			System.out.println("statistics2與User1可靠度 "+statistics2.getMean()+" "+" "+final_User1DownLeft_User1DownRight);        //可靠度
			DistanceRank.add(final_User1DownLeft_User1DownRight);
			
			
			
    		Collections.sort(User2DownLeft_User2DownRight);
			Statistics statistics3 =new Statistics(User2DownLeft_User2DownRight.
					subList(User2DownLeft_User2DownRight.size()-slide_windows, User2DownLeft_User2DownRight.size()));   //去頭去尾，如果size=10，就是取(1,9)
			
			//for(double temp :User2DownLeft_User2DownRight.
			//		subList(User2DownLeft_User2DownRight.size()-slide_windows+1, User2DownLeft_User2DownRight.size()-1)){
			//	System.out.println("statistics3: "+temp);
			//}
			System.out.println("statistics3: "+statistics3.getMean());        //可靠度
			
			
    		Collections.sort(User2DownRight_User2DownLeft);
			Statistics statistics4 =new Statistics(User2DownRight_User2DownLeft.
					subList(User2DownRight_User2DownLeft.size()-slide_windows, User2DownRight_User2DownLeft.size()));   //去頭去尾，如果size=10，就是取(1,9)
			
			//for(double temp :User2DownRight_User2DownLeft.
			//		subList(User2DownRight_User2DownLeft.size()-slide_windows+1, User2DownRight_User2DownLeft.size()-1)){
			//	System.out.println("statistics4: "+temp);
			//}
			final_User2DownRight_User2DownLeft=(statistics3.getMean()+statistics4.getMean())/2;
			System.out.println("statistics4與User2可靠度 "+statistics4.getMean()+" "+final_User2DownRight_User2DownLeft);        //可靠度
			DistanceRank.add(final_User2DownRight_User2DownLeft);

			
			Collections.sort(User1DownLeft_User2DownLeft);
			Statistics statistics5 =new Statistics(User1DownLeft_User2DownLeft.
					subList(User1DownLeft_User2DownLeft.size()-slide_windows, User1DownLeft_User2DownLeft.size()));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics5: "+statistics5.getMean());        //可靠度
			
    		Collections.sort(User2DownLeft_User1DownLeft);
			Statistics statistics6 =new Statistics(User2DownLeft_User1DownLeft.
					subList(User2DownLeft_User1DownLeft.size()-slide_windows, User2DownLeft_User1DownLeft.size()));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownLeft_User2DownLeft=(statistics5.getMean()+statistics6.getMean())/2;
			System.out.println("statistics6 "+statistics6.getMean()+" "+final_User1DownLeft_User2DownLeft);        //可靠度
			DistanceRank.add(final_User1DownLeft_User2DownLeft);

			
			Collections.sort(User1DownLeft_User2DownRight);
			Statistics statistics7 =new Statistics(User1DownLeft_User2DownRight.
					subList(User1DownLeft_User2DownRight.size()-slide_windows, User1DownLeft_User2DownRight.size()));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics7: "+statistics7.getMean());        //可靠度
			
    		Collections.sort(User2DownRight_User1DownLeft);
			Statistics statistics8 =new Statistics(User2DownRight_User1DownLeft.
					subList(User2DownRight_User1DownLeft.size()-slide_windows, User2DownRight_User1DownLeft.size()));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownLeft_User2DownRight=(statistics7.getMean()+statistics8.getMean())/2;
			System.out.println("statistics8 "+statistics8.getMean()+" "+final_User1DownLeft_User2DownRight);        //可靠度
			DistanceRank.add(final_User1DownLeft_User2DownRight);
			
			Collections.sort(User1DownRight_User2DownLeft);
			Statistics statistics9 =new Statistics(User1DownRight_User2DownLeft.
					subList(User1DownRight_User2DownLeft.size()-slide_windows, User1DownRight_User2DownLeft.size()));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics9: "+statistics9.getMean());        //可靠度
			
    		Collections.sort(User2DownLeft_User1DownRight);
			Statistics statistics10 =new Statistics(User2DownLeft_User1DownRight.
					subList(User2DownLeft_User1DownRight.size()-slide_windows, User2DownLeft_User1DownRight.size()));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownRight_User2DownLeft=(statistics9.getMean()+statistics10.getMean())/2;
			System.out.println("statistics10 "+statistics10.getMean());        //可靠度
			DistanceRank.add(final_User1DownRight_User2DownLeft);
			for(double temp :User2DownRight_User1DownRight)
				System.out.println("User1DownRight_User2DownRight: "+temp);
	
			Collections.sort(User1DownRight_User2DownRight);
			Statistics statistics11 =new Statistics(User1DownRight_User2DownRight.
					subList(User1DownRight_User2DownRight.size()-slide_windows, User1DownRight_User2DownRight.size()));   //去頭去尾，如果size=10，就是取(1,9)
			System.out.println("statistics11: "+statistics11.getMean()+" "+statistics11.getStdDev()+" "+statistics11.getCoeffVar());        //可靠度
			
			for(double temp :User2DownRight_User1DownRight)
				System.out.println("User2DownRight_User1DownRight: "+temp);
			
    		Collections.sort(User2DownRight_User1DownRight);
			Statistics statistics12 =new Statistics(User2DownRight_User1DownRight.
					subList(User2DownRight_User1DownRight.size()-slide_windows, User2DownRight_User1DownRight.size()));   //去頭去尾，如果size=10，就是取(1,9)		
			final_User1DownRight_User2DownRight=(statistics11.getMean()+statistics12.getMean())/2;
			System.out.println("statistics12 "+statistics12.getMean());        //可靠度
			DistanceRank.add(final_User1DownRight_User2DownRight);
	*/		
			
			
			
			
			//***********************************************************************************//


			


			    	System.out.println("DistanceRank: "+DistanceRank[2]+" "+DistanceRank[5]+" "+DistanceRank[12]+" "+DistanceRank[18]);
			    	if(DistanceRank[2] > (DistanceRank[5]+DistanceRank[12]+DistanceRank[18])/3){     
    				Currentstate="state1";
    				System.out.println("state1");
    				
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
			    	else{
			    		System.out.println("another state");
			    		
			    	}
			    	
			    	
			    
    			
			    
    			
    			
            System.exit(1);

    		
    		
    	} //判斷雙向 Rssi要過門檻
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

    
	public void Precompute(){         //做資料前運算
		
		try{
			for(int i=0;i<Totoalline.size();i++){
				
				if(Totoalline.get(i).size()!=0)
					System.out.println(Totoalline.get(i));
			}
			
		for(int i=0;i<Totoalline.size();i=i+2){    //兩條雙線變成一條有唯一的值
			
			/*if(Totoalline.get(i).size()!=0 &&Totoalline.get(i+1).size()!=0){
			Collections.sort(Totoalline.get(i));
			Statistics statistics1 =new Statistics(Totoalline.get(i).subList(1, Totoalline.get(i).size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			
			Collections.sort(Totoalline.get(i+1));
			Statistics statistics2 =new Statistics(Totoalline.get(i+1).subList(1, Totoalline.get(i+1).size()-1));   //去頭去尾，如果size=10，就是取(1,9)	
			
			
			DistanceRank[i/2]=(statistics1.getMean()+statistics2.getMean())/2;
			}*/
			
			if(Totoalline.get(i).size()!=0 &&Totoalline.get(i+1).size()!=0){
			
			for(int j=0;j<Totoalline.get(i+1).size();j++){
				Totoalline.get(i).add(Totoalline.get(i+1).get(j));
			}			
			Collections.sort(Totoalline.get(i));
			Statistics statistics1 =new Statistics(Totoalline.get(i).subList(1, Totoalline.get(i).size()-1));   //去頭去尾，如果size=10，就是取(1,9)

			DistanceRank[i/2]=statistics1.getMean();
			}
			
		
		}
		System.out.println("DistanceRank: ");
		for(int i=0;i<DistanceRank.length;i++)
			System.out.print(i+":"+DistanceRank[i]+" ");
		}
		catch(Exception e){
			e.printStackTrace();
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

