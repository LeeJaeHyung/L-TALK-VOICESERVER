package com.ltalk.voiceserver.entity;

import com.ltalk.voiceserver.enums.ProtocolType;
import com.ltalk.voiceserver.request.JoinVoiceChatRequest;
import com.ltalk.voiceserver.response.CreateVoiceMemberResponse;
import lombok.Getter;

@Getter
public class Data {
    ProtocolType protocolType;
    JoinVoiceChatRequest joinVoiceChatRequest;
    CreateVoiceMemberResponse createVoiceMemberResponse;
    boolean status;


    public Data(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public Data(ProtocolType protocolType, CreateVoiceMemberResponse createVoiceMemberResponse, boolean status) {
        this.protocolType = protocolType;
        this.createVoiceMemberResponse = createVoiceMemberResponse;
        this.status = status;
    }
}
