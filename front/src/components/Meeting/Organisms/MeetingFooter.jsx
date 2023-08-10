import styled from "styled-components";
import MicButton from "../MicButton";
import VideoButton from "../VideoButton";
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

export default function MeetingFooter({
  toggleSideBar,
  changeOption,
  initRecord,
  pauseRecord,
  resumeRecord,
  stopRecord,
}) {
  return (
    <Container>
      <GroupingContainer>
        <MicButton></MicButton>
        <VideoButton></VideoButton>
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
      <ExitButton></ExitButton>
    </Container>
  );
}
