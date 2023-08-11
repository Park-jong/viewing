import { useSelector } from "react-redux";
import ArticleList from "../components/Board/Organisms/ArticleList";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { styled } from "styled-components";
import { useLocation } from "react-router-dom";

const Title = styled.div`
  font-size: 30px;
  margin-bottom: 10px;
  margin-top: 20px;
`;

export default function MypageLike() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [free, setFree] = useState([]);
  const [interview, setInterview] = useState([]);
  const [qna, setQna] = useState([]);

  useEffect(() => {
    customAxios()
      .get(`members/${member_id}/article?board=general&searchType=favor`)
      .then((res) => setFree(res.data))
      .catch((err) => console.log(err));

    customAxios()
      .get(`members/${member_id}/article?board=review&searchType=favor`)
      .then((res) => setInterview(res.data))
      .catch((err) => console.log(err));

    customAxios()
      .get(`members/${member_id}/article?board=qna&searchType=favor`)
      .then((res) => setQna(res.data))
      .catch((err) => console.log(err));
  }, []);

  
  return (
    <>
    
      <Title>자유게시판</Title>
      <ArticleList data={free} width={800}></ArticleList>
      <Title>면접게시판</Title>
      <ArticleList data={interview} width={800}></ArticleList>
      <Title>질문게시판</Title>
      <ArticleList data={qna} width={800}></ArticleList>
    </>
  );
}
