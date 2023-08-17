import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import Bookmark from "../../Icons/bookmark";
import { BiUser } from "react-icons/bi";
import UserProfile from "../Common/UserProfile";
import CompanyJobTag from "./CompanyJobTag";
import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { useNavigate } from "react-router-dom";

const Container = styled(Link)`
  width: 280px;
  height: 320px;
  border-radius: 36px;
  border: 2px solid var(--gray-200);
  position: relative;

  display: flex;
  margin: 8px;

  /* Add transition for smooth scaling on hover */
  transition: transform 0.2s ease;

  /* Apply scaling on hover */
  &:hover {
    transform: scale(1.02);
  }

  cursor: pointer;

  text-decoration: none;
  color: var(--gray-900);
`;

const BookmarkContainer = styled.div`
  position: absolute;
  top: 15px;
  right: 20px;
  /* z-index: 300; */
  cursor: pointer;
`;

const CompanyContainer = styled.div`
  position: absolute;
  /* top: 30px; */
  bottom: 210px;
  left: 5px;

  display: flex;

  font-size: 12px;
  font-weight: 500;

  white-space: nowrap; /* Add this line to prevent text wrapping */
  overflow: hidden; /* Add this line to hide the overflowed text */
  text-overflow: ellipsis; /* Add this line to add ellipsis (...) for overflowed text */

  flex-wrap: wrap; /* Add this line to allow tags to wrap to multiple lines */
`;

const DateContainer = styled.div`
  position: absolute;
  /* top: 50px; */

  font-size: 11px;
  color: var(--gray-400);
  font-weight: 300;

  left: 10px;
  bottom: 190px;
`;

const RecruitContainer = styled.div`
  position: absolute;
  /* top: 50px; */

  font-size: 11px;
  color: var(--gray-400);
  font-weight: 300;

  left: 10px;
  bottom: 260px;
`;

const TitleContainer = styled.div`
  position: absolute;
  top: 140px;
  left: 10px;

  font-size: 16px;
  color: var(--gray-900);
  font-weight: 600;

  line-height: 130%;
`;

const TagContainer = styled.div`
  position: absolute;
  /* top: 30px; */
  top: 185px;
  left: 5px;

  display: flex;

  font-size: 10px;
  font-weight: 300;

  white-space: nowrap; /* Add this line to prevent text wrapping */
  overflow: hidden; /* Add this line to hide the overflowed text */
  text-overflow: ellipsis; /* Add this line to add ellipsis (...) for overflowed text */

  flex-wrap: wrap; /* Add this line to allow tags to wrap to multiple lines */
`;

const TagStyled = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: fit-content;
  height: 20px;
  background-color: var(--gray-100);
  color: var(--primary);

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

const HorizontalLine = styled.div`
  position: absolute;
  bottom: 45px;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
  height: 1px;
  background-color: var(--gray-300);
`;

const ProfileContainer = styled.div`
  position: absolute;
  bottom: 8px;
  left: 20px;
`;

const CapacityContainer = styled.div`
  position: absolute;
  bottom: 15px;
  right: 20px;

  display: flex;
  align-items: center;

  color: var(--gray-400);
  font-weight: 300;
`;

export default function StudyCard({ study }) {
  const memberId = useSelector((state) => state.UserReducer.memberId);
  const navigate = useNavigate();

  const [bookmarked, isBookmarked] = useState();

  useEffect(() => {
    isBookmarked(study.bookmark);
  }, []);

  const moveStudy = () => {
    customAxios()
      .get(`studies/${study.study_id}/member`)
      .then(() => {
        navigate(`/study/${study.study_id}`);
      })
      .catch((error) => {
        navigate(`/study/${study.study_id}/detail`);
      });
  };

  const addBookmark = (e) => {
    e.stopPropagation();
    e.preventDefault();
    customAxios()
      .post(`members/${memberId}/studies/${study.study_id}/bookmark`)
      .then(() => {
        isBookmarked(true);
      })
      .catch(() => {
        alert("이미 찜한 스터디입니다.");
      });
  };

  const removeBookmark = (e) => {
    e.stopPropagation();
    e.preventDefault();
    customAxios()
      .delete(`members/${memberId}/studies/${study.study_id}/bookmark`)
      .then(() => {
        isBookmarked(false);
      })
      .catch(() => {
        alert("아직 찜하지 않은 스터디입니다.");
      });
  };

  switch (study.career_level) {
    case "NEWCOMER":
      study.career_level = "신입";
      break;
    case "INTERN":
      study.career_level = "인턴";
      break;
    case "ALL":
      study.career_level = "무관";
      break;
    case "EXPERIENCED":
      study.career_level = "경력";
      break;
  }

  return (
    <Container
      onClick={() => {
        moveStudy();
      }}
    >
      <BookmarkContainer onClick={bookmarked ? removeBookmark : addBookmark}>
        {bookmarked ? (
          <Bookmark fill={"#453CF8"} />
        ) : (
          <Bookmark fill={"none"} />
        )}
      </BookmarkContainer>

      <CompanyContainer>
        <CompanyJobTag
          company={study.applied_company}
          position={study.applied_job}
          career={study.career_level}
        />
      </CompanyContainer>

      <DateContainer>마감일 | {study.deadline.split(" ")[0]}</DateContainer>

      <RecruitContainer>
        {study.recruitment ? "모집 중" : "모집 마감"}
      </RecruitContainer>

      <TitleContainer>{study.title}</TitleContainer>

      <TagContainer>
        {study.tags.map((tag, idx) => (
          <TagStyled key={idx}>{tag}</TagStyled>
        ))}
      </TagContainer>

      <HorizontalLine></HorizontalLine>

      <ProfileContainer>
        <UserProfile
          nickname={study.leader.nickname}
          backgroundcolor={study.leader.background}
          characterimg={study.leader.character}
          member_id={study.leader.member_id}
        />
      </ProfileContainer>
      <CapacityContainer>
        <BiUser style={{ marginRight: "5px" }} />
        {study.head_count}/{study.capacity}
      </CapacityContainer>
    </Container>
  );
}
