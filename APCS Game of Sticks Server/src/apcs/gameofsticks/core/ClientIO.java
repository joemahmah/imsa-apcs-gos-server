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
    
    public ClientIO(Client client) throws IOException{
        this.client = client;
        
        in = new Scanner(client.getClientSocket().getInputStream());
        out = new PrintWriter(client.getClientSocket().getOutputStream());
        
    }
    
    
    
}
