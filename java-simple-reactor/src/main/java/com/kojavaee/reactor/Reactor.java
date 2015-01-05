package com.kojavaee.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 一个简单的reactor模型
 * 
 * @author lzh
 */
public class Reactor implements Runnable {
    
    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    
    private void log(String str) {
        System.out.println(str);
    }
    
    public Reactor(String host, int port) throws IOException {
        //创建选择器
        selector = Selector.open();
        //打开服务器套接字通道
        serverSocket = ServerSocketChannel.open();
        
        InetSocketAddress address = new InetSocketAddress(host,port);
        serverSocket.socket().bind(address);
        
        //调整此通道的阻塞模式为异步
        serverSocket.configureBlocking(false);
        
        //向selector注册该channel，用于套接字接受操作的操作
        SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        
        log("-->Start serverSocket.register!");
        
        //利用sk的attache功能绑定Acceptor 如果有消息，就激活Acceptor
        selectionKey.attach(new Acceptor(serverSocket));
        
        log("-->attach(new Acceptor()!)");
    }
    
    public void run() {

    }

}
