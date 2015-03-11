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

    private int wins = 0;
    private int losses = 0;
    private int gamesPlayed = 0;

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

//        synchronized (this) {
        clientIO.write("inMatch");
        gamesPlayed++;
//        }
    }

    /**
     * Gets the current match being played. Not used...
     * @deprecated 
     * @return 
     */
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

    /**
     * Tells the client if the match is still active.
     * @param isActive 
     */
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

    /**
     * Waits for the client to send the data. Once data is sent, it is reported to the server.
     * @return The number of sticks taken by the client.
     * @see Error checking handled in the IO handler.
     * @see clearSticksTaken() MUST be run to use this method again.
     */
    public int getSticksTaken() {
        synchronized (this) {
            while (sticksTaken <= 0) {
                System.err.print(""); //This actually fixes the sync issue...
            }
            System.out.println("Client " + this + " took " + sticksTaken + " sticks...");
            return sticksTaken;
        }
    }

    /**
     * Cleans up so the getSticksTaken method can be run again.
     */
    public void clearSticksTaken() {
        sticksTaken = -404;
    }

    /**
     * Tells the client that it is their turn to play.s
     */
    public void isTurn() {
//        synchronized (this) {
        if (match != null) {
            clientIO.write("isTurn");
        }
//        }
    }

    /**
     * Tells the client who the winner is. Also adds statistics to the serverside client.
     */
    public void getWinner() {
        if (match != null && !match.isMatchStillActive()) {
            boolean won = this == match.getWinner();
            clientIO.write("isWinner " + (won));
            
            if(won){
                wins++;
            } else{
                losses++;
            }
        }
    }

    /**
     * Tells the client how many sticks remain.
     */
    public void getSticksRemaining() {
//        synchronized (clientIO) {
        if (match != null) {
            clientIO.write(match.getSticksRemaining() + "");
        }
//        }
    }

    /**
     * Tells the client how many sticks it may take.
     */
    public void getMaxSticks() {
//        synchronized (this) {
        if (match != null) {
            clientIO.write(match.getMaxSticksToTake() + "");
        }
//        }
    }

    /**
     * Sets the number of sticks taken.
     * @param num Number of sticks to take.
     */
    public void takeSticks(int num) {
        sticksTaken = num;
    }

    /**
     * Not used. Original purpose was to kill the client safely.
     * @deprecated 
     */
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

    /**
     * Makes the client object human readable...
     * @return 
     */
    public String toString() {
        return "" + id;
    }

}
