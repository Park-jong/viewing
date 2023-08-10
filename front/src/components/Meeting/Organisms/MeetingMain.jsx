import styled from "styled-components";
import CamBox from "../CamBox";

const Container = styled.div`
  position: relative;
  border: 1px solid black;
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  width: 100%;
  height: 100%;
`;

const CamContainer = styled.div`
  border: 5px solid black;
  position: absolute;
  left: 0;
  top: 0;
  transform: translate(70%);
  width: 480px;
  height: 270px;
`;

export default function MeetingMain({ publisher, subscribers }) {
  return (
    <Container>
      <CamContainer>
        <CamBox streamManager={publisher}></CamBox>
      </CamContainer>
      {subscribers.subs.map((sub, i) => {
        return (
          <CamContainer>
            <CamBox streamManager={sub} key={i}></CamBox>
          </CamContainer>
        );
      })}
    </Container>
  );
}
