/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apcs.gameofsticks.core;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author mhrcek
 */
public class Client implements Runnable {

    private Socket socket;
    private volatile ClientIO clientIO;

    public Client(Socket socket) {
        this.socket = socket;

        try {
            clientIO = new ClientIO(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return socket;
    }

    @Override
    public void run() {

    }

}
