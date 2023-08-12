import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import ReplyList from "../components/Board/Organisms/ReplyList";
import StudyArticleDetail from "../components/Board/StudyArticleDetail";
import { customAxios } from "../modules/Other/Axios/customAxios";

export default function StudyPkBoardPk() {
  const param = useLocation().pathname.split("/")[2]; // studyId
  const article_id = useLocation().pathname.split("/")[4];
  const [res, setRes] = useState([]);
  const [reply, setReply] = useState([]);
  const [count, setCount] = useState([]);

  useEffect(() => {
    customAxios()
      .get(`studies/${param}/boards/${article_id}`)
      .then((response) => {
        // 받아온 응답 데이터를 상태 변수에 저장
        setRes(response.data);
      })
      .catch(console.log("error"));

    customAxios()
      .get(`studies/${param}/boards/${article_id}/comments`)
      .then((response) => {
        setReply(response.data);
      });
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
      <StudyArticleDetail
        data={res}
        setData={setRes}
        count={count}
      ></StudyArticleDetail>
      <ReplyList reply={reply} setReply={setReply}></ReplyList>
    </>
  );
}
