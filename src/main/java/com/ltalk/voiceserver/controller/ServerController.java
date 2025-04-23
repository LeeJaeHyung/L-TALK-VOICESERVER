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
            DatagramPacket receivepacket = new DatagramPacket(buffer, buffer.length);
            System.out.println("UDP 음성 채팅 서버 시작...");

            while (true) {
                System.out.println("수신 완료 전송");
                serverSocket.receive(receivepacket);
                ByteBuffer byteBuffer = ByteBuffer.wrap(receivepacket.getData(), 0, receivepacket.getLength());
                long chatRoomId = byteBuffer.getLong();
                long senderId = byteBuffer.getLong();
                byte[] voiceData = new byte[byteBuffer.remaining()];
                byteBuffer.get(voiceData);
                VoiceRoom room = voiceRooms.computeIfAbsent(chatRoomId, VoiceRoom::new);
                Map<Long, VoiceMember> members = room.getMembers();
                // 같은 채팅방에 속한 다른 멤버들에게 전송
                members.forEach((memberId, member) -> {
                    System.out.println("데이터 전송중");
                    if (memberId != senderId) {
                        SocketAddress targetAddress = member.getAddress();
                        DatagramPacket packet = new DatagramPacket(receivepacket.getData(), receivepacket.getLength(), targetAddress);
                        try {
                            serverSocket.send(packet);
                        } catch (IOException e) {
                            System.err.println("전송 실패: " + e.getMessage());
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
