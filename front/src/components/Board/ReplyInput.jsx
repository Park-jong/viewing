import React from "react";
import styled from "styled-components";
import { BiLike, BiCommentDetail } from "react-icons/bi";
import UserProfile from "../Common/UserProfile";

const ReplyContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid var(--gray-100);
  //border-radius: 8px;
  display: flex;
  align-items: flex-start;
`;

const InputStyled = styled.textarea`
  flex-grow: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
  resize: vertical;
  /* Higher specificity for the placeholder color */

  color: var(--gary-800);
    font-family: "Pretendard";
    font-size: 13px;

  &::placeholder {
    color: var(--gary-300);
    font-family: "Pretendard";
    font-size: 13px;

    font-weight: 300;
  }
`;

const ButtonReply = styled.button`
  padding: 10px 16px;
  font-size: 12px;
  color: #fff;
  background-color: var(--secondary);
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: var(--primary);
  }
`;

export default function ReplyInput() {
  return (
    <ReplyContainer>
      <InputStyled as="textarea" placeholder="댓글을 입력하세요." />
      <ButtonReply>등록</ButtonReply>
    </ReplyContainer>
  );
}
