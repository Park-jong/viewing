// 프론트 단독 실행용 openvidu-browser 대체 모듈.
// 실제 미디어 서버 없이 화상회의 화면이 동작하도록 필요한 API만 흉내낸다.

export const LocalRecorderState = {
  READY: "READY",
  RECORDING: "RECORDING",
  PAUSED: "PAUSED",
  FINISHED: "FINISHED",
};

// 데모용 원격 참가자 (회의 화면에서 다른 멤버가 보이도록 한다)
const REMOTE_PARTICIPANTS = [
  {
    connectionId: "mock-remote-1",
    clientData: {
      memberId: 2,
      member_id: 2,
      nickname: "면접마스터",
      backgroundColor: "#ff6767",
      backgroundImg: "cow",
    },
    audioActive: true,
  },
  {
    connectionId: "mock-remote-2",
    clientData: {
      memberId: 3,
      member_id: 3,
      nickname: "취준생민수",
      backgroundColor: "#a5ffa5",
      backgroundImg: "penguin",
    },
    audioActive: false,
  },
];

// OpenVidu 인스턴스가 렌더마다 새로 만들어지므로 현재 세션은 모듈 수준으로 기억한다.
let currentSession = null;

async function tryGetMedia(constraints) {
  try {
    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
      return null;
    }
    return await navigator.mediaDevices.getUserMedia(constraints);
  } catch (e) {
    return null;
  }
}

class StubStreamManager {
  constructor(stream, mediaStream = null) {
    this.stream = stream;
    this.mediaStream = mediaStream;
  }

  addVideoElement(element) {
    if (element && this.mediaStream && element.srcObject !== this.mediaStream) {
      element.srcObject = this.mediaStream;
    }
    return element;
  }
}

class StubSession {
  constructor() {
    this.handlers = {};
    this.connection = { connectionId: "mock-local", data: "" };
    this.timers = [];
  }

  on(type, handler) {
    (this.handlers[type] = this.handlers[type] || []).push(handler);
    return this;
  }

  off(type, handler) {
    this.handlers[type] = (this.handlers[type] || []).filter(
      (h) => h !== handler
    );
    return this;
  }

  emit(type, event) {
    (this.handlers[type] || []).forEach((handler) => {
      try {
        handler(event);
      } catch (e) {
        console.warn("[mock openvidu] 이벤트 처리 오류", e);
      }
    });
  }

  async connect(token, metadata) {
    this.connection.data =
      typeof metadata === "string" ? metadata : JSON.stringify(metadata || {});
    REMOTE_PARTICIPANTS.forEach((p, i) => {
      this.timers.push(
        setTimeout(() => {
          const data = JSON.stringify({ clientData: p.clientData });
          const stream = {
            streamId: `${p.connectionId}-stream`,
            connection: { connectionId: p.connectionId, data },
            videoActive: false,
            audioActive: p.audioActive,
          };
          this.emit("streamCreated", { stream });
          this.emit("signal:join", { from: { data }, type: "join" });
          if (i === 0) {
            this.emit("signal:chat", {
              from: { data },
              data: "안녕하세요! 곧 시작하겠습니다.",
              type: "chat",
            });
          }
        }, 400 * (i + 1))
      );
    });
  }

  disconnect() {
    this.timers.forEach(clearTimeout);
    this.timers = [];
    this.handlers = {};
  }

  publish() {
    return Promise.resolve();
  }

  unpublish() {
    return Promise.resolve();
  }

  subscribe(stream) {
    return new StubStreamManager(stream);
  }

  unsubscribe() {}

  // 본인에게 보내거나 전체 전송인 경우에만 로컬 리스너로 되돌려준다.
  signal({ data, to = [], type } = {}) {
    const toSelf =
      to.length === 0 ||
      to.some((c) => c && c.connectionId === this.connection.connectionId);
    if (toSelf && type) {
      setTimeout(
        () =>
          this.emit(`signal:${type}`, {
            from: { data: this.connection.data },
            data,
            type,
          }),
        0
      );
    }
    return Promise.resolve();
  }
}

class StubLocalRecorder {
  constructor() {
    this.state = LocalRecorderState.READY;
  }

  record() {
    this.state = LocalRecorderState.RECORDING;
  }

  pause() {
    this.state = LocalRecorderState.PAUSED;
  }

  resume() {
    this.state = LocalRecorderState.RECORDING;
  }

  stop() {
    this.state = LocalRecorderState.FINISHED;
    return Promise.resolve();
  }

  clean() {
    this.state = LocalRecorderState.READY;
  }

  getBlob() {
    return new Blob([], { type: "video/webm" });
  }
}

export class OpenVidu {
  initSession() {
    currentSession = new StubSession();
    return currentSession;
  }

  async initPublisherAsync(target, properties = {}) {
    const wantVideo = properties.publishVideo !== false;
    const mediaStream = wantVideo ? await tryGetMedia({ video: true }) : null;
    const stream = {
      streamId: "mock-local-stream",
      connection: currentSession
        ? currentSession.connection
        : { connectionId: "mock-local", data: "" },
      videoActive: !!mediaStream,
      audioActive: properties.publishAudio !== false,
    };
    return new StubStreamManager(stream, mediaStream);
  }

  initLocalRecorder() {
    return new StubLocalRecorder();
  }

  async getDevices() {
    try {
      if (!navigator.mediaDevices || !navigator.mediaDevices.enumerateDevices) {
        return [];
      }
      const devices = await navigator.mediaDevices.enumerateDevices();
      return devices.filter(
        (d) => d.kind === "videoinput" || d.kind === "audioinput"
      );
    } catch (e) {
      return [];
    }
  }
}

// 회의 입장 화면을 거치지 않고 바로 들어왔을 때 쓰는 기본 기기 정보 (카메라/마이크 없음)
export const DEFAULT_DEVICE_INFO = {
  video: { deviceId: "noDevice", kind: "videoinput", label: "카메라끄기" },
  audio: { deviceId: "noDevice", kind: "audioinput", label: "마이크끄기" },
};
