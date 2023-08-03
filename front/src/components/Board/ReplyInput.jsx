import React, { useState } from "react";
import styled from "styled-components";
import { BiLike, BiCommentDetail } from "react-icons/bi";
import UserProfile from "../Common/UserProfile";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { useLocation, useNavigate } from "react-router-dom";

const ReplyContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  /* border: 1px solid var(--gray-100); */
  //border-radius: 8px;
  display: flex;
  align-items: flex-start;

  /* display: block; */
`;

const InputStyled = styled.textarea`
  flex-grow: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
  resize: none;
  height: 16px;
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

export default function ReplyInput({commentId}) {
  const [text, setText] = useState("");
  const param = useLocation().pathname.split("/")[3];
  const navigate = useNavigate()
  const location = useLocation().pathname


  const handleInput = (e) => {
    setText(e.target.value);
  };

  const handleClick = () => {
    const url = commentId ? `boards/${param}/comments/${commentId}/replies` : `boards/${param}/comments`
    customAxios()
      .post(url,{content : text})
      .then((res) => {
        navigate(location)
      });
  };

  return (
    <ReplyContainer>
      <InputStyled
        as="textarea"
        placeholder="댓글을 입력하세요."
        onChange={handleInput}
      />
      <ButtonReply onClick={handleClick}>등록</ButtonReply>
    </ReplyContainer>
  );
}
