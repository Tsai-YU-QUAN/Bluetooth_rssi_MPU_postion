package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import demo.Experiment.Experiment_Globalvariable;





public class Experiment_Revolution {
	   String Prestate="", Currentstate="";
	   String text_name="公轉45度",time_name="公轉45度時間";
	   double adjsut_scale=0.0;
	   long starttime,currenttime;
	   static int OK_times=0, fail_times;
	   int slide_windows=20;
	   double Real_User1DownLeft_User1DownRight=20;    //20CM

	   
	   //Tim=user1   , Jack=user2
	   static ArrayList<Double> User1DownLeft_User1DownRight = new ArrayList<Double>(); static ArrayList<Double> User1DownRight_User1DownLeft = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User2DownLeft_User2DownRight = new ArrayList<Double>();       //校正用
	   static ArrayList<Double> User1DownLeft_User2DownLeft = new ArrayList<Double>();          //可以觀察用
	   static ArrayList<Double> User1DownLeft_User2DownRight = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownLeft = new ArrayList<Double>();
	   static ArrayList<Double> User1DownRight_User2DownRight = new ArrayList<Double>();        //可以觀察用
	   static double Distance;
	    private String[] nodeNames={"Tim_Up_Left","Tim_Up_Right","Tim_Down_Left","Tim_Down_Right",
                "Jack_Up_Left","Jack_Up_Right","Jack_Down_Left","Jack_Down_Right"};
		static String nodeName;
	    String Clear_MAC;
	    String RSSI;
	   static int User1DownLeft_User2DownLeft_size=-1 ,User1DownLeft_User2DownLeft_presize=-2;
	   static int User1DownLeft_User2DownRight_size=-1,User1DownLeft_User2DownRight_presize=-2;
	   static int User1DownRight_User2DownLeft_size=-1,User1DownRight_User2DownLeft_presize=-2;
	   static int User1DownRight_User2DownRight_size=-1,User1DownRight_User2DownRight_presize=-2;
	   double final_User1DownLeft_User1DownRight=0;
	   double final_User1DownLeft_User2DownLeft=0, final_User1DownLeft_User2DownRight=0;
	   double final_User1DownRight_User2DownLeft=0,final_User1DownRight_User2DownRight=0;
	   
	   private static FileWriter fw1,fw2,fw3,fw4;


