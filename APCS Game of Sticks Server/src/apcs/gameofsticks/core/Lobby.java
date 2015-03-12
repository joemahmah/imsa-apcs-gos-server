/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds clients while they are waiting for a match.
 *
 * @author mhrcek
 */
public class Lobby implements Runnable {

    private volatile ArrayList<Client> clientsInLobby;

    /**
     * Constructor for the lobby.
     */
    public Lobby() {
        clientsInLobby = new ArrayList<>();
    }

    /**
     * Add a client to the lobby.
     *
     * @param client The client to be added.
     */
    public synchronized void addToLobby(Client client) {
        clientsInLobby.add(client);
    }

    /**
     * Removes a client from the lobby.
     *
     * @param client Client to be removed.
     * @return The client which was removed from the lobby.
     * @throws Exception Client not in lobby.
     */
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

    /**
     * Determines all clients which are flagged as ready for a match.
     *
     * @return Clients ready for a match.
     */
    public synchronized List<Client> getClientsFlaggedAsReady() {
        List ready = new ArrayList();

        synchronized (this) {
            for (Client client : clientsInLobby) {
                if (client.isReady()) {
                    ready.add(client);
                }
            }
        }
        return ready;
    }

    /**
     * Remove clients from the lobby.
     *
     * @param clients List of clients to be removed.
     */
    public void removeClients(List clients) {
        synchronized (this) {
            clientsInLobby.removeAll(clients);
        }
    }

    /**
     * Add clients to the lobby.
     *
     * @param clients List of clients to be added.
     */
    public void addClients(List clients) {
        synchronized (this) {
            clientsInLobby.addAll(clients);
        }
    }

    /**
     * Main loop.
     */
    @Override
    public void run() {

        while (true) {
            try {
//                System.out.println(clientsInLobby.size());
                for (Client c : clientsInLobby) {
                    if (c.getSocketOut().isClosed()) {
                        clientsInLobby.remove(c);
                    }
                }
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
