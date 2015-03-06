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
    public static boolean run(){ // takes 2 AIs
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
        if(Player1GameWins > Player2GameWins){
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player1RoundWins++;
            System.out.println("Player 1!");
        }else{
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player2RoundWins++;
            System.out.println("Player 2!");
        }
        System.out.println("Round 2, Fight!");
        for(int g = 0; g < 1001; g++){
            if(g % 2 != 0){
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
                System.out.println("The winner of Round 2 is...");
            }
        }
        if(Player1GameWins > Player2GameWins){
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player1RoundWins++;
            System.out.println("Player 1!");
        }else{
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player2RoundWins++;
            System.out.println("Player 2!");
        }
        if(Player1RoundWins == 2){
            System.out.println("Player 1 wins!");
            return true;
        }
        if(Player2RoundWins == 2){
            System.out.println("Player 2 wins!");
            return false;
        }
        double randall = Math.random() * 100;
        int startingNumberOfSticks = 50 + (int) randall;
        System.out.println("Round 3, fight!");
        for(int g = 0; g < 1001; g++){
            if(g % 2 == 0){
                if(game(startingNumberOfSticks, 3, true)){
                    Player1GameWins++;
                }else{
                    Player2GameWins++;
                }
            }else{
                if(game(startingNumberOfSticks, 3, true)){
                    Player1GameWins++;
                }else{
                    Player2GameWins++;
                }
            }
            if(g == 700){
                System.out.println("The winner of Round 3 is...");
            }
        }
        if(Player1GameWins > Player2GameWins){
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player1RoundWins++;
            System.out.println("Player 1!");
        }else{
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player2RoundWins++;
            System.out.println("Player 2!");
        }
        randall = Math.random() * 100;
        startingNumberOfSticks = 50 + (int) randall;
        System.out.println("Round 4, fight!");
        for(int g = 0; g < 1001; g++){
            if(g % 2 != 0){
                if(game(startingNumberOfSticks, 3, true)){
                    Player1GameWins++;
                }else{
                    Player2GameWins++;
                }
            }else{
                if(game(startingNumberOfSticks, 3, true)){
                    Player1GameWins++;
                }else{
                    Player2GameWins++;
                }
            }
            if(g == 700){
                System.out.println("The winner of Round 4 is...");
            }
        }
        if(Player1GameWins > Player2GameWins){
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player1RoundWins++;
            System.out.println("Player 1!");
        }else{
            Player1GameWins = 0;
            Player2GameWins = 0;
            Player2RoundWins++;
            System.out.println("Player 2!");
        }
        if(Player1RoundWins == 3){
            System.out.println("Player 1 wins!");
            return true;
        }
        if(Player2RoundWins == 3){
            System.out.println("Player 2 wins!");
            return false;
        }
        
        return false;
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
