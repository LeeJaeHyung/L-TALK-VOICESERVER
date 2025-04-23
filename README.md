# L-TALK-VOICESERVER

## 📌 소개
`L-TALK-VOICESERVER`는 **L-TALK 프로젝트의 음성 채팅 서버**로, 메인 서버(`LTALK-SERVER-v2`)와의 TCP/IP 비동기 통신과 클라이언트와의 UDP 음성 데이터 전송을 담당합니다.  
메인 서버로부터 전달받은 클라이언트 정보 및 채팅방 정보를 기반으로, 음성 채팅방을 생성 및 관리하며, 클라이언트들 간의 음성 데이터를 중계하는 역할을 수행합니다.

---

## 🛠️ 주요 기능

### ✅ 메인 서버와의 비동기 TCP/IP 통신
- `AsynchronousSocketChannel`을 사용한 논블로킹 비동기 TCP 통신
- 메인 서버와의 연결을 통해 클라이언트 접속 정보 (`chatRoomId`, `memberId`, `ip`, `port`) 수신
- 수신된 정보를 바탕으로 음성 채팅방을 구성 및 클라이언트 관리

### ✅ UDP 기반 음성 데이터 전송
- 클라이언트 ↔ VOICESERVER 간 UDP를 통한 음성 패킷 송수신
- 각 클라이언트의 `DatagramSocket` 주소를 기반으로 데이터 전송
- 멀티 클라이언트 환경에서 음성 데이터 전파 (다중 사용자 음성 채팅 지원)

### ✅ 채팅방 및 클라이언트 관리
- `Map<chatRoomId, List<Member>>` 구조로 채팅방 및 참여자 정보 관리
- 클라이언트의 입장, 퇴장 처리 및 채팅방 내 사용자 목록 유지
- 음성 데이터 송수신 시 해당 채팅방의 모든 참여자에게 패킷 전송

---

## ⚡ 주요 통신 흐름

1. 메인 서버 → VOICESERVER: TCP를 통해 `chatRoomId`, `memberId`, `ip`, `port` 전달
2. VOICESERVER: 전달받은 정보로 채팅방 구성 (`VoiceRoom`, `VoiceMember` 생성 및 관리)
3. 클라이언트 ↔ VOICESERVER: UDP를 이용한 음성 데이터 송수신
4. VOICESERVER: 수신된 음성 데이터를 같은 채팅방의 다른 클라이언트들에게 전파

---

## 🚀 사용 기술

| 기술 스택                  | 설명                                         |
| ------------------------- | -------------------------------------------- |
| Java (NIO)                | 비동기 TCP 통신 (`AsynchronousSocketChannel`) |
| UDP (DatagramSocket)      | 클라이언트와 음성 데이터 송수신             |
| Gradle                    | 빌드 및 의존성 관리                         |

---

## 📝 주요 클래스 설명

### ▶️ `MainVoiceServer.java`
- 음성 서버 실행 및 초기화
- `ServerController` 실행

### ▶️ `ServerController.java`
- 메인 서버와의 TCP 비동기 통신 처리
- 클라이언트 UDP 패킷 송수신 및 클라이언트-채팅방 관리

### ▶️ `VoiceRoom.java`
- 채팅방 ID, 참여자 목록 (`List<VoiceMember>`) 관리

### ▶️ `VoiceMember.java`
- `memberId`, `chatRoomId`, 클라이언트의 UDP `SocketAddress` 정보 보유

---

## 📌 개발 중 참고한 이슈들

- TCP로 클라이언트 입장 정보를 수신 후, UDP를 통한 음성 데이터 송수신 처리
- 패킷 손실, 지연 문제 대응을 위한 기본 설계
- 멀티스레딩 환경에서의 동시 사용자 관리

---

## 💬 향후 개발 계획

- UDP 패킷 지연 보정 및 패킷 손실 대응 로직 추가
- 연결된 클라이언트의 비정상 종료 감지 및 정리 로직 개선

---

## 📧 Contact

- 개발자: JaeHyung Lee  
- 프로젝트: L-TALK

