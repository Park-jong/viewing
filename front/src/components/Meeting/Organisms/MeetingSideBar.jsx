import styled from "styled-components";
import MeetingTabBar from "./../MeetingTabBar";
import MeetingChatBox from "./../MeetingChatBox";
import MemberBox from "./../MemberBox";
import MeetingFeedbackBox from "./../MeetingFeedbackBox";

const Container = styled.div`
  border: 1px solid black;
  width: 100%;
`;

const TabBarContainer = styled.div`
  position: absolute;
  top: 0;
  width: 100%;
  height: 5%;
  overflow: auto;
`;

const MemberContainer = styled.div`
  position: absolute;
  top: 5%;
  width: 100%;
  height: 95%;
  border: 1px solid black;
`;

const ChatContainer = styled.div`
  position: absolute;
  top: 5%;
  width: 100%;
  height: 95%;
  border: 1px solid black;
`;

const FeedbackContainer = styled.div`
  position: absolute;
  top: 5%;
  width: 100%;
  height: 95%;
  border: 1px solid black;
`;

const ChatLogArea = styled.div`
  position: absolute;
  top: 5%;
  height: 95%;
  width: 100%;
  overflow: auto;
`;

const ChatInputArea = styled.div`
  position: absolute;
  height: 5%;
  bottom: 0;
`;

const ChatInput = styled.input`
  display: inline;
`;

const SendChatButton = styled.div`
  display: inline;
`;

export default function MeetingSideBar({
  toggleSideBar,
  option,
  changeOption,
  chatData,
  fnEnter,
  sendChat,
}) {
  return (
    <Container>
      <TabBarContainer>
        <MeetingTabBar
          changeOption={changeOption}
          toggleSideBar={toggleSideBar}
        ></MeetingTabBar>
      </TabBarContainer>
      <MemberContainer hidden={option !== "member"}>
        <MemberBox></MemberBox>
        <MemberBox></MemberBox>
        <MemberBox></MemberBox>
      </MemberContainer>
      <ChatContainer hidden={option !== "chat"}>
        <ChatLogArea id="chatLogArea">
          {chatData.map((data) => {
            console.log(data);
            return <MeetingChatBox data={data}></MeetingChatBox>;
          })}
        </ChatLogArea>
        <ChatInputArea>
          <ChatInput
            id="chatInput"
            onKeyUp={(e) => {
              fnEnter(e);
            }}
          ></ChatInput>
          <SendChatButton
            onClick={() => {
              sendChat();
            }}
          >
            전송
          </SendChatButton>
        </ChatInputArea>
      </ChatContainer>
      <FeedbackContainer hidden={option !== "feedback"}>
        <MeetingFeedbackBox></MeetingFeedbackBox>
        <MeetingFeedbackBox></MeetingFeedbackBox>
        <MeetingFeedbackBox></MeetingFeedbackBox>
      </FeedbackContainer>
    </Container>
  );
}
