import styled from "styled-components";
import MeetingTabBar from "./../MeetingTabBar";
import MeetingChatBox from "./../MeetingChatBox";
import MemberBox from "./../MemberBox";
import MeetingFeedbackBox from "./../MeetingFeedbackBox";
import { useState } from "react";

const Container = styled.div`
  /* position: relative; */
  /* border: 1px solid black; */
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
  /* border: 1px solid black; */
  overflow-y: auto;
`;

const ChatContainer = styled.div`
  position: absolute;
  top: 5%;
  width: 100%;
  height: 95%;
  /* border: 1px solid black; */
`;

const FeedbackContainer = styled.div`
  position: absolute;
  top: 5%;
  width: 100%;
  height: 95%;
  /* border: 1px solid black; */
  overflow-y: auto;
`;

const ChatLogArea = styled.div`
  position: absolute;
  height: 95%;
  width: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column-reverse;
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

const FeedbackTargetContainer = styled.div``;

const SelectContainer = styled.select``;

const OptionContainer = styled.option``;

const FeedBackWriteContainer = styled.div``;

export default function MeetingSideBar({
  toggleSideBar,
  option,
  changeOption,
  chatData,
  fnEnter,
  sendChat,
  subscribers,
  writeFeedback,
  userData,
}) {
  const defaultTarget = "피드백 대상 선택";
  const [feedbackTarget, setFeedbackTarget] = useState(defaultTarget);

  const changeFeedbackTaget = (e) => {
    setFeedbackTarget(e.currentTarget.value);
  };

  return (
    <Container>
      <TabBarContainer>
        <MeetingTabBar
          changeOption={changeOption}
          toggleSideBar={toggleSideBar}
        ></MeetingTabBar>
      </TabBarContainer>
      <MemberContainer hidden={option !== "member"}>
        <MemberBox data={userData}></MemberBox>
        {subscribers.subs.map((sub, i) => {
          return (
            <MemberBox
              data={JSON.parse(sub.stream.connection.data).clientData}
              key={i}
            ></MemberBox>
          );
        })}
      </MemberContainer>
      <ChatContainer hidden={option !== "chat"}>
        <ChatLogArea id="chatLogArea">
          {chatData.map((data, i) => {
            return <MeetingChatBox data={data} key={i}></MeetingChatBox>;
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
        <FeedbackTargetContainer>
          <SelectContainer
            onChange={changeFeedbackTaget}
            defaultValue={defaultTarget}
          >
            <OptionContainer value={defaultTarget} disabled>
              {defaultTarget}
            </OptionContainer>
            {subscribers.subs.map((sub, i) => {
              return (
                <OptionContainer
                  value={JSON.stringify(sub.stream.connection.data)}
                  key={i}
                >
                  {JSON.parse(sub.stream.connection.data).clientData.nickname}
                </OptionContainer>
              );
            })}
          </SelectContainer>
        </FeedbackTargetContainer>
        {subscribers.subs.map((sub, i) => {
          return (
            <FeedBackWriteContainer
              hidden={
                feedbackTarget !== JSON.stringify(sub.stream.connection.data)
              }
              key={i}
            >
              <MeetingFeedbackBox
                writeFeedback={writeFeedback}
                subscriber={sub}
              ></MeetingFeedbackBox>
            </FeedBackWriteContainer>
          );
        })}
      </FeedbackContainer>
    </Container>
  );
}
