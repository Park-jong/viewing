import { styled } from "styled-components";
import Calendar from "./../Calendar";
import TimeBar from "../TimeBar";
import { useState, useEffect } from "react";
import { FakeData } from "../FakeData";
import { FakeData2 } from "../FakeData2";
import moment from "moment";

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default function CalenderTemplate() {
  const [data, dataChange] = useState([]);
  const [value, onChange] = useState(new Date());

  useEffect(() => {
    dataChange(FakeData);
  }, []);
  const data2 = data.filter(
    (d) => {
      console.log(moment(d.started_at).format("YY.MM.DD"))
      console.log(moment(value).format("YY.MM.DD"))
      return moment(value).format("YY.MM.DD") === moment(d.started_at).format("YY.MM.DD")
    }
  );

  return (
    <Container>
      <Calendar
        dataChange={dataChange}
        data={data}
        value={value}
        onChange={onChange}
      ></Calendar>
      <TimeBar data={data2}></TimeBar>
    </Container>
  );
}
