import React, { useEffect, useState } from "react";
import styled from "styled-components";
import ArticleDetail from "../components/Board/ArticleDetail";
import ReplyList from "../components/Board/Organisms/ReplyList";
import { useLocation } from "react-router-dom";
import ArticleAxios from "../modules/Other/Axios/ArticleAxios";
import CommentAxios from "../modules/Other/Axios/CommentAxios";

export default function BoardPk() {
  const [data, setData] = useState([]);
  const [reply, setReply] = useState([]);
  const [count, setCount] = useState([]);
  const param = useLocation().pathname.split("/")[3];
  const type = useLocation().pathname.split("/")[2];

  useEffect(() => {
    ArticleAxios(setData, param, type);
    CommentAxios(setReply, param);
  }, []);

  useEffect(() => {
    let rereply = 0;
    reply.forEach((re) => {
      rereply = rereply + re.replies.length;
    });
    setCount(reply.length + rereply);
  }, [reply]);

  return (
    <>
      <ArticleDetail
        data={data}
        setData={setData}
        count={count}
      ></ArticleDetail>
      <ReplyList reply={reply} setReply={setReply}></ReplyList>
    </>
  );
}
