/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mhrcek
 */
public class MatchMaker implements Runnable {

    private List<Match> matchesInProgress;
    private Lobby lobby;

    private Random rand = new Random();

    public MatchMaker(Lobby lobby) {
        matchesInProgress = new ArrayList<>();
        this.lobby = lobby;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    List<Client> clientsReady = lobby.getClientsFlaggedAsReady();

                    if (clientsReady.size() > 1) {
                        int target1 = rand.nextInt(clientsReady.size());
                        Client client1 = clientsReady.get(target1);
                        clientsReady.remove(client1);
                        lobby.removeClient(client1);

                        int target2 = rand.nextInt(clientsReady.size());
                        Client client2 = clientsReady.get(target2);
                        clientsReady.remove(client2);
                        lobby.removeClient(client2);

                        Match match = new Match(lobby, 20, 3, client1, client2);
                        client1.joinMatch(match);
                        client2.joinMatch(match);
                        
                        System.out.println("Match made (" + match.getMatchID() + "): " + client1 + " v. " + client2);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MatchMaker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

}
