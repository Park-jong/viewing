import { styled } from "styled-components";
import HeaderBox from "./../components/Layout/HeaderBox";
import Footer from "../components/Layout/Footer";
import MainButton from "../components/Button/MainButton";
import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { OpenVidu } from "openvidu-browser";

const Container = styled.div``;

const BodyContainer = styled.div`
  padding-left: 150px;
  padding-right: 150px;
  border: 1px solid black;
`;

const TitleContainer = styled.div`
  border: 1px solid black;
  text-align: center;
  font-size: 30px;
`;

const TestContainer = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  text-align: center;
`;

const TestVideo = styled.video`
  width: 640px;
  height: 480px;
  margin-right: 5px;
  margin-right: 5px;
`;

const TestAudio = styled.div`
  width: 30px;
  height: ${(props) => `${props.height * 3}px`};
  background: linear-gradient(lightCyan, skyBlue, deepSkyBlue);
  margin-right: 5px;
  margin-right: 5px;
  margin-top: auto;
`;

const UtilContainer = styled.div`
  border: 1px solid black;
  padding: 5px;
  width: auto;
  height: 50px;
  display: flex;
  align-items: center;
  text-align: center;
`;

const SelectContainer = styled.select`
  border: 1px solid black;
  font-size: 15px;
`;

const OptionContainer = styled.option`
  border: 1px solid black;
`;

const LabelContainer = styled.label`
  margin-left: 30px;
  margin-right: 10px;
  font-size: 15px;
`;

const EntranceContainer = styled.div`
  display: inline;
  margin-left: auto;
  border: 1px solid black;
