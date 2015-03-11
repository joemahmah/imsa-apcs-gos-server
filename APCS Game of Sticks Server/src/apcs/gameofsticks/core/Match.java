/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mhrcek
 */
public class Match extends Thread {

    private volatile static int lastMatchID = 0;
    private volatile int matchID;

    private volatile Client player1;
    private volatile Client player2;
    private volatile Client winningClient;
    private volatile Client activeClient;

    private volatile int totalSticks;
    private volatile int sticksRemaining;
    private volatile int maxSticksToTake;

    private volatile Lobby lobby;

    public Match(Lobby lobby, int totalSticks, int maxSticksToTake, Client player1, Client player2) {
        synchronized (this) {
            this.totalSticks = totalSticks;
            this.sticksRemaining = totalSticks;
            this.maxSticksToTake = maxSticksToTake;
            this.matchID = lastMatchID++;
            this.lobby = lobby;
            this.player1 = player1;
            this.player2 = player2;
            this.start();
        }
    }

    public boolean isMatchStillActive() {
        return sticksRemaining != 0;
    }

    public int getSticksRemaining() {
        return sticksRemaining;
    }

    public int getTotalSticks() {
        return totalSticks;
    }

    public int getMaxSticksToTake() {
        return maxSticksToTake;
    }

    public boolean isWinner(Client c) {
        return c == winningClient;
    }

    @Override
    public void run() {

        int round = 0;

        synchronized (this) {

            while (sticksRemaining != 0) {
                System.out.println("Match " + matchID + ": Round " + ++round);
                System.out.println("Match " + matchID + ": Client " + player1 + " active.");
                activeClient = player1;
                takeSticks(player1);
                halt(10);
                if (sticksRemaining != 0) {
                    System.out.println("Match " + matchID + ": Client " + player2 + " active.");
                    activeClient = player2;
                    takeSticks(player2);
                    if (sticksRemaining == 0) {
                        winner(player1);
                        break;
                    }
                } else {
                    winner(player2);
                    break;
                }
                halt(50);
            }
            
            halt(500);
            isOver();
        }
    }

