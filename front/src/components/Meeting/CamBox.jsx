import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import { BiVolumeMute } from "react-icons/bi";
import UserProfile from "./../Common/UserProfile";

function CamBox({ streamManager }) {
  const videoRef = useRef();

  // useEffect(() => {
  //   if (streamManager && !!videoRef) {
  //     streamManager.addVideoElement(videoRef.current);
  //   }
  // });

  const getUserData = () => {
    // const stringData = streamManager.stream.connection.data.toString();
    // return JSON.parse(stringData);
    return { background: "red", character: "cow", nickname: "tmp" };
  };

  const Container = styled.div`
    position: relative;
    width: 640px;
    height: 360px;
    border: 1px solid black;
  `;

  const CamContainer = styled.video`
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: grey;
    z-index: -990;
  `;

  const ProfileContainer = styled.div`
    opacity: 50%;
    &:hover {
      opacity: 100%;
    }

    position: absolute;
    width: 100%;
    height: 100%;
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
        {/* {streamManager ? ( */}
        <CamContainer autoPlay={true} ref={videoRef}></CamContainer>
        {/* ) : null} */}
        {/* <InfoContainer hidden={streamManager.stream.videoActive}> */}
        <ProfileContainer>
          <ProfileBox style={false ? { zIndex: "-999" } : { zIndex: "999" }}>
            <UserProfile
              nickname={getUserData().nickname}
              backgroundcolor={getUserData().background}
              characterimg={getUserData().character}
            />
          </ProfileBox>
        </ProfileContainer>
        {/* <BiMicrophoneOff display={streamManager.stream.audioActive?"":"none"}> */}
        <BiVolumeMute display={""} size={50} color="red"></BiVolumeMute>
      </Container>
    </>
  );
}

export default CamBox;
