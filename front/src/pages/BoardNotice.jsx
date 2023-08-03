import { styled } from "styled-components";
import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";

//공지글
const data = [
  {
    article_id: 1,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 2,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 3,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 4,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 5,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 6,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 7,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 8,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 9,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 10,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 11,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 12,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 13,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 14,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 15,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
  {
    article_id: 16,
    title: "공지입니다",
    created_at: "2023-07-10 10:00:00",
  },
];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default function BoardNotice() {
  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} width={1000} type={"notice"}/>
    </Container>
  );
}
