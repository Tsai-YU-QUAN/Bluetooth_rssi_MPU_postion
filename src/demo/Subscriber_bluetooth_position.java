    /* 這個專案為了測試三角定位，定點的定位=>Real time定位=>
       把演算法丟入具有3軸的裏面做比較
 11/8算出相對距離  目前做兩個人的相對定位
 暫定Tim 不動，為起始點
                                                     */
package demo;
                                        

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.sun.org.apache.bcel.internal.generic.ATHROW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Subscriber_bluetooth_position 
{

    private static final String TEST_TOPIC = "demo.EXCHANGE1";    //這邊是收集RSSI的資訊Topic
    private static final String TOPIC_postion_1 = "demo.EXCHANGE_postion1";
    private static final String TOPIC_postion_2 = "demo.EXCHANGE_postion2";

    static ArrayList<Integer> PKrssi = new ArrayList<Integer>(); //之後要寫活的
    static ArrayList<Integer> PKrssi2 = new ArrayList<Integer>();
    static ArrayList<Integer> PKrange = new ArrayList<Integer>();
    static Boolean ifSettingtime=false;
    static double tempx_y[][]={{0.0,0.0},{0.0,50.0}}; // {x,y}     //先給訂坐標，之後跟這著動後，這邊也會更新到
    static String currentmacaddress=null;
    static int RSSI_count=0;
    static String Select_MAC="";
    static String MACaddress;
    static int Intrssi;
    static double distance;
    static double dx; static double dy; static double d; static double a=0;
    static double x2=0.0;static double y2=0.0; static double h=0.0; static double rx=0.0;
    static double ry=0.0;static double xi1=0.0;static double xi2=0.0;static double yi1=0.0;
    static double yi2=0;
    static int starttime=0;
    
    static double startTime;
    static double endTime;
    static double totTime;
    static int pre_rssi,mid_rssi,now_rssi,average_pre_rssi,average_mid_rssi,average_now_rssi;
    static double pre_distance,mid_distance,now_distance;
    static int count_rssi=0,average_count_rssi=0;
	static int count_distance=0;
    static int threshold_rssi=3;

    

    static Channel channel;
    static Channel channel_publish;
    static Channel channel_publish2;

    interface ThreeFiled {
    	public  String macaddress[]={"80:C1:BA:74:56:6B","80:C1:BA:73:48:CC","80:C1:BA:75:EF:66"};   
    	//之後也要寫活的，要跟arduino上BLE_Rssi.java的MAC address一樣
        public  String tempmacaddress[]={null,null};
        public  double r_distance[]={0.0,0.0};
    	
    }
    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException
    {
    	
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@127.0.0.1");    //如果是要傳amqp://userName:password; localhost=127.0.0.1
        Connection connection = factory.newConnection();
         channel = connection.createChannel();
        
        channel.exchangeDeclare(TEST_TOPIC, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, TEST_TOPIC, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        
        /***************** 第一個人的位置****************/
        ConnectionFactory factory_publish = new ConnectionFactory();
      try {
      	factory_publish.setUri("amqp://admin:admin@192.168.1.100");
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
      Connection connection_publish = factory_publish.newConnection();
      channel_publish = connection_publish.createChannel();
      channel_publish.exchangeDeclare(TOPIC_postion_1, "fanout");
      
      /***************** 第二個人的位置****************/
      ConnectionFactory factory_publish2 = new ConnectionFactory();
    try {
    	factory_publish2.setUri("amqp://admin:admin@192.168.1.100");
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
    Connection connection_publish2 = factory_publish.newConnection();
    channel_publish2 = connection_publish2.createChannel();
    channel_publish2.exchangeDeclare(TOPIC_postion_2, "fanout");
        

        
        
        
        int j=0,intrssi; 
        String tempmocap,temppostion;
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String line = new String(delivery.getBody());
           // System.out.println(line);
            

    	     
            Rssi_to_Unity(line.substring(0, 4),Integer.valueOf(line.substring(line.indexOf("&")+1)));
            
            
            
            
            //System.out.println(MACaddress+" "+Intrssi);
            
//******************  以下為做實驗看數據，如何要怎麼挑整     ***********************************
           if(starttime>=150){
        	   if(starttime==150){
        	   System.out.println("LET start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        	    startTime = System.currentTimeMillis();
        	   }
        	   starttime=starttime+1;
        	   
            if(line.indexOf("LeftWaistDMP")>=0){
            	
            
            //System.out.println(line);
			 
			FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/MPU6050LeftUpLegDMP_P.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw);

			bufferedWriter.write(line.substring(line.indexOf(':')+1, line.indexOf('.'))+" "+"\n");
			bufferedWriter.close();
			
			String tempstString= line.substring(line.indexOf('.')+1);
			//int tempindex=line.indexOf('.');
			
			FileWriter fw2 =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/MPU6050LeftUpLegDMP_R.txt",true);
			BufferedWriter bufferedWriter2 = new BufferedWriter(fw2);

			//System.out.println("tempstString.indexOf('.')-1"+" "+tempstString+(tempstString.indexOf('.')+1));
			bufferedWriter2.write(tempstString.substring(0, tempstString.indexOf('.'))+" "+"\n");
			bufferedWriter2.close();
			
			String tempstString2= tempstString.substring(tempstString.indexOf('.'));
			
			FileWriter fw3 =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/MPU6050LeftUpLegDMP_Y.txt",true);
			BufferedWriter bufferedWriter3 = new BufferedWriter(fw3);

			bufferedWriter3.write(tempstString2.substring(tempstString2.indexOf('.')+1)+" "+"\n");
			bufferedWriter3.close();
			
			
           }
            
            
            if(line.indexOf("RightWaistDMP")>=0){
            	
                
            //System.out.println(line);
			 
			FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/MPU6050RightUpLegDMP_P.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw);

			bufferedWriter.write(line.substring(line.indexOf(':')+1, line.indexOf('.'))+" "+"\n");
			bufferedWriter.close();
			
			String tempstString= line.substring(line.indexOf('.')+1);
			//int tempindex=line.indexOf('.');
			
			FileWriter fw2 =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/MPU6050RightUpLegDMP_R.txt",true);
			BufferedWriter bufferedWriter2 = new BufferedWriter(fw2);

			//System.out.println("tempstString.indexOf('.')-1"+" "+tempstString+(tempstString.indexOf('.')+1));
			bufferedWriter2.write(tempstString.substring(0, tempstString.indexOf('.'))+" "+"\n");
			bufferedWriter2.close();
			
			String tempstString2= tempstString.substring(tempstString.indexOf('.'));
			
			FileWriter fw3 =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/MPU6050RightUpLegDMP_Y.txt",true);
			BufferedWriter bufferedWriter3 = new BufferedWriter(fw3);

			bufferedWriter3.write(tempstString2.substring(tempstString2.indexOf('.')+1)+" "+"\n");
			bufferedWriter3.close();
			
			
           }
            //// 以下是PI收到的RSSI資料

            if(line.indexOf("80:C1:BA:75:EF:66")>=0){      //TimLeftWaistRSSI
            	
                
            //System.out.println(line);
			 
			FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/TimLeftWaistRSSI.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw);

			bufferedWriter.write(line.substring(line.indexOf('&')+1,line.indexOf('&')+3)+" "+"\n");
			bufferedWriter.close();
			
           } 
            if(line.indexOf("80:C1:BA:74:56:6B")>=0){   //TimRightWaistRSSI
            	
                
            //System.out.println(line);
			 
			FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/TimRightWaistRSSI.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw);

			bufferedWriter.write(line.substring(line.indexOf('&')+1,line.indexOf('&')+3)+" "+"\n");
			bufferedWriter.close();
			
           }
            
           if(line.indexOf("80:C1:BA:76:1E:30")>=0){   //JackLeftWaistRSSI
            	
                
            //System.out.println(line);
			 
			FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/JackLeftWaistRSSI.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw);

			bufferedWriter.write(line.substring(line.indexOf('&')+1,line.indexOf('&')+3)+" "+"\n");
			bufferedWriter.close();
			
           }
            /////以下是arduino收到的RSSI資料
           if(line.indexOf("LeftWaistR")>=0){
        	   //System.out.println(line);  
            
      /*      for(int i=0;i<Globalvariable.Tim_leftwaist_RSSI.length;i++){
            	//System.out.println(line.substring(10,18));
            	if(line.substring(10,18).equals(Globalvariable.Tim_leftwaist_RSSI[i])){
            		FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/LeftWaist"+Globalvariable.Tim_leftwaist_RSSI[i]+".txt",true);
            		BufferedWriter bufferedWriter = new BufferedWriter(fw);
            		bufferedWriter.write(line.substring(19,21)+"\n");
            		bufferedWriter.close();
            	}
            }
            */
               
        	   
        	   
           }
           else if(line.indexOf("RightWaistR")>=0){
              	//System.out.println(line.substring(11,19));
              	
    /*           for(int i=0;i<Globalvariable.Tim_rightwaist_RSSI.length;i++){
               	if(line.substring(11,19).equals(Globalvariable.Tim_rightwaist_RSSI[i])){
               		FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/RightWaist"+Globalvariable.Tim_rightwaist_RSSI[i]+".txt",true);
               		BufferedWriter bufferedWriter = new BufferedWriter(fw);
               		bufferedWriter.write(line.substring(20,22)+"\n");
               		bufferedWriter.close();
               	}
               }
               */
        	   
           }
           
          else if(line.indexOf("JackLeftR")>=0){
             	//System.out.println(line.substring(11,19));
             	
  /*            for(int i=0;i<Globalvariable.Jack_leftwaist_RSSI.length;i++){
              	if(line.substring(9,17).equals(Globalvariable.Jack_leftwaist_RSSI[i])){
              		FileWriter fw =new  FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Bluetooth_rssi_MPU_postion/Jack_leftwaist"+Globalvariable.Jack_leftwaist_RSSI[i]+".txt",true);
              		BufferedWriter bufferedWriter = new BufferedWriter(fw);
              		bufferedWriter.write(line.substring(18,20)+"\n");
              		bufferedWriter.close();
              	}
              }
              */
       	   
          }
           endTime = System.currentTimeMillis();
           //取得程式結束的時間
           totTime = endTime - startTime;
           System.out.println("totTime"+totTime);
            
           }
           else{
        	   starttime=starttime+1;
           }
			
            //MACaddress=line.substring(0,17);                  ///取得RSSI等資料而已，之後去做運算
            //Intrssi=Integer.valueOf(line.substring(18, 20)); 
			//Calculate(MACaddress,Intrssi);
			


        }
        
        
    }
    
    
    public static void Rssi_to_Unity(String name,int intrssi){
    	String temppostion;
    	System.out.println("原先:"+intrssi);
    	
    	//average filter 把誤差縮小
    	if(count_rssi%3==0){
    		pre_rssi=intrssi;
    	}else if(count_rssi%3==1){
    		mid_rssi=intrssi;
    	}else if(count_rssi%3==2){
        	now_rssi=intrssi;
        	intrssi=(pre_rssi+now_rssi+mid_rssi)/3;
        	System.out.println("之後:"+pre_rssi+" "+now_rssi+" "+mid_rssi+" "+intrssi);
        	
        	// 判斷有沒有動 1. threshold 2.陀螺儀有沒有動

        	if(average_count_rssi%3==0){
        		 average_pre_rssi=intrssi;
        	}else if(average_count_rssi%3==1){
        		 average_mid_rssi=intrssi;
        	}else if(average_count_rssi%3==2){
        		average_now_rssi=intrssi;
        		if(Math.abs(average_pre_rssi-average_now_rssi)<3){
        			//沒有動
        			intrssi=(average_now_rssi+average_pre_rssi+average_mid_rssi)/3;
        			System.out.println("沒動"+intrssi);
        			
        		}
        		else{
        			//有動
        			intrssi=(average_now_rssi+average_pre_rssi+average_mid_rssi)/3;
        			System.out.println("有動"+intrssi);
        		}


            
        	
        
        // if(line.indexOf("&")>1){
         //System.out.println(intrssi);
 		if(intrssi<=30){
 			//System.out.println("5CM"+Math.pow(12,1.5*(intrssi/25.569897)-1)); 
 			distance=Math.pow(12,1.5*(intrssi/25.569897)-1);
 			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.569897)-1)+"\n");
 		}
 		else if(intrssi>=31 && intrssi<=40){
 			//System.out.println("20CM"+Math.pow(12,1.5*(intrssi/25.1784927)-1));
 			distance=Math.pow(12,1.5*(intrssi/25.1784927)-1);
 			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.1784927)-1)+"\n");
 			
 		}
 		else if(intrssi >=41 && intrssi<=80){   //intrssi<=54
 			//System.out.println("60CM"+Math.pow(12,1.5*(intrssi/25.5186138)-1));
 			distance=Math.pow(12,1.5*(intrssi/25.5186138)-1);
 			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.5186138)-1)+"\n");
 			
 		}
 		else {
 			System.out.println("OUT of range"+intrssi);
 		}

    	

         //}
 		distance=distance/100.0;
 		//*****為了distance優化可以平順移動
 		if(count_distance%3==0){
 			pre_distance=distance;
 		}else if(count_distance%3==1){
 			mid_distance=distance;
 		}else if(count_distance%3==2){
 			now_distance=distance;
 			distance=(pre_distance+mid_distance+now_distance)/3.0;
 		 
 		
 		temppostion ="{" + "Position"+": { x:"+0+", y:"+0+", z:"+distance+ "} }";
 	     try {
 	    	// if(name.equals("Jack")){
 	     		System.out.println(temppostion);
 	    	 //channel_publish.basicPublish(TOPIC_postion_1, "", null, temppostion.getBytes());
 	    	// }
 	    	// else{
 	     		 //System.out.println(temppostion);
 	 	    	 channel_publish2.basicPublish(TOPIC_postion_2, "", null, temppostion.getBytes());
	 
 	    	// }
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		} //count_distance 平均化
 		count_distance=count_distance+1;
 	     
        	}//average_count_rssi%2==1要判斷到底要動 or 不動
        	
        	average_count_rssi=average_count_rssi+1;
    	}

    	
        count_rssi=count_rssi+1;   //最外圍的if

    }
    
    

    public static void Calculate(String currmacString,int intrssi){
    	/*** 換算成 距離  數值部分還要再調整的EXcel***/
		if(intrssi<=30){
			//System.out.println("5CM"+Math.pow(12,1.5*(intrssi/25.569897)-1)); 
			distance=Math.pow(12,1.5*(intrssi/25.569897)-1);
			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.569897)-1)+"\n");
		}
		else if(intrssi>=31 && intrssi<=40){
			//System.out.println("20CM"+Math.pow(12,1.5*(intrssi/25.1784927)-1));
			distance=Math.pow(12,1.5*(intrssi/25.1784927)-1);
			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.1784927)-1)+"\n");
			
		}
		else if(intrssi >=41 && intrssi<=80){   //intrssi<=54
			//System.out.println("60CM"+Math.pow(12,1.5*(intrssi/25.5186138)-1));
			distance=Math.pow(12,1.5*(intrssi/25.5186138)-1);
			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.5186138)-1)+"\n");
			
		}
		else {
			System.out.println("OUT of range"+intrssi);
		}
		
		/***     算出PI(人)的位置       ***/
		
		if(currmacString.equals(ThreeFiled.macaddress[0])){
			ThreeFiled.tempmacaddress[0]=currmacString;
			ThreeFiled.r_distance[0]=distance;
			
		}
		else if(currmacString.equals(ThreeFiled.macaddress[1])){
			ThreeFiled.tempmacaddress[1]=currmacString;
			ThreeFiled.r_distance[1]=distance;
		}
		
		if(ThreeFiled.tempmacaddress[0]!=null && ThreeFiled.tempmacaddress[1]!=null){
			
			
			System.out.println(ThreeFiled.tempmacaddress[0]+ThreeFiled.r_distance[0]+" "+
					ThreeFiled.tempmacaddress[1]+ThreeFiled.r_distance[1]+tempx_y[0][0]+tempx_y[0][1]+tempx_y[1][0]+tempx_y[1][1]);
			
			
			// (x-a)+(y-b)=c^2  ;  (x-d)+(y-e)=f^2   互減後=>-2(a-d)x-2(b-e)y+a^2-d^2+b^2-e^2=c^2-f^2
			
			dx=Math.abs(tempx_y[0][0]-tempx_y[1][0]);
			dy=Math.abs(tempx_y[0][1]-tempx_y[1][1]);
			d=(Math.sqrt(dx*dx)+(dy*dy));
			
			if(d>(ThreeFiled.r_distance[0]+ThreeFiled.r_distance[1])){
				System.out.println("no solution. circles do Not intersect");
			}
			if(d<Math.abs(ThreeFiled.r_distance[0]-ThreeFiled.r_distance[1])){
				System.out.println("no solution. one circle is contained in the other");
			}
			
		    // Determine the distance from point 0 To point 2.
		    a = ((ThreeFiled.r_distance[0]*ThreeFiled.r_distance[0])-(ThreeFiled.r_distance[1]*ThreeFiled.r_distance[1]) + (d*d)) / (2.0 * d);
			x2=tempx_y[0][0]+(dx *a/d);
			y2=tempx_y[0][1]+(dy*a/d);
			
		    //Determine the distance from point 2 To either of the
		    //intersection points.
		    h = Math.sqrt((ThreeFiled.r_distance[0]*ThreeFiled.r_distance[0]) - (a*a));
		 
		    //Now determine the offsets of the intersection points from
		    //point 2.
		    rx = (0-dy) * (h/d);
		    ry = dx * (h/d);
		 
		    //Determine the absolute intersection points.
		    xi1 = x2 + rx;
		    xi2 = x2 - rx;
		    yi1 = y2 + ry;
		    yi2 = y2 - ry;
		    
		    System.out.println("兩圓算出的交點: " + xi1+ yi1 +xi2 +yi2 );
			
			ThreeFiled.tempmacaddress[0]=ThreeFiled.tempmacaddress[1]=null;
			ThreeFiled.r_distance[0]=ThreeFiled.r_distance[1]=0.0;
			
			
		}
		
    	
    }
    
    public static void Comparerssi(){
    	
		if(Select_MAC.equals(ThreeFiled.macaddress[0]) &&ifSettingtime==false){       //收集此device的rssi
			System.out.println("intrssi"+Select_MAC+" "+Intrssi);
			PKrssi.add(Intrssi);
			
		}
		else if(Select_MAC.equals(ThreeFiled.macaddress[1])&&ifSettingtime==false){
			PKrssi2.add(Intrssi);
			
		}
	if(PKrssi.size()==30 || PKrssi2.size()==30){
		ifSettingtime=true;
		Collections.sort(PKrssi);     //排序
		Collections.sort(PKrssi2);

		int i=0;
		while (i<PKrssi.size()) {
			System.out.println(PKrssi.get(i)+"@"); //小到大
			i=i+1;
		}
		PKrange.add(0, PKrssi.get(PKrssi.size()-1)-PKrssi.get(0));
		
		int j=0;
		while (j<PKrssi2.size()) {
			System.out.println(PKrssi2.get(j));
			j=j+1;
		}
		PKrange.add(1, PKrssi2.get(PKrssi2.size()-1)-PKrssi2.get(0));
		
		System.out.println("PKrange"+PKrange.get(0)+" "+PKrange.get(1));
		
		if(PKrange.get(0)<PKrange.get(1)){     //算出目前比較小的值
			currentmacaddress=ThreeFiled.macaddress[0];
		}
		else{
			currentmacaddress=ThreeFiled.macaddress[1];	
		}
		System.out.println("PKrssiend"+PKrssi.size()+" "+PKrssi2.size());

		ifSettingtime=false;
		PKrssi.clear();     //清空
		PKrssi2.clear();
		PKrange.clear();
		
	   System.out.println("currentmacaddress"+currentmacaddress); 			    //目前比較穩的BLE
	}

		
		

	}
}
