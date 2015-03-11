/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the server-side representation of a player. Clients run on their own
 * threads.
 *
 * @author mhrcek
 */
public class Client implements Runnable {

    private String id; //User's id will be their IP
    private static volatile int ID = 0;

    private Socket socketOut;
    private Socket socketIn;
    
    private volatile ClientIO clientIO;
    private volatile Match match;

    private volatile boolean isReady;

    private volatile ClientState state;

    private int sticksTaken;

    /**
     * The constructor for the client.
     *
     * @param socketOut
     */
    public Client(Socket socketOut, Socket socketIn) {
        this.socketOut = socketOut;
        this.socketIn = socketIn;
        
        match = null;

        synchronized (this) {
            id = ID++ + "";
        }

        isReady = false;
        state = ClientState.lobby;

        try {
            clientIO = new ClientIO(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sticksTaken = -404;

        flagReady();
    }

    /**
     * Inserts the client into a match.
     *
     * @param match Match to join.
     */
    public synchronized void joinMatch(Match match) {
        this.match = match;

        synchronized (this) {
            clientIO.write("inMatch");
        }
    }

    public Match getMatch() {
        return match;
    }

    /**
     * Gets the client's output socket.
     *
     * @return Client socket.
     */
    public Socket getSocketOut() {
        return socketOut;
    }

    /**
     * Gets the client's input socket.
     *
     * @return Client socket.
     */
    public Socket getSocketIn() {
        return socketIn;
    }
    
    /**
     * Flag the client as ready to play a match.
     */
    public synchronized void flagReady() {
        isReady = true;
    }

    /**
     * Flag the client as not ready to play a match (i.e. sit in the lobby).
     */
    public synchronized void flagUnready() {
        isReady = false;
    }

    /**
     * Checks if the client is ready.
     *
     * @return If the client is ready for a match.
     */
    public synchronized boolean isReady() {
        return isReady;
    }

    public synchronized void matchStillActive(boolean isActive) {
        clientIO.write("matchStillActive " + isActive);
    }

    /**
     * Main loop for the client.
     */
    @Override
    public void run() {
        while (true) {

        }
    }

    public int getSticksTaken() {
        synchronized (this) {
            while (sticksTaken <= 0) {
                System.err.print(""); //This actually fixes the sync issue...
            }
            System.out.println("Client " + this + " took " + sticksTaken + " sticks...");
            return sticksTaken;
        }
    }

    public void clearSticksTaken() {
        sticksTaken = -404;
    }

    public void isTurn() {
//        synchronized (this) {
            if (match != null) {
                clientIO.write("isTurn");
            }
//        }
    }

    public void getWinner() {
        if (match != null && !match.isMatchStillActive()) {
            clientIO.write("isWinner " + (this == match.getWinner()));
        }
    }

    public void getSticksRemaining() {
//        synchronized (clientIO) {
            if (match != null) {
                clientIO.write(match.getSticksRemaining() + "");
            }
//        }
    }

    public void getMaxSticks() {
//        synchronized (this) {
            if (match != null) {
                clientIO.write(match.getMaxSticksToTake() + "");
            }
//        }
    }

    public void takeSticks(int num) {
        sticksTaken = num;
    }

    public void terminate() {
//        synchronized (this) {
            try {
                clientIO.write("terminated");
                if (match != null) {
                    match.terminate(this);
                }
                match = null;
                socketOut.close();
                socketIn.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("Client " + id + " has been disconnected!");
            }
//        }
    }

    public String toString() {
        return "" + id;
    }

}
