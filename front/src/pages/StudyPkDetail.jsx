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
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

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
  white-space: pre-wrap;
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
  flex-wrap: wrap;
`;

export default function StudyPkDetail() {
  const studyPk = useParams().studyPk;

  const loginMember = useSelector((state) => state.UserReducer);

  const navigate = useNavigate();

  const careerFilter = {
    ALL: "무관",
    INTERN: "인턴",
    NEWCOMER: "신입",
    EXPERIENCED: "경력",
  };

  const emptyStudyData = {
    study_id: studyPk,
    title: "",
    description: "",
    applied_company: "",
    applied_job: "",
    capacity: 0,
    head_count: 0,
    created_at: "",
    deadline: "",
    recruitment: true,
    leader: {
      member_id: null,
      nickname: "",
      background: "",
      character: "",
    },
    career_level: "ALL",
    tags: [],
  };
  const [studyData, SetstudyData] = useState(emptyStudyData);

  useEffect(() => {
    customAxios()
      .get(`studies/${studyPk}`)
      .then((res) => {
        SetstudyData(() => res.data);
      })
      .catch((err) => {
        if (!loginMember.memberId) {
          window.alert("로그인이 필요합니다.");
          navigate("/login");
        }
      });
  }, []);

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
          member_id={studyData.leader.member_id}
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
              career={careerFilter[studyData.career_level]}
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
        <DetailTag>
          모집여부&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
          <DetailContent>
            {studyData.recruitment ? "모집 중" : "모집 마감"}
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
