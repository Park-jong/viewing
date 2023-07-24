import React from "react";
import styled from "styled-components";

const Container = styled.div`
  width: fit-content;
  height: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-200);
  color: var(--gray-800);
  font-weight: 600;
  border-radius: 8px;
  padding: 0 12px;
  font-size: 11px;
  cursor: pointer;
`;

export default function SubButton(props) {
  const { content, onClick } = props;

  return <Container onClick={onClick}>{content}</Container>;
}
