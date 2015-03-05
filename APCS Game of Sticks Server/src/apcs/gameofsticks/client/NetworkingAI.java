/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.client;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author mhrcek
 */
public abstract class NetworkingAI implements Runnable{
    
    private static final int PORT = 80;
    private String host;
    
    private Socket socket;
    
    private volatile boolean isInGame;
    
    public NetworkingAI(String host){
        this.host = host;
    }
    
    private void connectToServer(){
        
        try{
            socket = new Socket(host, PORT);
            
            Thread t = new Thread(this);
        } catch(IOException e){
            System.err.println("Unable to connect to server!");
        }
        
    }
    
    public int requestMaxSticksTaken(){
        return -1;
    }
    
    public int requestSticksRemaining(){
        return -1;
    }
    
    public boolean takeSticks(int numSticks){
        return false;
    }
    
    public boolean wonGame(){
        return false;
    }
    
    /**
     * Runs when the game finishes. This is where you can tell your AI if it
     * won the game.
     */
    public abstract void gameOver();
    
    /**
     * AI game playing logic goes here.
     */
    public abstract void playGame();
    
}
