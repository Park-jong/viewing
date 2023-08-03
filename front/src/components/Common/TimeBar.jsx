import styled from "styled-components";
import { FakeData2 } from "./FakeData2";
import moment, { months } from "moment";

const BigContainer = styled.div`
  margin-top : ${(props) => {
    if (props.$isflex) {
      return "0px"
    } else {
      return "50px"
    };
  }};
`

const Container = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  
`;

const NoSchedule = styled.div`
  width: 500px;
  text-align: center;
  margin-top : ${(props) => {
    if (props.$isflex) {
      return "0px"
    } else {
      return "50px"
    };
  }};
`

const NickName = styled.div`
  text-align: right;
  width: 80px;
  margin-right: 20px;
`;

const BarContainer = styled.div`
  height: 50px;
  width: 400px;
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
  width: ${(props) => `${(props.end - props.start) * 400}px`};
  margin-left: ${(props) => `${props.start * 400}px`};
  background-color: var(--primary);

  
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
    if (keyList[i] === String(schedule.author.nickname)) {
      return true;
    }
  }
  return false;
}

export default function TimeBar(props) {
  const studySchedules = [];
  const personalSchedules = {};
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
        personalSchedules[schedule.author.nickname].push(
          <Schedule start={start} end={end} key={index}></Schedule>
        );
      } else {
        personalSchedules[schedule.author.nickname] = [
          <Schedule start={start} end={end} key={index}></Schedule>,
        ];
      }
    }
  });

  const finalSchedules = [];

  for (let i in personalSchedules) {
    finalSchedules.push(
      <Container key={i}>
        <NickName>{i}</NickName>
        <BarContainer>
          <Bar></Bar>
          {personalSchedules[i]}
        </BarContainer>
      </Container>
    );
  }
  let res = <NoSchedule $isflex={props.isFlex}> 일정이 없습니다. </NoSchedule>;
  if (data.length) {
    res = (
      <BigContainer>
        <Container $isflex={props.isFlex}>
          <NickName>Study</NickName>
          <BarContainer>
            <Bar></Bar>
            {studySchedules}
          </BarContainer>
        </Container>
        {finalSchedules}
      </BigContainer>
    );
  }

  return res;
}
