package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import demo.Experiment.Experiment_Globalvariable;





public class Experiment_Exhibition_Revolution {
	   //Tim=user1   , Jack=user2
	   static ArrayList<Integer> User1DownLeft_ExperimentNode1 = new ArrayList<Integer>();       
	   static ArrayList<Integer> User1DownLeft_ExperimentNode2 = new ArrayList<Integer>();       
	   static ArrayList<Integer> User1DownLeft_ExperimentNode3 = new ArrayList<Integer>();         
	   static ArrayList<Integer> User1DownRight_ExperimentNode1 = new ArrayList<Integer>();
	   static ArrayList<Integer> User1DownRight_ExperimentNode2 = new ArrayList<Integer>();
	   static ArrayList<Integer> User1DownRight_ExperimentNode3 = new ArrayList<Integer>();        
	   static ArrayList<Integer> TotalSort = new ArrayList<Integer>();

	   static double Distance;
	    private String[] nodeNames={"Tim_Up_Left","Tim_Up_Right","Tim_Down_Left","Tim_Down_Right",
                "Jack_Up_Left","Jack_Up_Right","Jack_Down_Left","Jack_Down_Right"};
	    private static FileWriter fw1,fw2,fw3,fw4;
		static String nodeName;
	    String Clear_MAC;
	    String RSSI;
	   static int User1DownLeft_ExperimentNode1_size=-1 ,User1DownLeft_ExperimentNode1_presize=-2;
	   static int User1DownLeft_ExperimentNode2_size=-1,User1DownLeft_ExperimentNode2_presize=-2;
	   static int User1DownLeft_ExperimentNode3_size=-1,User1DownLeft_ExperimentNode3_presize=-2;
	   static int User1DownRight_ExperimentNode1_size=-1,User1DownRight_ExperimentNode1_presize=-2;
	   static int User1DownRight_ExperimentNode2_size=-1,User1DownRight_ExperimentNode2_presize=-2;
	   static int User1DownRight_ExperimentNode3_size=-1,User1DownRight_ExperimentNode3_presize=-2;
	   
	   int final_User1DownLeft_ExperimentNode1=0, final_User1DownLeft_ExperimentNode2=0;
	   int final_User1DownLeft_ExperimentNode3=0, final_User1DownRight_ExperimentNode1=0;
	   int final_User1DownRight_ExperimentNode2=0, final_User1DownRight_ExperimentNode3=0;
	   int current_lowrssi, current_secondssi;


	   long starttime,currenttime;
	   static int OK_times=0, fail_times;

    public void Revolution_processing(String posedata)
    {   starttime=System.currentTimeMillis();
    	nodeName=posedata.substring(0, posedata.indexOf(":"));
    	Clear_MAC=posedata.substring(posedata.length()-11, posedata.length()-3);
    	RSSI=posedata.substring(posedata.length()-2, posedata.length());
    	System.out.println("posedata: "+nodeName+" "+Clear_MAC+" "+RSSI);  

        
        if(nodeName.equals(nodeNames[2])){      //Tim_Down_Left
        	for(int i=0;i<Experiment_Globalvariable.Tim_Down_Left.length;i++){
        		if(Experiment_Globalvariable.Tim_Down_Left[i].equals(Clear_MAC)){
        			try {
        				

    				int intrssi=Integer.valueOf(RSSI);
    				intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Left[0].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownLeft_ExperimentNode1.size());
    			        User1DownLeft_ExperimentNode1.add(intrssi);
    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[1].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownLeft_ExperimentNode2.size());
    			        User1DownLeft_ExperimentNode2.add(intrssi);

    				}
    				if(Experiment_Globalvariable.Tim_Down_Left[2].equals(Clear_MAC)){
    			        System.out.println("size "+User1DownLeft_ExperimentNode3.size());
    			        User1DownLeft_ExperimentNode3.add(intrssi);

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
    				intRssi_to_distance(intrssi);
    				
    				if(Experiment_Globalvariable.Tim_Down_Right[0].equals(Clear_MAC)){
    			        User1DownRight_ExperimentNode1.add(intrssi);
    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[1].equals(Clear_MAC)){
    			        User1DownRight_ExperimentNode2.add(intrssi);

    				}
    				if(Experiment_Globalvariable.Tim_Down_Right[2].equals(Clear_MAC)){
    			        User1DownRight_ExperimentNode3.add(intrssi);

    				}  		
    				
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    			}      		
        		
        	}        	
        }
      // *********************************以下判斷公轉 ****************************//
        User1DownRight_ExperimentNode1_size=User1DownRight_ExperimentNode1.size();
        User1DownRight_ExperimentNode2_size=User1DownRight_ExperimentNode2.size();
        User1DownRight_ExperimentNode3_size=User1DownRight_ExperimentNode3.size();
       
