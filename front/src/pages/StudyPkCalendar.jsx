import { styled } from "styled-components";
import StudyCalendarTemplate from "../components/Common/Organisms/StudyCalendarTemplate";
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
        setCalendarData((prev) => data);
      })
      .catch();
  }, []);

  return (
    <Container>
      <StudyCalendarTemplate
        isFlex={true}
        data={calendarData}
        dataChange={setCalendarData}
      ></StudyCalendarTemplate>
    </Container>
  );
}
