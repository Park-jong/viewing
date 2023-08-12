import styled from "styled-components";
import React from "react";
import ReplyBox from "../ReplyBox";
import ReplyInput from "../ReplyInput";
import { useLocation } from "react-router-dom";

const Container = styled.div``;

export default function ReplyList({ reply, setReply }) {
  const param = useLocation().pathname.split("/")[1];
  const isStudy = param === "study" ? true : false;

  return (
    <>
      <Container>
        {isStudy ? (
          <>
            {reply.map((comment, idx) => (
              <React.Fragment key={idx}>
                <ReplyBox
                  comment_id={comment.comment_id}
                  author={comment.author.member_id}
                  nickname={comment.author.nickname}
                  character={comment.author.member_profile_image}
                  background={comment.author.member_profile_background}
                  content={comment.content}
                  created_at={comment.created_at}
                  updated_at={comment.updated_at}
                  reply_count={comment.comment_count}
                  isDelete={comment.is_delete}
                  isNestedReply={false}
                  setReply={setReply}
                  isStudyBoard={isStudy}
                />
                {comment.replies.map((reply, replyIdx) => (
                  <ReplyBox
                    key={replyIdx}
                    comment_id={reply.comment_id}
                    nickname={reply.author.nickname}
                    author={reply.author.member_id}
                    character={reply.author.member_profile_image}
                    background={reply.author.member_profile_background}
                    content={reply.content}
                    created_at={reply.created_at}
                    updated_at={reply.updated_at}
                    isDelete={reply.is_delete}
                    isNestedReply={true}
                    setReply={setReply}
                    isStudyBoard={isStudy}
                  />
                ))}
              </React.Fragment>
            ))}
          </>
        ) : (
          <>
            {reply.map((comment, idx) => (
              <React.Fragment key={idx}>
                <ReplyBox
                  comment_id={comment.comment_id}
                  author={comment.author.member_id}
                  nickname={comment.author.nickname}
                  character={comment.author.member_profile_image}
                  background={comment.author.member_profile_background}
                  content={comment.content}
                  created_at={comment.created_at}
                  updated_at={comment.updated_at}
                  like_count={comment.like_count}
                  reply_count={comment.comment_count}
                  isLike={comment.is_like}
                  isDelete={comment.is_delete}
                  isNestedReply={false}
                  setReply={setReply}
                  isStudyBoard={isStudy}
                />
                {comment.replies.map((reply, replyIdx) => (
                  <ReplyBox
                    key={replyIdx}
                    comment_id={reply.comment_id}
                    nickname={reply.author.nickname}
                    author={reply.author.member_id}
                    character={reply.author.member_profile_image}
                    background={reply.author.member_profile_background}
                    content={reply.content}
                    created_at={reply.created_at}
                    updated_at={reply.updated_at}
                    like_count={reply.like_count}
                    isDelete={reply.is_delete}
                    isLike={reply.is_like}
                    isNestedReply={true}
                    setReply={setReply}
                    isStudyBoard={isStudy}
                  />
                ))}
              </React.Fragment>
            ))}
          </>
        )}
        <ReplyInput setReply={setReply}></ReplyInput>
      </Container>
    </>
  );
}
