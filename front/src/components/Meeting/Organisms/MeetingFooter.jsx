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
`;

const GroupingContainer = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
`;

export default function MeetingFooter() {
  return (
    <Container>
      <GroupingContainer>
        <MicButton></MicButton>
        <VideoButton></VideoButton>
      </GroupingContainer>
      <GroupingContainer>
        <TimerButton></TimerButton>
        <RecordButton></RecordButton>
      </GroupingContainer>
      <GroupingContainer>
        <MembersButton></MembersButton>
        <ChatButton></ChatButton>
        <FeedbackButton></FeedbackButton>
      </GroupingContainer>
      <ExitButton></ExitButton>
    </Container>
  );
}
