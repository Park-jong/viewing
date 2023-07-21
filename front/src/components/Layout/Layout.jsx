import styled from "styled-components";
import HeaderBox from "./HeaderBox";

const LayoutContainer = styled.div`
  width: 100%;
  max-width: 100vw;
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

export default function Layout({ children }) {
  return (
    <LayoutContainer>
      <HeaderBox />
      <LayoutBody>{children}</LayoutBody>
    </LayoutContainer>
  );
}
