
import { styled } from "styled-components";
import { FakeData } from "../components/Common/FakeData";
import CalenderTemplate from "../components/Common/Organisms/CalenderTemplate";

const Container = styled.div`
  width: 820px;
  display: flex;
  justify-content: center;
  align-items: center;
`

export default function MypageCalendar() {
  return <Container>
    <CalenderTemplate isFlex={false}></CalenderTemplate>
  </Container>
}
