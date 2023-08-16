import { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import { BiTimer } from "react-icons/bi";

const Container = styled.div`
  /* border: 1px solid black; */
  display: flex;
  align-items: center;
`;

const ReadyState = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-100);
  border-radius: 10px;
  border: 1px solid var(--gray-200);
  margin-right: 15px;
  cursor: pointer;
`;
const TimeContatiner = styled.div`
  width: 3em;
`;

const IconBiTimer = styled(BiTimer)`
  font-size: 2rem;
`;

export default function TimerButton() {
  const [timerState, setTimerState] = useState("ready");
  const [timer, setTimer] = useState(0);
  const [saveTime, setSaveTime] = useState(0);

  const checkTime = () => {
    setTimeout(() => {
      setTimer((prev) => prev + 1);
      checkTime();
    }, 1000);
  };

  const saveCurrentTime = () => {
    setSaveTime(timer);
  };

  const changeState = () => {
    if (timerState === "ready") {
      setTimer(0);
      setTimerState("start");
    } else if (timerState === "start") {
      saveCurrentTime();
      setTimerState("stop");
    } else if (timerState === "stop") {
      setTimerState("ready");
      setTimer(0);
    }
  };

  useEffect(() => {
    checkTime();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Container>
      <ReadyState
        onClick={() => {
          changeState();
        }}
      >
        <IconBiTimer />
        &nbsp;{" "}
        {timerState === "ready" ? (
          <TimeContatiner>{"타이머"}</TimeContatiner>
        ) : timerState === "start" ? (
          <TimeContatiner>{"정지"}</TimeContatiner>
        ) : timerState === "stop" ? (
          <TimeContatiner>{"초기화"}</TimeContatiner>
        ) : undefined}{" "}
        &nbsp;
      </ReadyState>
      <TimeContatiner>
        {timerState === "ready"
          ? "00:00"
          : timerState === "start"
          ? `${
              timer / 60 < 10
                ? `0${Math.round(timer / 60)}:${
                    timer % 60 < 10 ? "0" + (timer % 60) : timer % 60
                  }`
                : `${Math.round(timer / 60)}:${
                    timer % 60 < 10 ? "0" + (timer % 60) : timer % 60
                  }`
            }`
          : timerState === "stop"
          ? Math.round(saveTime / 60) < 10
            ? `0${Math.round(saveTime / 60)}:${
                saveTime % 60 < 10 ? "0" + (saveTime % 60) : saveTime % 60
              }`
            : `${Math.round(saveTime / 60)}:${
                saveTime % 60 < 10 ? "0" + (saveTime % 60) : saveTime % 60
              }`
          : "00:00"}
      </TimeContatiner>
    </Container>
  );
}
