/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author mhrcek
 */
public class Client implements Runnable {

    private String id; //User's id will be their IP
    
    private Socket socket;
    private volatile ClientIO clientIO;
    private volatile Match match;

    public Client(Socket socket) {
        this.socket = socket;
        
        id = socket.getInetAddress() + "";

        try {
            clientIO = new ClientIO(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void joinMatch(Match match){
        this.match = match;
    }
    
    public Socket getClientSocket() {
        return socket;
    }

    @Override
    public void run() {

    }

}
