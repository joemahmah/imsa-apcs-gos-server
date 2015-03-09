/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.client;

import apcs.gameofsticks.core.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

    /**
     *
     * @param host The ip of the server.
     */
    public NetworkingAI(String host) {
        this.host = host;
        this.isTurn = false;
        this.inMatch = false;
    }

    private void connectToServer() {

        try {
            socket = new Socket(host, PORT);

            Thread t = new Thread(this);
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
        return false;
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
                waitForMatch();
                requestMatchData();
                while (requestIsMatchStillActive()) {
                    waitForTurn();
                    requestMatchDataUpdate();
                    playGame();
                }
                requestHasWonMatch();
                gameOver();
            }
        }
    }

    private synchronized void waitForTurn() {

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
        }

    }

    private synchronized void requestMatchData() {

        synchronized (this) {

        }

    }

    private synchronized void requestMatchDataUpdate() {

    }

    private synchronized boolean requestIsMatchStillActive() {
        return false;
    }

    private synchronized void terminate() {

    }

    private class NetworkIOManager extends Thread {

        private volatile Scanner in;
        private volatile PrintWriter out;

        private NetworkIOManager() throws IOException {

            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            this.start();

        }

        public synchronized void listen() {
            synchronized (this) {
                if (in.hasNextLine()) {
                    parseCommand(in.nextLine());
                }
            }
        }

        @Override
        public void run() {
            synchronized (this) {
                while (true) {
                    listen();
                }
            }
        }

        /*
        
        
         private volatile boolean matchStillActive;
         private volatile int maxSticksTaken;
         private volatile int sticksRemaining;
         private volatile boolean hasWonMatch;

         private volatile boolean isTurn;
         private volatile boolean inMatch;
        
         */
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

                if (command.contains("isTurn")) {
                    isTurn = true;
                }

                if (command.contains("isWinner")) {
                    if (command.contains("true")) {
                        hasWonMatch = true;
                    } else {
                        hasWonMatch = false;
                    }
                }
            }
        }

        public synchronized void write(String message) {
            out.println(message);
        }

    }
}
