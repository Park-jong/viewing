import axios from "axios";
import { LocalRecorderState, OpenVidu } from "openvidu-browser";
import { styled } from "styled-components";
import MeetingFooter from "../components/Meeting/Organisms/MeetingFooter";
import MeetingMain from "../components/Meeting/Organisms/MeetingMain";
import MeetingSideBar from "../components/Meeting/Organisms/MeetingSideBar";
import BeforeExitModal from "../components/Meeting/BeforeExitModal";
import { useEffect, useState } from "react";

const Container = styled.div`
  width: 100%;
  height: 100%;
  overflow: hidden;
  /* 웹킷 기반 브라우저 스크롤바 숨기기 */
  &::-webkit-scrollbar {
    display: none;
  }
  /* IE 및 Edge 스크롤바 숨기기 */
  -ms-overflow-style: none;
  /* Firefox 스크롤바 숨기기 */
  scrollbar-width: none;
`;

const ModalContainer = styled.div`
  position: absolute;
  left: 50%;
  top: 50%;
  border: 1px solid black;
  transform: translate(-50%, -50%);
  z-index: 999;
`;

const HeaderContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 99.9%;
  height: 5%;
`;

const TitleContainer = styled.div`
  border: 1px solid black;
  margin-left: auto;
`;

const MainContainer = styled.div`
  position: absolute;
  display: flex;
  flex-direction: row;
  width: 99.9%;
  height: 90%;
  top: 5%;
  bottom: 5%;
`;

const MeetingMainContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  right: ${(props) => `${props.right}%`};
  width: 100%;
  height: 100%;
`;

const MeetingSideContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  left: 80%;
  width: 19.9%;
  height: 100%;
`;

const ButtonContainer = styled.div`
  border: 1px solid black;
  margin-left: auto;
`;

const LayoutButton = styled.button`
  border: 1px solid black;
  margin: 1px;
`;

const FooterContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  align-items: center;
  top: 95%;
  width: 99.9%;
  height: 4.8%;
`;

