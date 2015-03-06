/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhrcek
 */
public class MatchMaker implements Runnable{

    List<Match> matchesInProgress;
    
    public MatchMaker(){
        matchesInProgress = new ArrayList<>();
    }
    
    @Override
    public void run() {
        
    }
    
}
