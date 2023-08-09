import styled from "styled-components";
import CamBox from "../CamBox";

const Container = styled.div`
  border: 1px solid black;
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  width: 100%;
  height: 100%;
`;

export default function MeetingMain() {
  return (
    <Container>
      <CamBox></CamBox>
      <CamBox></CamBox>
      <CamBox></CamBox>
      <CamBox></CamBox>
      <CamBox></CamBox>
      <CamBox></CamBox>
    </Container>
  );
}
