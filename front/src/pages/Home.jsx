import styled from "styled-components";
import MainTop from "../components/Home/Organisms/MainTop";
import HeaderBox from "./../components/Layout/HeaderBox";

const LayoutContainer = styled.div`
  width: 100%;
  max-width: 100vw;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const HeroContainer = styled.div``;

const LayoutBody = styled.div`
  width: 1200px;
  background-color: red;
  height: 100vh;
  margin: 0 auto;
`;

export default function Home() {
  return (
    <LayoutContainer>
      <HeaderBox />
      <LayoutBody></LayoutBody>
    </LayoutContainer>
  );
}
