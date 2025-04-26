package com.ltalk.voiceserver.entity;

import lombok.Getter;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class VoiceRoom {
    private final long chatRoomId;
    private final Map<Long, VoiceMember> members = new ConcurrentHashMap<>();

    public VoiceRoom(long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void addMember(long memberId, SocketAddress address) {
        members.put(memberId, new VoiceMember(memberId, address));
    }

    public void removeMember(long memberId) {
        members.remove(memberId);
    }

    public Map<Long, VoiceMember> getMembers() {
        return members;
    }
}
