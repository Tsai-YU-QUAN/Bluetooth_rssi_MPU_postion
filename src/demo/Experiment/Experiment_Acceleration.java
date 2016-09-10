package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.internal.ir.LiteralNode.ArrayLiteralNode.ArrayUnit;

import org.apache.poi.util.ArrayUtil;
import org.json.JSONException;

import demo.Globalvariable;

public class Experiment_Acceleration {
	String UserName="",acc_x,acc_y;
	static long currenttime;
	static boolean if_first_time_slot=false;
	static int index=0;

	static int Thresholdcount=0;
	static double User1Final_x, User1Final_y;
	
    public void ACC_processing(String Accdata)
            throws java.io.IOException, JSONException
    {
    	//User1A1.14!0.15c   我把負的轉正的，因為傳送上比較方便
    	//System.out.println("Accdata "+Accdata);

    	UserName=Accdata.substring(0, 5);
    	acc_x=Accdata.substring(6,10);
    	acc_y=Accdata.substring(11,15);
    	try {
    	if(UserName.equals("User1")){
    	User1Final_x=Double.parseDouble(acc_x)-Globalvariable.User1offset_x;
    	User1Final_y=Double.parseDouble(acc_y)-Globalvariable.User1offset_y;
    	System.out.println("UserName後: "+UserName+" "+User1Final_x+" "+User1Final_y);   //ACC Threshold超過0.1，而且個數1/2代表有在移動
    	Globalvariable.User1Totalacc_x.add(User1Final_x);//=================以下會找到兩個Threshold並回傳兩個機率，一個是State5=>State3; State5=>State1
    	Globalvariable.User1Totalacc_y.add(User1Final_y);
    	currenttime=System.currentTimeMillis();
		//System.out.println("UserName後: "+Globalvariable.User1Totalacc_x+" "+Globalvariable.User1Totalacc_y);
		 if(Double.parseDouble(acc_x) >=0.1){
			Thresholdcount=Thresholdcount+1;
		 }
		 if(Double.parseDouble(acc_y) >=0.1){
			 Thresholdcount=Thresholdcount+1;
		 }
		   //System.out.println("currenttime: "+currenttime+" "+Globalvariable.endslotime+" "+(currenttime-Globalvariable.endslotime)+" "+
		   //Globalvariable.time_slot);
  	   if(if_first_time_slot!=true && (currenttime - Globalvariable.starttime >=Globalvariable.time_slot)){ //第一次時 5s內取10個data
 		  if_first_time_slot=true;
	      	FileWriter fw1;
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/"
						+"Acc"+String.valueOf(currenttime-Globalvariable.starttime)+".txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				bufferedWriter.write(String.valueOf((double) Thresholdcount/(Globalvariable.User1Totalacc_x.size()+Globalvariable.User1Totalacc_y.size())+"\n"));
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		   
 		  System.out.println("5s有沒有移動"+(double) Thresholdcount/(Globalvariable.User1Totalacc_x.size()+Globalvariable.User1Totalacc_y.size()));
 		 Thresholdcount=0;
 		 Globalvariable.User1Totalacc_x.clear();
 		 Globalvariable.User1Totalacc_y.clear();
  	   }
  	   else if(if_first_time_slot==true &&  (currenttime - Globalvariable.endslotime)>=Globalvariable.time_slot){
	    /*  	FileWriter fw1;
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/"
						+"Acc"+String.valueOf(currenttime-Globalvariable.starttime)+".txt",true);
				BufferedWriter bufferedWriter = new BufferedWriter(fw1);
				//bufferedWriter.write(String.valueOf((double) Thresholdcount/(Globalvariable.User1Totalacc_x.size()+Globalvariable.User1Totalacc_y.size())+"\n"));
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
  		   


  	   }
    	
    }else if(UserName.equals("User2")){
    	
    }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

}
