package com.kojavaee.reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
    
    private final ServerSocketChannel serverSocket;
    private final Selector selector;
    
    public Acceptor(ServerSocketChannel serverSocket,Selector selector) {
        this.serverSocket = serverSocket;
        this.selector = selector;
    }
    
    public void run() {
        try {
            System.out.println("-->ready for accept ! ");
            SocketChannel channel = serverSocket.accept();
            if(channel != null) {
                new SocketReadHandler(selector,channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
