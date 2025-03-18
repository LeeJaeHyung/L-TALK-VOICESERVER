package com.ltalk.voiceserver.controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class ServerController {

    private static final int PORT = 50005;
    private static final int BUFFER_SIZE = 1024;
    private static final Set<ClientInfo> clients = new HashSet<>();

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            System.out.println("UDP 음성 채팅 서버 시작...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                ClientInfo client = new ClientInfo(clientAddress, clientPort);

                // 새로운 클라이언트인지 확인 후 추가
                if (!clients.contains(client)) {
                    clients.add(client);
                    System.out.println("새 클라이언트 연결: " + clientAddress + ":" + clientPort);
                }

                // 모든 클라이언트에게 패킷 전달
                for (ClientInfo c : clients) {
                    if (!c.equals(client)) { // 송신자 제외
                        DatagramPacket forwardPacket = new DatagramPacket(
                                packet.getData(), packet.getLength(),
                                c.getAddress(), c.getPort()
                        );
                        serverSocket.send(forwardPacket);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 클라이언트 정보 저장을 위한 클래스
    private static class ClientInfo {
        private final InetAddress address;
        private final int port;

        public ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ClientInfo that = (ClientInfo) obj;
            return port == that.port && address.equals(that.address);
        }

        @Override
        public int hashCode() {
            return address.hashCode() + port;
        }
    }
}
