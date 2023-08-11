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

export default function MypageArticles() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [free, setFree] = useState([]);
  const [interview, setInterview] = useState([]);
  const [qna, setQna] = useState([]);
  const type = useLocation().pathname.split("/")[2];

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
      .get(`members/${member_id}/article?board=general&searchType=${query()}`)
      .then((res) => setFree(res.data))
      .catch((err) => console.log(err));

    customAxios()
      .get(`members/${member_id}/article?board=review&searchType=${query()}`)
      .then((res) => setInterview(res.data))
      .catch((err) => console.log(err));

    customAxios()
      .get(`members/${member_id}/article?board=qna&searchType=${query()}`)
      .then((res) => setQna(res.data))
      .catch((err) => console.log(err));
  }, [type]);

  return (
    <>
      <Title>자유게시판</Title>
      <ArticleList data={free} width={800} type={"free"}></ArticleList>
      <Title>면접게시판</Title>
      <ArticleList data={interview} width={800} type={"interview"}></ArticleList>
      <Title>질문게시판</Title>
      <ArticleList data={qna} width={800} type={"question"}></ArticleList>
    </>
  );
}
