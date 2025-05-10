package com.ltalk.voiceserver.controller;

import com.ltalk.voiceserver.entity.VoiceMember;
import com.ltalk.voiceserver.entity.VoiceRoom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerController {

    private static final int PORT = 7878;
    private static final int BUFFER_SIZE = 1024;
    public static Map<Long, VoiceRoom> voiceRooms = new ConcurrentHashMap<>();

    public static void startVoiceServer() {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[BUFFER_SIZE];

            System.out.println("UDP 음성 채팅 서버 시작...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);

                ByteBuffer byteBuffer = ByteBuffer.wrap(receivePacket.getData(), 0, receivePacket.getLength());
                int type = byteBuffer.getInt();

                switch (type) {
                    case 0 -> handleVoiceData(byteBuffer, receivePacket, serverSocket);
                    case 1 -> handleJoinRequest(byteBuffer, receivePacket.getSocketAddress());
                    default -> System.out.println("알 수 없는 요청 type: " + type);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleJoinRequest(ByteBuffer byteBuffer, SocketAddress address) {
        long chatRoomId = byteBuffer.getLong();
        long memberId = byteBuffer.getLong();

        VoiceRoom room = voiceRooms.computeIfAbsent(chatRoomId, VoiceRoom::new);
        room.getMembers().put(memberId, new VoiceMember(memberId, address));

        System.out.println("유저 참여 완료: room=" + chatRoomId + ", member=" + memberId);

        // 클라이언트에게 응답 보내기 (성공 응답 1)
        try {
            ByteBuffer responseBuffer = ByteBuffer.allocate(4);
            responseBuffer.putInt(1);
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer.array(), 4, address);
            DatagramSocket socket = new DatagramSocket(); // 응답용 임시 소켓
            socket.send(responsePacket);
            socket.close();
        } catch (IOException e) {
            System.err.println("참여 응답 전송 실패: " + e.getMessage());
        }
    }

    public static void handleVoiceData(ByteBuffer byteBuffer, DatagramPacket receivePacket, DatagramSocket serverSocket) {
        System.out.println("음성 데이터 수신 완료");

        long chatRoomId = byteBuffer.getLong();
        long senderId = byteBuffer.getLong();

        VoiceRoom room = voiceRooms.get(chatRoomId);
        if (room == null) return;

        room.getMembers().forEach((memberId, member) -> {
            if (memberId != senderId) {
                try {
                    DatagramPacket forwardPacket = new DatagramPacket(
                            receivePacket.getData(),
                            receivePacket.getOffset(),
                            receivePacket.getLength(),
                            member.getAddress()
                    );
                    serverSocket.send(forwardPacket);
                    System.out.println("전송 → " + memberId);
                } catch (IOException e) {
                    System.err.println("전송 실패 → " + memberId + ": " + e.getMessage());
                }
            }
        });
    }

}