import styled, { css } from "styled-components";
import { PiUsersThreeFill } from "react-icons/pi";
import { BsFillChatFill } from "react-icons/bs";
import { BiSolidNote } from "react-icons/bi";
import { useEffect, useState } from "react";

const Container = styled.div`
  /* border: 1px solid black; */
  display: flex;
  flex-direction: row;
`;

const ButtonContainer = styled.div`
  border: 0.5px solid var(--gray-100);
  display: flex;
  align-items: center;
  justify-content: center;
  height: 30px;
  cursor: pointer;
  /* transition: background-color 0.3s ease, color 0.3s ease; */

  &:hover {
    background-color: var(--secondary);
  }
  ${(props) =>
    props.active &&
    css`
      background-color: var(--primary);
      color: white;
    `}
`;

const ExitContainer = styled.div`
  position: absolute;
  right: 0%;
  /* border: 1px solid black; */
  cursor: pointer;
`;

export default function MeetingTabBar({
  currentOption,
  changeOption,
  toggleSideBar,
}) {
  return (
    <Container>
      <ButtonContainer
        active={currentOption === "member"}
        onClick={() => {
          changeOption("member");
        }}
      >
        &nbsp; <PiUsersThreeFill />
        &nbsp; &nbsp;참여자&nbsp;&nbsp;
      </ButtonContainer>
      <ButtonContainer
        active={currentOption === "chat"}
        onClick={() => {
          changeOption("chat");
        }}
      >
        &nbsp;
        <BsFillChatFill />
        &nbsp;&nbsp;채팅&nbsp;&nbsp;
      </ButtonContainer>
      <ButtonContainer
        active={currentOption === "feedback"}
        onClick={() => {
          changeOption("feedback");
        }}
      >
        &nbsp; <BiSolidNote />
        &nbsp;&nbsp; 피드백 &nbsp;&nbsp;
      </ButtonContainer>
      <ExitContainer
        onClick={() => {
          toggleSideBar(true);
        }}
      >
        X
      </ExitContainer>
    </Container>
  );
}