export default function MeetingPk() {
  // 유저 데이터 - 나중에 redux를 통해 가져와야 함
  const userData = {
    id: String(Math.ceil(Math.random() * 1000)),
    nickname: "nickname" + String(Math.ceil(Math.random() * 1000)),
    background: "red",
    character: "cow",
    image: "/test.jpg",
  };

  // 스터디 아이디  - 이거 pathvariable로 가져오거나 따로 불러오거나
  //                  pathvariable로 가져올거면 설정 화면에서 url에 스터디 아이디를 넣어줘야 함
  const studyId = "1";

  // .env 파일에서 불러오는 Spring API 호출 주소
  const APPLICATION_SERVER_URL = process.env.REACT_APP_SERVER_URL;
  // openvidu 객체 생성
  const OV = new OpenVidu();

  // 설정창에서 받아온 사용기기 정보
  let deviceInfo = undefined;
  let currentVideoDevice = undefined;
  let currentAudioDevice = undefined;
  useEffect(() => {
    deviceInfo = JSON.parse(localStorage.getItem("deviceInfo"));
    currentVideoDevice = deviceInfo ? deviceInfo.video : undefined;
    currentAudioDevice = deviceInfo ? deviceInfo.audio : undefined;
  });

  // 백엔드 통신을 통한 세션/토큰 발급
  const createSession = async () => {
    let mURL = `${APPLICATION_SERVER_URL}studies/${studyId}/conference`;
    await axios({
      url: mURL,
      method: "POST",
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json",
      },
      data: {
        user_id: userData.userId,
        customSessionId: studyId,
      },
    })
      .then((res) => {
        localStorage.setItem("sessionId", res.data);
      })
      .catch(() => {
        console.log("세션 생성 실패");
        alert("오류가 발생했습니다. 다시 시도해보세요.");
        window.close();
      });
  };

  const createToken = async () => {
    let mURL = `${APPLICATION_SERVER_URL}studies/${studyId}/conferences/members`;
    await axios({
      url: mURL,
      method: "POST",
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json",
      },
      data: {
        user_id: userData.userId,
        customSessionId: studyId,
      },
    })
      .then((res) => {
        localStorage.setItem("openviduToken", res.data);
      })
      .catch(() => {
        console.log("토큰 발급 실패");
        alert("오류가 발생했습니다. 다시 시도해보세요.");
        window.close();
      });
  };

  const getToken = async () => {
    await createSession();
    await createToken();
  };

  //// 백엔드 통신을 통한 세션/토큰 발급

  // WebRTC 관련 객체
  const [session, setSession] = useState();
  const [subscribers, setSubscribers] = useState({
    subs: [],
  });
  const [publisher, setPublisher] = useState();

  // 본인의 세션 입장/퇴장 관리
  const joinSession = () => {
    const newSession = OV.initSession();
    setSession(newSession);
  };

  const leaveSession = async () => {
    if (!!!session) return;
    subscribers.subs.forEach((d) => session.unsubscribe(d));
    setSubscribers((prev) => ({
      ...prev,
      subs: [],
    }));
    setPublisher(undefined);
    session.disconnect();
    setSession(undefined);
    localStorage.removeItem("sessionId");
    localStorage.removeItem("openviduToken");
    localStorage.removeItem("deviceInfo");
    window.close();
  };

  useEffect(() => {
    joinSession();
  }, []);
  ///// 본인의 세션 입장/퇴장 관리

  // 세션 설정
  useEffect(() => {
    if (!!!session) return;

    // 타인의 입장/퇴장 관리

    // 입장리스너
    session.on("streamCreated", (event) => {
      let newSubscriber = session.subscribe(event.stream, undefined);
      setSubscribers((prev) => ({ subs: prev.subs.concat(newSubscriber) }));
    });

    // 퇴장리스너
    session.on("streamDestroyed", (event) => {
      setSubscribers((prev) => ({
        subs: prev.subs.filter(
          (sub) =>
            sub.stream.connection.connectionId !==
            event.stream.connection.connectionId
        ),
      }));
    });

    // 예외리스너
    session.on("exception", (exception) => {
      console.warn(exception);
    });

    // 채팅리스너
    session.on("signal:chat", (event) => {
      let newChatList = chat.log;
      let newChat = {
        sender: event.from.data,
        message: event.data,
      };
      newChatList.unshift(newChat);
      setChat((prev) => ({ ...prev, log: newChatList }));
    });

    // 피드백리스너
    session.on("signal:feedback", (event) => {
      let newFeedback = JSON.parse(event.data);
      setFeedback((prev) => ({
        feedbacks: prev.feedbacks
          .filter((feed) => feed.writer !== newFeedback.writer)
          .concat(newFeedback),
      }));
    });

    session.on("signal:join", (event) => {
      let newChatList = chat.log;
      let newJoin = {
        sender: "notice",
        message: `${
          JSON.parse(event.from.data).clientData.nickname
        } 님이 입장하셨습니다.`,
      };
      newChatList.unshift(newJoin);
      setChat((prev) => ({ ...prev, log: newChatList }));
    });

    //// 타인의 입장/퇴장 관리

    // 토큰 생성 및 스트림 등록
    getToken().then(() => {
      session
        .connect(localStorage.getItem("openviduToken"), {
          clientData: userData,
        })
        .then(async () => {
          let newPublisher = await OV.initPublisherAsync(undefined, {
            audioSource:
              currentAudioDevice.deviceId === "noDevice"
                ? undefined
                : currentAudioDevice.deviceId,
            videoSource:
              currentVideoDevice.deviceId === "noDevice"
                ? undefined
                : currentVideoDevice.deviceId,
            publishAudio: !(currentAudioDevice.deviceId === "noDevice"),
            publishVideo: !(currentVideoDevice.deviceId === "noDevice"),
            resolution: "1280x720",
            frameRate: 30,
            insertMode: "APPEND",
            mirror: false,
          });
          session.publish(newPublisher);
          setPublisher(newPublisher);
          session
            .signal({
              to: [],
              type: "join",
            })
            .catch((e) => {
              console.log(e);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    });
  }, [session]);
  //// 토큰 생성 및 스트림 등록

  //// 세션 설정

  const beforeUnloadListener = (e) => {
    // eslint-disable-next-line no-restricted-globals
    if (hideModdal) {
      alert("지금 나가시면 더 이상 피드백 확인이 불가합니다.");
    }
    e.preventDefault();
    return (e.returnValue = "나갈꺼야?");
  };

  useEffect(() => {
    window.addEventListener("beforeunload", beforeUnloadListener);
  }, []);

  useEffect(() => {
    findDevice();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 사용가능한 디바이스 정보
  const [devices, setDevices] = useState({
    video: [],
    audio: [],
  });

  // 사용가능한 디바이스 찾기
  const findDevice = async () => {
    await OV.getDevices()
      .then((res) => {
        let audios = [];
        let videos = [];
        res.forEach((d) => {
          if (d.kind === "videoinput") {
            videos.push(d);
          }
          if (d.kind === "audioinput") {
            audios.push(d);
          }
        });
        if (videos.length === 0 || audios.length === 0) {
          // 어떻게 처리할까?
        }
        setDevices({
          ...devices,
          video: [
            ...videos,
            { deviceId: "noDevice", kind: "videoinput", label: "카메라끄기" },
          ],
          audio: [
            ...audios,
            { deviceId: "noDevice", kind: "audioinput", label: "마이크끄기" },
          ],
        });
      })
      .catch((err) => {
        console.log("디바이스를 찾지 못했습니다. \n" + err);
      });
  };

  // 비디오 변경
  const changeVideo = (e) => {
    if (
      recorder &&
      recorder.state !== LocalRecorderState.FINISHED &&
      recorder.state !== LocalRecorderState.READY
    ) {
      alert("녹화 중에는 기기 변경이 불가능합니다.");
      document.getElementById("choiceVideo").value =
        JSON.stringify(currentVideoDevice);
      return;
    }
    let newDeviceInfo = {
      video: JSON.parse(e.target.value),
      audio: currentAudioDevice,
    };
    localStorage.setItem("deviceInfo", JSON.stringify(newDeviceInfo));
    changeDevice();
  };

  // 오디오 변경
  const changeAudio = (e) => {
    if (
      recorder &&
      recorder.state !== LocalRecorderState.FINISHED &&
      recorder.state !== LocalRecorderState.READY
    ) {
      alert("녹화 중에는 기기 변경이 불가능합니다.");
      document.getElementById("choiceAudio").value =
        JSON.stringify(currentAudioDevice);
      return;
    }
    let newDeviceInfo = {
      video: currentVideoDevice,
      audio: JSON.parse(e.target.value),
    };
    localStorage.setItem("deviceInfo", JSON.stringify(newDeviceInfo));
    changeDevice();
  };

  // 비디오/오디오 변경사항 적용
  const changeDevice = async () => {
    const newDeviceInfo = JSON.parse(localStorage.getItem("deviceInfo"));
    const newPublisher = await OV.initPublisherAsync(undefined, {
      audioSource:
        newDeviceInfo.audio.deviceId === "noDevice"
          ? undefined
          : newDeviceInfo.audio.deviceId,
      videoSource:
        newDeviceInfo.video.deviceId === "noDevice"
          ? undefined
          : newDeviceInfo.video.deviceId,
      publishAudio: !!!(newDeviceInfo.audio.deviceId === "noDevice"),
      publishVideo: !!!(newDeviceInfo.video.deviceId === "noDevice"),
      resolution: "1280x720",
      frameRate: 30,
      insertMode: "APPEND",
      mirror: false,
    });
    await session.unpublish(publisher).then(() => {
      session.publish(newPublisher);
      setPublisher(newPublisher);
    });
  };
  //// 세션 설정

  // 녹화 객체 생성
  const [recorder, setRecorder] = useState();

  useEffect(() => {
    if (
      !currentVideoDevice ||
      currentVideoDevice.deviceId === "noDevice" ||
      !currentAudioDevice ||
      currentAudioDevice.deviceId === "noDevice"
    ) {
      setRecorder(undefined);
    }
    if (!!publisher && !recorder) {
      setRecorder(OV.initLocalRecorder(publisher.stream));
    }
  }, [publisher]);

  // 녹화 시작
  const initRecord = () => {
    if (
      !!!recorder ||
      (recorder.state !== LocalRecorderState.READY &&
        recorder.state !== LocalRecorderState.FINISHED)
    ) {
      alert("카메라와 마이크가 없는 상태에서는 녹화를 할 수 없습니다.");
      return "err";
    }
    recorder.clean();
    recorder.record();
  };

  // 녹화 일시중지
  const pauseRecord = () => {
    if (!recorder || recorder.state !== LocalRecorderState.RECORDING) {
      alert("녹화 중이 아닙니다.");
      return "err";
    }
    recorder.pause();
  };

  // 녹화 재개
  const resumeRecord = () => {
    if (!recorder || recorder.state !== LocalRecorderState.PAUSED) {
      alert("일시정지된 녹화가 없습니다.");
      return "err";
    }
    recorder.resume();
  };

  // 녹화 종료 (자동 다운로드)
  const stopRecord = async () => {
    if (
      !recorder ||
      (recorder.state !== LocalRecorderState.RECORDING &&
        recorder.state !== LocalRecorderState.PAUSED)
    ) {
      alert("종료할 녹화가 없습니다.");
      return "err";
    }
    await recorder.stop().then(() => {
      let blob = recorder.getBlob();
      let url = window.URL.createObjectURL(blob);
      let a = document.createElement("a");
      a.href = url;
      let today = new Date();
      let title =
        today.getFullYear().toString() +
        (today.getMonth() + 1).toString() +
        today.getDate().toString() +
        "_interview_record";
      a.download = `${title}.mp4`;
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    });
    setRecorder(OV.initLocalRecorder(publisher.stream));
  };
  //// 녹화

  // 채팅

  // 채팅 객체
  const [chat, setChat] = useState({
    log: [],
  });

  const fnEnter = (e) => {
    if (e.key === "Enter") {
      sendChat();
    }
  };

  const sendChat = () => {
    session
      .signal({
        data: document.getElementById("chatInput").value,
        to: [],
        type: "chat",
      })
      .catch((e) => {
        console.log(e);
      });
    document.getElementById("chatInput").value = "";
  };

  //// 채팅

  // 피드백

  // 피드백 객체
  const [feedback, setFeedback] = useState({
    feedbacks: [],
  });
  const [hideModdal, setHideModal] = useState(true);

  const openModal = () => {
    setHideModal(false);
  };

  const closeModal = () => {
    setHideModal(true);
  };

  // 피드백 입력
  const writeFeedback = (sub, value) => {
    const data = JSON.stringify({
      writer: userData.nickname,
      feed: value,
    });
    session
      .signal({
        data: data,
        to: [sub.stream.connection],
        type: "feedback",
      })
      .catch((e) => {
        console.log(e);
      });
  };

  // 피드백 종합하기
  const compFeedback = () => {
    let text = "";
    feedback.feedbacks.map((f) => {
      return (text += `-${f.writer}-\n${f.feed}\n\n`);
    });

    return text;
  };

  // 피드백 다운로드
  const downloadFeedback = () => {
    let text = compFeedback();

    const link = document.createElement("a");
    link.setAttribute(
      "href",
      "data:text/plain;charset=utf-8," + encodeURIComponent(text)
    );
    link.setAttribute("download", "download.txt");
    link.click();
    link.remove();
  };

  // 피드백 복사
  const copyFeedback = () => {
    document.getElementById("feedbackArea").select();
    document.execCommand("copy");
  };

  //// 피드백

  // 사이드바
  const [closeSideBar, setCloseSideBar] = useState(false);
  const [option, setOption] = useState("member");
  const changeOption = (value) => {
    setOption(value);
  };
  const toggleSideBar = (value) => {
    setCloseSideBar(value);
  };
  //// 사이드바

  return (
    // eslint-disable-next-line no-restricted-globals
    <Container>
      <ModalContainer hidden={hideModdal}>
        <BeforeExitModal
          downloadFeedback={downloadFeedback}
          copyFeedback={copyFeedback}
          compFeedback={compFeedback}
          closeModal={closeModal}
          leaveSession={leaveSession}
        ></BeforeExitModal>
      </ModalContainer>
      <HeaderContainer>
        <TitleContainer>{"스터디 제목"}</TitleContainer>
        <ButtonContainer>
          <LayoutButton>보기1</LayoutButton>
          <LayoutButton>보기2</LayoutButton>
          <LayoutButton>보기3</LayoutButton>
        </ButtonContainer>
      </HeaderContainer>
      <MainContainer>
        <MeetingMainContainer right={closeSideBar ? 0 : 20}>
          <MeetingMain
            publisher={publisher}
            subscribers={subscribers}
          ></MeetingMain>
        </MeetingMainContainer>
        <MeetingSideContainer hidden={closeSideBar}>
          <MeetingSideBar
            toggleSideBar={toggleSideBar}
            changeOption={changeOption}
            option={option}
            chatData={chat.log}
            fnEnter={fnEnter}
            sendChat={sendChat}
            subscribers={subscribers}
            writeFeedback={writeFeedback}
            userData={userData}
          ></MeetingSideBar>
        </MeetingSideContainer>
      </MainContainer>
      <FooterContainer>
        <MeetingFooter
          toggleSideBar={toggleSideBar}
          changeOption={changeOption}
          initRecord={initRecord}
          pauseRecord={pauseRecord}
          resumeRecord={resumeRecord}
          stopRecord={stopRecord}
          openModal={openModal}
          leaveSession={leaveSession}
          devices={devices}
          changeVideo={changeVideo}
          changeAudio={changeAudio}
          currentAudioDevice={currentAudioDevice}
          currentVideoDevice={currentVideoDevice}
        ></MeetingFooter>
      </FooterContainer>
    </Container>
  );
}
