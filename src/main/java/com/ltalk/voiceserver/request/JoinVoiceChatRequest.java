package com.ltalk.voiceserver.request;

import lombok.Getter;

@Getter
public class JoinVoiceChatRequest {
    Long chatRoomId;
    Long memberId;
    String memberName;
    String ip;
    int port;

    public JoinVoiceChatRequest(Long chatRoomId, Long memberId, String memberName, String ip , int port ) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.ip = ip;
        this.port = port;
    }

    public JoinVoiceChatRequest(Long chatRoomId){
        this.chatRoomId = chatRoomId;
    }
}
