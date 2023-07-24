import React, { useState } from 'react';
import styled, { css } from 'styled-components';

const Container = styled.div`
  display: flex;
  align-items: center;
`

const H1 = styled.h1`
  font-size: 25px;
`

const ToggleContainer = styled.div`
  width: 40px;
  height: 15px;
  background-color: var(--gray-100);
  display: flex;
  align-items: center;
  justify-content: left;
  cursor: pointer;
  border-radius: 10px;
  overflow: hidden;
  padding: 3px;
  margin-left: 5px;
`;

const ToggleButton = styled.div`
  width: 15px;
  height: 15px;
  border-radius: 7.5px;
  background-color: var(--primary);
  transition: transform 0.3s ease-in-out;

  ${({ isToggled }) =>
    isToggled &&
    css`
      transform: translateX(25px);
    `}
`;

const Toggle = ({text}) => {
  const [isToggled, setIsToggled] = useState(false);

  const handleToggle = () => {
    setIsToggled(!isToggled);
  };

  return (
    <Container>
      <H1 onClick={handleToggle}>모집 중만 보기</H1>
      <ToggleContainer onClick={handleToggle}>
        <ToggleButton isToggled={isToggled} />
      </ToggleContainer>
    </Container>
  );
};

export default Toggle;