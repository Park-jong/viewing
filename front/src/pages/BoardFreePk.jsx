import React, { useEffect, useState } from "react";
import styled from "styled-components";
import ArticleDetail from "../components/Board/ArticleDetail";
import ReplyList from "../components/Board/Organisms/ReplyList";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useLocation } from "react-router-dom";

//게시글
// const data = {
//   author: {
//     id: 1,
//     nickname: "배고파요",
//     hat: "모자",
//     character: "cow",
//     background: "green",
//   },
//   article_id: 123,
//   created_at: "2023-07-17",
//   updated_at: "2023-07-18",
//   title: "제목입니다",
//   content: "내용입니다내용입니다내용입니다내용입니다내용입니다",
//   view_count: 123,
//   comment_count: 123,
//   like_count: 25,
//   upload_files: [
//     {
//       file_name: "파일명",
//       file_content: "바이너리파일 or 파일 링크",
//       file_type: "docx",
//     },
//   ],
// };

// //댓글
// const reply = [
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "rabbit",
//       background: "green",
//     },
//     comment_id: 123,
//     content: "댓글입니다",
//     like_count: 25,
//     reply_count: 13,
//     created_at: "2023-06-24",
//     updated_at: "2023-06-24",
//     isDelte: false,
//     isLike: false,
//     replies: [
//       {
//         author: {
//           id: 3,
//           nickname: "배고파",
//           hat: "모자",
//           character: "rabbit",
//           background: "green",
//         },
//         comment_id: 123,
//         content: "첫번째 댓글의 대댓글입니다",
//         like_count: 25,
//         created_at: "2023-06-24",
//         updated_at: "2023-06-24",
//         isDelte: false,
//         isLike: false,
//       },
//       {
//         author: {
//           id: 3,
//           nickname: "배고파",
//           hat: "모자",
//           character: "rabbit",
//           background: "green",
//         },
//         comment_id: 123,
//         content: "첫번째 댓글의 대댓글입니다2",
//         like_count: 25,
//         created_at: "2023-06-24",
//         updated_at: "2023-06-24",
//         isDelte: false,
//         isLike: false,
//       },
//     ],
//   },
//   {
//     author: {
//       id: 1,
//       nickname: "배고파요",
//       hat: "모자",
//       character: "rabbit",
//       background: "green",
//     },
//     comment_id: 123,
//     content: "댓글입니다",
//     like_count: 25,
//     reply_count: 13,
//     created_at: "2023-06-24",
//     updated_at: "2025-12-21",
//     isDelte: false,
//     replies: [],
//   },
// ];

export default function BoardFreePk() {
  const [data, setData] = useState([]);
  const [reply, setReply] = useState([]);
  const param = useLocation().pathname.split("/")[3]
  console.log(param)

  useEffect(()=>{
    customAxios().get(`boards/general/${param}`)
    .then((res) => {
      setData(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
    customAxios().get(`boards/${param}/comments`)
    .then((res) => {
      setReply(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  },[])


  return (
    <>
      <ArticleDetail data={data}></ArticleDetail>
      <ReplyList reply={reply}></ReplyList>
    </>
  );
}
