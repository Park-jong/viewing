import { data } from "./db";
import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import Alarm from "../../Icons/Alarm";
import { Link } from "react-router-dom";
import { PiBellThin } from "react-icons/pi";

const Container = styled.div`
  cursor: pointer;
  
  /* background-color: white; */
`;

const AlarmIcon = styled.div`
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

const CardContainer = styled(Link)`
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

export default function DropAlert() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [showItems, setShowItems] = useState(4); // Number of items to show initially
  const AlertRef = useRef(null);

  const handleAlarmClick = () => {
    setMenuOpen(!menuOpen);
  };

  const handleClickOutside = (event) => {
    if (AlertRef.current && !AlertRef.current.contains(event.target)) {
      setMenuOpen(false);
    }
  };

  useEffect(() => {
    window.addEventListener("click", handleClickOutside);
    return () => {
      window.removeEventListener("click", handleClickOutside);
      // Reset showItems to 4 when the dropdown is closed
      setShowItems(4);
    };
  }, []);

  const sortedAlertList = [...data].sort((a, b) =>
    a.is_read === b.is_read ? 0 : a.is_read ? 1 : -1
  );

  const handleShowMore = (event) => {
    // Stop the event propagation to prevent dropdown from closing

    event.stopPropagation();
    // Show 2 more items when the "더보기" (More) button is clicked
    setShowItems((prevShowItems) => prevShowItems + 4);
  };

  return (
    <Container ref={AlertRef}>
      <AlarmIcon onClick={handleAlarmClick}>
        <BellIcon size={24} />
      </AlarmIcon>
      <DropdownMenu open={menuOpen}>
        {sortedAlertList.slice(0, showItems).map((alert, index) => (
          <CardContainer key={index} $isRead={alert.is_read}>
            <CardTitle $isRead={alert.is_read}>{alert.message}</CardTitle>
            <br />
            <CardDate $isRead={alert.is_read}>{alert.created_at}</CardDate>
          </CardContainer>
        ))}
        {sortedAlertList.length > showItems && (
          <ButtonContainer onClick={handleShowMore}>더보기</ButtonContainer>
        )}
      </DropdownMenu>
    </Container>
  );
}
