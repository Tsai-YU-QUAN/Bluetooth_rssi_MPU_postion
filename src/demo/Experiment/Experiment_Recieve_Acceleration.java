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

public class Experiment_Recieve_Acceleration {
	String UserName="",acc_x,acc_y;
	static long currenttime;
	static boolean if_first_time_slot=false;
	static int index=0;

	static double initTx=0.1,initTy=0.1; //=================為了不必要的90次迴圈
	static double User1Final_x, User1Final_y;
	static double User1OldFinal_x, User1OldFinal_y;

	static int allzerocount=0;//===============================為了acc不動時狀況設計
	
    public void ACC_receive(String Accdata)
            throws java.io.IOException, JSONException
    {
    	//User1Ap0.01!n0.01c   
    	System.out.println("Accdata "+Accdata);

    	UserName=Accdata.substring(0, 5);

    	try {
    	if(UserName.equals("User1")){
        	if(Accdata.substring(6,7).equals("n")){
        		acc_x=Accdata.substring(7,11);
        		User1Final_x=-Double.parseDouble(acc_x)+Globalvariable.User1offset_x;
        	}else{
        		acc_x=Accdata.substring(7,11);
        		User1Final_x=Double.parseDouble(acc_x)+Globalvariable.User1offset_x;
        	}
        	if(Accdata.substring(12,13).equals("n")){
            	acc_y=Accdata.substring(13,17);
            	User1Final_y=-Double.parseDouble(acc_y)+Globalvariable.User1offset_y;
        	}else {
            	acc_y=Accdata.substring(13,17);
            	User1Final_y=Double.parseDouble(acc_y)+Globalvariable.User1offset_y;
			}
        	
    	System.out.println("UserName後: "+UserName+" "+
    	User1Final_x+" "+User1Final_y+" "+Globalvariable.if_statrtacc);   //ACC Threshold超過0.1，而且個數1/2代表有在移動
    	Globalvariable.User1Totalacc_x.add(User1Final_x);
    	Globalvariable.User1Totalacc_y.add(User1Final_y);
    	Globalvariable.User1Postiveacc_x.add(Math.abs(User1Final_x));//=================以下會找到兩個Threshold並回傳兩個機率，一個是State5=>State3; State5=>State1
    	Globalvariable.User1Postiveacc_y.add(Math.abs(User1Final_y));
    	currenttime=System.currentTimeMillis();
    	System.out.println("if_acccompute "+Globalvariable.if_U1acccompute_x+" "+Globalvariable.if_U1acccompute_y);
    	if(Globalvariable.User1Totalacc_x.size()>=2 && Globalvariable.if_firstx==false){    //===一旦開始運動我們取在加速值，不取相反值
    		User1OldFinal_x=Globalvariable.User1Totalacc_x.get(Globalvariable.User1Totalacc_x.size()-2);   //取得上次的值
    		if(Math.abs(User1Final_x) < initTx || (User1Final_x*User1OldFinal_x) <0){
    			Globalvariable.if_U1acccompute_x=false;                                         //關閉ACC運算
    		}
    	}
    	
    	if(Globalvariable.User1Totalacc_y.size()>=2 && Globalvariable.if_first_y==false){    //===一旦開始運動我們取在加速值，不取相反值
    		User1OldFinal_y=Globalvariable.User1Totalacc_y.get(Globalvariable.User1Totalacc_y.size()-2);   //取得上次的值
    		if(Math.abs(User1Final_y) < initTy || (User1Final_y*User1OldFinal_y) <0){
    			Globalvariable.if_U1acccompute_y=false;                                         //關閉ACC運算
    		}
    	}
    	if(Globalvariable.if_U1acccompute_x==true){
    	if(Math.abs(User1Final_x) > initTx ){ //=========================================區間裏取得可能有方向的值
    		if(Globalvariable.if_firstx==true){
    			if(User1Final_x<0){
    				Globalvariable.AccX_PNegative="Negative";
    			}else{
    				Globalvariable.AccX_PNegative="Postive";
    			}
    			Globalvariable.if_firstx=false;
    		}
    		if(Globalvariable.AccX_PNegative=="Negative" && User1Final_x<0){
    	    Globalvariable.User1Work_array_x.add(User1Final_x);
    		Globalvariable.User1Work_x=Globalvariable.User1Work_x+User1Final_x;
    		System.out.println("User1Final_x "+User1Final_x);
    		}else if(Globalvariable.AccX_PNegative=="Postive" && User1Final_x>0){
        	    Globalvariable.User1Work_array_x.add(User1Final_x);
        		Globalvariable.User1Work_x=Globalvariable.User1Work_x+User1Final_x;
        		System.out.println("User1Final_x "+User1Final_x);
        		
    		}
    		//Globalvariable.if_initTx=true;

    	}}
    	if(Globalvariable.if_U1acccompute_y==true){
    	if(Math.abs(User1Final_y) > initTy ){
       		if(Globalvariable.if_first_y==true){
    			if(User1Final_y<0){
    				Globalvariable.AccY_PNegative="Negative";
    			}else{
    				Globalvariable.AccY_PNegative="Postive";
    			}
    			Globalvariable.if_first_y=false;
    		}
    		if(Globalvariable.AccY_PNegative=="Negative" && User1Final_y<0){
        	Globalvariable.User1Work_array_y.add(User1Final_y);
    		Globalvariable.User1Work_y=Globalvariable.User1Work_y+User1Final_y;
    		System.out.println("User1Final_y "+User1Final_y);
    		
    		}else if(Globalvariable.AccY_PNegative=="Postive" && User1Final_y>0){
            	Globalvariable.User1Work_array_y.add(User1Final_y);
        		Globalvariable.User1Work_y=Globalvariable.User1Work_y+User1Final_y;
        		System.out.println("User1Final_y "+User1Final_y);
        		
    		}
    	}}
    	
    	System.out.println("allzerocount "+allzerocount);
		if(Math.abs(User1Final_x) < initTx && Math.abs(User1Final_y) < initTy 
		   && Globalvariable.if_U1acccompute_x==false && Globalvariable.if_U1acccompute_y==false){ 
			//沒在動時我們才開始算加速度參數(收集連續7個都是x<initTx ,y<initTy)
			if(allzerocount>=8){             //allzerocount=1，則跑7次
			Globalvariable.if_U1acccompute_x=true;//把acc可算打開
			Globalvariable.if_U1acccompute_y=true;
		    Globalvariable.if_firstx=true;Globalvariable.if_first_y=true;
			allzerocount=0;
			}
			allzerocount=allzerocount+1;
		}else{                                              //中間有不是零的就歸0，代表不連續
			allzerocount=0;
		}
    	FileWriter fw1;
		   try {
				fw1 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance"
						+ "/"+"User1Final.txt",true);
			BufferedWriter bufferedWriter = new BufferedWriter(fw1);
			bufferedWriter.write((currenttime-Globalvariable.starttime)+" "+User1Final_x+" "+User1Final_y+"\n");
			//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
			bufferedWriter.flush();
			bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	
    }else if(UserName.equals("User2")){
    	
    }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

}
