import styled from "styled-components";
import ArticleDetail from "../components/Board/ArticleDetail";

const data = {
  article_id: 123,
  created_at: "2023-07-17 10:00:00",
  updated_at: "2023-07-18 13:00:00",
  title: "공지입니다",
  content: "내용입니다",
};

export default function BoardNoticePk() {
  return (
    <>
      <ArticleDetail data={data}></ArticleDetail>
    </>
  );
}
