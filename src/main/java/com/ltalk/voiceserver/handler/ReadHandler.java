package com.ltalk.voiceserver.handler;

import com.ltalk.voiceserver.entity.Data;
import com.ltalk.voiceserver.service.DataService;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static com.ltalk.voiceserver.controller.ChatServerController.gson;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel channel;
    private ByteBuffer lengthBuffer = ByteBuffer.allocate(4); //  4바이트 길이 버퍼
    private ByteBuffer dataBuffer;
    private boolean readingLength = true;

    public ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
        receiveResponse();
    }

    private void receiveResponse() {
        if (readingLength) {
            lengthBuffer.clear();
            channel.read(lengthBuffer, lengthBuffer, this);
        } else {
            dataBuffer.clear();
            channel.read(dataBuffer, dataBuffer, this);
        }
    }

    @Override
    public void completed(Integer bytesRead, ByteBuffer buffer) {
        if (bytesRead == -1) {
            System.out.println("서버가 연결을 종료했습니다.");
            return;
        }

        buffer.flip();

        if (readingLength) {
            if (buffer.remaining() >= 4) {
                int messageLength = buffer.getInt(); //  메시지 길이 읽기
                System.out.println("수신할 데이터 크기: " + messageLength);
                dataBuffer = ByteBuffer.allocate(messageLength); //  길이에 맞는 버퍼 생성
                readingLength = false;
                receiveResponse(); //  본문 데이터 읽기
            }
        } else {
            byte[] receivedData = new byte[dataBuffer.remaining()];
            dataBuffer.get(receivedData);
            String receivedJson = new String(receivedData);
            System.out.println("서버 응답 JSON: " + receivedJson);
            Data data = gson.fromJson(receivedJson, Data.class);
            DataService dataService = new DataService(data);
            dataService.interpret();

//            try {
//                ServerResponse responseData = SocketController.gson.fromJson(responseJson, ServerResponse.class);
//                System.out.println("서버 응답 객체: " + responseData);
//                SocketController.getInstance().interpret(responseData);
//            } catch (IOException | NoSuchAlgorithmException e) {
//                throw new RuntimeException(e);
//            }

            readingLength = true; //  다음 응답을 위해 길이부터 다시 읽도록 설정
            receiveResponse();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.err.println("서버 응답 수신 실패: " + exc.getMessage());
    }
}
