import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import styled from "styled-components";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";


//면접게시판
// const data = [
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "cow",
//       background: "#ff6767",
//     },
//     article_id: 123,
//     title: "제목입니다",
//     view_count: 123,
//     comment_count: 123,
//     like_count: 25,
//   },
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "cow",
//       background: "#ff6767",
//     },
//     article_id: 123,
//     title: "제목입니다",
//     view_count: 123,
//     comment_count: 123,
//     like_count: 25,
//   },
// ];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default function BoardInterview() {

  const [data, setData] = useState([]);

  useEffect(()=>{
    customAxios().get("boards/review")
    .then((res) => {
      setData(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  }, [])

  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} width={1000} type={"interview"}/>
      <SearchBoxBoard></SearchBoxBoard>
    </Container>
  );
}
