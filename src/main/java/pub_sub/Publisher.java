package pub_sub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Publisher{

    private static final String EXCHANGE_NAME = "topic_logs";

    private String name;
    private Channel channel;

    public Publisher(String name) {
        this.name = name;
    }

    public void connectPublisher(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
            this.channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String routingKey, String message){
        try {
            this.channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(this.name+" sent '" + routingKey + "':'" + message + "'\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
