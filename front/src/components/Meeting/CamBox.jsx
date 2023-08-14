import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import { BiVolumeMute } from "react-icons/bi";
import UserProfile from "./../Common/UserProfile";

const Container = styled.div`
  width: 100%;
  height: 100%;
  border: 1px solid black;
`;

const CamContainer = styled.video`
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: grey;
  z-index: -100;
`;

const ProfileContainer = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: grey;
  z-index: 50;
`;

const ProfileBox = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const InfoContainer = styled.div`
  position: absolute;
  bottom: 0;
  left: 0;
  z-index: 100;
`;

function CamBox({ streamManager, getActive }) {
  const videoRef = useRef();

  useEffect(() => {
    if (streamManager && !!videoRef) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, []);

  useEffect(() => {
    if (streamManager) {
      function loop() {
        alert("루프");
        setTimeout(() => {
          const active = getActive(streamManager.stream.streamId);
          setVideoActive(active.videoActive);
          setAudioActive(active.audioActive);
          loop();
        }, 1000);
      }
      loop();
    }
  }, []);

  const getUserData = () => {
    const data = streamManager
      ? streamManager.stream.connection.data
      : undefined;
    return data
      ? JSON.parse(data).clientData
      : { nickname: "로딩 중", background: "cow", character: "red" };
  };

  const [mouseOver, setMouseOver] = useState(false);

  const hover = () => {
    setMouseOver(true);
  };

  const unhover = () => {
    setMouseOver(false);
  };

  const [videoActive, setVideoActive] = useState(true);
  const [audioActive, setAudioActive] = useState(true);

  return (
    <>
      <Container onMouseOver={hover} onMouseLeave={unhover}>
        {streamManager ? (
          <CamContainer
            autoPlay={videoActive}
            ref={videoRef}
            hidden={!videoActive}
          ></CamContainer>
        ) : null}
        <ProfileContainer hidden={videoActive}>
          <ProfileBox>
            <UserProfile
              nickname={getUserData().nickname}
              backgroundcolor={getUserData().backgroundColor}
              characterimg={getUserData().backgroundImg}
            />
          </ProfileBox>
        </ProfileContainer>
        <BiVolumeMute
          size={50}
          color="red"
          display={audioActive ? "none" : ""}
        ></BiVolumeMute>
        <InfoContainer hidden={!mouseOver}>
          {getUserData().nickname}{" "}
        </InfoContainer>
      </Container>
    </>
  );
}

export default CamBox;
