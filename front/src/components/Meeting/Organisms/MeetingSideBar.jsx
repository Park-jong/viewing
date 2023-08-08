import styled from "styled-components";
import MeetingTabBar from "./../MeetingTabBar";
import MeetingChatBox from "./../MeetingChatBox";
import MemberBox from "./../MemberBox";
import MeetingFeedbackBox from "./../MeetingFeedbackBox";

const Container = styled.div`
  border: 1px solid black;
  width: 100%;
`;

const ChatContainer = styled.div`
  border: 1px solid black;
`;

const FeedbackContainer = styled.div`
  border: 1px solid black;
`;

export default function MeetingSideBar() {
  return (
    <Container>
      <MeetingTabBar></MeetingTabBar>
      <MemberBox></MemberBox>
      <ChatContainer>
        <MeetingChatBox></MeetingChatBox>
        <MeetingChatBox></MeetingChatBox>
        <MeetingChatBox></MeetingChatBox>
      </ChatContainer>
      <FeedbackContainer>
        <MeetingFeedbackBox></MeetingFeedbackBox>
        <MeetingFeedbackBox></MeetingFeedbackBox>
        <MeetingFeedbackBox></MeetingFeedbackBox>
      </FeedbackContainer>
    </Container>
  );
}
