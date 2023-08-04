import styled from "styled-components";
import React, { useRef, useState, useEffect } from "react";
import stompjs from "stompjs";
import * as SockJS from "sockjs-client";
import UserProfile from "../Common/UserProfile";
import { useLocation } from "react-router-dom";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { BiCaretUp, BiCaretDown } from "react-icons/bi";
import { useSelector } from "react-redux";

const Container = styled.div`
  border: "2px solid black";
  padding: "16px";

  width: 1000px;
  height: 500px;
`;

const ChatArea = styled.div`
  width: 100%;
  height: 100%;
  overflow: auto;
`;

const ChatBox = styled.div``;

const ChatProfile = styled.span`
  //display: inline-block;
`;

const ChatText = styled.span`
  padding-left: 40px;
  font-size: 16px;
  color: var(--primary);
`;

const ChatTime = styled.span`
  font-size: 10px;
`;

const SendButton = styled.button``;

const MessageInputBox = styled.input``;

const ShowOldBox = styled.div`
  background: var(--gray-50);
  height: 50px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  user-select: none;
`;

const NewMessageAlram = styled.div`
  background: var(--gray-100);
  height: 50px;
  width: 100%;
  opacity: 0.5;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  user-select: none;
  position: relative;
  margin-top: -50px;
`;

export default function Chat() {
  const user = useSelector((state) => state.UserReducer);
  const [msg, setMsg] = useState("");
  const [msgList, setMsgList] = useState([]);
  const sockJS = new SockJS("http://70.12.246.107:8080/studyChat");
  const stompClient = stompjs.over(sockJS);
  const studyId = useLocation().pathname.split("/")[2];
  const [oldMsgState, setOldMsgState] = useState(true);
  const scrollRef = useRef();
  const [newPage, setNewPage] = useState(0);
  const [newMsgState, setNewMsgState] = useState(false);
  const maxLength = 5000;

  //메시지 전송
  const sendMsg = () => {
    try {
      stompClient.send(
        "/app/chats/studies/" + studyId,
        {},
        JSON.stringify({ member_id: 1, content: msg }) //로그인 되면 수정
      );
      moveEnd();
      setMsg("");
    } catch (error) {
      console.log("전송 실패");
    }
  };

  //이전 채팅 불러오기
  const getOldMsg = () => {
    customAxios()
      .get(`studies/${studyId}/chats?startChatId=${msgList[0].chat_id}`)
      .then(({ data }) => {
        if (data.length < 100) {
          setOldMsgState((prev) => false);
        }
        const oldMessage = data;
        setMsgList((arr) => [...oldMessage, ...arr]);
        scrollRef.current.scrollTop = 100;
      });
  };

  //메시지 입력 값 변경
  const chgMsg = (e) => {
    setMsg(e.target.value);
  };

  //메시지가 유효한지
  const checkSendState = () => {
    if (msg.length > maxLength) {
      console.log(`입력가능한 최대 글자 수는 ${maxLength}자 입니다.`);
    } else if (msg) {
      sendMsg();
    }
  };

  //마지막으로 스크롤이동
  const moveEnd = () => {
    scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
  };

  //스크롤 이동에 따른 이벤트
  const handleScroll = () => {
    let chatAreaHeight = parseInt(
      window
        .getComputedStyle(scrollRef.current)
        .getPropertyValue("height")
        .replace(/[^0-9]/g, "")
    );
    if (
      Math.abs(
        scrollRef.current.scrollHeight -
          chatAreaHeight -
          scrollRef.current.scrollTop
      ) < 100
    ) {
      if (newMsgState) {
        setNewMsgState((prev) => false);
      }
    }
  };

  //처음 접속 시
  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/chats`) //로그인 되면 수정
      .then(({ data }) => {
        if (data.length < 100) setOldMsgState((prev) => false);
        const oldMessage = data;
        setMsgList((arr) => [...oldMessage, ...arr]);
      });
    stompClient.connect({}, () => {
      stompClient.subscribe("/topic/" + studyId, (data) => {
        const newMessage = JSON.parse(data.body);
        setMsgList((arr) => [...arr, newMessage]);

        //현재 영역 크기
        let chatAreaHeight = parseInt(
          window
            .getComputedStyle(scrollRef.current)
            .getPropertyValue("height")
            .replace(/[^0-9]/g, "")
        );
        if (
          Math.abs(
            scrollRef.current.scrollHeight -
              chatAreaHeight -
              scrollRef.current.scrollTop
          ) >= 100
        ) {
          setNewMsgState((prev) => true);
        }
      });
    });
    return () => {
      stompClient.disconnect();
    };
  }, []);

  //새로운(,이전) 채팅을 불러왔을 때
  useEffect(() => {
    if (newPage < 2) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
      setNewPage((prev) => prev + 1);
    }
    //현재 영역 크기
    let chatAreaHeight = parseInt(
      window
        .getComputedStyle(scrollRef.current)
        .getPropertyValue("height")
        .replace(/[^0-9]/g, "")
    );

    //스크롤이 높이 있는지
    if (
      Math.abs(
        scrollRef.current.scrollHeight -
          chatAreaHeight -
          scrollRef.current.scrollTop
      ) < 100
    ) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [msgList]);

  return (
    <Container>
      <ChatArea ref={scrollRef} onScroll={handleScroll}>
        {oldMsgState && (
          <ShowOldBox onClick={getOldMsg}>
            <BiCaretUp></BiCaretUp>
          </ShowOldBox>
        )}
        {msgList.map((m, i) => {
          return (
            <ChatBox key={m.chat_id}>
              <ChatProfile>
                <UserProfile
                  nickname={m.user.nickname}
                  backgroundcolor={"red"}
                  characterimg={"cow"}
                />
              </ChatProfile>
              <ChatText>{m.content}</ChatText>
              <ChatTime>{m.created_at}</ChatTime>
            </ChatBox>
          );
        })}
      </ChatArea>
      {newMsgState && (
        <NewMessageAlram onClick={moveEnd}>
          새로운 메시지
          <BiCaretDown></BiCaretDown>
        </NewMessageAlram>
      )}
      <MessageInputBox
        type="text"
        onKeyUp={(e) => {
          if (e.key === "Enter") checkSendState();
        }}
        onChange={chgMsg}
        value={msg}
      ></MessageInputBox>
      <SendButton onClick={checkSendState}>전송</SendButton>
    </Container>
  );
}
