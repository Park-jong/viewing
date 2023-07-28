import React from "react";
import styled from "styled-components";
import { BiBullseye, BiCommentDetail, BiHeart } from "react-icons/bi";
import UserProfile from "../Common/UserProfile";
import { useLocation, useParams } from "react-router-dom";
import { PiSirenLight } from "react-icons/pi";

// import { data } from "../Layout/db";
import { data } from "./../Layout/db";

const ArticleContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid var(--gray-100);
  /* background-color: #f2f2f2; */
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
`;

const BoardType = styled.div`
  font-size: 14px;
  color: #666666;
`;

const Title = styled.div`
  font-size: 28px;
  font-weight: bold;
  margin: 30px 0px;
`;

const AuthorDateContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const AuthorInfo = styled.div``;

const CountDateContainer = styled.div`
  font-size: 12px;
  font-weight: 300;
  color: #b8b8b8;

  display: flex;

  align-items: center;
`;

const DateInfo = styled.div``;

const Content = styled.div`
  font-size: 16px;
  line-height: 1.6;
  color: #333;
`;

const HorizontalLine = styled.hr`
  border: 0;
  border-top: 1px solid var(--gray-100);
  margin: 20px 0;
`;

const CountInfo = styled.div`
  font-size: 14px;
  color: #888;
  margin-top: 10px;
`;

const IconWrapper = styled.span`
  display: inline-flex;
  align-items: center;
  margin-right: 10px;

  font-weight: 300;
`;

const BottomContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

export default function ArticleDetail({ data }) {
  // const boardType = useParams(); // Extract the boardType from the URL
  // console.log(boardType);

  const location = useLocation();

  const url = location.pathname;
  let boardType = "";
  const pattern = /\/board\/(\w+)\/\d+/;
  const matches = url.match(pattern);
  if (matches) {
    boardType = matches[1];
    // console.log(boardType); // "free"가 콘솔에 출력됩니다.
  }

  // Map the boardType to the appropriate display text
  const boardTypeText = (boardType) => {
    // console.log(boardType, 1)
    switch (boardType) {
      case "free":
        return "자유게시판";
      case "notice":
        return "공지사항";
      case "question":
        return "질문게시판";
      case "interview":
        return "면접 후기";
    }
  };

  // Assuming we have only one data in the data array for this example
  // const data = data[0];

  return (
    <ArticleContainer>
      <BoardType>
        <span>{boardTypeText(boardType)}</span>
      </BoardType>
      <Title>{data.title}</Title>
      <AuthorDateContainer>
        <AuthorInfo>
          <UserProfile
            backgroundcolor={data.author.background}
            characterimg={data.author.character}
            nickname={data.author.nickname}
          />
        </AuthorInfo>
        <CountDateContainer>
          <IconWrapper>
            <BiBullseye size={16} />
            <span> {data.view_count}</span>
          </IconWrapper>
          <DateInfo>
            {" "}
            &nbsp;작성일
            {data.created_at}
          </DateInfo>
        </CountDateContainer>
      </AuthorDateContainer>

      <HorizontalLine />

      <Content>{data.content}</Content>
      <HorizontalLine />

      <BottomContainer>
        <CountInfo>
          <IconWrapper>
            <BiCommentDetail size={16} />
            <span> &nbsp; {data.comment_count}</span>
          </IconWrapper>
          <IconWrapper>
            <BiHeart size={16} />
            <span> &nbsp; {data.like_count}</span>
          </IconWrapper>
        </CountInfo>

        <IconWrapper style={{ color: " #888" }}>
          <PiSirenLight size={16} />

          <span style={{ fontSize: "12px" }}> &nbsp;신고하기</span>
        </IconWrapper>
      </BottomContainer>
    </ArticleContainer>
  );
}
