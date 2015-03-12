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
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class which handles networking.
 *
 * @see DO NOT EDIT
 * @author mhrcek
 */
public abstract class NetworkingAI extends Thread {

    private static final int PORT = 404;
    private String host;

    private Socket socketOut;
    private Socket socketIn;

    private volatile boolean matchStillActive;
    private volatile int maxSticksTaken;
    private volatile int sticksRemaining;
    private volatile boolean hasWonMatch;

    private volatile boolean isTurn;
    private volatile boolean inMatch;

    private NetworkIOManager networkIOManager;

    private volatile String replyTakeSticks = null;

    /**
     * Constructor. Automatically connects to the server.
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

    /**
     * Handles the actual connection to the server.
     */
    private void connectToServer() {

        try {
            socketIn = new Socket(host, PORT);
            socketOut = new Socket(host, PORT);

            start();
        } catch (IOException e) {
            System.err.println("Unable to connect to server!");
        }

    }

    /**
     * Get the max number of sticks you may take.
     *
     * @return Maximum number of sticks that may be taken.
     */
    public int getMaxSticksTaken() {
        return maxSticksTaken;
    }

    /**
     * Get the number of sticks remaining.
     *
     * @return Number of sticks the server has.
     */
    public int getSticksRemaining() {
        return sticksRemaining;
    }

    /**
     * Take some sticks from the server.
     *
     * @param numSticks Number of sticks to take.
     * @return If the sticks could be taken.
     */
    public boolean takeSticks(int numSticks) {
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
    public boolean wonMatch() {
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
     *
     * @see DO NOT EDIT
     */
    @Override
    public void run() {
//        synchronized (this) {
        while (true) {
            waitForMatch();
            System.out.println("Joined Match");
            requestMatchData();
            while (requestIsMatchStillActive()) {
                waitForTurn();
                requestMatchDataUpdate();
                playGame();
                halt(10);
            }
            halt(50);
            requestHasWonMatch();
            System.out.println("done");
            gameOver();
        }
//        }
    }

    /**
     * Delays the thread for some time.
     *
     * @param millis Time to sleep.
     */
    private void halt(int millis) {
        try {
            sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Waits for the client's turn. The server will tell the client when.
     */
    private void waitForTurn() {

//        System.out.println("Waiting for turn...");
        synchronized (this) {
            while (!isTurn) {

            }

            isTurn = false;
        }

    }

    /**
     * Requests the winner of the match from the server.
     */
    private void requestHasWonMatch() {

//        synchronized (this) {
        inMatch = false;
        networkIOManager.write("request winner");
//        }

    }

    /**
     * Waits until a match is joined..
     */
    private void waitForMatch() {

//        synchronized (this) {
        hasWonMatch = false;

        while (!inMatch) {

        }
        matchStillActive = true;
//        }

    }

    /**
     * Requests initial match data.
     */
    private void requestMatchData() {

        System.out.println("Getting game data...");

//        synchronized (this) {
        networkIOManager.write("request maxStick");
        networkIOManager.write("request currentSticks");
//        }

    }

    /**
     * Requests updated match data.
     */
    private void requestMatchDataUpdate() {

//        System.out.println("Requesting game data...");
//        synchronized (this) {
        networkIOManager.write("request maxStick");
        networkIOManager.write("request currentSticks");
//        }

    }

    /**
     * Gets the state of the match.
     *
     * @return Match state. (true -> active, false -> inactive)
     */
    private boolean requestIsMatchStillActive() {

        return matchStillActive;

    }

    /**
     * Kills the client.
     *
     * @deprecated Not implemented
     */
    private void terminate() {

        try {
            socketOut.close();
        } catch (IOException ex) {
            Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * A class that handles managing network IO.
     */
    private class NetworkIOManager extends Thread {

        private volatile BufferedReader in;
        private volatile PrintWriter out;

        /**
         * Constructor. Automatically starts listening.
         *
         * @throws IOException
         */
        private NetworkIOManager() throws IOException {

            in = new BufferedReader(new InputStreamReader(socketIn.getInputStream()));
            out = new PrintWriter(socketOut.getOutputStream(), true);

            this.start();

        }

        /**
         * Listens for input from the server.
         */
        public void listen() {
            try {
                String input;
                if ((input = in.readLine()) != null) {
//                    System.out.println("Recieved " + input + " from the server.");
                    parseCommand(input);
                }
            } catch (IOException ex) {
                Logger.getLogger(NetworkingAI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        /**
         * Main loop.
         */
        @Override
        public void run() {
            while (true) {
                listen();
            }

        }

        /**
         *
         * Parses commands sent from the server.
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
//            synchronized (this) {

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

            if (command.contains("STICK_")) {
                if (command.contains("GOOD")) {
                    replyTakeSticks = "GOOD";
                } else {
                    replyTakeSticks = "BAD";
                }
            }
//            }
        }

        /**
         * Sends messages to the server.
         *
         * @param message Message to be sent.
         */
        public void write(String message) {
//            System.out.println("Sent " + message + " to the server.");
            out.println(message);
        }

    }
}