        if(User1DownRight_ExperimentNode1_size!=User1DownRight_ExperimentNode1_presize ||
        		User1DownRight_ExperimentNode2_size!=User1DownRight_ExperimentNode2_presize ||
        		User1DownRight_ExperimentNode3_size!=User1DownRight_ExperimentNode3_presize){
    	if(User1DownRight_ExperimentNode1.size()>=10 &&
    	   User1DownRight_ExperimentNode2.size()>=10  && User1DownRight_ExperimentNode3.size()>=10){
    		
    		/*for(int i=User1DownLeft_ExperimentNode1.size()-1,count=0;count<10 ;i--,count++){ 
    			final_User1DownLeft_ExperimentNode1=final_User1DownLeft_ExperimentNode1+User1DownLeft_ExperimentNode1.get(i);
    			
    		}
    		final_User1DownLeft_ExperimentNode1=final_User1DownLeft_ExperimentNode1/10;
    		
    		for(int i=User1DownLeft_ExperimentNode2.size()-1,count=0;count<10 ;i--,count++){ 
    			final_User1DownLeft_ExperimentNode2=final_User1DownLeft_ExperimentNode2+User1DownLeft_ExperimentNode2.get(i);
    			
    		}	
    		final_User1DownLeft_ExperimentNode2=final_User1DownLeft_ExperimentNode2/10;
    		
    		
    		for(int i=User1DownLeft_ExperimentNode3.size()-1,count=0;count<10 ;i--,count++){ 
    			final_User1DownLeft_ExperimentNode3=final_User1DownLeft_ExperimentNode3+User1DownLeft_ExperimentNode3.get(i);
    			
    		}
    		final_User1DownLeft_ExperimentNode3=final_User1DownLeft_ExperimentNode3/10;
    		*/
    		
    		for(int i=User1DownRight_ExperimentNode1.size()-1,count=0;count<10 ;i--,count++){ 
    			final_User1DownRight_ExperimentNode1=final_User1DownRight_ExperimentNode1+User1DownRight_ExperimentNode1.get(i);
    			
    		}
    		final_User1DownRight_ExperimentNode1=final_User1DownRight_ExperimentNode1/10;
    		TotalSort.add(final_User1DownRight_ExperimentNode1);
    		
    		for(int i=User1DownRight_ExperimentNode2.size()-1,count=0;count<10 ;i--,count++){ 
    			final_User1DownRight_ExperimentNode2=final_User1DownRight_ExperimentNode2+User1DownRight_ExperimentNode2.get(i);
    			
    		}
    		final_User1DownRight_ExperimentNode2=final_User1DownRight_ExperimentNode2/10;
    		TotalSort.add(final_User1DownRight_ExperimentNode2);

    		for(int i=User1DownRight_ExperimentNode3.size()-1,count=0;count<10 ;i--,count++){ 
    			final_User1DownRight_ExperimentNode3=final_User1DownRight_ExperimentNode3+User1DownRight_ExperimentNode3.get(i);
    			
    		}
    		final_User1DownRight_ExperimentNode3=final_User1DownRight_ExperimentNode3/10;
    		TotalSort.add(final_User1DownRight_ExperimentNode3);
    	    
    		
    		
    		Collections.sort(TotalSort);
    		current_lowrssi=TotalSort.get(0);
    		current_secondssi=TotalSort.get(1);
    		System.out.println("current_rssi!!!!!!"+current_lowrssi+" "+final_User1DownRight_ExperimentNode1+" "+
    		current_secondssi+" "+final_User1DownRight_ExperimentNode2);
    		if(current_lowrssi ==final_User1DownRight_ExperimentNode1 &&
    		   current_secondssi ==	final_User1DownRight_ExperimentNode2){     //State1
    			  try {
  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
  							+ "/公轉展演State2.txt",true);
  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
  				bufferedWriter.write(("State1_NO")+"\n");
  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
  				bufferedWriter.flush();
  				bufferedWriter.close();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
      			  fail_times++;

    		}else if(current_lowrssi ==final_User1DownRight_ExperimentNode2 &&     //State2
    				current_secondssi ==final_User1DownRight_ExperimentNode1){
  			  try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/公轉展演State2.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write(("State2_OK")+"\n");
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
			  OK_times++;

    		}
    		else if(current_lowrssi ==final_User1DownRight_ExperimentNode2 &&
    				current_secondssi ==final_User1DownRight_ExperimentNode3){    //State3
    			  try {
  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
  							+ "/公轉展演State2.txt",true);
  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
  				bufferedWriter.write(("State3_NO")+"\n");
  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
  				bufferedWriter.flush();
  				bufferedWriter.close();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    		  
    		  fail_times++;
    		}
    		else if(current_lowrssi ==final_User1DownRight_ExperimentNode3 &&
    				current_secondssi ==final_User1DownRight_ExperimentNode2){    //State4
    			  try {
  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
  							+ "/公轉展演State2.txt",true);
  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
  				bufferedWriter.write(("State4_NO")+"\n");
  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
  				bufferedWriter.flush();
  				bufferedWriter.close();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    			
    		  fail_times++;
    			
    		}
    		else{
    			  try {
  					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
  							+ "/公轉展演State2.txt",true);
  				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
  				bufferedWriter.write(("NOstate")+"\n");
  				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
  				bufferedWriter.flush();
  				bufferedWriter.close();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    			fail_times++;
    		}
			/*   try {
					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
							+ "/公轉右轉45度準確率.txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write(("OK")+"\n");
				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
				bufferedWriter.flush();
				bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			   
			/*   
    		System.out.println("User1DownLeft_ExperimentNode1");
    		for (int i = 0; i < User1DownLeft_ExperimentNode1.size(); i++) {
				System.out.print(User1DownLeft_ExperimentNode1.get(i)+" ");
			}
    		System.out.print("\n");
    		System.out.println("User1DownLeft_ExperimentNode2");
    		for (int i = 0; i < User1DownLeft_ExperimentNode2.size(); i++) {
				System.out.print(User1DownLeft_ExperimentNode2.get(i)+" ");			
			}
    		System.out.print("\n");
    		System.out.println("User1DownLeft_ExperimentNode3");
    		for (int i = 0; i < User1DownLeft_ExperimentNode3.size(); i++) {
				System.out.print(User1DownLeft_ExperimentNode3.get(i)+" ");			
			}
    		
    		System.out.println("User1DownRight_ExperimentNode1");
    		for (int i = 0; i < User1DownRight_ExperimentNode1.size(); i++) {
				System.out.print(User1DownRight_ExperimentNode1.get(i)+" ");
			}
    		System.out.print("\n");
    		System.out.println("User1DownRight_ExperimentNode2");
    		for (int i = 0; i < User1DownRight_ExperimentNode2.size(); i++) {
				System.out.print(User1DownRight_ExperimentNode2.get(i)+" ");			
			}
    		System.out.print("\n");
    		System.out.println("User1DownRight_ExperimentNode3");
    		for (int i = 0; i < User1DownRight_ExperimentNode3.size(); i++) {
				System.out.print(User1DownRight_ExperimentNode3.get(i)+" ");			
			}
			*/
    		
						
    		if(OK_times+fail_times==100){
    			
 			   try {
 					fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute"
 							+ "/公轉展演State2準確率.txt",true);
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
			final_User1DownLeft_ExperimentNode1=0;final_User1DownLeft_ExperimentNode2=0;
			final_User1DownLeft_ExperimentNode3=0;final_User1DownRight_ExperimentNode1=0;
			final_User1DownRight_ExperimentNode2=0;final_User1DownRight_ExperimentNode3=0;
			TotalSort.clear();
			User1DownRight_ExperimentNode1_presize=User1DownRight_ExperimentNode1.size();
			User1DownRight_ExperimentNode2_presize=User1DownRight_ExperimentNode2.size();
			User1DownRight_ExperimentNode3_presize=User1DownRight_ExperimentNode3.size();
			

    		
    		
    	}}
        
    	
    	
    	


    
    }
    
    
	public double intRssi_to_distance(int intrssi){
	   	   if(intrssi<=40){
	   			Distance=(Math.pow(12,1.5*(intrssi/33.68361333)-1));
	   			Distance=Distance/100;
	   			return Distance;
	   			//System.out.println("5CM"+Math.pow(12,1.5*(intrssi/33.68361333)-1));  
	   			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.569897)-1)+"\n");
	   		}
	   		else if(intrssi>=41 && intrssi<=57){
	   			Distance=(Math.pow(12,1.5*(intrssi/36.95969358)-1));
	   			Distance=Distance/100;
	   			return Distance;
	   			//System.out.println("30CM"+Math.pow(12,1.5*(intrssi/36.95969358)-1));
	   			//bufferedWriter.write(Math.pow(12,1.5*(intrssi/25.1784927)-1)+"\n");
	   			
	   		}
	   		else if(intrssi >=58 && intrssi<=70){
	   			Distance=(Math.pow(12,1.5*(intrssi/39.4413766)-1));
	   			Distance=Distance/100;
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
