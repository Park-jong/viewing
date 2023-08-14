import React from "react";
import styled from "styled-components";
import UserProfile from "./../Common/UserProfile";
import { Link, useLocation } from "react-router-dom";
import { BiCommentDetail, BiHeart, BiSolidHeart } from "react-icons/bi";

const Container = styled.div`
  width: ${(props) => `${props.$width}px`};
  height: 35px;
  display: flex;
  align-items: center;
  /* background-color: #ffffff; */
  /* border-radius: 8px; */
  position: relative;
  /* border-top: 0.5px solid #e0e0e0; Add a top border */
  border-bottom: 0.5px solid #e0e0e0; /* Add a bottom border */

  padding: 2px 0;
`;

const ProfileContainer = styled.div`
  position: absolute;
  left: 20px;
  font-size: 13px;
`;

const Title = styled(Link)`
  position: absolute;
  left: 170px;
  font-size: 13px;
  margin: 0;
  color: #333;

  text-decoration: none;
`;

const MetaContainer = styled.div`
  display: flex;
  align-items: center;

  position: absolute;
  right: 10px;
`;

const MetaItem = styled.div`
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  margin-right: 24px;

  svg {
    margin-right: 4px;
  }
`;

export default function ArticleBox({
  nickname,
  backgroundcolor,
  character,
  title,
  commentCount,
  heartCount,
  url,
  viewCount,
  width,
  is_like,
  member_id,
}) {
  const location = useLocation();
  const study_id = useLocation().pathname.split("/")[2];
  const isNotice = false;
  const isStudyBoard = location.pathname === `/study/${study_id}/board`;

  return (
    <Container $width={width}>
      <ProfileContainer>
        {isNotice ? (
          <div style={{ fontSize: "13px" }}>관리자</div>
        ) : (
          <UserProfile
            nickname={nickname}
            backgroundcolor={backgroundcolor}
            characterimg={character}
            member_id={member_id}
          />
        )}
      </ProfileContainer>
      <Title to={`${url}`}>{title}</Title>

      <MetaContainer>
        {isStudyBoard ? null : <MetaItem>{viewCount}</MetaItem>}
        <MetaItem>
          <BiCommentDetail size={14} />
          {commentCount}
        </MetaItem>
        {isStudyBoard ? null : (
          <MetaItem>
            {is_like ? <BiSolidHeart size={14} /> : <BiHeart size={14} />}
            {heartCount}
          </MetaItem>
        )}
      </MetaContainer>
    </Container>
  );
}
