/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mhrcek
 */
public class ClientIO extends Thread {

    private Client client;
    private volatile BufferedReader in;
    private volatile PrintWriter out;

    public ClientIO(Client client) throws IOException {
        this.client = client;

        in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
        out = new PrintWriter(client.getClientSocket().getOutputStream(), true);

        this.start();
    }

    public synchronized void listen() {
        synchronized (this) {
            try {
                String input;
                if ((input = in.readLine()) != null) {
                    parseCommand(input);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                listen();
                System.out.println("Listen");
            }
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

            System.out.println("Message " + command + " recieved.");
            
            if (command.contains("request maxStick")) {
                client.getMaxSticks();
            }

            if (command.contains("request currentSticks")) {
                client.getSticksRemaining();
            }

            if (command.contains("request winner")) {
                client.getWinner();
            }

            if (command.contains("terminate")) {
                client.terminate();
            }

            if (command.contains("request turn")) {
                client.isTurn();
            }

            if (command.contains("take stick")) {
                String number = "";
                for (Character c : command.toCharArray()) {
                    if (Character.isDigit(c)) {
                        number += c;
                    }
                }

                int num = Integer.parseInt(number);
                
                if (num > 0 && num <= client.getMatch().getMaxSticksToTake()) {
                    client.takeSticks(num);
                    write("STICK_GOOD");
                } else {
                    write("STICK_BAD");
                }
            }
        }
    }

    public void write(String message) {
        System.out.println("Writing " + message + " to " + client);
        out.println(message);
    }

}
