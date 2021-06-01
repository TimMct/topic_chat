package token_ring_sockets;

import java.io.IOException;



// comunicare peer to peer: un process e si client si server
public class Peer {

    private String name;
    private Boolean token;
    private int portServer;
    private int portClient;



    private static final String IP_ADDRESS = "127.0.0.1";



    public ClientThread clientThread; // pls check if you need this, seems not to be used
    public ServerThread serverThread; // pls check if you need this, seems not to be used + it is hidden in startServerSideNewThread

    public Peer(int id, int noOfThreads, String name, int portServer, int portClient) {
        this.name = name;
        this.token = false;
        this.portServer = portServer;
        this.portClient = portClient;
        this.startServerSide(name);
        this.startClientSide(id, noOfThreads, IP_ADDRESS);
    }

    public Boolean hasToken() { // for boolean getters it is suggested to use "is" or "has" methods instead of "get", like "hasToken()"
        return token;
    }

    public void setToken(Boolean token) {
        this.token = token;
    }

    public void startServerSide(String name) {
        serverThread = new ServerThread(name, this.portServer);
        serverThread.start();
    }


    public void startClientSide(int id, int noOfThreads, String ip_address) {
        clientThread = new ClientThread(id, noOfThreads, ip_address, this.portClient);
        clientThread.start();

    }

    public void sendMessage(String message) {
        clientThread.getOutClient().println(message);
    }


    public String askForResponse() throws IOException {
        return clientThread.getInClient().readLine();
    }

}
