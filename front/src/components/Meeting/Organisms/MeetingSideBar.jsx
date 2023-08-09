import styled from "styled-components";
import MeetingTabBar from "./../MeetingTabBar";
import MeetingChatBox from "./../MeetingChatBox";
import MemberBox from "./../MemberBox";
import MeetingFeedbackBox from "./../MeetingFeedbackBox";

const Container = styled.div`
  border: 1px solid black;
  width: 100%;
`;

const MemberContainer = styled.div`
  border: 1px solid black;
`;

const ChatContainer = styled.div`
  border: 1px solid black;
`;

const FeedbackContainer = styled.div`
  border: 1px solid black;
`;

export default function MeetingSideBar({
  toggleSideBar,
  option,
  changeOption,
}) {
  return (
    <Container>
      <MeetingTabBar
        changeOption={changeOption}
        toggleSideBar={toggleSideBar}
      ></MeetingTabBar>
      <MemberContainer hidden={option !== "member"}>
        <MemberBox></MemberBox>
        <MemberBox></MemberBox>
        <MemberBox></MemberBox>
      </MemberContainer>
      <ChatContainer hidden={option !== "chat"}>
        <MeetingChatBox></MeetingChatBox>
        <MeetingChatBox></MeetingChatBox>
        <MeetingChatBox></MeetingChatBox>
      </ChatContainer>
      <FeedbackContainer hidden={option !== "feedback"}>
        <MeetingFeedbackBox></MeetingFeedbackBox>
        <MeetingFeedbackBox></MeetingFeedbackBox>
        <MeetingFeedbackBox></MeetingFeedbackBox>
      </FeedbackContainer>
    </Container>
  );
}
