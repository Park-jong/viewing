import React, { useState } from "react";
import styled, { css, keyframes } from "styled-components";

const Container = styled.div`
  display: flex;
  align-items: center;
`;

const H1 = styled.h1`
  font-size: 15px;
  font-weight: 700;
  color: var(--gray-800);
`;

const colorAnimation = keyframes`
  from {
    background-color: var(--gray-200);
  }

  to {
    background-color: var(--primary);
  }
`;
const colorAnimation2 = keyframes`
  from {
    background-color: var(--primary);
  }

  to {
    background-color: var(--gray-200);
  }
`;

const ToggleContainer = styled.div`
  width: 40px;
  height: 15px;
  background-color: var(--gray-200);
  display: flex;
  align-items: center;
  justify-content: left;
  cursor: pointer;
  border-radius: 10px;
  overflow: hidden;
  padding: 3px;
  margin-left: 5px;

  ${({ $isToggled }) =>
    $isToggled &&
    css`
      animation: ${colorAnimation} 0.3s linear;
      background-color: var(--primary);
    `}
  ${({ $isToggled }) =>
    !$isToggled &&
    css`
      animation: ${colorAnimation2} 0.3s linear;
      background-color: var(--gray-200);
    `}
`;

const ToggleButton = styled.div`
  width: 15px;
  height: 15px;
  border-radius: 7.5px;
  background-color: var(--gray-50);
  transition: transform 0.3s ease-in-out;

  ${({ $isToggled }) =>
    $isToggled &&
    css`
      transform: translateX(25px);
    `}
`;

const Toggle = ({ onClick, isToggled, setIsToggled}) => {
  // const [isToggled, setIsToggled] = useState(false);

  return (
    <Container>
      <H1 onClick={onClick}>모집 중만 보기</H1>
      <ToggleContainer onClick={onClick} $isToggled={isToggled}>
        <ToggleButton $isToggled={isToggled} />
      </ToggleContainer>
    </Container>
  );
};

export default Toggle;
