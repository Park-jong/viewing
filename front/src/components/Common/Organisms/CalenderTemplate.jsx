import { styled } from "styled-components";
import Calendar from "./../Calendar";
import TimeBar from "../TimeBar";
import { useState, useEffect } from "react";
import { FakeData } from "../FakeData";
import { FakeData2 } from "../FakeData2";
import moment from "moment";

const Container = styled.div`
  display: ${(props) => {
    if (props.isFlex) {
      return "flex"
    } else {
      return "block"
    }
  }};
  justify-content: center;
  align-items: center;
`;

export default function CalenderTemplate({isFlex}) {
  const [data, dataChange] = useState([]);
  const [value, onChange] = useState(new Date());

  useEffect(() => {
    dataChange(FakeData);
  }, []);
  const data2 = data.filter(
    (d) => {
      return moment(value).format("YY.MM.DD") === moment(d.started_at).format("YY.MM.DD")
    }
  );

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
