package com.ltalk.voiceserver.handler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel socketChannel;
    private ByteBuffer buffer;

    public WriteHandler(AsynchronousSocketChannel socketChannel, ByteBuffer buffer) {
        this.socketChannel = socketChannel;
        this.buffer = buffer;
    }

    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            socketChannel.write(buffer, buffer, this); // 남은 데이터가 있으면 계속 전송
        } else {
            System.out.println("[클라이언트] 요청 전송 완료");
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.err.println("[클라이언트] 데이터 전송 실패: " + exc.getMessage());
    }
}
