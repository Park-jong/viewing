import { styled } from "styled-components";
import CalendarTemplate from "../components/Common/Organisms/CalendarTemplate";
import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";

const Container = styled.div``;

export default function StudyPkCalendar() {
  const studyId = useParams().studyPk;
  const [calendarData, setCalendarData] = useState([]);
  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/calendars`)
      .then(({ data }) => {
        console.log(data);
        setCalendarData((prev) => data);
      })
      .catch();
  }, []);

  return (
    <Container>
      <CalendarTemplate isFlex={true}></CalendarTemplate>
    </Container>
  );
}
