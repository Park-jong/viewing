import { OpenVidu } from "openvidu-browser";
import { React, useEffect, useRef, useState } from "react";

function ReadyPage() {
  const CLIENT_URL = process.env.REACT_APP_CLIENT_URL;

  const OV = new OpenVidu();

  const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

  useEffect(() => {
    getPermission();
    findDevice();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [devices, setDevices] = useState({
    video: [],
    audio: [],
  });

  const [videoDevice, setVideoDevice] = useState();

  const [audioDevice, setAudioDevice] = useState();

  const [volume, setVolume] = useState("");

  const currVideo = useRef();
  const currAudio = useRef();

  const getPermission = async () => {
    await navigator.mediaDevices
      .getUserMedia({ audio: true, video: true })
      .then(() => {})
      .catch((error) => {
        console.error("미디어 액세스 권한을 허용하지 않았습니다:", error);
      });
  };

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
            { deviceId: "noDevice", kind: "videoinput", label: "noCAM" },
          ],
          audio: [
            ...audios,
            { deviceId: "noDevice", kind: "audioinput", label: "noMIC" },
          ],
        });
        setVideoDevice(videos.length > 0 ? videos[0] : undefined);
        setAudioDevice(audios.length > 0 ? audios[0] : undefined);
      })
      .catch((err) => {
        console.log("디바이스를 찾지 못했습니다. \n" + err);
      });
  };

  const onChangeVideo = (e) => {
    setVideoDevice(JSON.parse(e.target.value));
    if (document.getElementById("videoTest").srcObject) {
      testVideo();
    }
  };

  const onChangeAudio = (e) => {
    setAudioDevice(JSON.parse(e.target.value));
  };

  const testVideo = async () => {
    document.getElementById("startVideoTest").hidden = true;
    document.getElementById("stopVideoTest").hidden = false;
    const element = document.getElementById("videoTest");
    let stream = undefined;
    if (JSON.parse(currVideo.current.value).deviceId === "noDevice") {
      const img = document.getElementById("profileImage");
      const canvas = document.createElement("canvas");
      canvas.width = img.width;
      canvas.height = img.height;
      const canvasContext = canvas.getContext("2d");
      canvasContext.drawImage(img, 0, 0, img.width, img.height);
      stream = canvas.captureStream();
    } else {
      stream = await navigator.mediaDevices.getUserMedia({
        video: { deviceId: JSON.parse(currVideo.current.value).deviceId },
      });
    }
    element.srcObject = stream;
  };

  const stopTestVideo = () => {
    document.getElementById("startVideoTest").hidden = false;
    document.getElementById("stopVideoTest").hidden = true;
    document.getElementById("videoTest").srcObject = undefined;
  };

  const testAudio = async () => {
    const savedId = JSON.parse(currAudio.current.value).deviceId;
    const stream = await navigator.mediaDevices.getUserMedia({
      audio: { deviceId: savedId },
    });
    const source = audioCtx.createMediaStreamSource(stream);
    const analyser = audioCtx.createAnalyser();
    analyser.fftSize = 256;
    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);

    const stopButton = document.getElementById("stopAudioTest");
    const startButton = document.getElementById("startAudioTest");
    startButton.hidden = true;
    let stop = false;
    stopButton.hidden = false;
    function toggleStop() {
      stop = true;
      stopButton.hidden = true;
      startButton.hidden = false;
    }
    stopButton.addEventListener("click", toggleStop);

    const updateVolume = () => {
      if (savedId !== JSON.parse(currAudio.current.value).deviceId) {
        testAudio();
        return;
      }
      if (savedId === "noDevice") {
        toggleStop();
        alert("마이크가 선택되지 않았습니다.");
      }
      if (stop) {
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

  const enterRoom = () => {
    const deviceInfo = {
      video: videoDevice ? videoDevice : devices.video[0],
      audio: audioDevice ? audioDevice : devices.audio[0],
    };
    localStorage.setItem("deviceInfo", JSON.stringify(deviceInfo));
    window.open(`${CLIENT_URL}/test/main`);
    window.location.replace(`${CLIENT_URL}/test`);
  };

  return (
    <>
      <img id="profileImage" src={"/test.jpg"} alt="프로필이미지" hidden></img>
      <button onClick={enterRoom}>
        <h1>입장</h1>
      </button>
      <h1>비디오선택</h1>
      <select
        onChange={onChangeVideo}
        value={JSON.stringify(videoDevice)}
        ref={currVideo}
      >
        {devices.video
          ? devices.video.map((d) => (
              <option value={JSON.stringify(d)} key={d.deviceId}>
                {d.label}
              </option>
            ))
          : undefined}
      </select>
      <h1>오디오선택</h1>
      <select
        onChange={onChangeAudio}
        value={JSON.stringify(audioDevice)}
        ref={currAudio}
      >
        {devices.audio
          ? devices.audio.map((d) => (
              <option value={JSON.stringify(d)} key={d.deviceId}>
                {d.label}
              </option>
            ))
          : undefined}
      </select>
      <hr></hr>
      <div>
        선택된 비디오 기기 : {videoDevice ? videoDevice.label : undefined}
      </div>
      <div>
        선택된 오디오 기기 : {audioDevice ? audioDevice.label : undefined}
      </div>
      <hr></hr>
      <button id="startVideoTest" onClick={testVideo}>
        비디오테스트시작
      </button>
      <button id="stopVideoTest" onClick={stopTestVideo} hidden={true}>
        비디오테스트중지
      </button>
      <div
        style={{
          width: "640px",
          height: "480px",
          border: "2px solid black",
          backgroundColor: "grey",
        }}
      >
        <video
          id="videoTest"
          autoPlay={true}
          style={{ width: "100%", height: "100%" }}
        ></video>
      </div>
      <button id="startAudioTest" onClick={testAudio}>
        오디오테스트시작
      </button>
      <button id="stopAudioTest" hidden={true}>
        오디오테스트중지
      </button>
      <div>{volume ? "현재 음량 : " + volume : undefined}</div>
      <div
        id="audioTest"
        style={{
          width: `${volume ? volume : 0}%`,
          height: 50,
          backgroundColor: "red",
          border: "2px solid black",
        }}
      ></div>
    </>
  );
}

export default ReadyPage;
