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
    "123푸드",
    "삼성에스디에스",
    "47컴퍼니",
    "가야미",
    "멀티캠퍼스",
    "삼성물산",
    "삼성생명",
    "주원산업개발",
    "착한사람들컴퍼니",
  ];

  return (
    <Container>
      <TitleContainer>인기 스터디</TitleContainer>
      <BodyContainer>
        <FirstContainer>
          {rankings.slice(0, 5).map((study, index) => (
            <RankContainer
              to={`/search?appliedCompany=${study}&appliedJob=&careerLevel=ALL`}
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
              to={`/search?appliedCompany=${study}&appliedJob=&careerLevel=ALL`}
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
