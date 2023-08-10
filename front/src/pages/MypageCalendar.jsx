import { styled } from "styled-components";
import { FakeData } from "../components/Common/FakeData";
import CalendarTemplate from "../components/Common/Organisms/CalendarTemplate";

const Container = styled.div`
  width: 820px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default function MypageCalendar() {
  return (
    <Container>
      <CalendarTemplate isFlex={false}></CalendarTemplate>
    </Container>
  );
}
