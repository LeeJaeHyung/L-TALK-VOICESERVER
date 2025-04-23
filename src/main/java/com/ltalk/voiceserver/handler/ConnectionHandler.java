package com.ltalk.voiceserver.handler;

import com.ltalk.voiceserver.controller.ChatServerController;
import com.ltalk.voiceserver.entity.Data;
import com.ltalk.voiceserver.enums.ProtocolType;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static com.ltalk.voiceserver.controller.ChatServerController.sendData;

public class ConnectionHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private final AsynchronousSocketChannel channel;

    public ConnectionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel channel) {
        System.out.println("서버에 연결됨!");
        ChatServerController.isConnected = true;
        sendData(new Data(ProtocolType.CONNECT_VOICE_SERVER));
        new ReadHandler(channel); // 서버 응답 수신 시작
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
        System.err.println("서버 연결 실패: " + exc.getMessage());
    }
}

