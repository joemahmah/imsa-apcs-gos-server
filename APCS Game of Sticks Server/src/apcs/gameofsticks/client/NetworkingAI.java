/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mhrcek
 */
public abstract class NetworkingAI implements Runnable {

    private static final int PORT = 80;
    private String host;

    private Socket socket;

    private volatile boolean matchStillActive;
    private volatile int maxSticksTaken;
    private volatile int sticksRemaining;
    private volatile boolean hasWonMatch;

    private volatile boolean isTurn;
    private volatile boolean inMatch;

    private NetworkIOManager networkIOManager;
    
    private volatile String replyTakeSticks = null;

    /**
     *
     * @param host The ip of the server.
     */
    public NetworkingAI(String host) {
        try {
            this.host = host;
            this.isTurn = false;
            this.inMatch = false;

            connectToServer();
            networkIOManager = new NetworkIOManager();
        } catch (IOException ex) {
            Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void connectToServer() {

        try {
            socket = new Socket(host, PORT);

            Thread t = new Thread(this);
            t.start();
        } catch (IOException e) {
            System.err.println("Unable to connect to server!");
        }

    }

    /**
     * Get the max number of sticks you may take.
     *
     * @return Maximum number of sticks that may be taken.
     */
    public synchronized int getMaxSticksTaken() {
        return maxSticksTaken;
    }

    /**
     * Get the number of sticks remaining.
     *
     * @return Number of sticks the server has.
     */
    public synchronized int getSticksRemaining() {
        return sticksRemaining;
    }

    /**
     * Take some sticks from the server.
     *
     * @param numSticks Number of sticks to take.
     * @return If the sticks could be taken.
     */
    public synchronized boolean takeSticks(int numSticks) {
        networkIOManager.write("take stick " + numSticks);

        while (replyTakeSticks == null) {

        }

        if (replyTakeSticks.contains("GOOD")) {
            replyTakeSticks = null;
            return true;
        } else {

            replyTakeSticks = null;
            return false;
        }
    }

    /**
     * Determines if the player won the game.
     *
     * @return If the player has won.
     */
    public synchronized boolean wonMatch() {
        return hasWonMatch;
    }

    /**
     * Runs when the game finishes. This is where you can tell your AI if it won
     * the game.
     */
    public abstract void gameOver();

    /**
     * AI game playing logic goes here.
     */
    public abstract void playGame();

    /**
     * Main loop
     */
    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    waitForMatch();
                    requestMatchData();
                    Thread.sleep(100);
                    while (requestIsMatchStillActive()) {
                        waitForTurn();
                        requestMatchDataUpdate();
                        playGame();
                    }
                    requestHasWonMatch();
                    gameOver();
                } catch (InterruptedException ex) {
                    Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private synchronized void waitForTurn() {

        System.out.println("Waiting for turn...");

        synchronized (this) {
            while (!isTurn) {

            }

            isTurn = false;
        }

    }

    private synchronized void requestHasWonMatch() {

        synchronized (this) {
            inMatch = false;
        }

    }

    private synchronized void waitForMatch() {

        synchronized (this) {
            hasWonMatch = false;

            while (!inMatch) {

            }

            System.out.println("Joined Match!");
            matchStillActive = true;
        }

    }

    private synchronized void requestMatchData() {

        synchronized (this) {
            networkIOManager.write("request maxStick");
            networkIOManager.write("request currentSticks");
        }

    }

    private synchronized void requestMatchDataUpdate() {

        synchronized (this) {
            networkIOManager.write("request maxStick");
            networkIOManager.write("request currentSticks");
        }

    }

    private synchronized boolean requestIsMatchStillActive() {

        return matchStillActive;

    }

    private synchronized void terminate() {

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private class NetworkIOManager extends Thread {

        private volatile BufferedReader in;
        private volatile PrintWriter out;

        private NetworkIOManager() throws IOException {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            this.start();

        }

        public void listen() {
            try {
                String input;
                if ((input = in.readLine()) != null) {
                    parseCommand(input);
                }
            } catch (IOException ex) {
                Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void run() {
            while (true) {
                listen();
            }

        }

        /**
         *
         * @see Possible commands:
         * @see request maxStick
         * @see request currentSticks
         * @see request winner
         * @see take stick [number]
         * @see terminate
         *
         * @param command
         */
        private void parseCommand(String command) {
            synchronized (this) {

                if (command.contains("matchStillActive ")) {
                    if (command.contains("true")) {
                        matchStillActive = true;
                    } else {
                        matchStillActive = false;
                    }
                }

                if (command.contains("currentSticks")) {
                    String number = "";
                    for (Character c : command.toCharArray()) {
                        if (Character.isDigit(c)) {
                            number += c;
                        }
                    }

                    int num = Integer.parseInt(number);

                    sticksRemaining = num;
                }

                if (command.contains("maxStick")) {
                    String number = "";
                    for (Character c : command.toCharArray()) {
                        if (Character.isDigit(c)) {
                            number += c;
                        }
                    }

                    int num = Integer.parseInt(number);

                    maxSticksTaken = num;
                }

                if (command.contains("terminate")) {
                    terminate();
                }

                if (command.contains("inMatch")) {
                    inMatch = true;
                }

                if (command.contains("isTurn")) { //Make take boolean?
                    isTurn = true;
                }

                if (command.contains("isWinner")) {
                    if (command.contains("true")) {
                        hasWonMatch = true;
                    } else {
                        hasWonMatch = false;
                    }
                }

                if (command.contains("matchStillActive")) {
                    if (command.contains("true")) {
                        matchStillActive = true;
                    } else {
                        matchStillActive = false;
                    }
                }
            }
        }

        public synchronized void write(String message) {
            out.println(message);
        }

    }
}