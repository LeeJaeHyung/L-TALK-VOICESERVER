package com.ltalk.voiceserver.service;

import com.ltalk.voiceserver.entity.Data;
import com.ltalk.voiceserver.entity.VoiceMember;
import com.ltalk.voiceserver.entity.VoiceRoom;
import com.ltalk.voiceserver.enums.ProtocolType;
import com.ltalk.voiceserver.request.JoinVoiceChatRequest;
import com.ltalk.voiceserver.response.CreateVoiceMemberResponse;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static com.ltalk.voiceserver.controller.ChatServerController.sendData;
import static com.ltalk.voiceserver.controller.ServerController.voiceRooms;

public class DataService {

    Data data;

    public DataService (Data data){
        this.data = data;
    }

    public void interpret(){
        switch (data.getProtocolType()){
            case JOIN_VOICE_CHAT -> joinVoiceChat(data.getJoinVoiceChatRequest());
        }
    }

    private void joinVoiceChat(JoinVoiceChatRequest request) {
        System.out.println("joinVoiceChat() 실행 완료");
        long chatRoomId = request.getChatRoomId();
        long memberId = request.getMemberId();
        String ip = request.getIp();
        int port = request.getPort();


        // SocketAddress 생성
        SocketAddress address = new InetSocketAddress(ip, port);

        // 해당 chatRoomId에 대한 VoiceRoom 가져오기 또는 새로 생성
        VoiceRoom room = voiceRooms.computeIfAbsent(chatRoomId, VoiceRoom::new);

        // 멤버 추가
        room.addMember(memberId, address);

        System.out.println("현재 열려있는 채팅방");
        for(VoiceRoom voiceRoom : voiceRooms.values()){
            System.out.println(voiceRoom.getChatRoomId()+"번 방");
            for(VoiceMember voiceMember : voiceRoom.getMembers().values()){
                System.out.println(voiceMember.getMemberId() + voiceMember.getAddress().toString());
            }
        }

        sendData(new Data(ProtocolType.RESPONSE_CREATE_CHATROOM_MEMBER, new CreateVoiceMemberResponse(request), true));


    }

}
