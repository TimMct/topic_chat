package token_ring_sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{

    private String ip_address;
    private int portClient;
    private Socket socket;
    private PrintWriter outClient;
    private BufferedReader inClient;


    private int id;
    private int noOfThreads;

    public ClientThread(int id, int noOfThreads, String ip_address, int portClient) {
        this.id = id;
        this.noOfThreads = noOfThreads;
        this.ip_address = ip_address;
        this.portClient = portClient;
    }

    @Override
    public void run() {
        try{
            socket = new Socket(ip_address, portClient);
            outClient = new PrintWriter(socket.getOutputStream(), true);
            inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            int time = 10 * 1000;

            // the token ring algorithm
            while(true){

                if(id == 0){
                    this.sendMessage("token");
                }else{
                    this.sendMessage("no token");
                }
                id = (id + 1) % noOfThreads;
//                System.currentTimeMillis();

                sleep(time);

            }

        }catch(Exception e){
            e.printStackTrace();
        }


    }


//    public void giveToken() throws InterruptedException {
//        try {
//            mutex.acquire();
//            sendMessage("token");
//        } catch (InterruptedException e) {
//            // exception handling code
//        } finally {
//            mutex.release();
//        }
//    }


    public void sendMessage(String message) {
        this.getOutClient().println(message);
    }


    public String askForResponse() throws IOException {
        return this.getInClient().readLine();
    }


    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public int getPortClient() {
        return portClient;
    }

    public void setPortClient(int portClient) {
        this.portClient = portClient;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOutClient() {
        return outClient;
    }

    public void setOutClient(PrintWriter outClient) {
        this.outClient = outClient;
    }

    public BufferedReader getInClient() {
        return inClient;
    }

    public void setInClient(BufferedReader inClient) {
        this.inClient = inClient;
    }
}
