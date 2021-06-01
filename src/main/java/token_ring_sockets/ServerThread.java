package token_ring_sockets;

import view.GUI;
import pub_sub.Publisher;
import pub_sub.Subscriber;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{

    private String name;
    private int portServer;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter outServer;
    private BufferedReader inServer;


    private Publisher publisher;

    private List<Subscriber> subscribers;


//    private Subscriber subscriber1;
//    private Subscriber subscriber2;
//    private Subscriber subscriber3;


    private List<String> listOfTopics;

    private Boolean token;


    public Boolean hasToken() {
        return token;
    }

    public void setToken(Boolean token) {
        this.token = token;
    }

    public String getActualName() {
        return name;
    }


    public void setActualName(String name) {
        this.name = name;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

//    public Subscriber getSubscriber() {
//        return subscriber;
//    }
//
//    public void setSubscriber(Subscriber subscriber) {
//        this.subscriber = subscriber;
//    }


    public List<String> getListOfTopics() {
        return listOfTopics;
    }

    public void setListOfTopics(List<String> listOfTopics) {
        this.listOfTopics = listOfTopics;
    }

    public ServerThread(String name, int portServer) {
        this.name = name;
        this.portServer = portServer;
        this.subscribers = new ArrayList<Subscriber>();
    }




    @Override
    public void run() {

        String tokenMessage = "";

        try {
            serverSocket = new ServerSocket(portServer);
            clientSocket = serverSocket.accept();
            outServer = new PrintWriter(clientSocket.getOutputStream(), true);
            inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            GUI GUI = new GUI(this);
            GUI.configure();
            GUI.produce();

            this.name = GUI.getName();


            // create a publisher and a subscriber inside this thread
            this.publisher = new Publisher(this.name);


            this.subscribers.add(new Subscriber(this.name, "Cooking"));
            this.subscribers.add(new Subscriber(this.name, "Sport"));
            this.subscribers.add(new Subscriber(this.name, "Harry Potter"));


            for(Subscriber sub : subscribers){
                sub.connectSubscriber();
            }
            publisher.connectPublisher();


            while((tokenMessage = inServer.readLine()) != null){

                if(tokenMessage.equals("token")){
                    this.setToken(true);
                    System.out.println(this.name+": I can subscribe");

                }else{
                    this.setToken(false);
                }

//                for(String str : this.getListOfTopics()){
//                    System.out.println(str);
//                }


                String pubSubMessage = "";

                for(Subscriber sub : subscribers){
                    if(this.getListOfTopics().contains(sub.getTopic())) {
                        pubSubMessage = sub.consumeMessage();
                        sub.setReturnedMessage("");
                        if(!pubSubMessage.equals("") && pubSubMessage.contains(this.name.toUpperCase())){
                            GUI.addMessage2GUI(sub.getTopic(), SwingConstants.RIGHT, pubSubMessage);
                        }else{
                            GUI.addMessage2GUI(sub.getTopic(), SwingConstants.LEFT, pubSubMessage);
                        }
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public int getPortServer() {
        return portServer;
    }

    public void setPortServer(int portServer) {
        this.portServer = portServer;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public PrintWriter getOutServer() {
        return outServer;
    }

    public void setOutServer(PrintWriter outServer) {
        this.outServer = outServer;
    }

    public BufferedReader getInServer() {
        return inServer;
    }

    public void setInServer(BufferedReader inServer) {
        this.inServer = inServer;
    }
}
