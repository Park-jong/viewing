import styled from "styled-components";
import React, { useRef, useState, useEffect } from "react";
import stompjs from "stompjs";
import * as SockJS from "sockjs-client";
import UserProfile from "../Common/UserProfile";
import { useLocation, useParams } from "react-router-dom";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { BiCaretUp, BiCaretDown } from "react-icons/bi";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

const Container = styled.div`
  width: 1000px;
  height: 500px;
`;

const ChatArea = styled.div`
  width: 90%;
  height: 100%;
  overflow-x: auto;
  word-wrap: break-word;

  margin-bottom: 20px;
`;
const ChatBox = styled.div`
  display: flex;
  align-items: center;
  margin: 15px 0;
`;

const ChatProfile = styled.span`
  margin-right: 10px;
  color: var(--gray-500);
  font-weight: 300;

  font-size: 12px;
`;

const ChatText = styled.div`
  /* flex-grow: conten; */
  width: fit-content;
  padding: 10px;
  background-color: var(--gray-100);
  border-radius: 5px;
  font-size: 13px;
`;

const ChatTime = styled.span`
  font-size: 10px;
  margin-left: 10px;
  margin-right: 10px;

  color: var(--gray-300);
  font-weight: 300;
`;

const SendButton = styled.button`
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 10px;
  padding: 8px 20px;
  font-size: 14px;
  cursor: pointer;

  margin-left: 15px;

  &:focus {
    outline: black;
  }
`;

const MessageInputBox = styled.input`
  width: 80%;
  padding: 10px;
  border: 1px solid var(--gray-300);
  border-radius: 5px;
  font-size: 16px;
  outline: none;

  &:focus {
    border-color: blue;
  }
`;

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
  const member = useSelector((state) => state.UserReducer);
  const [msg, setMsg] = useState("");
  const [msgList, setMsgList] = useState([]);
  const sockJS = new SockJS(`${process.env.REACT_APP_SERVER_URL}studyChat`);
  const stompClient = stompjs.over(sockJS);
  stompClient.debug = null;
  const studyId = useParams().studyPk;
  const [oldMsgState, setOldMsgState] = useState(true);
  const scrollRef = useRef();
  const [newPage, setNewPage] = useState(0);
  const [newMsgState, setNewMsgState] = useState(false);
  const [scrollState, setScrollState] = useState(true);
  const maxLength = 5000;
  const navigate = useNavigate();

  //메시지 전송
  const sendMsg = () => {
    try {
      if (sockJS.readyState === 1) {
        stompClient.send(
          "/app/chats/studies/" + studyId,
          {},
          JSON.stringify({ member_id: member.memberId, content: msg })
        );
        moveEnd();
        setMsg("");
      }
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
      })
      .catch(() => {});
  };

  //메시지 입력 값 변경
  const chgMsg = (e) => {
    setMsg(e.target.value);
  };

  //메시지가 유효한지
  const checkSendState = () => {
    if (msg && msg.length <= maxLength) {
      sendMsg();
    }
  };

  //마지막으로 스크롤이동
  const moveEnd = () => {
    scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    setNewMsgState((prev) => false);
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
      setScrollState(() => true);
    } else {
      setScrollState(() => false);
    }
  };

  //처음 접속 시
  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/chats`)
      .then(({ data }) => {
        if (data.length < 100) setOldMsgState((prev) => false);
        const oldMessage = data;
        setMsgList((arr) => [...oldMessage, ...arr]);
      })
      .catch((err) => {
        stompClient.disconnect();
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
      if (sockJS.readyState === 1) {
        sockJS.close();
      }
      stompClient.unsubscribe("/topic/" + studyId);
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
    if (scrollState) {
      moveEnd();
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
                {m.member.nickname != null ? (
                  <UserProfile
                    nickname={m.member.nickname}
                    backgroundcolor={m.member.background}
                    characterimg={m.member.character}
                  />
                ) : (
                  <UserProfile nickname={"알 수 없음"} />
                )}
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
        maxLength={maxLength}
      ></MessageInputBox>
      <SendButton onClick={checkSendState}>전송</SendButton>
      <div style={{ color: "gray", fontSize: "12px" }}>
        {msg.length}/{maxLength}
      </div>
    </Container>
  );
}
