import React from "react";
import styled from "styled-components";
import {
  BiBullseye,
  BiCommentDetail,
  BiHeart,
  BiSolidHeart,
} from "react-icons/bi";
import UserProfile from "../Common/UserProfile";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import { PiAddressBook, PiAddressBookBold, PiSirenLight } from "react-icons/pi";

// import { data } from "../Layout/db";
//import { data } from "./../Layout/db";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { useSelector } from "react-redux";
import { UserReducer } from "./../../modules/UserReducer/UserReducer";
import ArticleAxios from "../../modules/Other/Axios/ArticleAxios";
import SubButton from "../Button/SubButton";
import { MdAttachFile } from "react-icons/md";

const ArticleContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid var(--gray-100);
  /* background-color: #f2f2f2; */
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
`;
const BoardTopContianer = styled.div`
  display: flex;
  justify-content: space-between;
`;
const BoardType = styled.span`
  font-size: 14px;
  color: #666666;
`;
const BoardListButton = styled.span`
  margin-right: 10px;
`;
const Title = styled.div`
  font-size: 28px;
  font-weight: bold;
  margin: 15px 0px;
  word-break: break-all;
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
  white-space: pre-wrap;
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
  cursor: pointer;
`;

const BottomContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const ButtonContainer = styled.div`
  display: flex;
`;
const ButtonContainerContainer = styled.div`
  display: inline-block;
`;

const FileConatainer = styled.div`
  margin-bottom: 20px;
`;

const Icon = styled(MdAttachFile)`
  transform: rotate(45deg);
`;

const FileDetailContainer = styled.div`
  display: flex;
  margin-top: 6px;
`;

const File = styled.a``;

export default function ArticleDetail({ data, setData, count }) {
  // const boardType = useParams(); // Extract the boardType from the URL
  // console.log(boardType);

  const param = useLocation().pathname.split("/")[3];
  const type = useLocation().pathname.split("/")[2];
  const location = useLocation();
  const memberId = useSelector((state) => state.UserReducer.memberId);
  const url = location.pathname;
  let boardType = "";
  const pattern = /\/board\/(\w+)\/\d+/;
  const matches = url.match(pattern);
  const navigate = useNavigate();
  if (matches) {
    boardType = matches[1];
    // console.log(boardType); // "free"가 콘솔에 출력됩니다.
  }

  const board_type = () => {
    if (type === "free") {
      return "general";
    } else if (type === "interview") {
      return "review";
    } else if (type === "question") {
      return "qna";
    }
  };

  const handleLike = (member_id, article_id) => {
    customAxios()
      .post(`members/${member_id}/likes/boards/${article_id}`)
      .then((res) => {
        ArticleAxios(setData, param, type);
      })
      .catch((err) => console.log(err));
  };

  const handleDislike = (member_id, article_id) => {
    customAxios()
      .delete(`members/${member_id}/likes/boards/${article_id}`)
      .then((res) => {
        ArticleAxios(setData, param, type);
      })
      .catch((err) => console.log(err));
  };

  const handleDelete = () => {
    customAxios()
      .delete(`boards/${board_type}/${param}`)
      .then((res) => {
        navigate(`/board/${type}`);
      })
      .catch((err) => console.log(err));
  };

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
      <BoardTopContianer>
        <BoardType>
          <span>{boardTypeText(boardType)}</span>
        </BoardType>
        <BoardListButton>
          <SubButton
            content="목록"
            onClick={() => navigate(`/board/${boardType}`)}
          ></SubButton>
        </BoardListButton>
      </BoardTopContianer>
      <Title>{data.title}</Title>
      <AuthorDateContainer>
        <AuthorInfo>
          {boardTypeText(boardType) === "공지사항" ? (
            <div>관리자</div>
          ) : (
            data.author && ( // Check if data.author exists
              <UserProfile
                backgroundcolor={data.author.member_profile_background}
                characterimg={data.author.member_profile_image}
                nickname={data.author.nickname}
                nicknameLength={500}
                member_id={data.author.member_id}
              />
            )
          )}
        </AuthorInfo>
        <CountDateContainer>
          {boardTypeText(boardType) !== "공지사항" && (
            <IconWrapper>
              <BiBullseye size={16} />
              <span> {data.view_count}</span>
            </IconWrapper>
          )}
          <DateInfo>
            {" "}
            &nbsp;작성일&nbsp;&nbsp;
            {data.created_at}
          </DateInfo>
        </CountDateContainer>
      </AuthorDateContainer>

      <HorizontalLine />
      <Content>{data.content}</Content>
      <br></br>
      {data.article_files && data.article_files.length > 0 ? (
        <>
          <FileConatainer>
            {data.article_files.map((file, idx) => (
              <FileDetailContainer>
                <Icon></Icon>
                <h1>첨부파일 :&nbsp;</h1>
                <File
                  href={
                    process.env.REACT_APP_SERVER_URL +
                    `studies/${param}/boards/${data.article_id}/files/${file.fileId}`
                  }
                  key={idx}
                >
                  {file.name}
                </File>
                <br />
              </FileDetailContainer>
            ))}
          </FileConatainer>
        </>
      ) : (
        <></>
      )}

      {boardTypeText(boardType) !== "공지사항" && <HorizontalLine />}
      {boardTypeText(boardType) !== "공지사항" && (
        <BottomContainer>
          <CountInfo>
            <IconWrapper>
              <BiCommentDetail size={16} />
              <span> &nbsp; {count}</span>
            </IconWrapper>
            <IconWrapper>
              {data.is_like ? (
                <BiSolidHeart
                  onClick={() => handleDislike(memberId, data.article_id)}
                  size={16}
                />
              ) : (
                <BiHeart
                  onClick={() => handleLike(memberId, data.article_id)}
                  size={16}
                />
              )}
              <span> &nbsp; {data.like_count}</span>
            </IconWrapper>
          </CountInfo>

          <IconWrapper style={{ color: " #888" }}>
            {data.author && memberId === data.author.member_id ? (
              <ButtonContainerContainer>
                <ButtonContainer>
                  <SubButton
                    content="수정하기"
                    onClick={() => navigate(`update`)}
                  ></SubButton>
                  <SubButton
                    content="삭제하기"
                    onClick={() => handleDelete()}
                  ></SubButton>
                </ButtonContainer>
              </ButtonContainerContainer>
            ) : (
              <></>
            )}
          </IconWrapper>
        </BottomContainer>
      )}
    </ArticleContainer>
  );
}
