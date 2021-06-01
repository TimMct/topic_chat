package pub_sub;

import com.rabbitmq.client.*;
import java.io.IOException;

public class Subscriber{

    private static final String EXCHANGE_NAME = "topic_logs";
    private String name;
    private String topic;
    private Channel channel;
    private String queueName;
    private DeliverCallback callback;
    private String returnedMessage = "";

    public Subscriber(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getReturnedMessage() {
        return returnedMessage;
    }

    public void setReturnedMessage(String returnedMessage) {
        this.returnedMessage = returnedMessage;
    }

    public void connectSubscriber(){
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            this.channel = connection.createChannel();

            this.channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            this.queueName = this.channel.queueDeclare().getQueue();

            this.channel.queueBind(this.queueName, EXCHANGE_NAME, this.topic);

            this.callback = (consumerTag, delivery) -> {
                this.returnedMessage = new String(delivery.getBody(), "UTF-8");
                System.out.println(this.name+" received '" + delivery.getEnvelope().getRoutingKey() + "':'" + this.returnedMessage + "'\n");
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String consumeMessage(){
        try {
            this.channel.basicConsume(this.queueName, true, this.callback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.returnedMessage;
    }

}
