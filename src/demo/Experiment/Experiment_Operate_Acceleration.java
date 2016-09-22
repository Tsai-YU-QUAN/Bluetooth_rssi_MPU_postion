package demo.Experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;

import demo.Globalvariable;

public class Experiment_Operate_Acceleration {
	String UserName="",acc_x,acc_y;
	static double User1Final_x, User1Final_y;
	public static double StateMinCount,StateMaxCount;
	static long currenttime;
	static boolean if_first_time_slot=false;
	static int index=0;
	static double ThresholdMin=0.15,ThresholdMax=0.2; //====================有小與大的Threshold
	static double Th1x_Direction=0.15,Th1y_Direction=0.15,Th2x_Direction=0.15,Th2y_Direction=0.15;
	static int User1Allacc;
    int leftcount=0, rightcount=0 ,upcount=0 ,downcount=0;
    double U1MinLefterror=-1,U1MinRighterror=-1,U1MinUperror=-1,U1MinDownerror=-1;//初始化獨到的值都為正
	
    public void ACC_processing()
            throws java.io.IOException, JSONException
    {
    	//User1A1.14!0.15c   我把負的轉正的，因為傳送上比較方便
    	//System.out.println("InACC_processing");


    	try {
    	currenttime=System.currentTimeMillis();
  	   if(if_first_time_slot!=true && (currenttime - Globalvariable.starttime >=1)){ //大於一毫秒
 		  if_first_time_slot=true;
  	   }
  	   else if(Globalvariable.if_statrtacc==true &&if_first_time_slot==true &&  (currenttime - Globalvariable.endslotime)>=Globalvariable.time_slot){
  	    	User1Allacc=Globalvariable.User1Totalacc_x.size()+Globalvariable.User1Totalacc_y.size();
  		   for (int i = 0; i < Globalvariable.User1Postiveacc_x.size(); i++) {//==============x,y長度都一樣
  	  		   if((ThresholdMin-0.05) <=Globalvariable.User1Postiveacc_x.get(i) &&(ThresholdMin+0.025) >=Globalvariable.User1Postiveacc_x.get(i)){ //0.1<=data<=0.175
  	  			   StateMinCount=StateMinCount+1;
  	  		   }
  	  		   if((ThresholdMin-0.05) <=Globalvariable.User1Postiveacc_y.get(i) &&(ThresholdMin+0.025) >=Globalvariable.User1Postiveacc_y.get(i)){ //0.1<=data<=0.175
  	  			   StateMinCount=StateMinCount+1;
  	  		   }
  	  		   if((ThresholdMax-0.025) <Globalvariable.User1Postiveacc_x.get(i)){//0.175<data
  	  			   StateMaxCount=StateMaxCount+1;
  	  		   }
  	  		   if((ThresholdMax-0.025) <Globalvariable.User1Postiveacc_y.get(i)){//0.175<data
  	  			   StateMaxCount=StateMaxCount+1;
  	  		   }
		}
  		   Globalvariable.User1MinMaxacc.add(StateMinCount+StateMaxCount);
		      	FileWriter fw2;
 			   try {
 					fw2 = new FileWriter("/Users/tsai/Desktop/穿戴式/穿戴式展演資料/Wise_server_compute/confindence_distance/StateCount.txt",true);
 				    BufferedWriter bufferedWriter = new BufferedWriter(fw2);
 					bufferedWriter.write("StateMaxCount "+(StateMaxCount/User1Allacc)+"StateMinCount "+(StateMinCount/User1Allacc)+"\n");
 					
 				//bufferedWriter.write(TimDownLeft_yaw+" "+TimDownRight_yaw+" "+(Tim_yaw-Jack_yaw-456)+"\n");
 				bufferedWriter.flush();
 				bufferedWriter.close();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			   
  		   System.out.println("StateMaxCount+StateMinCount "+StateMaxCount+" "+StateMinCount);
  		   for (int i = 0; i <=8; i++) {//先做初始化 size=9
  			 Globalvariable.AccStaterate.add(0.0); 			   
  		   }
  		   System.out.println("Globalvariable.AccStaterate "+Globalvariable.AccStaterate.size());
  		   if(StateMaxCount+StateMinCount==0){             //不動情況下
  			   for (int i = 1; i <= 8; i++) {
  				if(Globalvariable.CurrentState==i){
  				Globalvariable.AccStaterate.set(i, 1.0);  //機率百分之百
  				}else{
				Globalvariable.AccStaterate.set(i, 0.0);  //機率=0
  				}
			}
  		   }else if(Globalvariable.if_rotation==true){
  			   //有旋轉但加速度歸0，此時全部由RSSI偵測
  			   
  		   }else if(StateMaxCount+StateMinCount>0){
  			   Globalvariable.if_operateACC=true;
  			   System.out.println("inStateMaxCount");
  			   if((Globalvariable.User1Work_x!=0.0 || Globalvariable.User1Work_y!=0.0)){
  				   if(Globalvariable.User1Work_x!=0){
  					 Globalvariable.User1Work_x=Globalvariable.User1Work_x/Globalvariable.User1Work_array_x.size();
  				   }
  				   if(Globalvariable.User1Work_y!=0){
    			     Globalvariable.User1Work_y=Globalvariable.User1Work_y/Globalvariable.User1Work_array_y.size();
  				   }
  	  			   System.out.println("Globalvariable.User1Work "+Globalvariable.User1Work_x+" "+Globalvariable.User1Work_y);
  	  			   //=============================================以下計算User1 S2,S4,S6,S8情況
  	  			   if(Globalvariable.CurrentState==2){
  	  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往下面
  	  	  	  				U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
  	  							   
  	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往下面
  	  	  	  			    U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
  	  							   
  	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往下面
  	  	  	  			    U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
  	  	  	  				
  	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往下面
  	  	  	  			   U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
  	  	  	  			   }
  	  	  	  			   
  	  	  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_x >=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_y>=0){//往左
  	  	  	  				U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);
  	  							   
  	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_x >=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_y<=0){//往左
  	  	  	  			    U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);

  	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_x <=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_y<=0){//往左
  	  	  	  			    U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);

  	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_x <=0 && 
  	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_y>=0){//往左
  	  	  	  			    U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);

  	  	  	  			   }
  	  	  	  			   if(U1MinLefterror>0 && U1MinDownerror>0){//都有值，所以要做比較
  	  	  	  				   if(U1MinLefterror < U1MinDownerror){
  	  							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
  	  							   Globalvariable.AccStaterate.set(2, 0.0);
  	  							   Globalvariable.AccStaterate.set(3, 0.0);
  	  							   Globalvariable.AccStaterate.set(4, 0.0);
  	  							   Globalvariable.AccStaterate.set(5, 0.0);
  	  							   Globalvariable.AccStaterate.set(6, 0.0);
  	  							   Globalvariable.AccStaterate.set(7, 0.0);
  	  							   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);
  	  	  	  				   }else if(U1MinLefterror > U1MinDownerror){
  	  	  						   Globalvariable.AccStaterate.set(1, 0.0);
  	  	  						   Globalvariable.AccStaterate.set(2, 0.0);
  	  	  						   Globalvariable.AccStaterate.set(3, StateMinCount/User1Allacc);
  	  	  						   Globalvariable.AccStaterate.set(4, StateMinCount/User1Allacc);
  	  	  						   Globalvariable.AccStaterate.set(5, 0.0);
  	  	  						   Globalvariable.AccStaterate.set(6, 0.0);
  	  	  						   Globalvariable.AccStaterate.set(7, 0.0);
  	  	  						   Globalvariable.AccStaterate.set(8, 0.0);  
  	  	  	  					   
  	  	  	  				   }
  	  	  	  			   }else if(U1MinLefterror>0 && U1MinDownerror==-1){
	  							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
	  							   Globalvariable.AccStaterate.set(2, 0.0);
	  							   Globalvariable.AccStaterate.set(3, 0.0);
	  							   Globalvariable.AccStaterate.set(4, 0.0);
	  							   Globalvariable.AccStaterate.set(5, 0.0);
	  							   Globalvariable.AccStaterate.set(6, 0.0);
	  							   Globalvariable.AccStaterate.set(7, 0.0);
	  							   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);				   
  	  	  	  			   }else if(U1MinLefterror==-1 && U1MinDownerror>0){
	  	  						   Globalvariable.AccStaterate.set(1, 0.0);
	  	  						   Globalvariable.AccStaterate.set(2, 0.0);
	  	  						   Globalvariable.AccStaterate.set(3, StateMinCount/User1Allacc);
	  	  						   Globalvariable.AccStaterate.set(4, StateMinCount/User1Allacc);
	  	  						   Globalvariable.AccStaterate.set(5, 0.0);
	  	  						   Globalvariable.AccStaterate.set(6, 0.0);
	  	  						   Globalvariable.AccStaterate.set(7, 0.0);
	  	  						   Globalvariable.AccStaterate.set(8, 0.0);  				   
  	  	  	  			   }
  	  				   
  	  			   }else if(Globalvariable.CurrentState==4){
  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往上面
  	  				U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.1"+" "+U1MinUperror);
						   
  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往上面
  	  				U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.2"+" "+U1MinUperror);
						   
  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往上面
  	  				U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.3"+" "+U1MinUperror);
  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往上面
  	  				U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.4"+" "+U1MinUperror);
  	  			   }
  	  			   
  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_x >=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_y>=0){//往右
  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.5"+" "+U1MinRighterror);
						   
  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_x >=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_y<=0){//往右
  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.6"+" "+U1MinRighterror);					   
  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_x <=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_y<=0){//往右
  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.7"+" "+U1MinRighterror);
  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))<=0 && Globalvariable.User1Work_x <=0 && 
  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90))>=0 && Globalvariable.User1Work_y>=0){//往右
  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_x)+
  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+90) -Globalvariable.User1Work_y);
						   System.out.println("ExpAvgUser1Totalyaw "+" "+"Find4.8"+" "+U1MinRighterror);
  	  			   }
  	  			   if(U1MinRighterror>0 && U1MinUperror>0){
  	  				   if(U1MinRighterror < U1MinUperror){
						   Globalvariable.AccStaterate.set(1, 0.0);
						   Globalvariable.AccStaterate.set(2, 0.0);
						   Globalvariable.AccStaterate.set(3, 0.0);
						   Globalvariable.AccStaterate.set(4, 0.0);
						   Globalvariable.AccStaterate.set(5, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(6, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(7, 0.0);
						   Globalvariable.AccStaterate.set(8, 0.0);
  	  				   }else if(U1MinRighterror > U1MinUperror){
  						   Globalvariable.AccStaterate.set(1, 0.0);
  						   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);
  						   Globalvariable.AccStaterate.set(3, StateMinCount/User1Allacc);
  						   Globalvariable.AccStaterate.set(4, 0.0);
  						   Globalvariable.AccStaterate.set(5, 0.0);
  						   Globalvariable.AccStaterate.set(6, 0.0);
  						   Globalvariable.AccStaterate.set(7, 0.0);
  						   Globalvariable.AccStaterate.set(8, 0.0);  
  	  					   
  	  				   }
  	  			   }else if(U1MinRighterror>0 && U1MinUperror==-1){
					   Globalvariable.AccStaterate.set(1, 0.0);
					   Globalvariable.AccStaterate.set(2, 0.0);
					   Globalvariable.AccStaterate.set(3, 0.0);
					   Globalvariable.AccStaterate.set(4, 0.0);
					   Globalvariable.AccStaterate.set(5, StateMinCount/User1Allacc);
					   Globalvariable.AccStaterate.set(6, StateMinCount/User1Allacc);
					   Globalvariable.AccStaterate.set(7, 0.0);
					   Globalvariable.AccStaterate.set(8, 0.0); 				   
  	  			   }else if(U1MinRighterror==-1 && U1MinUperror>0){
					   Globalvariable.AccStaterate.set(1, 0.0);
					   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);
					   Globalvariable.AccStaterate.set(3, StateMinCount/User1Allacc);
					   Globalvariable.AccStaterate.set(4, 0.0);
					   Globalvariable.AccStaterate.set(5, 0.0);
					   Globalvariable.AccStaterate.set(6, 0.0);
					   Globalvariable.AccStaterate.set(7, 0.0);
					   Globalvariable.AccStaterate.set(8, 0.0);  				   
  	  			   }

  	  				   
  	  			   }else if(Globalvariable.CurrentState==6){
  	  				   
  	  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往上面
   	  	  	  				U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
   	  							   
   	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往上面
   	  	  	  			    U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
   	  							   
   	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往上面
   	  	  	  		    	U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
   	  	  	  				
   	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往上面
   	  	  	  			    U1MinUperror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
   	  	  	  			   }
   	  	  	  			   
   	  	  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_x >=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_y>=0){//往左
   	  	  	  				U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);
   	  							   
   	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_x >=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_y<=0){//往左
   	  	  	  			    U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);

   	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_x <=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_y<=0){//往左
   	  	  	  			    U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);

   	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_x <=0 && 
   	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_y>=0){//往左
   	  	  	  			    U1MinLefterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
   	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);

   	  	  	  			   }
   	  	  	  			   if(U1MinLefterror>0 && U1MinUperror>0){//都有值，所以要做比較
   	  	  	  				   if(U1MinLefterror < U1MinUperror){
   	  							   Globalvariable.AccStaterate.set(1, 0.0);
   	  							   Globalvariable.AccStaterate.set(2, 0.0);
   	  							   Globalvariable.AccStaterate.set(3, 0.0);
   	  							   Globalvariable.AccStaterate.set(4, StateMinCount/User1Allacc);
   	  							   Globalvariable.AccStaterate.set(5, StateMinCount/User1Allacc);
   	  							   Globalvariable.AccStaterate.set(6, 0.0);
   	  							   Globalvariable.AccStaterate.set(7, 0.0);
   	  							   Globalvariable.AccStaterate.set(8, 0.0);
   	  	  	  				   }else if(U1MinLefterror > U1MinUperror){
   	  	  						   Globalvariable.AccStaterate.set(1, 0.0);
   	  	  						   Globalvariable.AccStaterate.set(2, 0.0);
   	  	  						   Globalvariable.AccStaterate.set(3, 0.0);
   	  	  						   Globalvariable.AccStaterate.set(4, 0.0);
   	  	  						   Globalvariable.AccStaterate.set(5, 0.0);
   	  	  						   Globalvariable.AccStaterate.set(6, 0.0);
   	  	  						   Globalvariable.AccStaterate.set(7, StateMinCount/User1Allacc);
   	  	  						   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);  
   	  	  	  					   
   	  	  	  				   }
   	  	  	  			   }else if(U1MinLefterror>0 && U1MinUperror==-1){
 	  							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
 	  							   Globalvariable.AccStaterate.set(2, 0.0);
 	  							   Globalvariable.AccStaterate.set(3, 0.0);
 	  							   Globalvariable.AccStaterate.set(4, 0.0);
 	  							   Globalvariable.AccStaterate.set(5, 0.0);
 	  							   Globalvariable.AccStaterate.set(6, 0.0);
 	  							   Globalvariable.AccStaterate.set(7, 0.0);
 	  							   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);				   
   	  	  	  			   }else if(U1MinLefterror==-1 && U1MinUperror>0){
	  	  						   Globalvariable.AccStaterate.set(1, 0.0);
	  	  						   Globalvariable.AccStaterate.set(2, 0.0);
	  	  						   Globalvariable.AccStaterate.set(3, 0.0);
	  	  						   Globalvariable.AccStaterate.set(4, 0.0);
	  	  						   Globalvariable.AccStaterate.set(5, 0.0);
	  	  						   Globalvariable.AccStaterate.set(6, 0.0);
	  	  						   Globalvariable.AccStaterate.set(7, StateMinCount/User1Allacc);
	  	  						   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);  		   
   	  	  	  			   }
   	  				   
  	  				   
  	  			   }else if (Globalvariable.CurrentState==8){
  	  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往下面
    	  	  	  				U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
    	  							   
    	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_x >=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往下面
    	  	  	  				U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
    	  							   
    	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_y<=0){//往下面
    	  	  	  				U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
    	  	  	  				
    	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))<=0 && Globalvariable.User1Work_x <=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0))>=0 && Globalvariable.User1Work_y>=0){//往下面
    	  	  	  				U1MinDownerror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+0) -Globalvariable.User1Work_y);
    	  	  	  			   }
    	  	  	  			   
    	  	  	  			   if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_x >=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_y>=0){//往右
    	  	  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);
    	  							   
    	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_x >=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_y<=0){//往右
    	  	  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);

    	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_x <=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_y<=0){//往左
    	  	  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);

    	  	  	  			   }else if(Th1x_Direction*Math.sin(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))<=0 && Globalvariable.User1Work_x <=0 && 
    	  	  	  				  Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270))>=0 && Globalvariable.User1Work_y>=0){//往右
    	  	  	  				U1MinRighterror=Math.abs(Th1x_Direction*Math.sin(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_x)+
    	  	  	  						      Math.abs(Th1y_Direction*Math.cos(Globalvariable.AvgUser1Totalyaw+270) -Globalvariable.User1Work_y);

    	  	  	  			   }
    	  	  	  			   if(U1MinRighterror>0 && U1MinDownerror>0){//都有值，所以要做比較
    	  	  	  				   if(U1MinRighterror < U1MinDownerror){
    	  							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
    	  							   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);
    	  							   Globalvariable.AccStaterate.set(3, 0.0);
    	  							   Globalvariable.AccStaterate.set(4, 0.0);
    	  							   Globalvariable.AccStaterate.set(5, 0.0);
    	  							   Globalvariable.AccStaterate.set(6, 0.0);
    	  							   Globalvariable.AccStaterate.set(7, 0.0);
    	  							   Globalvariable.AccStaterate.set(8, 0.0);
    	  	  	  				   }else if(U1MinRighterror > U1MinDownerror){
    	  	  						   Globalvariable.AccStaterate.set(1, 0.0);
    	  	  						   Globalvariable.AccStaterate.set(2, 0.0);
    	  	  						   Globalvariable.AccStaterate.set(3, 0.0);
    	  	  						   Globalvariable.AccStaterate.set(4, 0.0);
    	  	  						   Globalvariable.AccStaterate.set(5, 0.0);
    	  	  						   Globalvariable.AccStaterate.set(6, StateMinCount/User1Allacc);
    	  	  						   Globalvariable.AccStaterate.set(7, StateMinCount/User1Allacc);
    	  	  						   Globalvariable.AccStaterate.set(8, 0.0);  
    	  	  	  					   
    	  	  	  				   }
    	  	  	  			   }else if(U1MinRighterror>0 && U1MinDownerror==-1){
	  							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
	  							   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);
	  							   Globalvariable.AccStaterate.set(3, 0.0);
	  							   Globalvariable.AccStaterate.set(4, 0.0);
	  							   Globalvariable.AccStaterate.set(5, 0.0);
	  							   Globalvariable.AccStaterate.set(6, 0.0);
	  							   Globalvariable.AccStaterate.set(7, 0.0);
	  							   Globalvariable.AccStaterate.set(8, 0.0);				   
    	  	  	  			   }else if(U1MinRighterror==-1 && U1MinDownerror>0){
	  	  						   Globalvariable.AccStaterate.set(1, 0.0);
	  	  						   Globalvariable.AccStaterate.set(2, 0.0);
	  	  						   Globalvariable.AccStaterate.set(3, 0.0);
	  	  						   Globalvariable.AccStaterate.set(4, 0.0);
	  	  						   Globalvariable.AccStaterate.set(5, 0.0);
	  	  						   Globalvariable.AccStaterate.set(6, StateMinCount/User1Allacc);
	  	  						   Globalvariable.AccStaterate.set(7, StateMinCount/User1Allacc);
	  	  						   Globalvariable.AccStaterate.set(8, 0.0);  		   
    	  	  	  			   }
  	  				   
  	  			   }
  	  			   //=============================================以下計算User1 S1,S3,S5,S7情況
  	  			   if(Globalvariable.CurrentState==1){//==========state1下
  					   for (int i = 0; i <= 90; i++) {  //loop 90次
  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))+" "+
  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)));
  						 if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0){//左邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   leftcount=leftcount+1;
								   }	
  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0){//左邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   leftcount=leftcount+1;
								   }			 
  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0){//左邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   leftcount=leftcount+1;
								   }			 
						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0){//左邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   leftcount=leftcount+1;
								   }			 
						 }
  						 
						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))+" "+
								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)));
						 if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0){//右邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
								   rightcount=rightcount+1;
								   }	
						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0){//右邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
								   rightcount=rightcount+1;
								   }			 
						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0){//右邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
								   rightcount=rightcount+1;
								   }			 
						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0){//右邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
								   rightcount=rightcount+1;
								   }			 
						 }				   
  						   }
  					   if(leftcount < rightcount){
						   Globalvariable.AccStaterate.set(8, 0.0);//n-1
						   Globalvariable.AccStaterate.set(7, 0.0);//n-2
						   Globalvariable.AccStaterate.set(6, 0.0);//n-3
						   Globalvariable.AccStaterate.set(5, StateMaxCount/User1Allacc);//n-4
						   Globalvariable.AccStaterate.set(1, 0.0);
						   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(3, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(4, StateMaxCount/User1Allacc);  						   
  					   }else{
							   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);//n-1
							   Globalvariable.AccStaterate.set(7, StateMinCount/User1Allacc);//n-2
							   Globalvariable.AccStaterate.set(6, StateMaxCount/User1Allacc);//n-3
							   Globalvariable.AccStaterate.set(5, StateMaxCount/User1Allacc);//n-4
							   Globalvariable.AccStaterate.set(1, 0.0);
							   Globalvariable.AccStaterate.set(2, 0.0);
							   Globalvariable.AccStaterate.set(3, 0.0);
							   Globalvariable.AccStaterate.set(4, 0.0);
  					   }
  					   }
				   
				   if(Globalvariable.CurrentState==3){//==========state3下
  					   for (int i = 0; i <= 90; i++) {  //loop 90次
  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))+" "+
  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)));
  						   if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0){//左邊
  							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
  							   Globalvariable.User1Work_y>= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   leftcount=leftcount+1;
  								   }
  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0){//左邊
  							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
  							   Globalvariable.User1Work_y>= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){
  								   leftcount=leftcount+1;
  								   }				   
  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0){//左邊
  							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
  							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   leftcount=leftcount+1;
  								   }				   
  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0){//左邊
  							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
  							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   leftcount=leftcount+1;
  								   }				   
  						   }
  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))+" "+
  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)));
  						 if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0){//右邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   rightcount=rightcount+1;
								   }	
  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0){//右邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   rightcount=rightcount+1;
								   }			 
  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0){//右邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){ 
								   rightcount=rightcount+1;
								   }			 
						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0){//右邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))){
								   rightcount=rightcount+1;
								   }			 
						 }
  						   }
  					   
  					   if(leftcount < rightcount){
						   Globalvariable.AccStaterate.set(2, 0.0);//n-1
						   Globalvariable.AccStaterate.set(1, 0.0);//n-2
						   Globalvariable.AccStaterate.set(8, 0.0);//n-3
						   Globalvariable.AccStaterate.set(7, StateMaxCount/User1Allacc);//n-4
						   Globalvariable.AccStaterate.set(3, 0.0);
						   Globalvariable.AccStaterate.set(4, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(5, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(6, StateMaxCount/User1Allacc);
  						   
  					   }else{
							   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);//n-1
							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);//n-2
							   Globalvariable.AccStaterate.set(8, StateMaxCount/User1Allacc);//n-3
							   Globalvariable.AccStaterate.set(7, StateMaxCount/User1Allacc);//n-4
							   Globalvariable.AccStaterate.set(3, 0.0);
							   Globalvariable.AccStaterate.set(4, 0.0);
							   Globalvariable.AccStaterate.set(5, 0.0);
							   Globalvariable.AccStaterate.set(6, 0.0);
  					   }
  					   }
  				  
					   if(Globalvariable.CurrentState==5){//=======state5下
  					   for (int i = 0; i <= 90; i++) {  //loop 90次
  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))+" "+
  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)));
  						   if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0){//左邊
  							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
  							   Globalvariable.User1Work_y>= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
  								   leftcount=leftcount+1;
  								   }
  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0){//左邊
  							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
  							   Globalvariable.User1Work_y>= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){
  								   leftcount=leftcount+1;
  								   }				   
  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0){//左邊
  							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
  							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
  								   leftcount=leftcount+1;
  								   }				   
  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0){//左邊
  							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
  							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
  								   leftcount=leftcount+1;
  								   }				   
  						   }
  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))+" "+
  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)));
  						 if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0){//右邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   rightcount=rightcount+1;
								   }	
  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0){//右邊
							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   rightcount=rightcount+1;
								   }			 
  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) <= 0){//右邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   rightcount=rightcount+1;
								   }			 
						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i)) >= 0){//右邊
							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))&& 
							   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+0+i))){ 
  								   rightcount=rightcount+1;
								   }			 
						 }
  						   }
  					   if(leftcount <rightcount){
						   Globalvariable.AccStaterate.set(4, 0.0);//n-1
						   Globalvariable.AccStaterate.set(3, 0.0);//n-2
						   Globalvariable.AccStaterate.set(2, 0.0);//n-3
						   Globalvariable.AccStaterate.set(1, StateMaxCount/User1Allacc);//n-4
						   Globalvariable.AccStaterate.set(5, 0.0);
						   Globalvariable.AccStaterate.set(6, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(7, StateMinCount/User1Allacc);
						   Globalvariable.AccStaterate.set(8, StateMaxCount/User1Allacc);
  						   
  					   }else {
							   Globalvariable.AccStaterate.set(4, StateMinCount/User1Allacc);//n-1
							   Globalvariable.AccStaterate.set(3, StateMinCount/User1Allacc);//n-2
							   Globalvariable.AccStaterate.set(2, StateMaxCount/User1Allacc);//n-3
							   Globalvariable.AccStaterate.set(1, StateMaxCount/User1Allacc);//n-4
							   Globalvariable.AccStaterate.set(5, 0.0);
							   Globalvariable.AccStaterate.set(6, 0.0);
							   Globalvariable.AccStaterate.set(7, 0.0);
							   Globalvariable.AccStaterate.set(8, 0.0);
  					   }
					   }
					   
					   if(Globalvariable.CurrentState==7){//=======state7下
	  					   for (int i = 0; i <= 90; i++) {  //loop 90次
	  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))+" "+
	  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)));
	  						   if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0){//左邊
	  							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
	  							   Globalvariable.User1Work_y>= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
	  								   leftcount=leftcount+1;
	  								   }
	  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0){//左邊
	  							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
	  							   Globalvariable.User1Work_y>= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){
	  								   leftcount=leftcount+1;
	  								   }				   
	  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0){//左邊
	  							   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
	  							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
	  								   leftcount=leftcount+1;
	  								   }				   
	  						   }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0){//左邊
	  							   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+90+i))&& 
	  							   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))){ 
	  								   leftcount=leftcount+1;
	  								   }				   
	  						   }
	  						   System.out.println("Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180"+" "+Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))+" "+
	  								 Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)));
	  						 if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0){//右邊
								   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
								   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
	  								   rightcount=rightcount+1;
									   }	
	  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) >= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0){//右邊
								   if(Globalvariable.User1Work_x>= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
								   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
	  								   rightcount=rightcount+1;
									   }			 
	  						 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) <= 0){//右邊
								   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
								   Globalvariable.User1Work_y <= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
	  								   rightcount=rightcount+1;
									   }			 
							 }else if(Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i)) <= 0 && Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i)) >= 0){//右邊
								   if(Globalvariable.User1Work_x<= Th1x_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+180+i))&& 
								   Globalvariable.User1Work_y >= Th1y_Direction*Math.cos(Math.toRadians(Globalvariable.AvgUser1Totalyaw+270+i))){ 
	  								   rightcount=rightcount+1;
									   }			 
							 }
	  						   }
	  					if(leftcount <rightcount){
							   Globalvariable.AccStaterate.set(6, 0.0);//n-1
							   Globalvariable.AccStaterate.set(5, 0.0);//n-2
							   Globalvariable.AccStaterate.set(4, 0.0);//n-3
							   Globalvariable.AccStaterate.set(3, StateMaxCount/User1Allacc);//n-4
							   Globalvariable.AccStaterate.set(7, 0.0);
							   Globalvariable.AccStaterate.set(2, StateMinCount/User1Allacc);
							   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
							   Globalvariable.AccStaterate.set(8, StateMaxCount/User1Allacc);
	  						
	  					}else{
							   Globalvariable.AccStaterate.set(6, StateMinCount/User1Allacc);//n-1
							   Globalvariable.AccStaterate.set(5, StateMinCount/User1Allacc);//n-2
							   Globalvariable.AccStaterate.set(4, StateMaxCount/User1Allacc);//n-3
							   Globalvariable.AccStaterate.set(3, StateMaxCount/User1Allacc);//n-4
							   Globalvariable.AccStaterate.set(7, 0.0);
							   Globalvariable.AccStaterate.set(2, 0.0);
							   Globalvariable.AccStaterate.set(1, 0.0);
							   Globalvariable.AccStaterate.set(8, 0.0);
	  					}
					   }
					   
					   if(leftcount<rightcount){
						   System.out.println("leftcount "+leftcount+"rightcount "+rightcount);
					   }else {
						   System.out.println("leftcount "+leftcount+"rightcount "+rightcount);
					}
					   leftcount=0;rightcount=0;
  					   }
  				   
  			   Globalvariable.if_operateACC=false;
  		   }
  		   /*else if(StateMaxCount == StateMinCount){      //ACC機率不明情況下
  			   for (int i = 1; i <=8; i++) {
  				   if(Globalvariable.CurrentState==i){
  					   Globalvariable.AccStaterate.set(i, 0.0);    //機率=0.0
  				   }else{
  						Globalvariable.AccStaterate.set(i,StateMinCount/User1Allacc);
  				   }
  				   }
  		   }
  		   else{                                          //移動中
  			   System.out.println("移動中");
  			   Globalvariable.AccStaterate.set(Globalvariable.CurrentState,0.0);
  			   //if(StateMinCount>StateMaxCount) //不可能是目前的state，但因為會有反加速度所以還是加入機率
  			   //   Globalvariable.AccStaterate.set(Globalvariable.CurrentState, StateMinCount/User1Allacc);
  			   //else if(StateMinCount<StateMaxCount)
  			   //	Globalvariable.AccStaterate.set(Globalvariable.CurrentState, StateMaxCount/User1Allacc);
  			   //================================================================== Min 三個超出index
  			   if(Globalvariable.CurrentState-2==-1 ||Globalvariable.CurrentState-1==0 ){ //1-2=-1 //1-1=0
  	  			   Globalvariable.AccStaterate.set(8,0.5*(StateMinCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(7,StateMinCount/User1Allacc);
  			   }
  			   else if(Globalvariable.CurrentState-2==0){  //2-2=0
  	  			   Globalvariable.AccStaterate.set(1,0.5*(StateMinCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(8,StateMinCount/User1Allacc);
  			   }
  			   else{
  			   Globalvariable.AccStaterate.set(Globalvariable.CurrentState-1, 0.5*(StateMinCount/User1Allacc));
  			   Globalvariable.AccStaterate.set(Globalvariable.CurrentState-2, StateMinCount/User1Allacc);
  			   }
  			   
  			   if(Globalvariable.CurrentState+1==8){//7+1=8
  				   Globalvariable.AccStaterate.set(8, 0.5*(StateMinCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(1, StateMinCount/User1Allacc);
  			   }else if (Globalvariable.CurrentState+2==8) {//6+2=8
  	  			   Globalvariable.AccStaterate.set(7, 0.5*(StateMinCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(8, StateMinCount/User1Allacc);   
  	  		   }
  			   else{
  			   Globalvariable.AccStaterate.set((Globalvariable.CurrentState+1)%8, 0.5*(StateMinCount/User1Allacc));
  			   Globalvariable.AccStaterate.set((Globalvariable.CurrentState+2)%8, StateMinCount/User1Allacc);
  			   }
  			   //================================================================= Max
  			   if(Globalvariable.CurrentState+3==8){//5+3=8
  				   Globalvariable.AccStaterate.set(8, 0.5*(StateMinCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(1, StateMaxCount/User1Allacc);
  	  			   Globalvariable.AccStaterate.set(2, 0.5*(StateMaxCount/User1Allacc));
  	  	      }else if (Globalvariable.CurrentState+4==8) {//4+4=8
  	  			   Globalvariable.AccStaterate.set(7, 0.5*(StateMaxCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(8, StateMaxCount/User1Allacc); 
  	  			   Globalvariable.AccStaterate.set(1, 0.5*(StateMaxCount/User1Allacc));
  	  		   }else if (Globalvariable.CurrentState+5==8) {//3+5=8
  	  			   Globalvariable.AccStaterate.set(6, 0.5*(StateMaxCount/User1Allacc));
  	  			   Globalvariable.AccStaterate.set(7, StateMaxCount/User1Allacc);
  	  			   Globalvariable.AccStaterate.set(8, 0.5*(StateMaxCount/User1Allacc));   
  	  		   }
  			   else{
  			   Globalvariable.AccStaterate.set((Globalvariable.CurrentState+3)%8, 0.5*(StateMaxCount/User1Allacc));
  			   Globalvariable.AccStaterate.set((Globalvariable.CurrentState+4)%8, StateMaxCount/User1Allacc);
  			   Globalvariable.AccStaterate.set((Globalvariable.CurrentState+5)%8, 0.5*(StateMaxCount/User1Allacc));
  			   }

  			   
  		   }*/
  		   
		   Globalvariable.if_statrtacc=false;
  		   


  	   }

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

}
