import React, { useEffect, useRef } from "react";
import styled from "styled-components";
import { BiVolumeMute } from "react-icons/bi";
import UserProfile from "./../Common/UserProfile";

function CamBox({ streamManager }) {
  const videoRef = useRef();

  useEffect(() => {
    if (streamManager && !!videoRef) {
      streamManager.addVideoElement(videoRef.current);
    }
  });

  const getUserData = () => {
    const data = streamManager
      ? streamManager.stream.connection.data
      : undefined;
    return data
      ? JSON.parse(data).clientData
      : { nickname: "로딩 중", background: "cow", character: "red" };
  };

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
    z-index: -100;
  `;

  const ProfileBox = styled.div`
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  `;

  return (
    <>
      <Container>
        {streamManager ? (
          <CamContainer autoPlay={true} ref={videoRef}></CamContainer>
        ) : null}
        <ProfileContainer
          hidden={streamManager ? streamManager.stream.videoActive : false}
        >
          <ProfileBox>
            <UserProfile
              nickname={getUserData().nickname}
              backgroundcolor={getUserData().background}
              characterimg={getUserData().character}
            />
          </ProfileBox>
        </ProfileContainer>
        <BiVolumeMute
          display={
            streamManager
              ? streamManager.stream.audioActive
                ? "none"
                : ""
              : "none"
          }
          size={50}
          color="red"
        ></BiVolumeMute>
      </Container>
    </>
  );
}

export default CamBox;
