package demo.Experiment;

import org.json.JSONException;

import demo.Globalvariable;

public class Experiment_Operate_Acceleration {
	String UserName="",acc_x,acc_y;
	static double User1Final_x, User1Final_y ,StateMinCount,StateMaxCount;
	static long currenttime;
	static boolean if_first_time_slot=false;
	static int index=0;
	static double ThresholdMin=0.15,ThresholdMax=0.2; //=================有小與大的Threshold

	
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
  		   System.out.println("StateMaxCount+StateMinCount "+StateMaxCount+" "+StateMinCount);
  		   Globalvariable.AccStaterate.add(0,1.0);         //先亂塞值
  		   if(StateMaxCount+StateMinCount==0){             //不動情況下
  			   for (int i = 1; i <= 8; i++) {
  				if(Globalvariable.CurrentState==i){
  				Globalvariable.AccStaterate.add(i, 1.0);  //機率百分之百
  				}else{
				Globalvariable.AccStaterate.add(i, 0.0);     //機率=0
  				}
			}
  		   }else if(StateMaxCount == StateMinCount){      //ACC機率不明情況下
  			   for (int i = 1; i <=8; i++) {
  				   if(Globalvariable.CurrentState==i){
  					   Globalvariable.AccStaterate.add(i, 0.0);    //機率=0.0
  				   }else{
  						Globalvariable.AccStaterate.add(i,0.5);    //機率=0.5
  				   }
  				   }
  		   }
  		   else{                                          //移動中
  			   Globalvariable.AccStaterate.set(Globalvariable.CurrentState, 0.0);//不可能是目前的state
  			   
  			   //================================================================== Min 三個超出index
  			   if(Globalvariable.CurrentState-2==-1 ||Globalvariable.CurrentState-1==0 ){ //1-2=-1 //1-1=0
  	  			   Globalvariable.AccStaterate.add(7,StateMinCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add(8,StateMinCount/(StateMaxCount+StateMinCount));
  			   }
  			   else if(Globalvariable.CurrentState-2==0){  //2-2=0
  	  			   Globalvariable.AccStaterate.add(Globalvariable.CurrentState-1,StateMinCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add(8,StateMinCount/(StateMaxCount+StateMinCount));
  			   }
  			   else{
  			   Globalvariable.AccStaterate.add(Globalvariable.CurrentState-1, StateMinCount/(StateMaxCount+StateMinCount));
  			   Globalvariable.AccStaterate.add(Globalvariable.CurrentState-2, StateMinCount/(StateMaxCount+StateMinCount));
  			   }
  			   
  			   if(Globalvariable.CurrentState+1==8){//7+1=8
  				   Globalvariable.AccStaterate.add(8, StateMinCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+2)%8, StateMinCount/(StateMaxCount+StateMinCount));
  			   }else if (Globalvariable.CurrentState+2==8) {//6+2=8
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+1)%8, StateMinCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add(8, StateMinCount/(StateMaxCount+StateMinCount));   
  	  		   }
  			   else{
  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+1)%8, StateMinCount/(StateMaxCount+StateMinCount));
  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+2)%8, StateMinCount/(StateMaxCount+StateMinCount));
  			   }
  			   //================================================================= Max
  			   if(Globalvariable.CurrentState+3==8){//5+3=8
  				   Globalvariable.AccStaterate.add(8, StateMinCount/(StateMaxCount+StateMaxCount));
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+4)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+5)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  	  	      }else if (Globalvariable.CurrentState+4==8) {//4+4=8
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+3)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add(8, StateMaxCount/(StateMaxCount+StateMinCount)); 
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+5)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  	  		   }else if (Globalvariable.CurrentState+5==8) {//3+5=8
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+3)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+4)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  	  			   Globalvariable.AccStaterate.add(8, StateMaxCount/(StateMaxCount+StateMinCount));   
  	  		   }
  			   else{
  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+3)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+4)%8, StateMaxCount/(StateMaxCount+StateMinCount));
  			   Globalvariable.AccStaterate.add((Globalvariable.CurrentState+5)%8, StateMaxCount/(StateMaxCount+StateMinCount));
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
