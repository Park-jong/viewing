import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import Alarm from "../../Icons/Alarm";
import { Link, useNavigate } from "react-router-dom";
import { PiBellThin } from "react-icons/pi";
import { useDispatch, useSelector } from "react-redux";
import { UserReducer } from "./../../modules/UserReducer/UserReducer";
import { EventSourcePolyfill, NativeEventSource } from "event-source-polyfill";
import {
  GetAllAlarm,
  GetOneAlarm,
  HandleRead,
} from "./../../modules/UserReducer/Actions";
import { customAxios } from "../../modules/Other/Axios/customAxios";

const Container = styled.div`
  cursor: pointer;

  /* background-color: white; */
`;

const AlarmIcon = styled.div`
  position: relative;
  width: 24px;
  height: 24px;
  cursor: pointer;
`;

const DropdownMenu = styled.div`
  position: absolute;
  right: 65px;
  top: 64px;
  width: 400px;
  background-color: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: ${(props) => (props.open ? "block" : "none")};

  overflow-y: auto; /* Make the menu scrollable vertically */
  overflow-x: hidden; /* Hide the horizontal scrollbar */
  max-height: 400px; /* Set a max height to limit the menu height */
  z-index: 99;
`;

const CardContainer = styled.div`
  width: 400px;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: ${(props) =>
    props.$isRead ? "var(--gray-200)" : "var(--gray-50)"};
  border: 1px var(--gray-200) solid;
  text-decoration: none;
  z-index: 999;

  overflow-wrap: break-word; /* Prevent div from getting cut off */

  /* padding: 0 px; */

  &:hover {
    background-color: var(--gray-200);
  }
`;

const CardTitle = styled.div`
  color: ${(props) => (props.$isRead ? "var(--gray-400)" : "var(--gray-700)")};
  font-weight: 500;
  font-size: 14px;
  word-wrap: break-word; /* Prevent text truncation and allow word wrapping */
`;

const CardDate = styled.div`
  color: var(--gray-400);
  font-weight: 300;
  font-size: 12px;
`;

const ButtonContainer = styled.div`
  width: 400px;

  display: flex;
  justify-content: center;
  align-items: center;

  color: var(--primary);

  font-weight: 500;
  font-size: 12px;
  padding: 10px 0;
  cursor: pointer;
  &:hover {
    background-color: #f0f0f0;
  }
`;

const BellIcon = styled(PiBellThin)`
  margin-right: 4px;
`;

const Dot = styled.div`
  position: absolute;
  top: 12px;
  left: 13px;
  height: 8px;
  width: 8px;
  background-color: #f87171;
  border-radius: 50%;
`;

export default function DropAlert() {
  const data = useSelector((state) => state.UserReducer.alarms);
  const [menuOpen, setMenuOpen] = useState(false);
  const [showItems, setShowItems] = useState(4); // Number of items to show initially
  const AlertRef = useRef(null);
  const SERVER = process.env.REACT_APP_SERVER_URL;
  const memberId = useSelector((state) => state.UserReducer.memberId);
  const token = localStorage.getItem("access_token");
  const dispatch = useDispatch();
  const navigate = useNavigate();
  let read = false;

  // SSE

  const EventSource = EventSourcePolyfill;

  const handleAlarmClick = () => {
    setMenuOpen(!menuOpen);
  };

  const handleClickOutside = (event) => {
    if (AlertRef.current && !AlertRef.current.contains(event.target)) {
      setMenuOpen(false);
    }
  };

  useEffect(() => {
    if(token==null) return;
    let eventSource;
    const fetchSse = () => {
      eventSource = new EventSource(
        SERVER + `members/${memberId}/notification`,
        {
          headers: {
            Authorization: "Bearer " + token,
          },
          heartbeatTimeout: 12000000,
        }
      );
      eventSource.addEventListener("test", (event) => {
        console.log(event);
      });

      eventSource.addEventListener("lastNotification", (event) => {
        try {
          dispatch(GetAllAlarm(JSON.parse(event.data).notificationDtoList));
        } catch (e) {
          console.log(e);
        }
      });

      eventSource.addEventListener("notification", (event) => {
        try {
          const temp = JSON.parse(event.data);
          temp.isRead = false;
          dispatch(GetOneAlarm(temp));
        } catch (e) {
          console.log(e);
        }
      });

      eventSource.onerror = (event) => {
        console.error(event);

        // 연결이 닫혔다면 재연결 시도
        if (eventSource.readyState === EventSource.CLOSED) {
          console.log("알림 SSE 재연결 시도...");
          setTimeout(fetchSse, 5000); // 5초 후에 재연결 시도
        }
      };
    };
    fetchSse();

    window.addEventListener("click", handleClickOutside);
    return () => {
      window.removeEventListener("click", handleClickOutside);
      // Reset showItems to 4 when the dropdown is closed
      setShowItems(4);
      eventSource.close();
    };
  }, []);

  if (data.length > 0) {
    for (let alarm of data) {
      if (alarm.isRead === false) {
        read = true;
        break;
      }
    }
  }

  const sortedAlertList = [...data].sort((a, b) =>
    a.is_read === b.is_read ? 0 : a.is_read ? 1 : -1
  );

  const handleShowMore = (event) => {
    // Stop the event propagation to prevent dropdown from closing

    event.stopPropagation();
    // Show 2 more items when the "더보기" (More) button is clicked
    setShowItems((prevShowItems) => prevShowItems + 4);
  };

  const handleRead = (alert) => {
    customAxios()
      .put(`members/${memberId}/notification/${alert.notificationId}`)
      .then((res) => {
        dispatch(HandleRead(alert));
        switch (alert.notificationType) {
          case "Message":
            navigate("mypage/get");
            return;
          case "Approve":
            return;
          case "Apply":
            return;
          case "StudyArticle":
            return;
          case "StudyMeeting":
            return;
          case "StudyComment":
            return;
          case "StudyReply":
            return;
          case "BoardComment":
            return;
          case "BoardReply":
            return;
          case "Leader":
            return;
          case "StudyCalendar":
            return;
          case "Comment":
            return;
          case "Reply":
            return;
          default:
            return;
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <Container ref={AlertRef}>
      <AlarmIcon onClick={handleAlarmClick}>
        <BellIcon size={24} />
        {read ? <Dot></Dot> : <></>}
      </AlarmIcon>
      <DropdownMenu open={menuOpen}>
        {sortedAlertList.slice(0, showItems).map((alert, index) => (
          <CardContainer
            onClick={() => handleRead(alert)}
            key={index}
            $isRead={alert.isRead}
          >
            <CardTitle $isRead={alert.isRead}>{alert.content}</CardTitle>
            <br />
            <CardDate $isRead={alert.isRead}>{alert.createdAt}</CardDate>
          </CardContainer>
        ))}
        {sortedAlertList.length > showItems && (
          <ButtonContainer onClick={handleShowMore}>더보기</ButtonContainer>
        )}
      </DropdownMenu>
    </Container>
  );
}