    private void halt(int millis) {
        try {
            sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void takeSticks(Client c) {
//        synchronized (this) {
        System.out.println("Match " + matchID + ": Waiting on client " + c + ".");
        halt(10);
        c.matchStillActive(true);
        halt(10);
        c.isTurn();
        halt(10);
        int i = c.getSticksTaken();
        System.out.println("Match " + matchID + ": Client " + c + " took " + i + " sticks.");
        sticksRemaining -= i;
        halt(10);
        c.clearSticksTaken();
        halt(10);
    }

    public boolean isClientTurn(Client c) {
        return c == activeClient;
    }

    private void winner(Client c) {
        winningClient = c;
        player1.matchStillActive(false);
        player2.matchStillActive(false);
    }

    public int getMatchID() {
        return matchID;
    }

    public void terminate(Client c) {
        if (c == player1) {
            lobby.addToLobby(player2);
        } else {
            lobby.addToLobby(player1);
        }

        //kill THIS!!!!
    }
    
    public void isOver(){
        lobby.addToLobby(player1);
        lobby.addToLobby(player2);
        
    }

    /*
     int Player1RoundWins = 0;
     int Player2RoundWins = 0;
     int Player1GameWins = 0;
     int Player2GameWins = 0;
     int announcementTiming = 700;
     System.out.println("Round 1, Fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 == 0) {
     if (game(20, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(20, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 1 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     System.out.println("Round 2, Fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 != 0) {
     if (game(20, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(20, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 2 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     if (Player1RoundWins == 2) {
     System.out.println("Player 1 wins!");
     return true;
     }
     if (Player2RoundWins == 2) {
     System.out.println("Player 2 wins!");
     return false;
     }
     double randall = Math.random() * 100;
     int startingNumberOfSticks = 50 + (int) randall;
     System.out.println("Round 3, fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 == 0) {
     if (game(startingNumberOfSticks, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(startingNumberOfSticks, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 3 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     randall = Math.random() * 100;
     startingNumberOfSticks = 50 + (int) randall;
     System.out.println("Round 4, fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 != 0) {
     if (game(startingNumberOfSticks, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(startingNumberOfSticks, 3, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 4 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     if (Player1RoundWins == 3) {
     System.out.println("Player 1 wins!");
     return true;
     }
     if (Player2RoundWins == 3) {
     System.out.println("Player 2 wins!");
     return false;
     }
     randall = Math.random() * 5;
     int maxSticksToTake = 4 + (int) randall;
     System.out.println("Round 5, fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 == 0) {
     if (game(20, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(20, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 5 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     randall = Math.random() * 5;
     maxSticksToTake = 4 + (int) randall;
     System.out.println("Round 6, fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 != 0) {
     if (game(20, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(20, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 6 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     if (Player1RoundWins == 4) {
     System.out.println("Player 1 wins!");
     return true;
     }
     if (Player2RoundWins == 4) {
     System.out.println("Player 2 wins!");
     return false;
     }
     randall = Math.random() * 5;
     maxSticksToTake = 4 + (int) randall;
     randall = Math.random() * 100;
     startingNumberOfSticks = 50 + (int) randall;
     System.out.println("Round 7, fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 == 0) {
     if (game(startingNumberOfSticks, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(startingNumberOfSticks, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 7 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     randall = Math.random() * 5;
     maxSticksToTake = 4 + (int) randall;
     randall = Math.random() * 100;
     startingNumberOfSticks = 50 + (int) randall;
     System.out.println("Round 8, fight!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 != 0) {
     if (game(startingNumberOfSticks, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (game(startingNumberOfSticks, maxSticksToTake, true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming) {
     System.out.println("The winner of Round 8 is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player1RoundWins++;
     System.out.println("Player 1!");
     } else {
     Player1GameWins = 0;
     Player2GameWins = 0;
     Player2RoundWins++;
     System.out.println("Player 2!");
     }
     if (Player1RoundWins == 5) {
     System.out.println("Player 1 wins!");
     return true;
     }
     if (Player2RoundWins == 5) {
     System.out.println("Player 2 wins!");
     return false;
     }
     System.out.println("FINAL ROUND, FIGHT!");
     for (int g = 0; g < 1001; g++) {
     if (g % 2 == 0) {
     if (finalRoundGame(true)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     } else {
     if (finalRoundGame(false)) {
     Player1GameWins++;
     } else {
     Player2GameWins++;
     }
     }
     if (g == announcementTiming / 2) {
     System.out.println("The winner is...");
     }
     }
     if (Player1GameWins > Player2GameWins) {
     System.out.println("PLAYER 1!");
     return true;
     } else {
     System.out.println("PLAYER 2!");
     return false;
     }
     }

     public static boolean game(int numberOfSticks, int maxPlay, boolean player1Starts) { // returns true if player 1 wins
     int sticksOnBoard = numberOfSticks;
     if (player1Starts) {
     sticksOnBoard -= 1; // 1 = number of sticks player 1 chooses to take
     if (sticksOnBoard == 0) {
     return false;
     }
     }
     boolean whoseTurn = false;
     while (sticksOnBoard > 0) {
     if (whoseTurn) {
     sticksOnBoard -= 1; // 1 = number of sticks player 1 chooses to take
     } else {
     sticksOnBoard += 2; // 2 = number of sticks player 2 chooses to take
     }
     whoseTurn = !whoseTurn;
     }
     return whoseTurn;
     }

     public static boolean finalRoundGame(boolean player1Starts) { // returns true if player 1 wins
     boolean whoseTurn = player1Starts;
     double uncleRandy = Math.random() * 190;
     int sticksOnBoard = 10 + (int) uncleRandy;
     while (sticksOnBoard > 0) {
     uncleRandy = Math.random() * 10;
     int maxPlay = 1 + (int) uncleRandy;
     if (whoseTurn) {
     sticksOnBoard -= 1; // the number of sticks player 1 chooses to take (with a maximum of maxPlay)
     } else {
     sticksOnBoard -= 2; // the number of sticks player 2 chooses to take (with a maximum of maxPlay)
     }
     whoseTurn = !whoseTurn;
     }
     return whoseTurn;
     }
     */
}
