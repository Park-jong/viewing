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
import TagStyled from "../components/Study/TagStyled";

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

  margin-top: 60px;
  margin-bottom: 30px;
`;

const DetailTitle = styled.div`
  font-size: 24px;
  font-weight: 700;

  margin-bottom: 30px;
`;

const CreatedAt = styled.div`
  font-size: 12px;
`;

const FlexContainer = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: center;
`;

const Description = styled.div`
  margin: 30px 0px;
  padding: 30px 0px;
  /* border: 1px solid var(--gray-200); */
  color: var(--gray-700);
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const HorizontalLine = styled.div`
  width: 100%;
  height: 3px;
  background-color: var(--gray-100);
  margin-bottom: 20px;
`;

const DetailContainer = styled.div`
  display: flex;
  flex-direction: column;

  font-size: 20px;

  margin-bottom: 100px;
`;

const DetailTag = styled.div`
  color: var(--gray-500);
  font-weight: 500;

  display: flex;
  align-items: center;

  margin: 10px 0;
`;

const DetailContent = styled.div`
  color: var(--gray-900);
  font-weight: 500;
  display: flex;
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
      <Title>{studyData.title}</Title>
      <FlexContainer>
        <UserProfile
          nickname={studyData.leader.nickname}
          backgroundcolor={studyData.leader.background}
          characterimg={studyData.leader.character}
        />
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        <CreatedAt>{studyData.created_at}</CreatedAt>
      </FlexContainer>
      <HorizontalLine style={{ marginTop: "20px" }}></HorizontalLine>

      <DetailContainer>
        <DetailTag>
          마감일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <DetailContent>{studyData.deadline}</DetailContent>
        </DetailTag>
        <DetailTag>
          모집인원&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <DetailContent>
            <BiUser style={{ marginRight: "5px" }} />
            {studyData.head_count}/{studyData.capacity}
          </DetailContent>
        </DetailTag>
        <DetailTag>
          회사/직무&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <DetailContent>
            <CompanyJobTag
              company={studyData.applied_company}
              position={studyData.applied_job}
              career={studyData.career_level}
              style={{ marginTop: "0px" }}
            />
          </DetailContent>
        </DetailTag>
        <DetailTag>
          태그&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;
          <DetailContent>
            {studyData.tags.map((tag, idx) => (
              <TagStyled key={idx} content={tag} />
            ))}
          </DetailContent>
        </DetailTag>
      </DetailContainer>

      <DetailTitle>스터디소개</DetailTitle>
      <HorizontalLine></HorizontalLine>
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
