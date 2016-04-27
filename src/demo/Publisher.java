package demo;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;



public class Publisher implements SerialPortEventListener {
    public static final java.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    private SerialPort serialPort; // defining serial port object
    private CommPortIdentifier portId = null; // my COM port
    private static final int TIME_OUT = 5000; // time in milliseconds
    
    private static final int BAUD_RATE = 115200; // baud rate to 9600bps
    //private static final int BAUD_RATE = 57600; // baud rate to 9600bps
    public static final String COM_PORT_NAME = "/dev/cu.usbmodem1411";
    //public static final String COM_PORT_NAME = "/dev/tty.usbmodem1411";
    //public static final String COM_PORT_NAME = "/dev/tty.usbserial-AH02UH16";
    private BufferedReader input; // declaring my input buffer
    private OutputStream output; // declaring output stream
    private String name; // user input name string
    Scanner inputName; // user input name
    private static final String TEST_TOPIC = "demo.EXCHANGE1";

    // method initialize
    private void initialize() {
        CommPortIdentifier ports = null; // to browse through each port
                                         // identified
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers(); // store
                                                                        // all
                                                                        // available
                                                                        // ports
        while (portEnum.hasMoreElements()) { // browse through available ports
            ports = (CommPortIdentifier) portEnum.nextElement();
            // following line checks whether there is the port i am looking for
            // and whether it is serial
            if (ports.getPortType() == CommPortIdentifier.PORT_SERIAL
                    && ports.getName().equals(COM_PORT_NAME)) {

                System.out.println("COM port found:" + COM_PORT_NAME);
                portId = ports; // initialize my port
                break;
            }

        }
        // if serial port am looking for is not found
        if (portId == null) {
            System.out.println("COM port not found");
            System.exit(1);
        }

    }

    // end of initialize method

    // connect method

    private void portConnect() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@192.168.0.1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(TEST_TOPIC, "fanout");



        // connect to port
        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT); // down cast the comm port to serial port
                               // give the name of the application
                               // time to wait
            System.out.println("Port open succesful: " + COM_PORT_NAME);

            // set serial port parameters
            serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        } catch (PortInUseException e) {
            System.out.println("Port already in use");
            System.exit(1);
        } catch (NullPointerException e2) {
            System.out.println("COM port maybe disconnected");
        } catch (UnsupportedCommOperationException e3) {
            System.out.println(e3.toString());
        }

        // input and output channels
        try {
            // defining reader and output stream
            input = new BufferedReader(new InputStreamReader(
                    serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            // adding listeners to input and output streams
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnOutputEmpty(true);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        String tpstr = null;
        try {
            int readLen = -1;
            byte[] buffer = new byte[1024];
            int count=0;
            String data_line=null;
           // FileWriter fw = new FileWriter("/Users/mclab/Desktop/wearable/mapping/axis/test.txt");
            while((readLen=serialPort.getInputStream().read(buffer))!=-1){
            	//System.out.println("$$$"+readLen+"$$$");
                String strRead = new String(buffer, 0, readLen);
                String strTime = simpleDateFormat.format(new Date());
                if (strTime.length()==22 || strTime.length()==21 ){
                    int posA = strTime.lastIndexOf(".");
                    String padStr;
                    if (strTime.length()==22){
                        padStr = "0";
                    } else {
                        padStr = "00";
                    }
                    strTime = strTime.substring(0, posA+1) + padStr + strTime.substring(posA+1, strTime.length());
                }
                
                for (int i=0; i<readLen; i++){
                    char chr = (char)buffer[i];
                   	//System.out.print(chr+"!");
                    if(chr!='\u0000'){
                  	data_line= data_line+ chr;
                    }
                    if (chr=='\n'){
                    	
                    	String[] cmds=data_line.split(" ");
                    	for(String cmd:cmds){
                    		if(count > 50 ){   //有index -1 error
                    			
                    		String tempString = cmd.substring(4);
                    		System.out.println(tempString);
                    		
                    		String[] xString = tempString.split("\t"); 
                    		// §ì¨ú x,y,z
                    		//Double.parseDouble(String s)  turn to double
                    		//System.out.println(xString[0]);
                    		//System.out.println(xString[1]);
                    		//System.out.println(xString[2]);
                    		//Double.parseDouble(receive_LeftUpLegX)
                    		System.out.println (xString.length); 
                    		if (xString.length == 4)  // ypr	0.03	-0.05	-0.57
                    		{
                    		
	                    		double yaw = (90+Double.parseDouble(xString[1]));
	                    		double roll = -(Double.parseDouble(xString[2]));
	                    		String tempsend = " { " + "Head" + " : { x: " + xString[3] + ", y: " + yaw + ", z: " + roll + " } }"; 
	                    		
	                    		channel.basicPublish(TEST_TOPIC, "", null, tempsend.getBytes());
	                    		//fw.write( tempsend + "\n");
	                    		//fw.flush();
                    		}
                    		// ¹Bºâ ex:
                    		// ·í±ÛÂà90 
                    		
                    		//System.out.println(cArray[2]);
                    		
                    		//channel.basicPublish(TEST_TOPIC, "", null, " { \"LeftFore\": { x: 20, y: 100, z: 100 } }".getBytes());
                    		//System.out.println("{ \"LeftFore\": { x: 20, y: 100, z: 100 } }");
                    		//channel.basicPublish(TEST_TOPIC, "", null, " { \"RightArm\": { x: 20, y: 100, z: 100 } }".getBytes());
                    		//System.out.println("{ \"RightArm\": { x: 20, y: 100, z: 100 } }");
                    		//channel.basicPublish(TEST_TOPIC, "", null, " { \"RightForeArm\": { x: 20, y: 100, z: 100 } }".getBytes());
                    		//System.out.println("{ \"RightForeArm\": { x: 20, y: 100, z: 100 } }");

                    		
                    		//System.out.println(cmd);
                    		}
                            count=count+1;
                    	}
                    		
                    	data_line=null;
                    } else {
                    }
                }

            }
            
            //fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        channel.close();
        connection.close();


    }

    // end of portConncet method

    // readWrite method

    public void serialEvent(SerialPortEvent evt) {

        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) { // if data
                                                                    // available
                                                                    // on serial
                                                                    // port
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
                inputName = new Scanner(System.in); // get user name
                name = inputName.nextLine();
                name = name + '\n';
                System.out.printf("%s", name);
                output.write(name.getBytes()); // sends the user name
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

    }

    // end of serialEvent method

    // closePort method
    private void close() {
        if (serialPort != null) {
            serialPort.close(); // close serial port
        }
        input = null; // close input and output streams
        output = null;
    }




    @SuppressWarnings({ "null", "resource" })
	public static void main(String[] argv)
            throws java.io.IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException
    {
    	
     Publisher myTest = new Publisher(); // creates an object of the class
//      System.out.println(simpleDateFormat.format(new Date()));
      myTest.initialize();
      myTest.portConnect();
      System.out.println("Started");
      while (1 > 0)
          ; // wait till any activity

    }

}
