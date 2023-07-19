import styled from "styled-components";

const LayoutContainer = styled.div`
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const LayoutBody = styled.div`
  width: 1200px;
  background-color: red;
  height: 100vh;
  margin: 0 auto;
`;

const Header = styled.div`
  width: 100%;
  height: 64px;
  background-color: blue;
`;

const HeroImage = styled.div`
  width: 100%;
  height: 372px;
  background-color: green;
`;

export default function Layout({ children }) {
  return (
    <LayoutContainer>
      <Header />
      <HeroImage />
      <LayoutBody>{children}</LayoutBody>
    </LayoutContainer>
  );
}
