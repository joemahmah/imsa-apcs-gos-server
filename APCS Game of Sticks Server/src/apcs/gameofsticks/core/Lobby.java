/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.util.ArrayList;

/**
 *
 * @author mhrcek
 */
public class Lobby implements Runnable {

    private volatile ArrayList<Client> clientsInLobby;

    public Lobby() {
        clientsInLobby = new ArrayList<>();
    }

    public synchronized void addToLobby(Client c) {
        clientsInLobby.add(c);
    }

    public synchronized Client removeClient(Client client) throws Exception {
        for (Client c : clientsInLobby) {
            if (c.equals(client)) {
                Client temp = c;
                clientsInLobby.remove(c);
                return temp;
            }
        }
        throw new Exception("Client not in lobby!");
    }

    @Override
    public void run() {

    }

}
