import React, { useEffect, useState } from "react";
import styled from "styled-components";
import ArticleDetail from "../components/Board/ArticleDetail";
import ReplyList from "../components/Board/Organisms/ReplyList";
import { useLocation } from "react-router-dom";
import { customAxios } from "../modules/Other/Axios/customAxios";

export default function BoardPk() {
  const [data, setData] = useState([]);
  const [reply, setReply] = useState([]);
  const param = useLocation().pathname.split("/")[3];
	const type = useLocation().pathname.split("/")[2];

	

  useEffect(() => {
    customAxios()
      .get(`boards/qna/${param}`)
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
    customAxios()
      .get(`boards/${param}/comments`)
      .then((res) => {
        setReply(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <>
      <ArticleDetail data={data} setData={setData}></ArticleDetail>
      <ReplyList reply={reply} setReply={setReply}></ReplyList>
    </>
  );
}
