import styled from "styled-components";
import MainButton from "./../components/Button/MainButton";
import SubButton from "./../components/Button/SubButton";
import { useParams } from "react-router";
import { useState, useEffect } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import UserProfile from "../components/Common/UserProfile";
import { BiUser } from "react-icons/bi";

const studyData = {
  study_id: 1,
  title: "스터디 제목입니다!",
  description: "스터디 설명입니다.",
  applied_company: "삼성",
  applied_job: "개발",
  capacity: 6,
  head_count: 4,
  created_at: "2023-07-26T15:17",
  deadline: "2023-07-26",
  recruitment: true,
  leader: {
    member_id: null,
    nickname: "지우",
    background: "red",
    character: "cow",
  },
  career_level: "ALL",
  tags: ["tag1", "tag2", "tag3"],
};

const Container = styled.div``;

const CompanyContainer = styled.div`
  display: flex;
  margin-top: 30px;
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

const Title = styled.div`
  font-size: 36px;
  font-weight: 700;

  margin-top: 15px;
`;

const DateContainer = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: center;
`;

const Leader = styled.div`
  margin-top: 15px;
`;

const Date = styled.div`
  color: var(--gray-400);
  font-weight: 300;

  margin-top: 10px;
`;

const CapacityContainer = styled.div`
  display: flex;
  align-items: center;

  color: var(--gray-400);
  font-weight: 300;
`;

const Capacity = styled.div``;

const Tag = styled.div``;

const Description = styled.div`
  margin: 30px 0px;
`;

export default function StudyPkDetail() {
  // const [studyData, SetstudyData] = useState([]);

  // const studyPk = useParams().studyPk;

  // console.log(studyPk);

  // useEffect(() => {
  //   customAxios()
  //     .get(`studies/${studyPk}`)
  //     .then((res) => {
  //       SetstudyData(res.data);
  //     });
  // }, []);

  return (
    <Container>
      <CompanyContainer>
        <CompanyTag>{studyData.applied_company}</CompanyTag>
        <PositionTag>{studyData.applied_job}</PositionTag>
        <CareerTag>{studyData.career_level}</CareerTag>
      </CompanyContainer>
      <Title>{studyData.title}</Title>
      <Leader>
        <UserProfile
          nickname={studyData.leader.nickname}
          backgroundcolor={studyData.leader.background}
          characterimg={studyData.leader.character}
        />
      </Leader>
      <DateContainer>
        <Date>
          마감날짜 {"   "}
          {studyData.deadline}
        </Date>
        <CapacityContainer>
          <BiUser style={{ marginRight: "5px" }} />
          {"   "}
          {studyData.head_count}/{studyData.capacity}
        </CapacityContainer>
      </DateContainer>

      <Tag>
        {studyData.tags.map((tag, idx) => {
          <Tag></Tag>;
        })}
      </Tag>
      <Description>{studyData.description}</Description>
      <MainButton />
      <SubButton />
    </Container>
  );
}
