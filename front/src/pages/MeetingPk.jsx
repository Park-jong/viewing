import { styled } from "styled-components";
import CamBox from "../components/Meeting/CamBox";
import MeetingFooter from "../components/Meeting/Organisms/MeetingFooter";
import MeetingMain from "../components/Meeting/Organisms/MeetingMain";
import MeetingSideBar from "../components/Meeting/Organisms/MeetingSideBar";

const Container = styled.div``;

const HeaderContainer = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
`;

const TitleContainer = styled.div`
  border: 1px solid black;
  margin-left: auto;
`;

const MainContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const ButtonContainer = styled.div`
  border: 1px solid black;
  margin-left: auto;
`;

const LayoutButton = styled.button`
  border: 1px solid black;
  margin: 1px;
`;

export default function MeetingPk() {
  return (
    <Container>
      <HeaderContainer>
        <TitleContainer>스터디 제목</TitleContainer>
        <ButtonContainer>
          <LayoutButton>보기1</LayoutButton>
          <LayoutButton>보기2</LayoutButton>
          <LayoutButton>보기3</LayoutButton>
        </ButtonContainer>
      </HeaderContainer>
      <MainContainer>
        <MeetingMain></MeetingMain>
        <MeetingSideBar></MeetingSideBar>
      </MainContainer>
      <MeetingFooter></MeetingFooter>
    </Container>
  );
}
