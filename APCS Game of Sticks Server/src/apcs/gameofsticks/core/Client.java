/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.net.Socket;

/**
 * This is the server-side representation of a player. Clients run on their own
 * threads.
 * 
 * @author mhrcek
 */
public class Client implements Runnable {

    private String id; //User's id will be their IP
    
    private Socket socket;
    private volatile ClientIO clientIO;
    private volatile Match match;
    
    private volatile boolean isReady;
    
    private volatile ClientState state;

    /**
     * The constructor for the client.
     * @param socket 
     */
    public Client(Socket socket) {
        this.socket = socket;
        
        id = socket.getInetAddress() + "";

        isReady = false;
        state = ClientState.lobby;
        
        try {
            clientIO = new ClientIO(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the client into a match.
     * @param match Match to join.
     */
    private synchronized void joinMatch(Match match){
        this.match = match;
    }
    
    /**
     * Gets the client's socket.
     * @return Client socket.
     */
    public Socket getClientSocket() {
        return socket;
    }

    /**
     * Flag the client as ready to play a match.
     */
    public synchronized void flagReady(){
        isReady = true;
    }
    
    /**
     * Flag the client as not ready to play a match (i.e. sit in the lobby).
     */
    public synchronized void flagUnready(){
        isReady = false;
    }
    
    /**
     * Checks if the client is ready.
     * @return If the client is ready for a match.
     */
    public synchronized boolean isReady(){
        return isReady;
    }
    
    /**
     * Main loop for the client.
     */
    @Override
    public void run() {
        while(true){
            
        }
    }

}
