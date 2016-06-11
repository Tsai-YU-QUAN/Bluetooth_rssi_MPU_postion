package demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Rotation {
	static Channel channel;
    static Connection connection;
	static Channel channel_publish;
    private static final String TOPIC_pose = "wise.mocap.pose";  
    private static final String TOPIC_pose2 = "wise.mocap.pose2";    // 測試用    
    private static final String TOPIC_location = "wise.position";    
    private static final String TOPIC_location2 = "wise.position2";  // 測試用
	private String[] DMP_MacName ={"20:CD:39:93:BA:B6","20:CD:39:93:C0:E6","80:C1:BA:76:1E:30","80:C1:BA:74:56:6B",
            "20:CD:39:93:C2:E8","20:CD:39:93:BD:83","80:C1:BA:73:48:CC","80:C1:BA:75:EF:66"};
	private static boolean[] if_DMP_connect={false,false,false,false,false,false,false,false};
	
	private static double TimDownLeft_x,TimDownLeft_z;       //作為原點，公尺為單位
	private static double TimDownRight_x, TimDownRight_z;
	private static double JackDownLeft_x,  JackDownLeft_z;    //針對Z軸部分之後，初始化會以RSSI偵測
	private static double JackDownRight_x, JackDownRight_z;   //針對Z軸部分之後，初始化會以RSSI偵測
	private static double PreTimDownLeft_x=9999,PreTimDownLeft_z=9999,PreTimDownRight_x=9999,PreTimDownRight_z=9999;
	private static double PreJackDownLeft_x=9999,PreJackDownLeft_z=9999,PreJackDownRight_x=9999,PreJackDownRight_z=9999;
	private static int TimDownLeftX_forYangles=0, TimDownLeftZ_forYangles=0;
	private static int TimDownRightX_forYangles=0, TimDownRightZ_forYangles=0;
	private static int JackDownLeftX_forYangles=0, JackDownLeftZ_forYangles=0;
	private static int JackDownRightX_forYangles=0,JackDownRightZ_forYangles=0;
	private static int TimDownLeft_Xangles=0, TimDownLeft_Yangles=0,TimDownLeft_Zangles=0;       ///為了平均用
	private static int TimDownRight_Xangles=0, TimDownRight_Yangles=0,TimDownRight_Zangles=0;
	private static int JackDownLeft_Xangles=0, JackDownLeft_Yangles=0,JackDownLeft_Zangles=0;
	private static int JackDownRight_Xangles=0,JackDownRight_Yangles=0,JackDownRight_Zangles=0;
	
	private static int Timfinal_Xangles,Timfinal_Yangles,Timfinal_Zangles,
	                   Jackfinal_Xangles,Jackfinal_Yangles=0,Jackfinal_Zangles;
    static int radius=20;   //直徑  未來會以RSSI做偵測
    private static FileWriter fw1,fw2,fw3,fw4;
	private static int x, y, z;
	private static long endTime=0;
	private static long startTime=0;
	private static long currentTime=0;
	private static boolean  if_exe_one =false;
	private static String finaljson;
    
    static int i=0 ,j=0;
    static boolean intit_loop=false;
    JSONObject posedata_json;
    public void Pose_processing(String posedata)
            throws java.io.IOException, JSONException
    {
    	try {
    	
       //if(intit_loop==false){
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
        channel.exchangeDeclare(TOPIC_pose, "fanout");
       //}
        //********給一個目的標定  *********  //
  	    String cmd ="{" + "Position"+": { x:"+0+", y:"+y+", z:"+2+ "} }";   //z->>x(Real x)   x->>y(Real y)
  	    String cmdHips = "{" + "Hips"+": { x:"+360+", y:"+0+", z:"+-370+ "} }";
  	    channel.basicPublish(TOPIC_location2, "", null, cmd.getBytes());
	    channel.basicPublish(TOPIC_pose2, "", null, cmdHips.getBytes());

		 
		startTime= System.currentTimeMillis();   //from last node compute
		
         posedata_json= new JSONObject(posedata);
         System.out.println("posedata_json: "+posedata_json.has("TimDownLeft"));
        
        //posedata_json.getJSONObject("TimDownLeft");
       // System.out.println("posedata: "+posedata_json.getJSONObject("TimDownLeft").get("x"));
        
		if(posedata_json.has("TimDownLeft")){                   /////////For self-rotation
			if_DMP_connect[2]=true;
			x=(Integer) posedata_json.getJSONObject("TimDownLeft").get("x");
			y=(Integer) posedata_json.getJSONObject("TimDownLeft").get("y");
			z=(Integer) posedata_json.getJSONObject("TimDownLeft").get("z");
			TimDownLeft_Xangles=x;
			TimDownLeft_Yangles=y;
			TimDownLeft_Zangles=z;
			
			TimDownLeft_x = -radius*Math.cos(Math.toRadians(y))+20;  //20是作shift用(PPT48)  0
			TimDownLeft_z = radius*Math.sin(Math.toRadians(y));     //                     0
			
			if(PreTimDownLeft_x < 1000){
				TimDownLeftX_forYangles=(int) Math.toDegrees(Math.acos((Math.abs(TimDownLeft_x-PreTimDownLeft_x-20)*1/radius))); //先求變化量
				TimDownLeftX_forYangles=y+TimDownLeftX_forYangles;
			}
			if(PreTimDownLeft_z  <1000){
				TimDownLeftZ_forYangles=(int)Math.toDegrees(Math.asin((Math.abs(TimDownLeft_z-PreTimDownLeft_z)*1/radius))); //先求變化量
				TimDownLeftZ_forYangles=y+TimDownLeftZ_forYangles;
			}
			//System.out.println("TimDownLeft:"+TimDownLeftX_forYangles+" "+TimDownLeftZ_forYangles);
			//endTime=System.currentTimeMillis();
			PreTimDownLeft_x=TimDownLeft_x;
			PreTimDownLeft_z=TimDownLeft_z;
			
			if(PreTimDownLeft_z <1000 && if_DMP_connect[2] && if_DMP_connect[3]){    //Timfinal_angles ==> Tim Yaw
				Timfinal_Xangles =(TimDownLeft_Xangles+TimDownRight_Xangles)/2;
			    if(Math.abs(TimDownLeftX_forYangles -TimDownRightX_forYangles)<100){     //解決多個裝置資料不是同時性
				Timfinal_Yangles=(TimDownLeftX_forYangles+TimDownLeftZ_forYangles+TimDownRightX_forYangles+TimDownRightZ_forYangles)/4;
			    }
			    else{
					Timfinal_Yangles=(TimDownLeftX_forYangles+TimDownLeftZ_forYangles)/2;
			    }
				Timfinal_Zangles =(TimDownLeft_Zangles+TimDownRight_Zangles)/2;
				
				if(posedata_json.has("TimDownLeft")){
			   try {
					fw3 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/TimDownLeft.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw3);
				bufferedWriter.write(TimDownLeftX_forYangles+"&"+TimDownLeftZ_forYangles+":"
				+TimDownRightX_forYangles+"&"+TimDownRightZ_forYangles+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					finaljson = "{" + "Hips"+": { x:"+Timfinal_Xangles+", y:"+(Timfinal_Yangles-Jackfinal_Yangles)+", z:"+Timfinal_Zangles+ "} }";
			        System.out.println(finaljson);
					channel.basicPublish(TOPIC_pose, "", null, finaljson.getBytes()); 
					
				}

			}

		}
		else if(posedata_json.has("TimDownRight")){
			if_DMP_connect[3]=true;
			x=(Integer) posedata_json.getJSONObject("TimDownRight").get("x");
			y=(Integer) posedata_json.getJSONObject("TimDownRight").get("y");
			z=(Integer) posedata_json.getJSONObject("TimDownRight").get("z");
	
			TimDownRight_Xangles=x;
			TimDownRight_Yangles=y;
			TimDownRight_Zangles=z;
			TimDownRight_x =20+radius*Math.cos(Math.toRadians(y));   //20是作shift用  40
			TimDownRight_z =-radius*Math.sin(Math.toRadians(y));     //              0
			
			if(PreTimDownRight_x < 1000){
				TimDownRightX_forYangles=(int)Math.toDegrees(Math.acos((Math.abs(TimDownRight_x-PreTimDownRight_x-20)*1/radius)));
				TimDownRightX_forYangles=y+TimDownRightX_forYangles;
			}
			if(PreTimDownRight_z < 1000){
				TimDownRightZ_forYangles=(int)Math.toDegrees(Math.asin((Math.abs(TimDownRight_z-PreTimDownRight_z)*1/radius)));
				TimDownRightZ_forYangles=y+TimDownRightZ_forYangles;
			}
			//System.out.println("TimDownRight:"+TimDownRightX_forYangles+" "+TimDownRightZ_forYangles);

            //θfinal=(θZ1+θx1+θZ2+θx2)/4
			
			//endTime=System.currentTimeMillis();
			PreTimDownRight_x=TimDownRight_x;
			PreTimDownRight_z=TimDownRight_z;
			if(PreTimDownRight_z <1000 && if_DMP_connect[2] && if_DMP_connect[3]){    //Timfinal_angles ==> Tim Yaw
				Timfinal_Xangles =(TimDownLeft_Xangles+TimDownRight_Xangles)/2;
			    if(Math.abs(TimDownLeftX_forYangles -TimDownRightX_forYangles)<100){     //解決多個裝置資料不是同時性
				Timfinal_Yangles=(TimDownLeftX_forYangles+TimDownLeftZ_forYangles+TimDownRightX_forYangles+TimDownRightZ_forYangles)/4;
			    }
			    else{
					Timfinal_Yangles=(TimDownLeftX_forYangles+TimDownLeftZ_forYangles)/2;
			    }				
			    Timfinal_Zangles =(TimDownLeft_Zangles+TimDownRight_Zangles)/2;
				
				if(posedata_json.has("TimDownRight")){
					
					fw3 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/TimDownRight.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw3);
				bufferedWriter.write(TimDownLeft_Xangles+"&"+TimDownRight_Xangles+":"
				+TimDownLeft_Zangles+"&"+TimDownRight_Zangles+"\n");
				
					finaljson = "{" + "Hips"+": { x:"+Timfinal_Xangles+", y:"+(Timfinal_Yangles-Jackfinal_Yangles)+", z:"+Timfinal_Zangles+ "} }";
			        //System.out.println(finaljson);
					channel.basicPublish(TOPIC_pose, "", null, finaljson.getBytes()); 
				/*	try {
						fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/"
								+ "finaljson_tim.txt",true);
					BufferedWriter bufferedWriter = new BufferedWriter(fw2);
					endTime=System.currentTimeMillis();
					bufferedWriter.write(endTime-startTime+"\n");
					bufferedWriter.flush();
					bufferedWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					
				}

			}

		}
		else if(posedata_json.has("JackDownLeft")){
			if_DMP_connect[6]=true;
			x=(Integer) posedata_json.getJSONObject("JackDownLeft").get("x");
			y=(Integer) posedata_json.getJSONObject("JackDownLeft").get("y");
			z=(Integer) posedata_json.getJSONObject("JackDownLeft").get("z");
	
			JackDownLeft_Xangles=x;
			JackDownLeft_Yangles=y;
			JackDownLeft_Zangles=z;
			/*try {
				fw3 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
						+ "/JackDownLeft_yaw.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw3);
			bufferedWriter.write(y+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/

			JackDownLeft_x = 20-radius*Math.cos(Math.toRadians(y));   //20是作shift用      0
			JackDownLeft_z = 50+radius*Math.sin(Math.toRadians(y));   //要用RSSI，判斷Z軸的值 50
			
			if(PreJackDownLeft_x < 1000){
				JackDownLeftX_forYangles=(int) Math.toDegrees(Math.acos((Math.abs(JackDownLeft_x-PreJackDownLeft_x-20)*1/radius))); //先求變化量
				JackDownLeftX_forYangles=y+JackDownLeftX_forYangles;
			}
			if(PreJackDownLeft_z  <1000){
				JackDownLeftZ_forYangles=(int)Math.toDegrees(Math.asin((Math.abs(JackDownLeft_z-PreJackDownLeft_z-50)*1/radius))); //先求變化量
				JackDownLeftZ_forYangles=y+JackDownLeftZ_forYangles;
			}
			System.out.println("JackDownLeft:"+JackDownLeftX_forYangles+" "+JackDownLeftZ_forYangles);
			//endTime=System.currentTimeMillis();
			PreJackDownLeft_x=JackDownLeft_x;
			PreJackDownLeft_z=JackDownLeft_z;
			if(PreJackDownLeft_z <1000 && if_DMP_connect[6] && if_DMP_connect[7]){
			if(posedata_json.has("JackDownLeft")){
				
				Jackfinal_Xangles =(JackDownLeft_Xangles+JackDownRight_Xangles)/2;
				if(Math.abs(JackDownLeftX_forYangles-JackDownRightX_forYangles)< 100){ //解決多個裝置資料不是同時性
				Jackfinal_Yangles=(JackDownLeftX_forYangles+JackDownLeftZ_forYangles+JackDownRightX_forYangles+JackDownRightZ_forYangles)/4;
				}
				else{
					Jackfinal_Yangles=(JackDownLeftX_forYangles+JackDownLeftZ_forYangles)/2;
					
				}
				Jackfinal_Zangles =(JackDownLeft_Zangles+JackDownRight_Zangles)/2;
				

				  /* try {
						fw3 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
								+ "/JackDownLeft.txt",true);
					BufferedWriter bufferedWriter = new BufferedWriter(fw3);
					bufferedWriter.write(TimDownLeft_Xangles+"&"+TimDownRight_Xangles+":"
					+TimDownLeft_Zangles+"&"+TimDownRight_Zangles+"\n");
					bufferedWriter.flush();
					bufferedWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					
						finaljson = "{" + "Hips"+": { x:"+Timfinal_Xangles+", y:"+(Timfinal_Yangles-Jackfinal_Yangles)+", z:"+Timfinal_Zangles+ "} }";
				        System.out.println(finaljson);
						channel.basicPublish(TOPIC_pose, "", null, finaljson.getBytes()); 
						
					}
			}
		}
		else if(posedata_json.has("JackDownRight")){
			
			if_DMP_connect[7]=true;
			x=(Integer) posedata_json.getJSONObject("JackDownRight").get("x");
			y=(Integer) posedata_json.getJSONObject("JackDownRight").get("y");
			z=(Integer) posedata_json.getJSONObject("JackDownRight").get("z");
	
			JackDownRight_Xangles=x;
			JackDownRight_Yangles=y;
			JackDownRight_Zangles=z;
			//if(if_exe_one==true){
			//startTime= System.currentTimeMillis();   //from last node compute
			//if_exe_one=false;
			//}
			
			/*try {
				fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
						+ "/JackDownRight_yaw.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw2);
			bufferedWriter.write(y+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			JackDownRight_x =20+radius*Math.cos(Math.toRadians(y));  //20是作shift用  40
			JackDownRight_z =50-radius*Math.sin(Math.toRadians(y)); //              50
			
			if(PreJackDownRight_x < 1000){
				JackDownRightX_forYangles=(int)Math.toDegrees(Math.acos((Math.abs(JackDownRight_x-PreJackDownRight_x-20)*1/radius)));
				JackDownRightX_forYangles=y+JackDownRightX_forYangles;
			}
			if(PreJackDownRight_z < 1000){
				JackDownRightZ_forYangles=(int)Math.toDegrees(Math.asin((Math.abs(JackDownRight_z-PreJackDownRight_z-50)*1/radius)));
				JackDownRightZ_forYangles=y+JackDownRightZ_forYangles;
			}
			System.out.println("JackDownRight:"+JackDownRightX_forYangles+" "+JackDownRightZ_forYangles);

            //θfinal=(θZ1+θx1+θZ2+θx2)/4
			
			//endTime=System.currentTimeMillis();
			PreJackDownRight_x=JackDownRight_x;
			PreJackDownRight_z=JackDownRight_z;
			if(PreJackDownRight_z <1000 && if_DMP_connect[6] && if_DMP_connect[7]){    //Timfinal_angles ==> Tim Yaw
				Jackfinal_Xangles =(JackDownLeft_Xangles+JackDownRight_Xangles)/2;
				if(Math.abs(JackDownLeftX_forYangles-JackDownRightX_forYangles)< 100){  //解決多個裝置資料不是同時性
				Jackfinal_Yangles=(JackDownLeftX_forYangles+JackDownLeftZ_forYangles+JackDownRightX_forYangles+JackDownRightZ_forYangles)/4;
				}
				else{
					Jackfinal_Yangles=(JackDownRightZ_forYangles+JackDownRightZ_forYangles)/2;
					
				}				Jackfinal_Zangles =(JackDownLeft_Zangles+JackDownRight_Zangles)/2;				//Timfinal_angles=Timfinal_angles-60;
				
					if(posedata_json.has("JackDownRight")){
						//data.setY(Jackfinal_angles);     //for 自轉
						finaljson = "{" + "Hips"+": { x:"+Timfinal_Xangles+", y:"+(Timfinal_Yangles-Jackfinal_Yangles)+", z:"+Timfinal_Zangles+ "} }";
				        System.out.println(finaljson);
						channel.basicPublish(TOPIC_pose, "", null, finaljson.getBytes()); 
					}
					
				
			}
			
		}
		else{
			//finaljson = "{" + "Hips"+": { x:"+x+", y:"+y+", z:"+z+ "} }";
	        channel.basicPublish(TOPIC_pose, "", null, posedata.getBytes());
		}
		
        intit_loop=true;
        
        
        

      channel.close();
      connection.close();
      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
    
	}
    
}
