import styled from "styled-components";
// import MicButton from "../MicButton";
// import VideoButton from "../VideoButton";
import MembersButton from "../MembersButton";
import TimerButton from "../TimerButton";
import RecordButton from "../RecordButton";
import ExitButton from "../ExitButton";
import ChatButton from "../ChatButton";
import FeedbackButton from "../FeedbackButton";

const Container = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  width: 100%;
`;

const GroupingContainer = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  width: 20%;
`;

const SelectContainer = styled.select`
  width: 50%;
`;

const OptionContainer = styled.option``;

export default function MeetingFooter({
  toggleSideBar,
  changeOption,
  initRecord,
  pauseRecord,
  resumeRecord,
  stopRecord,
  openModal,
  leaveSession,
  devices,
  changeVideo,
  changeAudio,
  currentVideoDevice,
  currentAudioDevice,
}) {
  return (
    <Container>
      <GroupingContainer>
        <SelectContainer
          onChange={changeVideo}
          value={JSON.stringify(
            JSON.parse(sessionStorage.getItem("deviceInfo")).video
          )}
          title="비디오 선택"
          id="choiceVideo"
        >
          {devices.video
            ? devices.video.map((d) => (
                <OptionContainer value={JSON.stringify(d)} key={d.deviceId}>
                  {d.label}
                </OptionContainer>
              ))
            : undefined}
        </SelectContainer>
        {/* <VideoButton></VideoButton> */}
        <SelectContainer
          onChange={changeAudio}
          value={JSON.stringify(
            JSON.parse(sessionStorage.getItem("deviceInfo")).audio
          )}
          title="오디오 선택"
          id="choiceAudio"
        >
          {devices.audio
            ? devices.audio.map((d) => (
                <OptionContainer value={JSON.stringify(d)} key={d.deviceId}>
                  {d.label}
                </OptionContainer>
              ))
            : undefined}
        </SelectContainer>
        {/* <MicButton></MicButton> */}
      </GroupingContainer>
      <GroupingContainer>
        <TimerButton></TimerButton>
        <RecordButton
          initRecord={initRecord}
          pauseRecord={pauseRecord}
          resumeRecord={resumeRecord}
          stopRecord={stopRecord}
        ></RecordButton>
      </GroupingContainer>
      <GroupingContainer>
        <MembersButton
          toggleSideBar={toggleSideBar}
          changeOption={changeOption}
        ></MembersButton>
        <ChatButton
          toggleSideBar={toggleSideBar}
          changeOption={changeOption}
        ></ChatButton>
        <FeedbackButton
          toggleSideBar={toggleSideBar}
          changeOption={changeOption}
        ></FeedbackButton>
      </GroupingContainer>
      <ExitButton
        openModal={openModal}
        leaveSession={leaveSession}
      ></ExitButton>
    </Container>
  );
}
