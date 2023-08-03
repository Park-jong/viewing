import React, { useState } from "react";
import styled from "styled-components";
import { BiLike, BiCommentDetail } from "react-icons/bi";
import UserProfile from "../Common/UserProfile";
import { css } from "styled-components";
import ReplyInput from "./ReplyInput";

// const Container = styled.div`
//   width: 800px;
//   height: fit-content;
//   display: flex;
//   justify-content: center;
//   align-items: center;
// `

const ReplyContainer = styled.div`
  width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 0.5px solid var(--gray-100);
  /* 
  &:last-child {
    border-bottom: none;
  } */
`;

const UserStyled = styled.div`
  ${(props) =>
    props.isNestedReply &&
    css`
      margin-left: 40px;
    `}
`;

const Content = styled.div`
  flex-grow: 1;
  margin: 20px 10px;
  font-size: 14px;
  color: var(--gray-600);

  ${(props) =>
    props.isNestedReply &&
    css`
      margin-left: 50px;
    `}
`;

const BottomContainer = styled.div`
  display: flex;
  align-items: center;

  color: var(--gray-400);
  font-size: 12px;

  ${(props) =>
    props.isNestedReply &&
    css`
      margin-left: 40px;
    `}
`;

const LikeCount = styled.div`
  display: flex;
  align-items: center;
  margin-right: 10px;
`;

const LikeIcon = styled(BiLike)`
  margin-right: 4px;
`;

const CommentIcon = styled(BiCommentDetail)`
  margin-right: 4px;
`;

const CreatedAt = styled.div`
  margin-right: 10px;
`;

const ReplyCount = styled.div`
  display: flex;
  align-items: center;
  margin-right: 10px;
`;

export default function ReplyBox({
  comment_id,
  nickname,
  character,
  background,
  content,
  created_at,
  like_count,
  reply_count,
  isDelete,
  isNestedReply,
}) {
  const [showInput, setShowInput] = useState(false); // Use parentheses instead of square brackets
  const toggleShowInput = () => setShowInput(!showInput);

  return (
    <>
      <ReplyContainer isNestedReply={isNestedReply}>
        <UserStyled isNestedReply={isNestedReply}>
          <UserProfile
            backgroundcolor={background}
            characterimg={character}
            nickname={nickname}
          />
        </UserStyled>
        <Content isNestedReply={isNestedReply}>{content}</Content>

        <BottomContainer isNestedReply={isNestedReply}>
          <CreatedAt> {created_at}</CreatedAt>
          <LikeCount>
            <LikeIcon size={16} />
            {like_count}
          </LikeCount>
          {!isNestedReply && (
            <ReplyCount onClick={toggleShowInput}>
              <CommentIcon size={16} />
              {reply_count}
            </ReplyCount>
          )}
        </BottomContainer>
        {showInput && <ReplyInput />}
      </ReplyContainer>
    </>
  );
}
