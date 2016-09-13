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
	static double ThresholdMin=0.15,ThresholdMax=0.2; //=================有小與大的Threshold
	static int User1Allacc;

	
    public void ACC_processing()
            throws java.io.IOException, JSONException
    {
    	//User1A1.14!0.15c   我把負的轉正的，因為傳送上比較方便
    	//System.out.println("Accdata "+Accdata);


    	try {
    	currenttime=System.currentTimeMillis();
  	   if(if_first_time_slot!=true && (currenttime - Globalvariable.starttime >=1)){ //大於一毫秒
 		  if_first_time_slot=true;
  	   }
  	   else if(Globalvariable.if_statrtacc==true &&if_first_time_slot==true &&  (currenttime - Globalvariable.endslotime)>=Globalvariable.time_slot){
  	    	User1Allacc=Globalvariable.User1Totalacc_x.size()+Globalvariable.User1Totalacc_y.size();
  		   for (int i = 0; i < Globalvariable.User1Totalacc_x.size(); i++) {//==============x,y長度都一樣
  	  		   if((ThresholdMin-0.1) <=Globalvariable.User1Totalacc_x.get(i) &&(ThresholdMin+0.025) >=Globalvariable.User1Totalacc_x.get(i)){ //0.05<=data<=0.175
  	  			   StateMinCount=StateMinCount+1;
  	  		   }
  	  		   if((ThresholdMin-0.1) <=Globalvariable.User1Totalacc_y.get(i) &&(ThresholdMin+0.025) >=Globalvariable.User1Totalacc_y.get(i)){ //0.05<=data<=0.175
  	  			   StateMinCount=StateMinCount+1;
  	  		   }
  	  		   if((ThresholdMax-0.025) <Globalvariable.User1Totalacc_x.get(i)){//0.175<data
  	  			   StateMaxCount=StateMaxCount+1;
  	  		   }
  	  		   if((ThresholdMax-0.025) <Globalvariable.User1Totalacc_y.get(i)){//0.175<data
  	  			   StateMaxCount=StateMaxCount+1;
  	  		   }
		}
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
  		   if(StateMaxCount+StateMinCount==0){             //不動情況下
  			   for (int i = 1; i <= 8; i++) {
  				if(Globalvariable.CurrentState==i){
  				Globalvariable.AccStaterate.set(i, 1.0);  //機率百分之百
  				}else{
				Globalvariable.AccStaterate.set(i, 0.0);     //機率=0
  				}
			}
  		   }else if(StateMaxCount == StateMinCount){      //ACC機率不明情況下
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

  			   
  		   }
  		   StateMinCount=0.0;StateMaxCount=0.0;
		   Globalvariable.if_statrtacc=false;
  		   


  	   }

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

}
