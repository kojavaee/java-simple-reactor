package com.kojavaee.reactor;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 普通nio server 的实现
 * 
 * @author lzh
 */
public class ComNioTest {
    public ComNioTest() {
    }
    
    private static void printSelectKey(SelectionKey key) {
        StringBuilder s = new StringBuilder();  
        
        s.append("Att: " + (key.attachment() == null ? "no" : "yes"))
        .append(", Read: " + key.isReadable())
        .append(", Acpt: " + key.isAcceptable())
        .append(", Cnct: " + key.isConnectable())
        .append(", Wrt: " + key.isWritable())
        .append(", Valid: " + key.isValid())
        .append(", Ops: " + key.interestOps())
        .append(",--------------------------");
        log(s.toString());
    }
    
    private static void log(String str) {
        System.out.println(str);
    }
    
    public void startServer(String host, int port) throws Exception {
        int channels = 0;
        int nKeys = 0;
        
        // 使用Selector
        Selector selector = Selector.open();
        
        // 建立Channel 并绑定到port
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(host,port);
        ssc.socket().bind(address);
        //设定为non-blocking的方式
        ssc.configureBlocking(false);
        
        SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
        printSelectKey(selectionKey);
        
        //轮询
        while (true) {
            log("Starting select");
            
            // Selector 通过select方法通知
            nKeys = selector.select();
            
            if(nKeys > 0) {
                log("Number of keys after select operation: " + nKeys);
                
                // Selector 传回一组SelectionKeys
                // 我们从这些key中的channel()方法中取得我们刚刚注册的channel.
                Set selectedKeysSet = selector.selectedKeys();
                Iterator iterator = selectedKeysSet.iterator();
                while (iterator.hasNext()) {
                    selectionKey = (SelectionKey)iterator.next();
                    printSelectKey(selectionKey);
                    
                    log("Client keys in selector : " + selector.keys().size());
                    iterator.remove();
                    if(selectionKey.isAcceptable()) {
                        //从channel()中取得我们刚刚注册的channel
                        Socket socket = ((ServerSocketChannel)selectionKey.channel()).accept().socket();
                        SocketChannel sChannel = socket.getChannel();
                        
                        sChannel.configureBlocking(false);
                        sChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        
                        log(String.valueOf(++channels));
                    } else {
                        log("channel not acceptable");
                    }
                }
            } else {
                log("Select finished without any keys.");
            }
        }
    }
    
    public static void main(String[] args) {
        ComNioTest nioTest = new ComNioTest();
        try {
            nioTest.startServer("127.0.0.1", 8000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