    public void Revolution_processing(String posedata)
    {   starttime=System.currentTimeMillis();
    	nodeName=posedata.substring(0, posedata.indexOf(":"));
    	Clear_MAC=posedata.substring(posedata.length()-11, posedata.length()-3);
    	RSSI=posedata.substring(posedata.length()-2, posedata.length());
    	System.out.println("posedata: "+nodeName+" "+Clear_MAC+" "+RSSI); 
    	//starttime=1331313L;

        
        if(nodeName.equals(nodeNames[2])){      //Tim_Down_Left
        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Left.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Left[i].equals(Clear_MAC)){
        			try {
        				

    				int intrssi=Integer.valueOf(RSSI);
    				//intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Left[0].equals(Clear_MAC)){
    			        System.out.println("User1DownLeft_User1DownRight "+User1DownLeft_User1DownRight.size() +" "+intRssi_to_distance(intrssi));
    					User1DownLeft_User1DownRight.add(intRssi_to_distance(intrssi));
    					
    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[1].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownLeft_User2DownLeft.size() +" "+User1DownLeft_User2DownLeft.size());
    					User1DownLeft_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[2].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownLeft_User2DownRight.size() +" "+User1DownLeft_User2DownRight.size());
    					User1DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));

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
    			        System.out.println("User1DownRight_User1DownLeft "+User1DownRight_User1DownLeft.size() +" "+intRssi_to_distance(intrssi));
    					System.out.println(User1DownRight_User1DownLeft.size());
    			        User1DownRight_User1DownLeft.add(intRssi_to_distance(intrssi));
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[1].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownRight_User2DownLeft.size() +" "+User1DownRight_User2DownLeft.size());
    			        User1DownRight_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[2].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownRight_User2DownRight.size() +" "+User1DownRight_User2DownRight.size());
    			        User1DownRight_User2DownRight.add(intRssi_to_distance(intrssi));

    				}  		
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
        
        /*
        if(posedata_json.has(nodeNames[4])){      //Jack_Down_Left
        	for(int i=0;i<Globalvariable.Jack_Down_Left.length;i++){
    			if(posedata_json.getJSONObject("Jack_Down_Left").has(Globalvariable.Jack_Down_Left[i])){
    				
    				int intrssi=(Integer) posedata_json.getJSONObject("Jack_Down_Left").get(Globalvariable.Jack_Down_Left[i]);
    				
    				if(posedata_json.getJSONObject("Jack_Down_Left").has(Globalvariable.Jack_Down_Left[0])){
    					User1DownLeft_User2DownLeft.add(intRssi_to_distance(intrssi));
    				}
    				if(posedata_json.getJSONObject("Jack_Down_Left").has(Globalvariable.Jack_Down_Left[1])){
    					User1DownRight_User2DownLeft.add(intRssi_to_distance(intrssi));

    				}
    				if(posedata_json.getJSONObject("Jack_Down_Left").has(Globalvariable.Jack_Down_Left[2])){
    					User2DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));

    				}  			
    			}      		
        		
        	}        	
        }
        
        if(posedata_json.has(nodeNames[5])){      //Jack_Down_Right
        	for(int i=0;i<Globalvariable.Tim_Down_Left.length;i++){
    			if(posedata_json.getJSONObject("Jack_Down_Right").has(Globalvariable.Jack_Down_Right[i])){
    				
    				int intrssi=(Integer) posedata_json.getJSONObject("Jack_Down_Right").get(Globalvariable.Jack_Down_Right[i]);
    				
    				if(posedata_json.getJSONObject("Jack_Down_Right").has(Globalvariable.Jack_Down_Right[0])){
    					User1DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));
    				}
    				if(posedata_json.getJSONObject("Jack_Down_Right").has(Globalvariable.Jack_Down_Right[1])){
    					User1DownRight_User2DownRight.add(intRssi_to_distance(intrssi));

    				}
    				if(posedata_json.getJSONObject("Jack_Down_Right").has(Globalvariable.Jack_Down_Right[2])){
    					User2DownLeft_User2DownRight.add(intRssi_to_distance(intrssi));

    				}  			
    			}      		
        		
        	}        	
        }
    	*/
      // *********************************以下判斷公轉 ****************************//
        User1DownLeft_User2DownLeft_size=User1DownLeft_User2DownLeft.size();
        User1DownLeft_User2DownRight_size=User1DownLeft_User2DownRight.size();
 	    User1DownRight_User2DownLeft_size=User1DownRight_User2DownLeft.size();
 	    User1DownRight_User2DownRight_size=User1DownRight_User2DownRight.size();
        System.out.println("size關係 "+User1DownLeft_User2DownLeft_size+" "+User1DownLeft_User2DownLeft_presize+" "+
        		User1DownLeft_User2DownRight_size+" "+User1DownLeft_User2DownRight_presize);
        
        
        if(User1DownLeft_User2DownLeft_size!=User1DownLeft_User2DownLeft_presize  &&
           User1DownLeft_User2DownRight_size!=User1DownLeft_User2DownRight_presize &&
           User1DownRight_User2DownLeft_size!=User1DownRight_User2DownLeft_presize &&
           User1DownRight_User2DownRight_size!=User1DownRight_User2DownRight_presize){
    	if(User1DownLeft_User1DownRight.size()>=slide_windows && User1DownRight_User1DownLeft.size() >=slide_windows &&
    	   User1DownLeft_User2DownLeft.size()>=slide_windows  && User1DownLeft_User2DownRight.size()>=slide_windows &&
    	   User1DownRight_User2DownLeft.size()>=slide_windows && User1DownRight_User2DownRight.size()>=slide_windows){
    		
    		Collections.sort(User1DownLeft_User1DownRight);
			Statistics statistics1 =new Statistics(User1DownLeft_User1DownRight.
					subList(User1DownLeft_User1DownRight.size()-slide_windows+1, User1DownLeft_User1DownRight.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			
			for(double temp :User1DownLeft_User1DownRight.
					subList(User1DownLeft_User1DownRight.size()-slide_windows+1, User1DownLeft_User1DownRight.size()-1)){
				System.out.println("statistics1: "+temp);
			}
			System.out.println("statistics1: "+statistics1.getMean()+" "+statistics1.getStdDev()+" "+statistics1.getCoeffVar());        //可靠度
         /*
			Collections.sort(User1DownLeft_User2DownLeft);
			Statistics statistics2 =new Statistics(User1DownLeft_User2DownLeft.subList(1, slide_windows-1));   //去頭去尾
			statistics2.getCoeffVar();        //可靠度
			
			Collections.sort(User1DownLeft_User2DownRight);
			Statistics statistics3 =new Statistics(User1DownLeft_User2DownRight.subList(1, slide_windows-1));   //去頭去尾
			statistics3.getCoeffVar();        //可靠度
			
			Collections.sort(User1DownRight_User2DownLeft);
			Statistics statistics4 =new Statistics(User1DownRight_User2DownLeft.subList(1, slide_windows-1));   //去頭去尾
			statistics4.getCoeffVar();        //可靠度
			
			Collections.sort(User1DownRight_User2DownRight);
			Statistics statistics5 =new Statistics(User1DownRight_User2DownRight.subList(1, slide_windows-1));   //去頭去尾
			statistics5.getCoeffVar();        //可靠度
	            */
			try {
				

    		Collections.sort(User1DownRight_User1DownLeft);
			Statistics statistics6 =new Statistics(User1DownRight_User1DownLeft.
					subList(User1DownRight_User1DownLeft.size()-slide_windows+1, User1DownRight_User1DownLeft.size()-1));   //去頭去尾，如果size=10，就是取(1,9)
			
			for(double temp :User1DownRight_User1DownLeft.
					subList(User1DownRight_User1DownLeft.size()-slide_windows+1, User1DownRight_User1DownLeft.size()-1)){
				System.out.println("statistics6: "+temp);
			}
			final_User1DownLeft_User1DownRight=(statistics1.getMean()*statistics1.getCoeffVar()+statistics6.getMean()*statistics6.getCoeffVar())
			/(statistics1.getCoeffVar()+statistics6.getCoeffVar());
			adjsut_scale=final_User1DownLeft_User1DownRight/Real_User1DownLeft_User1DownRight;
			System.out.println("statistics6與可靠度 "+statistics6.getMean()+" "+statistics6.getStdDev()+" "+statistics6.getCoeffVar()
					+" "+final_User1DownLeft_User1DownRight+" "+adjsut_scale);        //可靠度
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
			
			
    		/*
    		for(int i=User1DownRight_User2DownRight.size()-1,count=0;count<slide_windows ;i--,count++){ 
    			final_User1DownRight_User2DownRight=final_User1DownRight_User2DownRight+User1DownRight_User2DownRight.get(i);
    		}
    		final_User1DownRight_User2DownRight=final_User1DownRight_User2DownRight/slide_windows;
    		*/
    		
    		//************ 為了參數校正 ********//  CheckLinedata: 69 62 51 58

    		final_User1DownLeft_User2DownLeft=final_User1DownLeft_User2DownLeft-slide_windows;
    		final_User1DownLeft_User2DownRight=final_User1DownLeft_User2DownRight-slide_windows;
    		System.out.println("CheckLinedata: "+"x_ "+
    		final_User1DownLeft_User2DownLeft+" y:"+final_User1DownLeft_User2DownRight+
    	    " y_:"+final_User1DownRight_User2DownRight+" x:"+final_User1DownRight_User2DownLeft);
            System.exit(1);
            // 2016.6.7 依照頭影片施工，先求x,y, x",y"
            //x <x"<1m and y"<y<1m
    		if(final_User1DownRight_User2DownRight < 58+0.04*58 && final_User1DownRight_User2DownRight > 58-0.04*58
    		&& final_User1DownRight_User2DownLeft <51+0.04*51  && final_User1DownRight_User2DownLeft > 51-0.04*51 
    		&& Currentstate=="state1" || Currentstate=="state2" || Currentstate=="state3"
    		|| Currentstate=="state7" || Currentstate=="state8"){
    			Currentstate="state1";
  			   try {
  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
  							+ "/"+ text_name+".txt",true);
  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
  				bufferedWriter.write(("0度")+"\n");
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
   			   
    			//OK_times++;
     		}
            else if(final_User1DownLeft_User2DownLeft <58 &&
            		final_User1DownRight_User2DownRight >58+58*0.04 && final_User1DownRight_User2DownRight <67-67*0.04){
    			Currentstate="state2";
 			   try {
 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
 							+ "/"+ text_name+".txt",true);
 				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
 				bufferedWriter.write(("45度")+"\n");
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
  			   
   			//System.out.println("右轉45度");
   			//OK_times++;
    		}
            else if(final_User1DownLeft_User2DownLeft <58
            		&& final_User1DownRight_User2DownRight >67-0.04*67 &&final_User1DownRight_User2DownRight <67+0.04*67 ){
    			Currentstate="state3";
 			   try {
 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
 							+ "/"+ text_name+".txt",true);
 				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
 				bufferedWriter.write(("90度")+"\n");
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
  			   
   			//System.out.println("右轉45度");
   			//OK_times++;
    		}
            else if(final_User1DownRight_User2DownRight <58 
            		&& final_User1DownLeft_User2DownLeft >58+58*0.04 && final_User1DownLeft_User2DownLeft<58-58*0.04){
    			Currentstate="state7";

 			   try {
 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
 							+ "/"+ text_name+".txt",true);
 				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
 				bufferedWriter.write(("270度")+"\n");
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
  			   
   			//System.out.println("右轉45度");
   			//OK_times++;
    		}
            else if(final_User1DownRight_User2DownRight <58 
            		&& final_User1DownLeft_User2DownLeft >67-67*0.04 &&final_User1DownLeft_User2DownLeft<67+0.04*67){
    			Currentstate="state8";

 			   try {
 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
 							+ "/"+ text_name+".txt",true);
 				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
 				bufferedWriter.write(("315度")+"\n");
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
  			   
   			//System.out.println("右轉45度");
   			//OK_times++;
    		}
            else{
  			   try {
  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
  							+ "/"+ text_name+".txt",true);
  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
  				bufferedWriter.write(("Out of compute")+"\n");
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
    		/*System.out.println("User1DownLeft_User2DownLeft");
    		for (int i = 0; i < User1DownLeft_User2DownLeft.size(); i++) {
				System.out.print(User1DownLeft_User2DownLeft.get(i)+" ");
			}
    		System.out.print("\n");
    		System.out.println("User1DownLeft_User2DownRight");
    		for (int i = 0; i < User1DownLeft_User2DownRight.size(); i++) {
				System.out.print(User1DownLeft_User2DownRight.get(i)+" ");			
			}
    		System.out.print("\n");
    		*/
    		final_User1DownLeft_User2DownLeft=0;
    		final_User1DownLeft_User2DownRight=0;
    		final_User1DownRight_User2DownLeft=0;
    		final_User1DownRight_User2DownRight=0;
    		User1DownLeft_User2DownLeft_presize=User1DownLeft_User2DownLeft.size();
    		User1DownLeft_User2DownRight_presize=User1DownLeft_User2DownRight.size();
    		User1DownRight_User2DownLeft_presize=User1DownRight_User2DownLeft.size();
    		User1DownRight_User2DownRight_presize=User1DownRight_User2DownRight.size();
 
    		/*if(OK_times+fail_times==slide_windows0){			
 			   try {
 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
 							+ "/公轉右轉45度次數.txt",true);
 				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
 				bufferedWriter.write((OK_times+" "+fail_times)+"\n");
 				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
 				bufferedWriter.flush();
 				bufferedWriter.close();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			   
 			   System.exit(1);
    		}
    		*/

    		
    		
    	}
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
            return Math.sqrt(getVariance());
        }
        public double getCoeffVar()
        {
        	return 1-(getStdDev()/getMean());
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
	   		else if(intrssi >=58 && intrssi<=70){
	   			Distance=(Math.pow(12,1.5*(intrssi/39.4413766)-1));
	   			//Distance=Distance/100;
	   			return Distance;
	   			//System.out.println("50CM"+Math.pow(12,1.5*(intrssi/39.4413766)-1));
	   			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.5186138)-1)+"\n");
	   			
	   		}
	   		else {
	   			System.out.println("OUT of range"+intrssi);
	   			return 0.0;
	   		}
		
		
	}
	
	

}

