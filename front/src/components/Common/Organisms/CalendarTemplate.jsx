import { styled } from "styled-components";
import Calendar from "../Calendar";
import TimeBar from "../TimeBar";
import { useState, useEffect } from "react";
import { FakeData } from "../FakeData";
import { FakeData2 } from "../FakeData2";
import moment from "moment";
import { useSelector } from "react-redux";
import { customAxios } from "../../../modules/Other/Axios/customAxios";

const Container = styled.div`
  display: ${(props) => {
    if (props.isFlex) {
      return "flex";
    } else {
      return "block";
    }
  }};
  justify-content: center;
  align-items: center;
`;

export default function CalendarTemplate({ isFlex, value, onChange, modal }) {
  const [data, dataChange] = useState([]);
  const memberId = useSelector((state) => state.UserReducer.memberId);

  useEffect(() => {
    customAxios()
      .get(`members/${memberId}/calendars`)
      .then((res) => {
        console.log(res.data.data);
        dataChange(res.data.data)
      })
      .catch((err) => console.log(err));
  }, [modal]);
  const data2 = data.filter((d) => {
    return (
      moment(value).format("YY.MM.DD") ===
      moment(d.started_at).format("YY.MM.DD")
    );
  });

  return (
    <Container $isFlex={isFlex}>
      <Calendar
        dataChange={dataChange}
        data={data}
        value={value}
        onChange={onChange}
      ></Calendar>
      <TimeBar data={data2} isFlex={isFlex}></TimeBar>
    </Container>
  );
}
