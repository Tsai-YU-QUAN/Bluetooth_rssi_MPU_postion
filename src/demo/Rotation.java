package demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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

    
    static int i=0 ,j=0;
    static boolean intit_loop=false;
    public void Pose_processing(String posedata)
            throws java.io.IOException
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
        System.out.println("intit_loop: "+intit_loop);
        intit_loop=true;
        channel.basicPublish(TOPIC_pose, "", null, posedata.getBytes()); 
        
        
        
     //**** For test  rabbitmq transport  
     /*while(i<70000){
      if(i <2000){
          String cmd = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 60 + " } }";
          System.out.println("Hips"+ cmd);
          channel.basicPublish(TOPIC_pose, "", null, cmd.getBytes()); 
          i=i+1;
      }
      else if(i>=2000 && i<=50000){
          String cmd = " { " + "Head" + " : { x: " + 120 + ", y: " + 50 + ", z: " + 60 + " } }";
          System.out.println("Hips"+ cmd);
          channel.basicPublish(TOPIC_pose, "", null, cmd.getBytes()); 
          i=i+1;
    	  
    	  
      }
      else if(i>50000 && i<=70000){
          String cmd = " { " + "Head" + " : { x: " + 120 + ", y: " + 10 + ", z: " + 30 + " } }";
          System.out.println("Hips"+ cmd);
          channel.basicPublish(TOPIC_pose, "", null, cmd.getBytes()); 
          i=i+1;
    	  
    	  
      }
     }*/
      channel.close();
      connection.close();
      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
    
	}
    
}
