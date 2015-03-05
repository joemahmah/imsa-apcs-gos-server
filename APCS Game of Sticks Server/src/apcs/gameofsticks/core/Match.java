/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

/**
 *
 * @author mhrcek
 */
public class Match {
    public static void run(){ // takes 2 AIs
        int Player1RoundWins = 0;
        int Player2RoundWins = 0;
        int Player1GameWins = 0;
        int Player2GameWins = 0;
        System.out.println("Round 1, Fight!");
        for(int g = 0; g < 1001; g++){
            if(g % 2 == 0){
                if(game(20, 3, true)){
                    Player1GameWins++;
                }else{
                    Player2GameWins++;
                }
            }else{
                if(game(20, 3, true)){
                    Player1GameWins++;
                }else{
                    Player2GameWins++;
                }
            }
            if(g == 700){
                System.out.println("The winner of Round 1 is...");
            }
        }
    }
    
    public static boolean game(int numberOfSticks, int maxPlay, boolean player1Starts){ // returns true if player 1 wins
        int sticksOnBoard = numberOfSticks;
        if(player1Starts){
            sticksOnBoard -= 1; // 1 = number of sticks player 1 chooses to take
            if(sticksOnBoard == 0){
                return false;
            }
        }
        boolean whoseTurn = false;
        while(sticksOnBoard > 0){
            if(whoseTurn){
                sticksOnBoard -= 1; // 1 = number of sticks player 1 chooses to take
            }else{
                sticksOnBoard += 2; // 2 = number of sticks player 2 chooses to take
            }
            whoseTurn = !whoseTurn;
        }
        return whoseTurn;
    }
}
