/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

/**
 * Various client states.
 *
 * @author mhrcek
 */
public enum ClientState {

    lobby,
    pregame,
    game_turn,
    game_waiting,
    game_needsAnswer,
    postgame;

}
