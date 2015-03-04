/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author mhrcek
 */
public class Boot {

    private final int PORT = 404;
    
    Lobby lobby;
    MatchMaker matchMaker;
    ServerSocket server;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        
        Boot b = new Boot();
        b.run();
        
        while(true){
            
        }
        
    }
    
    public void run() throws IOException{
        server = new ServerSocket(PORT);
        
        lobby = new Lobby();
        Thread lobbyThread = new Thread(lobby);
        lobbyThread.start();
        
        matchMaker = new MatchMaker();
        Thread matchMakerThread = new Thread(matchMaker);
        matchMakerThread.start();
    }
    
}
