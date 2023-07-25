import styled from "styled-components";
import { Link } from "react-router-dom";

const Container = styled.div`
  width: 300px;
  height: 150px;
  border-radius: 17px;
  background: rgba(255, 255, 255, 0.76);
  /* hard */
  box-shadow: 4px 4px 5px 0px rgba(0, 0, 0, 0.5);

  padding: 10px;

  display: flex;
  flex-direction: column;
  align-items: center;
`;

const TitleContainer = styled.div`
  font-weight: 500;
  font-size: 13px;
  color: var(--gray-700);
`;

const DateContainer = styled.div`
  font-weight: 300;
  font-size: 11px;
  color: var(--gray-400);
`;

const BodyContainer = styled.div`
  width: 100%;

  display: flex;
  justify-content: space-around;

  margin-top: 12px;
  font-size: 20px;

  line-height: 80%;
`;

const RankContainer = styled(Link)`
  font-size: 12px;
  color: var(--gray-700);
  margin-bottom: 6px;

  text-decoration: none; /* Remove the underline */

  display: block;

  // padding-right: 10px; /* Add padding to create space between rank number and study name */
`;

const FirstContainer = styled.div``;

const SecondContainer = styled.div``;

export default function Hot10Box() {
  // Sample data for rankings
  const rankings = [
    "삼성전자",
    "카카오",
    "네이버",
    "멀캠",
    "싸피",
    "애플",
    "동원",
    "AWS",
    "Intel",
    "아모레",
  ];

  return (
    <Container>
      <TitleContainer>실시간 인기 스터디</TitleContainer>
      <DateContainer>2020.02.02 기준</DateContainer>
      <BodyContainer>
        <FirstContainer>
          {rankings.slice(0, 5).map((study, index) => (
            <RankContainer
              to={`/search?appliedCompany=${study}&job=&careerLevel=option1`}
              key={index}
            >
              {`${index + 1}`}
              &nbsp;&nbsp;&nbsp; {study}
            </RankContainer>
          ))}
        </FirstContainer>
        <SecondContainer>
          {rankings.slice(5, 10).map((study, index) => (
            <RankContainer
              to={`/search?appliedCompany=${study}&job=&careerLevel=option1`}
              key={index}
            >
              {`${index + 6}`} &nbsp;&nbsp;&nbsp; {study}
            </RankContainer>
          ))}
        </SecondContainer>
      </BodyContainer>
    </Container>
  );
}
