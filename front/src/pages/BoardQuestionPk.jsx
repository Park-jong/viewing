import React from "react";
import styled from "styled-components";
import ArticleDetail from "../components/Board/ArticleDetail";

const data = [
  {
    author: {
      id: 1,
      nickname: "배고파요",
      hat: "모자",
      character: "cow",
      background: "green",
    },
    article_id: 123,
    created_at: "2023-07-17",
    updated_at: "2023-07-18",
    title: "제목입니다",
    content: "내용입니다내용입니다내용입니다내용입니다내용입니다",
    view_count: 123,
    comment_count: 123,
    like_count: 25,
    upload_files: [
      {
        file_name: "파일명",
        file_content: "바이너리파일 or 파일 링크",
        file_type: "docx",
      },
    ],
  },
];

export default function BoardQuestionPk() {
  console.log("Ssss");
  return (
    <>
      <ArticleDetail data={data}></ArticleDetail>
    </>
  );
}
