import { useSelector } from "react-redux";
import ArticleList from "../components/Board/Organisms/ArticleList";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { styled } from "styled-components";
import { useLocation } from "react-router-dom";
import Pagination from "../components/Common/Pagination";

const Title = styled.div`
  font-size: 30px;
  margin-bottom: 10px;
  margin-top: 50px;
`;

export default function MypageArticles() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [free, setFree] = useState([]);
  const [interview, setInterview] = useState([]);
  const [qna, setQna] = useState([]);
  const type = useLocation().pathname.split("/")[2];

  //페이지네이션 재료
  const [freePage, setFreePage] = useState(0);
  const [interviewPage, setInterviewPage] = useState(0);
  const [qnaPage, setQnaPage] = useState(0);

  const maxFree = parseInt(free.length / 10);
  const maxInterview = parseInt(interview.length / 10);
  const maxQna = parseInt(qna.length / 10);

  useEffect(() => {
    const query = () => {
      if (type === "like") {
        return "favor";
      } else if (type === "myarticle") {
        return "write";
      } else {
        return type;
      }
    };
    customAxios()
      .get(
        `members/${member_id}/article?board=general&searchType=${query()}` +
          `&page=0&size=5&sort=createdAt,desc`
      )
      .then((res) => setFree(res.data))
      .catch((err) => console.log(err));

    customAxios()
      .get(
        `members/${member_id}/` +
          `article?board=review&searchType=${query()}` +
          `&page=0&size=5&sort=createdAt,desc`
      )
      .then((res) => setInterview(res.data))
      .catch((err) => console.log(err));

    customAxios()
      .get(
        `members/${member_id}/article?board=qna&searchType=${query()}` +
          `&page=0&size=5&sort=createdAt,desc`
      )
      .then((res) => setQna(res.data))
      .catch((err) => console.log(err));
  }, [type]);

  return (
    <>
      <Title>자유게시판</Title>
      <ArticleList
        data={free.slice(10 * freePage, 10 * freePage + 10)}
        width={800}
        type={"free"}
      ></ArticleList>
      {free.length === 0 ? (
        <></>
      ) : (
        <Pagination
          page={freePage}
          setPage={setFreePage}
          maxPage={maxFree}
          handleData={() => {}}
        ></Pagination>
      )}

      <Title>면접게시판</Title>
      <ArticleList
        data={interview.slice(10 * interviewPage, 10 * interviewPage + 10)}
        width={800}
        type={"interview"}
      ></ArticleList>
      {interview.length === 0 ? (
        <></>
      ) : (
        <Pagination
          page={interviewPage}
          setPage={setInterviewPage}
          maxPage={maxInterview}
          handleData={() => {}}
        ></Pagination>
      )}
      <Title>질문게시판</Title>
      <ArticleList
        data={qna.slice(10 * qnaPage, 10 * qnaPage + 10)}
        width={800}
        type={"qusetion"}
      ></ArticleList>
      {qna.length === 0 ? (
        <></>
      ) : (
        <Pagination
          page={qnaPage}
          setPage={setQnaPage}
          maxPage={maxQna}
          handleData={() => {}}
        ></Pagination>
      )}
    </>
  );
}
