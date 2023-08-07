import styled from "styled-components";
import MainButton from "./../components/Button/MainButton";
import SubButton from "./../components/Button/SubButton";
import { useParams } from "react-router";
import { useState, useEffect } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import UserProfile from "../components/Common/UserProfile";
import { BiUser } from "react-icons/bi";
import ApplyModal from "../components/Modal/ApplyModal";
import CompanyJobTag from "../components/Study/CompanyJobTag";

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
  padding: 30px;
  border: 1px solid var(--gray-200);
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
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

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <Container>
      <CompanyJobTag
        company={studyData.applied_company}
        position={studyData.applied_job}
        career={studyData.career_level}
      />
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
          {studyData.head_count}/{studyData.capacity}
        </CapacityContainer>
      </DateContainer>
      <Tag>
        {studyData.tags.map((tag, idx) => {
          <Tag>{tag}</Tag>;
        })}
      </Tag>
      <Description>{studyData.description}</Description>

      <ButtonContainer>
        <MainButton
          marginright={10}
          width={300}
          height={45}
          fontSize={16}
          content="신청하기"
          onClick={handleOpenModal}
        />
      </ButtonContainer>

      <ApplyModal
        isModalOpen={isModalOpen}
        onClose={handleCloseModal}
        studyData={studyData}
      />
    </Container>
  );
}
