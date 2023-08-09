import { styled } from "styled-components";
import MeetingFooter from "../components/Meeting/Organisms/MeetingFooter";
import MeetingMain from "../components/Meeting/Organisms/MeetingMain";
import MeetingSideBar from "../components/Meeting/Organisms/MeetingSideBar";
import { useState } from "react";

const Container = styled.div`
  width: 100%;
  height: 100%;
`;

const HeaderContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 5%;
`;

const TitleContainer = styled.div`
  border: 1px solid black;
  margin-left: auto;
`;

const MainContainer = styled.div`
  position: absolute;
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 90%;
  top: 5%;
  bottom: 5%;
`;

const MeetingMainContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  right: ${(props) => `${props.right}%`};
  width: 100%;
  height: 100%;
`;

const MeetingSideContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  left: 80%;
  width: 20%;
  height: 100%;
`;

const ButtonContainer = styled.div`
  border: 1px solid black;
  margin-left: auto;
`;

const LayoutButton = styled.button`
  border: 1px solid black;
  margin: 1px;
`;

const FooterContainer = styled.div`
  position: absolute;
  border: 1px solid black;
  display: flex;
  flex-direction: row;
  align-items: center;
  top: 95%;
  width: 100%;
  height: 5%;
`;

export default function MeetingPk() {
  const [closeSideBar, setCloseSideBar] = useState(false);

  const [option, setOption] = useState("member");

  const changeOption = (value) => {
    setOption(value);
  };

  const toggleSideBar = (value) => {
    setCloseSideBar(value);
  };

  return (
    // eslint-disable-next-line no-restricted-globals
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
        <MeetingMainContainer right={closeSideBar ? 0 : 20}>
          <MeetingMain></MeetingMain>
        </MeetingMainContainer>
        <MeetingSideContainer hidden={closeSideBar}>
          <MeetingSideBar
            toggleSideBar={toggleSideBar}
            changeOption={changeOption}
            option={option}
          ></MeetingSideBar>
        </MeetingSideContainer>
      </MainContainer>
      <FooterContainer>
        <MeetingFooter
          toggleSideBar={toggleSideBar}
          changeOption={changeOption}
        ></MeetingFooter>
      </FooterContainer>
    </Container>
  );
}
