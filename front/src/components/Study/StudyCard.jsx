import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import Bookmark from "../../Icons/bookmark";
import Capacity from "./../../Icons/capacity";

const Container = styled.div`
  width: 280px;
  height: 320px;
  border-radius: 36px;
  border: 2px solid var(--gray-200);
  position: relative;

  display: flex;

  /* Add transition for smooth scaling on hover */
  transition: transform 0.2s ease;

  /* Apply scaling on hover */
  &:hover {
    transform: scale(1.02);
  }

  cursor: pointer;
`;

const BookmarkContainer = styled.div`
  position: absolute;
  top: 15px;
  right: 20px;
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

const CompanyTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: fit-content;
  height: 25px;
  background-color: rgb(
    145,
    118,
    255
  ); /* Change the alpha value to adjust opacity */

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

const PositionTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: fit-content;
  height: 25px;

  background-color: var(--secondary);

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

const CareerTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: fit-content;
  height: 25px;
  background-color: var(--secondary);

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
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
  top: 200px;
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
  bottom: 15px;
  left: 20px;
`;

const CapacityContainer = styled.div`
  position: absolute;
  bottom: 15px;
  right: 20px;
`;

export default function StudyCard() {
  return (
    <Container>
      <BookmarkContainer>
        <Bookmark />
      </BookmarkContainer>

      <CompanyContainer>
        <CompanyTag>삼삼성전자삼성전자삼성전자</CompanyTag>
        <PositionTag>DX개발</PositionTag>
        <CareerTag>신입</CareerTag>
      </CompanyContainer>

      <DateContainer>마감일 | 2020.02.02</DateContainer>

      <TitleContainer>
        빡세게 준비하실 분 빡세게 준비하실 분 빡세게 준비하실 분 빡세게 준비하실
        분
      </TitleContainer>

      <TagContainer>
        <TagStyled>자소서 제출 필수</TagStyled>
        <TagStyled>압박면접</TagStyled>
        <TagStyled>정보공유</TagStyled>
        <TagStyled>합격 인증 필수</TagStyled>
        <TagStyled>합격 인증 필수</TagStyled>
      </TagContainer>

      <HorizontalLine></HorizontalLine>

      <ProfileContainer>지우</ProfileContainer>
      <CapacityContainer>
        <Capacity />
        6/8
      </CapacityContainer>
    </Container>
  );
}
