package demo;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;




public class Subscriber_data {
	static Channel channel;
	static Channel channel_publish;
    private static final String TOPIC_pose = "wise.temp.mocap.pose";  
    private static final String TOPIC_location = "wise.temp.position";    

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException
    {
    	new Thread(){
    		public void run(){
    	try {
			
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@127.0.0.1");    //如果是要傳amqp://userName:password; localhost=127.0.0.1
        Connection connection = factory.newConnection();
         channel = connection.createChannel();
        
        channel.exchangeDeclare(TOPIC_pose, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, TOPIC_pose, "");

        System.out.println(" [*] Waiting for pose messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        
        
        
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String line = new String(delivery.getBody());
            Rotation rotation = new Rotation();
            rotation.Pose_processing(line);
            System.out.println("Linepose: "+line);
        }
    		
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
    		}
		
	}.start();
	
	
	
	new Thread(){
		public void run(){
	try {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri("amqp://admin:admin@127.0.0.1");    //如果是要傳amqp://userName:password; localhost=127.0.0.1
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
		
		channel.exchangeDeclare(TOPIC_location, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, TOPIC_location, "");
		
		System.out.println(" [*] Waiting for location messages. To exit press CTRL+C");
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		
		while (true) {
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        String line = new String(delivery.getBody());
        System.out.println("Linelocation: "+line);
        }
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		}
		
	}.start();
    	
    	
    }
	

}
