package com.ltalk.voiceserver.entity;

import java.net.SocketAddress;

public class VoiceMember {
    private final long memberId;
    private final SocketAddress address;

    public VoiceMember(long memberId, SocketAddress address) {
        this.memberId = memberId;
        this.address = address;
    }

    public long getMemberId() {
        return memberId;
    }

    public SocketAddress getAddress() {
        return address;
    }
}
