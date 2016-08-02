package demo;

import java.io.FileWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Easypublish {
	  private final static String QUEUE_NAME = "hello";
	  private static final String TEST_TOPIC = "wise.mocap";
	  static String tempsend;
	  static String  tempmocap;
	  static String  temppostion;
		private String[] DMP_MacName ={"20:CD:39:93:BA:B6","20:CD:39:93:C0:E6","20:CD:39:93:C2:FB","20:CD:39:93:BE:A7",
                "20:CD:39:93:C2:E8","20:CD:39:93:BD:83","20:CD:39:93:C3:E7","20:CD:39:93:BE:9B"};
        private String[] DMP_nodeNames={"Tim_Up_Left","Tim_Up_Right","Tim_Down_Left","Tim_Down_Right",
                 "Jack_Up_Left","Jack_Up_Right","Jack_Down_Left","Jack_Down_Right"};
	   private static boolean  if_exe_one =false;
	  
		private static double TimDownLeft_x=0.0, TimDownLeft_z=0.0;       //作為原點，公尺為單位
		private static double TimDownRight_x=0.4, TimDownRight_z=0.0;
		private static double JackDownLeft_x=0.0,  JackDownLeft_z=0.4;    //針對Z軸部分之後，初始化會以RSSI偵測
		private static double JackDownRight_x=0.4, JackDownRight_z=-0.4;//針對Z軸部分之後，初始化會以RSSI偵測
		private static double PreTimDownLeft_x=9999,PreTimDownLeft_z=9999,PreTimDownRight_x=9999,PreTimDownRight_z=9999;
		private static double PreJackDownLeft_x=9999,PreJackDownLeft_z=9999,PreJackDownRight_x=9999,PreJackDownRight_z=9999;
		private static int TimDownLeft_x_angles=0, TimDownLeft_z_angles=0;
		private static int TimDownRight_x_angles=0, TimDownRight_z_angles=0;
		private static int JackDownLeft_x_angles=0, JackDownLeft_z_angles=0;
		private static int JackDownRight_x_angles=0,JackDownRight_z_angles=0;
		private static int Timfinal_angles;
		
		private static ArrayList<Double> LeftDownto_Right_distance = new ArrayList<Double>();                   //校正用
		private static ArrayList<Double> JackLeftDownto_JackDownRight_Distance = new ArrayList<Double>();       //校正用

		private static ArrayList<Double> LeftDownto_JackDownLeft_distance = new ArrayList<Double>();          //可以觀察用
		private static ArrayList<Double> LeftDownto_JackDownRight_distance = new ArrayList<Double>();
		private static ArrayList<Double> RightDownto_JackDownLeft_distance = new ArrayList<Double>();
		private static ArrayList<Double> RightDownto_JackDownRight_disatnce = new ArrayList<Double>();        //可以觀察用
		static FileWriter fw;
	    static int radius=20;   //直徑  未來會以RSSI做偵測
		static int x, y, z;
		
		private static long endTime=0;
		private static long startTime=0;
		private static long currentTime=0;
	  public static void main(String[] argv)
	      throws java.io.IOException {
		  
	        ConnectionFactory factory = new ConnectionFactory();
	        try {
				factory.setUri("amqp://admin:admin@192.168.1.101");
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
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();
	        channel.exchangeDeclare("demo.EXCHANGE1", "fanout");
	        int i=0;
	        /*
			if(this.macName.equalsIgnoreCase(DMP_MacName[0])){                   /////////For self-rotation
				
				try {
					fw = new FileWriter("/home/pi/Desktop/LeftUpLeg_yaw.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(y+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				TimDownLeft_x = -radius*Math.cos(Math.toRadians(y))+20;  //20是作shift用
				TimDownLeft_z = radius*Math.sin(Math.toRadians(y));
				
				
				
				if(Math.abs(TimDownLeft_x-PreTimDownLeft_x) <360){
					TimDownLeft_x_angles=(int) Math.acos((Math.abs(TimDownLeft_x-PreTimDownLeft_x)*1/radius));
				}
				if(Math.abs(TimDownLeft_z-PreTimDownLeft_z) <360){
					TimDownLeft_z_angles=(int)Math.asin((Math.abs(TimDownLeft_z-PreTimDownLeft_z)*1/radius));
				}
				endTime=System.currentTimeMillis();
				PreTimDownLeft_x=TimDownLeft_x;
				PreTimDownLeft_z=TimDownLeft_z;
				
				/************************* 以下為Log  ******************
						try {
					fw = new FileWriter("/home/pi/Desktop/LeftUpLeg_time.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(endTime-startTime+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						try {
					fw = new FileWriter("/home/pi/Desktop/leftUpLeg_x.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(TimDownLeft_x+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						try {
					fw = new FileWriter("/home/pi/Desktop/leftUpLeg_z.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(TimDownLeft_z+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if(this.macName.equalsIgnoreCase(DMP_MacName[1])){
				
				if(if_exe_one==true){
				startTime= System.currentTimeMillis();   //from last node compute
				if_exe_one=false;
				}
				
				try {
					fw = new FileWriter("/home/pi/Desktop/Tim_Down_Right_yaw.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(y+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TimDownRight_x =40+radius*Math.cos(Math.toRadians(y))-20;   //20是作shift用
				TimDownRight_z =-radius*Math.sin(Math.toRadians(y));
				
				if(Math.abs(TimDownRight_x-PreTimDownRight_x) <360){
					TimDownRight_x_angles=(int) Math.acos((Math.abs(TimDownRight_x-PreTimDownRight_x)*1/radius));
				}
				if(Math.abs(TimDownRight_z-PreTimDownRight_z) <360){
					TimDownRight_z_angles=(int)Math.asin((Math.abs(TimDownRight_z-PreTimDownRight_z)*1/radius));
				}
                //θfinal=(θZ1+θx1+θZ2+θx2)/4
				
				endTime=System.currentTimeMillis();
				PreTimDownRight_x=TimDownRight_x;
				PreTimDownRight_z=TimDownRight_z;
				if(Math.abs(TimDownRight_z-PreTimDownRight_z) <360)    //Timfinal_angles ==> Tim Yaw
					Timfinal_angles=(TimDownLeft_x_angles+TimDownLeft_z_angles+TimDownRight_x_angles+TimDownRight_z_angles)/4;
				//************************* 以下為Log  *****************

						try {
					fw = new FileWriter("/home/pi/Desktop/Tim_Down_Right_time.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(endTime-startTime+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					fw = new FileWriter("/home/pi/Desktop/tim_Down_Right_x.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(TimDownRight_x+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						try {
					fw = new FileWriter("/home/pi/Desktop/tim_Down_Right_z.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw);
				bufferedWriter.write(TimDownRight_z+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
			*/
			
		
	        //*************    以下測試 PC To Unity    ********//
	       /* while(i<70000){     
        		System.out.println("test"+i);
	        	if(i<200){
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        		//tempsend = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
       		        //channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	        		
	        		temppostion ="{" + "Position"+": { x:"+-0.2107528+", y:"+0+", z:"+5.2+ "} }";
	        		tempmocap ="{" + "Hips"+": { x:"+358.1081+", y:"+0.6104+", z:"+357.7805+ "} }";

 	        		System.out.println(tempmocap);
       		         channel.basicPublish(TEST_TOPIC, "", null, tempmocap.getBytes());

        		   //  channel.basicPublish(TEST_TOPIC, "", null, temppostion.getBytes());
	        	}
	        	else if(i>200 && i<400){
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        		//tempsend = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
       		        //channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	        		temppostion ="{" + "Position"+": { x:"+-0.3+", y:"+0+", z:"+0.4+ "} }";
	        		tempmocap ="{" + "Hips"+": { x:"+358.1081+", y:"+90.6104+", z:"+357.7805+ "} }";
	        		System.out.println(tempmocap);
        		     channel.basicPublish(TEST_TOPIC, "", null, tempmocap.getBytes());
        		     //channel.basicPublish(TEST_TOPIC, "", null, temppostion.getBytes());
	        		
	        	}
	        	else if(i>400 && i<600){
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        		//tempsend = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
       		        //channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	        		temppostion ="{" + "Position"+": { x:"+-0.4+", y:"+0+", z:"+0.6+ "} }";
	        		tempmocap ="{" + "Hips"+": { x:"+358.1081+", y:"+180.6104+", z:"+357.7805+ "} }";
	        		System.out.println(tempmocap);
        		     channel.basicPublish(TEST_TOPIC, "", null, tempmocap.getBytes());
        		     //channel.basicPublish(TEST_TOPIC, "", null, temppostion.getBytes());
	        		
	        	}
	        	else if(i>600 && i<800){
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        		//tempsend = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
       		        //channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	        		temppostion ="{" + "Position"+": { x:"+-0.5+", y:"+0+", z:"+0.8+ "} }";
	        		System.out.println(tempmocap);
        		     //channel.basicPublish(TEST_TOPIC, "", null, temppostion.getBytes());
	        		
	        	}
	        	else if(i>800 && i<1000){
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        		//tempsend = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
       		        //channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	        		temppostion ="{" + "Position"+": { x:"+-0.6+", y:"+0+", z:"+1.0+ "} }";
	        		System.out.println(tempmocap);
        		    // channel.basicPublish(TEST_TOPIC, "", null, temppostion.getBytes());
	        		
	        	}
	        	else if(i>1000 && i<1200){
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        		//tempsend = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
       		        //channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	        		temppostion ="{" + "Position"+": { x:"+-0.7+", y:"+0+", z:"+1.2+ "} }";
	        		System.out.println(tempmocap);
        		     //channel.basicPublish(TEST_TOPIC, "", null, temppostion.getBytes());
	        		
	        	}
        		


	        	i=i+1;
	        }
	        */
	        channel.close();
	        connection.close();

	      
	  }
	  
	  
	  public void time_rssi_average(ArrayList<Double> inputList){     //總結算一次目前節點的位置
		  
			new Thread(){                  //每一個delt time就更新一次
				@Override
				public void run(){
					
					
					
				}
			}.start();
		  
		  
	  }
	}
