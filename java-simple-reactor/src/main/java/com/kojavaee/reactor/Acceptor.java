package com.kojavaee.reactor;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
    
    private final ServerSocketChannel serverSocket;
    
    public Acceptor(ServerSocketChannel serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void run() {
        try {
            System.out.println("-->ready for accept ! ");
            SocketChannel channel = serverSocket.accept();
            if(channel != null) {
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
