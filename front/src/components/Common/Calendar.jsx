import styled from "styled-components";
import ReactCalendar from "react-calendar";
import { useState, useEffect } from "react";
import moment from "moment";

// import "react-calendar/dist/Calendar.css";
import { FakeData } from "./FakeData";

const Container = styled.div`
  .react-calendar {
    width: 800px;
    max-width: 100%;
    background: white;
    /* border: 1px solid #a0a096; */
    border-radius: 15px;
    /* font-family: Arial, Helvetica, sans-serif; */
    line-height: 2.125em;
    box-shadow: 0px 0px 25px 1px #d9d9d9;
  }

  .react-calendar--doubleView {
    width: 700px;
  }

  .react-calendar--doubleView .react-calendar__viewContainer {
    display: flex;
    margin: -0.5em;
  }

  .react-calendar--doubleView .react-calendar__viewContainer > * {
    width: 50%;
    margin: 0.5em;
  }

  .react-calendar,
  .react-calendar *,
  .react-calendar *:before,
  .react-calendar *:after {
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }

  .react-calendar button {
    margin: 0;
    border: 0;
    outline: none;
  }

  .react-calendar button:enabled:hover {
    border-radius: 15px;
    cursor: pointer;
  }

  .react-calendar__navigation {
    display: flex;
    height: 80px;
    margin-bottom: 1em;
  }

  .react-calendar__navigation button {
    min-width: 44px;
    background: none;
  }

  .react-calendar__navigation button:disabled {
    background-color: #f0f0f0;
    border-radius: 15px;
  }

  .react-calendar__navigation button:enabled:hover,
  .react-calendar__navigation button:enabled:focus {
    background-color: #e6e6e6;
    border-radius: 15px;
  }

  .react-calendar__month-view__weekdays {
    text-align: center;
    text-transform: uppercase;
    font-weight: bold;
    font-size: 0.75em;
  }

  .react-calendar__month-view__weekdays__weekday {
    padding: 0.5em;
  }

  .react-calendar__month-view__weekNumbers .react-calendar__tile {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 0.75em;
    font-weight: bold;
  }

  .react-calendar__month-view__days__day--weekend {
    color: #d10000;
  }

  .react-calendar__month-view__days__day--neighboringMonth {
    color: #757575;
  }

  .react-calendar__year-view .react-calendar__tile,
  .react-calendar__decade-view .react-calendar__tile,
  .react-calendar__century-view .react-calendar__tile {
    padding: 2em 0.5em;
  }

  .react-calendar__tile {
    max-width: 100%;
    padding: 10px 6.6667px;
    background: none;
    text-align: center;
    line-height: 40px; // 한 개의 사이즈
  }

  .react-calendar__tile:disabled {
    background-color: #f0f0f0;
    border-radius: 15px;
  }

  .react-calendar__tile:enabled:hover,
  .react-calendar__tile:enabled:focus {
    background-color: #e6e6e6;
    border-radius: 15px;
  }

  .react-calendar__tile--now {
    background: #ffff76;
    border-radius: 15px;
  }

  .react-calendar__tile--now:enabled:hover,
  .react-calendar__tile--now:enabled:focus {
    background: #ffffa9;
    border-radius: 15px;
  }

  .react-calendar__tile--hasActive {
    background: #76baff;
    border-radius: 15px;
  }

  .react-calendar__tile--hasActive:enabled:hover,
  .react-calendar__tile--hasActive:enabled:focus {
    background: #a9d4ff;
    border-radius: 15px;
  }

  .react-calendar__tile--active {
    background: #006edc;
    color: white;
    border-radius: 15px;
  }

  .react-calendar__tile--active:enabled:hover,
  .react-calendar__tile--active:enabled:focus {
    background: #1087ff;
    border-radius: 15px;
  }

  .react-calendar--selectRange .react-calendar__tile--hover {
    background-color: #e6e6e6;
    border-radius: 15px;
  }
`;

const DotContainer = styled.div`
  display: flex;
  height: 8px;
  justify-content: center;
`;

const Dot = styled.div`
  height: 8px;
  width: 8px;
  background-color: #f87171;
  border-radius: 50%;
  margin-left: 1px;
`;
const StudyDot = styled.div`
  height: 8px;
  width: 8px;
  background-color: #3bb9ea;
  border-radius: 50%;
  margin-left: 1px;
`;

export default function Calendar(props) {
  // const [value, onChange] = useState(new Date());
  // const [data, dataChange] = useState([]);
  const data = props.data;
  const dataChange = props.dataChange;

  const value = props.value;
  const onChange = props.onChange;

  // axios로 스터디원의 일정을 받아왔을 때!
  // useEffect(() => {
  //   dataChange(FakeData);
  // }, []);

  return (
    <Container>
      <ReactCalendar
        onChange={onChange}
        value={value}
        calendarType="gregory"
        tileContent={({ activeStartDate, date, view }) => {
          const NewDots = [];

          for (let i = 0; i < data.length; i++) {
            const schedule = data[i];
            const dataReformatted = moment(schedule.started_at).format(
              "YY-MM-DD"
            );
            const dateReformatted = moment(date).format("YY-MM-DD");

            if (dataReformatted === dateReformatted) {
              if (schedule.is_study_calendar) {
                NewDots.push(<StudyDot key={i}></StudyDot>);
              } else {
                NewDots.push(<Dot key={i}></Dot>);
              }
            }
          }

          return <DotContainer>{NewDots}</DotContainer>;
        }}
      />
    </Container>
  );
}