`;

export default function MeetingPkReady() {
  // 스터디 정보
  const [studyId, setStudy] = useState();

  // 우선 스터디를 숫자 1로 임시로 설정
  useEffect(() => {
    setStudy(1);
  }, []);

  // 임시 유저 데이터
  const getUserData = () => {
    // const stringData = streamManager.stream.connection.data.toString();
    // return JSON.parse(stringData);
    return { background: "red", character: "cow", nickname: "tmp" };
  };

  const OV = new OpenVidu();

  // 오디오 음량을 시각화하기 위한 객체
  const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

  const [devices, setDevices] = useState({
    video: [],
    audio: [],
  });

  const [videoDevice, setVideoDevice] = useState();

  const [audioDevice, setAudioDevice] = useState();

  const [volume, setVolume] = useState("");

  const currVideo = useRef();
  const currAudio = useRef();

  // 미디어 권한을 얻고, 가용기기를 찾는다.
  useEffect(() => {
    getPermission();
    findDevice();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 미디어 권한을 얻는 함수
  const getPermission = async () => {
    await navigator.mediaDevices
      .getUserMedia({ audio: true, video: true })
      .then(() => {})
      .catch((error) => {
        console.error("미디어 액세스 권한을 허용하지 않았습니다:", error);
      });
  };

  // 가용한 미디어 기기를 찾는 함수
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
        setDevices({
          ...devices,
          video: [
            ...videos,
            { deviceId: "noDevice", kind: "videoinput", label: "noCAM" },
          ],
          audio: [
            ...audios,
            { deviceId: "noDevice", kind: "audioinput", label: "noMIC" },
          ],
        });
        setVideoDevice(
          videos.length > 0
            ? videos[0]
            : { deviceId: "noDevice", kind: "videoinput", label: "noCAM" }
        );
        setAudioDevice(
          audios.length > 0
            ? audios[0]
            : { deviceId: "noDevice", kind: "audioinput", label: "noMIC" }
        );
      })
      .catch((err) => {
        console.log("디바이스를 찾지 못했습니다. \n" + err);
      });
  };

  // 비디오 기기 변경
  const onChangeVideo = (e) => {
    setVideoDevice(JSON.parse(e.target.value));
    testVideo();
  };

  // 오디오 기기 변경
  const onChangeAudio = (e) => {
    setAudioDevice(JSON.parse(e.target.value));
  };

  const testVideo = async () => {
    const element = document.getElementById("videoTest");
    let stream = undefined;
    const savedId = currVideo.current.value
      ? JSON.parse(currVideo.current.value).deviceId
      : devices.video[0];
    if (savedId === "noDevice") {
      const img = document.createElement("img");
      img.src = `/profile/${getUserData().character}.png`;
      const canvas = document.createElement("canvas");
      canvas.width = img.width;
      canvas.height = img.height;
      const canvasContext = canvas.getContext("2d");
      canvasContext.drawImage(img, 0, 0, img.width, img.height);
      stream = canvas.captureStream();
    } else {
      stream = await navigator.mediaDevices.getUserMedia({
        video: { deviceId: savedId },
      });
    }
    element.srcObject = stream;
  };

  const testAudio = async () => {
    const savedId = currAudio.current.value
      ? JSON.parse(currAudio.current.value).deviceId
      : devices.audio[0];
    const stream = await navigator.mediaDevices.getUserMedia({
      audio: { deviceId: savedId },
    });
    const source = audioCtx.createMediaStreamSource(stream);
    const analyser = audioCtx.createAnalyser();
    analyser.fftSize = 256;
    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);

    const updateVolume = () => {
      if (savedId !== JSON.parse(currAudio.current.value).deviceId) {
        testAudio();
        return;
      }
      if (savedId === "noDevice") {
        setVolume("");
        return;
      }

      setTimeout(() => {
        analyser.getByteFrequencyData(dataArray);
        let sum = 0;
        dataArray.forEach((v) => (sum += v));
        const avg = sum / bufferLength;
        setVolume(avg);
        requestAnimationFrame(updateVolume);
      }, 1000);
    };

    source.connect(analyser);
    updateVolume();
  };

  useEffect(() => {
    testVideo();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [videoDevice]);

  useEffect(() => {
    testAudio();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [audioDevice]);

  const goToConferencePage = () => {
    const deviceInfo = {
      video: videoDevice ? videoDevice : devices.video[0],
      audio: audioDevice ? audioDevice : devices.audio[0],
    };
    localStorage.setItem("deviceInfo", JSON.stringify(deviceInfo));
    window.open(`/meeting/${studyId}`);
    window.location.replace(`/study/${studyId}/meeting`);
  };

  return (
    <>
      <img
        src={`/profile/${getUserData().character}.png`}
        alt="프로필이미지"
        hidden
      ></img>
      <Container>
        <HeaderBox></HeaderBox>
        <BodyContainer>
          <TitleContainer>{"스터디 이름"}</TitleContainer>
          <TestContainer>
            <TestVideo id="videoTest" autoPlay={true}></TestVideo>
            <TestAudio height={volume}></TestAudio>
          </TestContainer>
          <UtilContainer>
            <LabelContainer htmlFor="choiceVideo">비디오 선택</LabelContainer>
            <SelectContainer
              id="choiceVideo"
              value={JSON.stringify(videoDevice)}
              ref={currVideo}
              onChange={onChangeVideo}
            >
              {devices.video
                ? devices.video.map((d) => (
                    <OptionContainer value={JSON.stringify(d)} key={d.deviceId}>
                      {d.label}
                    </OptionContainer>
                  ))
                : undefined}
            </SelectContainer>
            <LabelContainer htmlFor="choiceAudio">오디오 선택</LabelContainer>
            <SelectContainer
              id="choiceAudio"
              onChange={onChangeAudio}
              value={JSON.stringify(audioDevice)}
              ref={currAudio}
            >
              {devices.audio
                ? devices.audio.map((d) => (
                    <OptionContainer value={JSON.stringify(d)} key={d.deviceId}>
                      {d.label}
                    </OptionContainer>
                  ))
                : undefined}
            </SelectContainer>
            <EntranceContainer>
              <MainButton
                content={"입장"}
                fontSize={30}
                height={50}
                width={80}
                onClick={goToConferencePage}
              ></MainButton>
            </EntranceContainer>
          </UtilContainer>
        </BodyContainer>
        <Footer></Footer>
      </Container>
    </>
  );
}
