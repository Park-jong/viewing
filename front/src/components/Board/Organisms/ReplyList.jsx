import styled from "styled-components";
import React from "react";
import ReplyBox from "../ReplyBox";
import ReplyInput from "../ReplyInput";

export default function ReplyList({ reply }) {
  return (
    <>
      <ReplyInput></ReplyInput>
      {reply.map((comment, idx) => (
        <React.Fragment key={idx}>
          <ReplyBox
            comment_id={comment.comment_id}
            nickname={comment.author.nickname}
            character={comment.author.character}
            background={comment.author.background}
            content={comment.content}
            created_at={comment.created_at}
            updated_at={comment.updated_at}
            like_count={comment.like_count}
            reply_count={comment.reply_count}
            isDelete={comment.isDelete}
            isNestedReply={false}

          />
          {comment.replies.map((reply, replyIdx) => (
            <ReplyBox
              key={replyIdx}
              comment_id={reply.comment_id}
              nickname={reply.author.nickname}
              character={reply.author.character}
              background={reply.author.background}
              content={reply.content}
              created_at={reply.created_at}
              updated_at={reply.updated_at}
              like_count={reply.like_count}
              isDelete={reply.isDelete}
              isNestedReply={true}
            />
          ))}
        </React.Fragment>
      ))}
    </>
  );
}
