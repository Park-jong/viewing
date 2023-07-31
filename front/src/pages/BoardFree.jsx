import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import styled from "styled-components";

//자유게시판
const data = [
  {
    author: {
      id: 1,
      nickname: "배고파요",
      hat: "모자",
      character: "cow",
      background: "#ff6767",
    },
    article_id: 123,
    title: "제목입니다",
    view_count: 123,
    comment_count: 123,
    like_count: 25,
  },
  {
    author: {
      id: 1,
      nickname: "배고파요",
      hat: "모자",
      character: "cow",
      background: "#ff6767",
    },
    article_id: 123,
    title: "제목입니다",
    view_count: 123,
    comment_count: 123,
    like_count: 25,
  },
];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default function BoardFree() {
  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} />
    </Container>
  );
}
