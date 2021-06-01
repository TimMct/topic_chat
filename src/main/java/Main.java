import token_ring_sockets.Peer;

public class Main {

    public static void main(String[] args) {
        int noOfPeers = 3;
        for(int i = 0; i < noOfPeers; i++){
            if(i == 0){
                new Peer(i, noOfPeers, "Alice", 5000+i, 5000+noOfPeers-1-i);
            }
            if(i == 1){
                new Peer(i, noOfPeers, "Bob", 5000+i, 5000+noOfPeers-1-i);
            }
            if(i == 2){
                new Peer(i, noOfPeers, "Jordan", 5000+i, 5000+noOfPeers-1-i);
            }
//            new Peer(i, noOfPeers, "peer"+i, 5000+i, 5000+noOfPeers-1-i);
        }

    }

}
