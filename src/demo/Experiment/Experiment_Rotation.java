package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import demo.Globalvariable;

public class Experiment_Rotation {
	
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
	private static int TimDownLeft_pitch=999, TimDownLeft_row, TimDownLeft_yaw,
	                   TimDownRight_pitch=999,TimDownRight_row,TimDownRight_yaw,
	                   JackDownLeft_pitch=999, JackDownLeft_row, JackDownLeft_yaw,
	                   JackDownRight_pitch=999, JackDownRight_row, JackDownRight_yaw,
	                   Tim_pitch,Tim_row,Tim_yaw,
	                   Jack_pitch,Jack_row,Jack_yaw,final_yaw;
	private static long currentTime=0;
	private static boolean  if_exe_one =false;
	private static String finaljson;
	public static int DegreeTim_yaw=0;
	
	
    public void Pose_processing(String posedata)
            throws java.io.IOException, JSONException
    {

        JSONObject posedata_json;
        posedata_json= new JSONObject(posedata);
       
		if(posedata_json.has("TimDownLeft")){                   /////////For self-rotation
			TimDownLeft_pitch=(Integer) posedata_json.getJSONObject("TimDownLeft").get("x");
			TimDownLeft_yaw=(Integer) posedata_json.getJSONObject("TimDownLeft").get("y");
			TimDownLeft_row=(Integer) posedata_json.getJSONObject("TimDownLeft").get("z");
			
		}
		else if(posedata_json.has("TimDownRight")){
			TimDownRight_pitch=(Integer) posedata_json.getJSONObject("TimDownRight").get("x");
			Globalvariable.TimDownRight_yaw=(Integer) posedata_json.getJSONObject("TimDownRight").get("y");
			TimDownRight_row=(Integer) posedata_json.getJSONObject("TimDownRight").get("z");		
		}
		else if(posedata_json.has("JackDownLeft")){
			JackDownLeft_pitch=(Integer) posedata_json.getJSONObject("JackDownLeft").get("x");
			JackDownLeft_yaw=(Integer) posedata_json.getJSONObject("JackDownLeft").get("y");
			JackDownLeft_row=(Integer) posedata_json.getJSONObject("JackDownLeft").get("z");
			
		}
		else if(posedata_json.has("JackDownRight")){
			JackDownRight_pitch=(Integer) posedata_json.getJSONObject("JackDownRight").get("x");
			JackDownRight_yaw=(Integer) posedata_json.getJSONObject("JackDownRight").get("y");
			JackDownRight_row=(Integer) posedata_json.getJSONObject("JackDownRight").get("z");	
			
		}
		
		DegreeTim_yaw=Globalvariable.TimDownRight_yaw-Globalvariable.TimYawOffset;
		if(DegreeTim_yaw<0){// 0~359度
			DegreeTim_yaw=360+DegreeTim_yaw;
		}
		if(-(DegreeTim_yaw-360)>=0 && -(DegreeTim_yaw-360)<=360){ //=====================防止Gyo有異常	的數值 正常值:0~359
			Globalvariable.User1Totalyaw.add(-(DegreeTim_yaw-360));
			Globalvariable.User1Rad_yaw=Math.toRadians(-(DegreeTim_yaw-360));
			Globalvariable.User1TotalRad_yaw.add(Math.toRadians(-(DegreeTim_yaw-360)));
			System.out.println("未RadiansTimYaw,有: "+(-(DegreeTim_yaw-360))
					+" "+Globalvariable.User1Rad_yaw+" "+Math.abs(-(DegreeTim_yaw-360)-360));
		}

      	FileWriter fw1;
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance"
						+ "/"+"TimDownRight_yaw.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw1);
			bufferedWriter.write(-(DegreeTim_yaw-360)+"\n");
			//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   

		//====================================================以下在做User1Yaw平均,與User2Yaw平均並且User1Yaw-User2Yaw
		if(TimDownLeft_pitch!=999 && TimDownRight_pitch!=999){
			Tim_pitch=(TimDownLeft_pitch+TimDownRight_pitch)/2;
			Tim_row=(TimDownLeft_row+TimDownRight_row)/2;
			//TimDownRight_yaw=TimDownRight_yaw+150;
			if(Math.abs(TimDownLeft_yaw-TimDownRight_yaw)<100){
			Tim_yaw=(TimDownLeft_yaw+TimDownRight_yaw)/2;
			}
			else{
				Tim_yaw=TimDownLeft_yaw;
			}
			
			Jack_pitch=(JackDownLeft_pitch+JackDownRight_pitch)/2;
			Jack_row=(JackDownLeft_row+JackDownRight_row)/2;
			if(Math.abs(JackDownLeft_yaw-JackDownRight_yaw)<100){
			Jack_yaw=(JackDownLeft_yaw+JackDownRight_yaw)/2;
			}
			else{
				Jack_yaw=JackDownLeft_yaw;
			}
			//Jack_yaw沒有值的時候
			Jack_yaw=0;
			   
				currentTime= System.currentTimeMillis();   //from last node compute
	    //====================================================傳到Unity
		
			System.exit(1);
			
		}

		
		

    }

}
