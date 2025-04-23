package com.ltalk.voiceserver.response;

import com.ltalk.voiceserver.request.JoinVoiceChatRequest;

public class CreateVoiceMemberResponse {
    Long memberId;
    String memberName;

    public CreateVoiceMemberResponse(JoinVoiceChatRequest request) {
        memberId = request.getMemberId();
        memberName = request.getMemberName();
    }
}
