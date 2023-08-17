import { styled } from "styled-components";
import HeaderBox from "./../components/Layout/HeaderBox";
import Footer from "../components/Layout/Footer";
import MainButton from "../components/Button/MainButton";
import { useEffect, useRef, useState } from "react";
import { OpenVidu } from "openvidu-browser";
import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";
import { UserReducer } from "./../modules/UserReducer/UserReducer";

const Container = styled.div`
  background-color: var(--gray-100);
`;

const BeforePermit = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const AfterPermit = styled.div``;

const BodyContainer = styled.div`
  margin: 20px 150px;
  background-color: white;
  border-radius: 50px;
  /* border: 1px solid black; */
  padding: 5px;
`;

const SubTitle = styled.div`
  margin-left: 50px;
  /* margin-top: 50px; */
  padding-top: 20px;
`;

const TitleContainer = styled.div`
  /* border: 1px solid black; */
  /* text-align: center; */
  font-size: 25px;
  font-weight: 700;
  margin-left: 50px;
  margin-top: 10px;
`;

const TestContainer = styled.div`
  /* border: 1px solid black; */
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
  padding: 10px;
  border-radius: 30px;
`;

const TestAudio = styled.div`
  width: 30px;
  height: ${(props) => `${props.height * 3}px`};
  background: linear-gradient(lightCyan, skyBlue, deepSkyBlue);
  margin-right: 5px;
  margin-right: 5px;
  margin-top: auto;
`;

// const UtilContainer = styled.div`
//   border: 1px solid black;
//   padding: 10px;
//   display: flex;
//   justify-content: space-between;
//   align-items: center;
// `;

// const OptionContainer = styled.div`
//   display: flex;
//   align-items: center;
// `;

// const LabelContainer = styled.label`
//   font-size: 15px;
//   margin-right: 10px;
// `;

// const SelectContainer = styled.select`
//   border: 1px solid black;
//   font-size: 15px;
// `;

// const EntranceContainer = styled.div`
//   display: inline;
//   margin-right: 50px;
// `;

const UtilContainer = styled.div`
  /* border: 1px solid black; */
  padding: 5px;
  width: auto;
  height: 50px;
  display: flex;
  align-items: center;
  text-align: center;
`;

const SelectContainer = styled.select`
  /* border: 1px solid black; */
  font-size: 15px;
`;

const OptionContainer = styled.option`
  /* border: 1px solid black; */
`;

const LabelContainer = styled.label`
  margin-left: 30px;
  margin-right: 10px;
  font-size: 15px;
`;

const EntranceContainer = styled.div`
  display: inline;
  margin-left: auto;
  margin-right: 30px;
  /* border: 1px solid black; */
`;

export default function MeetingPkReady() {
  const studyId = useLocation().pathname.split("/")[2];
  const user = useSelector((state) => state.UserReducer);

  //userdata
  const nickname = user.nickname;
  const backgroundColor = user.backgroundColor;
  const backgroundImg = user.backgroundImg;

  // 임시 유저 데이터
  // const userData = useSelector((state) => state.UserReducer);
  // const userData = {
  //   memberId: "5",
  //   nickname: `nick${Math.ceil(Math.random() * 1000)}`,
  //   backgroundColor: "red",
  //   backgroundImg: "cow",
  // };

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

  const [permit, setPermit] = useState(false);

  const currVideo = useRef();
  const currAudio = useRef();

  // 미디어 권한을 얻고, 가용기기를 찾는다.
  useEffect(() => {
    getPermission();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 미디어 권한을 얻는 함수
  const getPermission = async () => {
    await navigator.mediaDevices
      .getUserMedia({ audio: true, video: true })
      .then(() => {
        setPermit(true);
      })
      .catch((error) => {
        console.error("미디어 액세스 권한을 허용하지 않았습니다:", error);
        alert(
          "'크롬설정 > 개인 정보 보호 및 보안 > 사이트 설정' 에서 당사이트에 대한 카메라,마이크 권한을 허용해주세요"
        );
        window.location.replace(`/study/${studyId}/meeting`);
      });
    findDevice();
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
      img.src = `/profile/${backgroundImg}.png`;
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
      if (!currAudio.current) return;
      if (savedId === "noDevice") {
        setVolume("");
        return;
      }
      if (
        currAudio.current.value &&
        savedId !== JSON.parse(currAudio.current.value).deviceId
      ) {
        testAudio();
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
    sessionStorage.setItem("deviceInfo", JSON.stringify(deviceInfo));
    window.open(`/meeting/${studyId}`);
    sessionStorage.removeItem("deviceInfo");
    window.location.replace(`/study/${studyId}/meeting`);
  };

  return (
    <>
      <img
        src={`/profile/${backgroundImg}.png`}
        alt="프로필이미지"
        hidden
      ></img>
      <Container>
        <BeforePermit hidden={permit}>
          {"카메라, 마이크 권한에 대한 접근 중입니다."}
        </BeforePermit>
        <AfterPermit hidden={!permit}>
          <HeaderBox></HeaderBox>

          <BodyContainer>
            <SubTitle>💻화상회의 참여 준비하기</SubTitle>
            <TitleContainer>🙋{nickname}</TitleContainer>
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
                      <OptionContainer
                        value={JSON.stringify(d)}
                        key={d.deviceId}
                      >
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
                      <OptionContainer
                        value={JSON.stringify(d)}
                        key={d.deviceId}
                      >
                        {d.label}
                      </OptionContainer>
                    ))
                  : undefined}
              </SelectContainer>
              <EntranceContainer>
                <MainButton
                  content={"입장"}
                  fontSize={20}
                  height={40}
                  width={70}
                  onClick={goToConferencePage}
                ></MainButton>
              </EntranceContainer>
            </UtilContainer>
          </BodyContainer>
          <Footer></Footer>
        </AfterPermit>
      </Container>
    </>
  );
}
