import styled from "styled-components";
import { FakeData2 } from "./FakeData2";
import moment, { months } from "moment";
import SubButton from "./../Button/SubButton";
import { customAxios } from "./../../modules/Other/Axios/customAxios";
import { useSelector } from "react-redux";
import { UserReducer } from "./../../modules/UserReducer/UserReducer";
import { BiSolidPencil, BiSolidTrash } from "react-icons/bi";
import { useState } from "react";
import ScheduleUpdateModal from "../Modal/ScheduleUpdateModal";

const BigContainer = styled.div`
  margin-top: ${(props) => {
    if (props.$isflex) {
      return "0px";
    } else {
      return "20px";
    }
  }};
`;

const Container = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
`;

const NoSchedule = styled.div`
  width: 800px;
  text-align: center;
  margin-top: ${(props) => {
    if (props.$isflex) {
      return "0px";
    } else {
      return "50px";
    }
  }};
`;

const NickName = styled.div`
  margin-top: 25px;
  text-align: right;
  width: 80px;
  margin-right: 20px;
`;

const TimeAndBarContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 80px;
  width: 600px;
`;
const TimeDisplayContainer = styled.div`
  width: calc(100% * (100 / 24));
  border-left-color: black;
  font-size: 3px;
  display: flex;
  flex-direction: column-reverse;
`;
const TimeDisplayText = styled.div``;

const TimeContainer = styled.div`
  margin-bottom: 10px;
  height: 20px;
  display: flex;
`;

const BarContainer = styled.div`
  height: 50px;
  width: 100%;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
`;

const Bar = styled.div`
  position: absolute;

  height: 100%;
  width: 100%;
  background-color: var(--gray-100);
`;
const Schedule = styled.div`
  position: absolute;

  height: 100%;
  width: ${(props) => `${(props.end - props.start) * 600}px`};
  margin-left: ${(props) => `${props.start * 600}px`};
  background-color: var(--primary);
`;

const Button = styled.div`
  margin-top: 30px;
  margin-left: 5px;
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background-color: var(--gray-200);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
`;

// props에 해당 날짜를 받는다. 날짜에 맞는 스케줄은 상위로직에서 수행한다.
// props.clicked 에 날짜가 담겨있다. props.clicked

// Raw 데이터를 날짜로 바꾸는 법
// Raw 데이터 시간만 가져오기 -> %변환 1440분이다

export function FromMidnightPercent(inputString) {
  // 입력 문자열을 moment 객체로 변환
  const targetTime = moment(inputString, "YYYY-MM-DD HH:mm");

  // 입력 시간과 같은 날 0시 00분의 moment 객체 생성
  const sameDayMidnight = moment(targetTime).startOf("day");

  // 같은 날 0시 00분과의 차이를 계산 (단위: 분)
  const minutesElapsed = targetTime.diff(sameDayMidnight, "minutes");

  // 결과 반환
  return minutesElapsed / 1440;
}

export function involve(schedule, object) {
  const keyList = Object.keys(object);
  for (let i = 0; i < keyList.length; i++) {
    if (keyList[i] === String(schedule.description)) {
      return true;
    }
  }
  return false;
}

export default function TimeBar(props) {
  const [updateData, setUpdateData] = useState([])
  const [updating, setUpdating] = [props.update, props.setUpdate]
  const memberId = useSelector((state) => state.UserReducer.memberId);
  const studySchedules = [];
  const personalSchedules = {};
  const personalScheduleNums = {};
  const personalScheduleUpdate = {};
  // personalSchedules 안에 들어갈 형식은 다음과 같다.
  // {'personalId' : []}   personalId에 <Schedule> 넣기

  const data = props.data;
  data.forEach((schedule, index) => {
    const start = FromMidnightPercent(schedule.started_at);
    const end = FromMidnightPercent(schedule.ended_at);
    if (schedule.is_study_calendar) {
      studySchedules.push(
        <Schedule start={start} end={end} key={index}></Schedule>
      );
    } else {
      if (involve(schedule, personalSchedules)) {
        personalSchedules[schedule.description].push(
          <Schedule start={start} end={end} key={index}></Schedule>
        );
        personalScheduleNums[schedule.description].push(schedule.id);
        personalScheduleUpdate[schedule.description].push(schedule);
      } else {
        personalSchedules[schedule.description] = [
          <Schedule start={start} end={end} key={index}></Schedule>,
        ];
        personalScheduleNums[schedule.description] = [schedule.id];
        personalScheduleUpdate[schedule.description] = [schedule];
      }
    }
  });

  const handleUpdate = async (schedules) => {
    console.log(schedules)
    setUpdating(true)
    setUpdateData(schedules)
  }

  const handleDelete = async (nums) => {
    for (let i of nums) {
      await customAxios()
        .delete(`members/${memberId}/calendars/${i}`)
        .then((res) => {})
        .catch((err) => alert("오류가 발생했습니다."));
    }
    customAxios()
      .get(`members/${memberId}/calendars`)
      .then((res) => {
        props.dataChange(res.data);
      })
      .catch((err) => alert("오류가 발생했습니다."));
  };

  const finalSchedules = [];

  const timeDisplayContainers = [];

  for (let i = 0; i < 24; i++) {
    timeDisplayContainers.push(
      <TimeDisplayContainer key={i}>
        <TimeDisplayText>{i}</TimeDisplayText>
      </TimeDisplayContainer>
    );
  }
  for (let i in personalSchedules) {
    finalSchedules.push(
      <Container key={i}>
        <NickName>{i}</NickName>
        <TimeAndBarContainer>
          <TimeContainer>{timeDisplayContainers}</TimeContainer>
          <BarContainer>
            <Bar></Bar>
            {personalSchedules[i]}
          </BarContainer>
        </TimeAndBarContainer>
        <Button onClick={() => handleUpdate(personalScheduleUpdate[i])}>
          <BiSolidPencil size={22} ></BiSolidPencil>
        </Button>
        <Button onClick={() => handleDelete(personalScheduleNums[i])}>
          <BiSolidTrash size={22}></BiSolidTrash>
        </Button>
      </Container>
    );
  }

  let res = <NoSchedule $isflex={props.isFlex}> 일정이 없습니다. </NoSchedule>;
  if (data.length) {
    res = (
      <BigContainer>
        {studySchedules.length > 0 ? (
          <Container $isflex={props.isFlex}>
            <NickName>Study</NickName>
            <TimeAndBarContainer>
              <TimeContainer>{timeDisplayContainers}</TimeContainer>
              <BarContainer>
                <Bar></Bar>
                {studySchedules}
              </BarContainer>
            </TimeAndBarContainer>
          </Container>
        ) : (
          <></>
        )}
        {finalSchedules}
        <ScheduleUpdateModal isOpen={updating} onClose={setUpdating} data={updateData}></ScheduleUpdateModal>
      </BigContainer>
    );
  }

  return res;
}
