package com.ltalk.voiceserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.voiceserver.entity.Data;
import com.ltalk.voiceserver.handler.ConnectionHandler;
import com.ltalk.voiceserver.handler.WriteHandler;
import com.ltalk.voiceserver.service.ChatServerService;
import com.ltalk.voiceserver.util.LocalDateTimeAdapter;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ChatServerController {
    private static AsynchronousChannelGroup group;
    public static boolean isConnected = false;
    public ChatServerService chatServerService = new ChatServerService();
    static {
        try {
            group = AsynchronousChannelGroup.withFixedThreadPool(4, Executors.defaultThreadFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    private static AsynchronousSocketChannel channel;

    static {
        try {
            channel = AsynchronousSocketChannel.open(group);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 7623;
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static ChatServerController chatServerController;

    public ChatServerController(){

    }

    public static ChatServerController getInstance() throws IOException {
        if(chatServerController == null){
            chatServerController = new ChatServerController();
            try{
                chatServerController.connect();
            }catch (IOException | ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }
        return chatServerController;
    }

    private void connect() throws IOException, ExecutionException, InterruptedException {
        channel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), channel, new ConnectionHandler(channel));
    }

    public static void sendData(Data data) {
        String jsonData = gson.toJson(data);
        System.out.println("[서버에게 전송 : "+jsonData+"]");
        ByteBuffer writeBuffer = ByteBuffer.wrap(jsonData.getBytes());
        channel.write(writeBuffer, writeBuffer, new WriteHandler(channel,writeBuffer));
    }

}
