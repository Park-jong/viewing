import styled from "styled-components";
import HeaderBox from "../components/Layout/HeaderBox";
import MainTop from "../components/Home/Organisms/MainTop";
import Hot10Box from "../components/Home/Hot10Box";
import SearchBox from "../components/Home/SearchBox";
import TutorialBox from "./../components/Home/TutorialBox";

const Container = styled.div`
  width: 100%;
  max-width: 100vw;
  min-height: 100vh;
  //max-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const BodyContainer = styled.div`
  width: 1200px;
  height: 100%;
  background-color: white;
  height: 100vh;
  margin: 0 auto;
`;

const HeroContainer = styled.div`
  width: 100%;
  max-width: 100%;
  height: 600px;
  /* background-color: green; */
  background-image: url("/main.png"); /* Replace with the URL of your image */
  background-size: cover;
  background-position: center;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const InnerContainer = styled.div`
  margin-top: 130px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Hot10Container = styled.div`
  margin-top: 30px;
`;

export default function Home() {
  return (
    <Container>
      <HeaderBox />
      <HeroContainer>
        <InnerContainer>
          <SearchBox />
          <Hot10Container>
            <Hot10Box />
          </Hot10Container>
        </InnerContainer>
      </HeroContainer>
      <BodyContainer>
        <TutorialBox />
      </BodyContainer>
    </Container>
  );
}
