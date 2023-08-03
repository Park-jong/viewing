//얘 일단 없이 해보기

// import React from "react";
// import styled from "styled-components";
// import { Link } from "react-router-dom";

// //공지글
// const sampleData1 = [
//   {
//     article_id: 123,
//     title: "공지입니다",
//     created_at: "2023-07-10 10:00:00",
//   },
//   {
//     article_id: 123,
//     title: "공지입니다",
//     created_at: "2023-07-10 10:00:00",
//   },
//   {
//     article_id: 123,
//     title: "공지입니다",
//     created_at: "2023-07-10 10:00:00",
//   },
//   {
//     article_id: 123,
//     title: "공지입니다",
//     created_at: "2023-07-10 10:00:00",
//   },
// ];

// //질문글
// const sampleData2 = [
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "rabbit",
//       background: "green",
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
//       character: "rabbit",
//       background: "green",
//     },
//     article_id: 123,
//     title: "제목입니다",
//     view_count: 123,
//     comment_count: 123,
//     like_count: 25,
//   },
// ];

// //자유게시판 글
// const sampledata3 = [
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "rabbit",
//       background: "green",
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
//       character: "rabbit",
//       background: "green",
//     },
//     article_id: 123,
//     title: "제목입니다",
//     view_count: 123,
//     comment_count: 123,
//     like_count: 25,
//   },
// ];

// //면접후기 글
// const sampledata4 = [
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "rabbit",
//       background: "green",
//     },
//     article_id: 221,
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
//       character: "rabbit",
//       background: "green",
//     },
//     article_id: 123,
//     title: "제목입니다",
//     view_count: 123,
//     comment_count: 123,
//     like_count: 25,
//   },
// ];

// const Container = styled.div`
//   width: 450px;
//   height: 250px;
//   background-color: #f7f7f7;
//   padding: 20px;
//   border-radius: 8px;
//   box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
//   margin-bottom: 20px;
// `;

// const TopContainer = styled.div`
//   display: flex;
//   justify-content: space-between;
//   align-items: center;
//   margin-bottom: 10px;
// `;

// const TitleContainer = styled.h1`
//   font-size: 24px;
//   margin: 0;
//   color: #333;
// `;

// const MoreContainer = styled(Link)`
//   font-size: 16px;
//   color: #007bff;
//   text-decoration: none;

//   &:hover {
//     text-decoration: underline;
//   }
// `;

// const ArticleContainer = styled.div`
//   padding: 10px;
//   margin: 10px;
//   width: 300px;
//   background-color: #fff;
//   border-radius: 4px;
//   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
// `;

// const ArticleTitle = styled.h2`
//   font-size: 20px;
//   margin: 0;
//   color: #333;
// `;

// export default function BoardCard() {
//   return (
//     <Container>
//       <TopContainer>
//         <TitleContainer>공지사항</TitleContainer>
//         <MoreContainer to={"/"}>더보기</MoreContainer>
//       </TopContainer>
//       {sampleData1.map((item) => (
//         <ArticleContainer key={item.article_id}>
//           <ArticleTitle>{item.title}</ArticleTitle>
//         </ArticleContainer>
//       ))}
//     </Container>
//   );
// }
