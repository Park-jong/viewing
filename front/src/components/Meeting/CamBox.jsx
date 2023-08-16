import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import { BiVolumeMute } from "react-icons/bi";
import UserProfile from "./../Common/UserProfile";

const Container = styled.div`
  width: 100%;
  height: 100%;
  /* border: 1px solid ; */
  /* border-radius: 100px; */
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

const InfoContainer = styled.div`
  position: absolute;
  bottom: 0;
  left: 0;
  z-index: 100;
  background-color: var(--gray-200);
  padding: 5px;
`;

function CamBox({ streamManager }) {
  const videoRef = useRef();

  useEffect(() => {
    if (streamManager && !!videoRef) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

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

  return (
    <>
      <Container onMouseOver={hover} onMouseLeave={unhover}>
        {streamManager ? (
          <CamContainer
            autoPlay={streamManager.stream.videoActive}
            ref={videoRef}
            hidden={!streamManager.stream.videoActive}
          ></CamContainer>
        ) : null}
        <ProfileContainer
          hidden={streamManager ? streamManager.stream.videoActive : false}
        >
          <ProfileBox>
            <UserProfile
              nickname={getUserData().nickname}
              backgroundcolor={getUserData().backgroundColor}
              characterimg={getUserData().backgroundImg}
              member_id={getUserData().member_id}
            />
          </ProfileBox>
        </ProfileContainer>
        <BiVolumeMute
          size={50}
          color="red"
          display={
            streamManager
              ? streamManager.stream.audioActive
                ? "none"
                : ""
              : "none"
          }
        ></BiVolumeMute>
        <InfoContainer hidden={!mouseOver}>
          {getUserData().nickname}{" "}
        </InfoContainer>
      </Container>
    </>
  );
}

export default CamBox;
