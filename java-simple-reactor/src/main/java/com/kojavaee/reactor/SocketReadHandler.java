package com.kojavaee.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * 
 * @author lzh
 */
public class SocketReadHandler implements Runnable {
    public static Logger logger = Logger.getLogger(SocketReadHandler.class.getSimpleName());
    
    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    
    public SocketReadHandler(Selector selector, SocketChannel channel) throws IOException {
        this.socketChannel = channel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }
    
    public void run() {
        try {
            ByteBuffer input = ByteBuffer.allocate(1024);
            input.clear();
            int bytesRead = socketChannel.read(input);
            
            System.out.println(bytesRead);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
