/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Launches the program.
 *
 * @author mhrcek
 */
public class Boot {

    private final int PORT = 80;

    Lobby lobby;
    MatchMaker matchMaker;
    ServerSocket server;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Boot b = new Boot();
        b.run();

    }

    public void run() {
        try {
            server = new ServerSocket(PORT);

            lobby = new Lobby();
            Thread lobbyThread = new Thread(lobby);
            lobbyThread.start();

            matchMaker = new MatchMaker(lobby);
            matchMaker.start();

            System.out.println("Server started!");
            System.out.println("Waiting for clients...");

            while (true) {

                Socket socketOut = server.accept();
                Socket socketIn = server.accept();
                Client client = new Client(socketOut, socketIn);

                synchronized (this) {
                    lobby.addToLobby(client);
                }
                Thread clientThread = new Thread(client);
                clientThread.start();

                System.out.println("Client added to lobby (" + client + ")");

            }
        } catch (IOException e) {
            System.err.println("An error has occured.");
            e.printStackTrace();
        }
    }

}
