/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mhrcek
 * @deprecated Not used. Meant to log matches.
 */
public class Log {
    
    public static synchronized void println(String fileName, String message){
        PrintStream ps = null;
        try {
            File f = new File(fileName);
            
            ps = new PrintStream(f);
            ps.append(message + "\n");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            ps.close();
        }
    }
    
    public static synchronized void print(String fileName, String message){
        PrintStream ps = null;
        try {
            File f = new File(fileName);
            
            ps = new PrintStream(f);
            ps.append(message);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            ps.close();
        }
    }
    
}
