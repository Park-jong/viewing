import React from "react";
import { BiBullseye, BiCommentDetail } from "react-icons/bi";
import { PiSirenLight } from "react-icons/pi";
import { useLocation, useNavigate } from "react-router-dom";
import styled from "styled-components";
import UserProfile from "../Common/UserProfile";
import { MdAttachFile } from "react-icons/md";
import { useSelector } from "react-redux";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import SubButton from "../Button/SubButton";

const ArticleContainer = styled.div`
  min-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid var(--gray-100);
  /* background-color: #f2f2f2; */
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
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

const FileDetailContainer = styled.a`
  display: flex;
`;

const File = styled.a``;

export default function StudyArticleDetail({ data, setData, count }) {
  const param = useLocation().pathname.split("/")[2]; // studyId
  const article_id = useLocation().pathname.split("/")[4]; // studyId

  const memberId = useSelector((state) => state.UserReducer.memberId);
  const navigate = useNavigate();

  const handleDelete = () => {
    customAxios()
      .delete(`studies/${param}/boards/${article_id}`)
      .then((res) => {
        navigate(`/study/${param}/board`);
      })
      .catch((err) => console.log(err));
  };

  return (
    <ArticleContainer>
      <Title>{data.title}</Title>
      <AuthorDateContainer>
        <AuthorInfo>
          {data.author && (
            <UserProfile
              backgroundcolor={data.author.member_profile_background}
              characterimg={data.author.member_profile_image}
              nickname={data.author.nickname}
            />
          )}
        </AuthorInfo>
        <CountDateContainer>
          <IconWrapper>
            <BiBullseye size={16} />
            <span> {data.view_count}</span>
          </IconWrapper>

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
              </FileDetailContainer>
            ))}
          </FileConatainer>
        </>
      ) : (
        <></>
      )}
      <HorizontalLine />
      <BottomContainer>
        <CountInfo>
          <IconWrapper>
            <BiCommentDetail size={16} />
            <span> &nbsp; {count}</span>
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
          <PiSirenLight size={16} />
          <span style={{ fontSize: "12px" }}> &nbsp;신고하기</span>
        </IconWrapper>
      </BottomContainer>
    </ArticleContainer>
  );
}
