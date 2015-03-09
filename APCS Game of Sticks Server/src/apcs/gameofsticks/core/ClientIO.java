/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author mhrcek
 */
public class ClientIO {

    private Client client;
    private volatile Scanner in;
    private volatile PrintWriter out;

    public ClientIO(Client client) throws IOException {
        this.client = client;

        in = new Scanner(client.getClientSocket().getInputStream());
        out = new PrintWriter(client.getClientSocket().getOutputStream());

    }

    public synchronized void listen() {
        synchronized (this) {
            if (in.hasNextLine()) {
                parseCommand(in.nextLine());
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
                
                if(num > 0 && num <= client.getMatch().getMaxSticksToTake()){
                    client.takeSticks(num);
                    write("STICK_GOOD");
                } else{
                    write("STICK_BAD");
                }
            }
        }
    }

    public synchronized void write(String message) {
        out.println(message);
    }

}
