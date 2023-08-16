import { useState } from "react";
import styled from "styled-components";
import { BiVideoRecording, BiStopCircle } from "react-icons/bi";
import { AiOutlinePauseCircle, AiFillPlayCircle } from "react-icons/ai";
import { RxResume } from "react-icons/rx";

const Container = styled.div`
  /* border: 1px solid black; */
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ReadyState = styled.div`
  display: flex;
  align-items: center;
`;
const PauseState = styled.div`
  display: flex;
  align-items: center;
`;
const RecordState = styled.div`
  display: flex;
  align-items: center;
`;

const RecordingButton = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-100);
  border-radius: 10px;
  border: 1px solid var(--gray-200);
  margin-right: 15px;
`;

const PauseButton = styled.div`
  /* background-color: var(--gray-100);
  border-radius: 10px;
  border: 1px solid var(--gray-200); */
`;

const ResumeButton = styled.div`
  /* border: 1px solid black; */
  display: inline;
`;
const StopButton = styled.div`
  /* background-color: var(--gray-100);
  /* border-radius: 10px;
  border: 1px solid var(--gray-200); */
  margin-right: 15px;
`;

const IconBiVideoRecording = styled(BiVideoRecording)`
  font-size: 2rem;
`;

const TextContainer = styled.div`
  width: 4em;
`;

export default function RecordButton({
  initRecord,
  pauseRecord,
  resumeRecord,
  stopRecord,
}) {
  const [recordState, setRecordState] = useState("ready");

  return (
    <Container>
      <ReadyState hidden={recordState !== "ready"}>
        <RecordingButton
          onClick={() => {
            const res = initRecord();
            if (res !== "err") {
              setRecordState("record");
            }
          }}
        >
          <IconBiVideoRecording /> &nbsp;
          {recordState === "ready" ? (
            <TextContainer>{"녹화시작"}</TextContainer>
          ) : recordState === "pause" ? (
            <TextContainer>{"정지중"}</TextContainer>
          ) : recordState === "record" ? (
            <TextContainer>{"녹화중"}</TextContainer>
          ) : undefined}
          &nbsp;
        </RecordingButton>
      </ReadyState>

      <PauseState hidden={recordState !== "pause"}>
        <PauseButton
          onClick={() => {
            const res = pauseRecord();
            if (res !== "err") {
              setRecordState("pause");
            }
          }}
        >
          <AiOutlinePauseCircle fontSize={"1.5rem"} />
        </PauseButton>
        <ResumeButton
          onClick={() => {
            const res = resumeRecord();
            if (res !== "err") {
              setRecordState("record");
            }
          }}
          style={{ marginRight: "17px" }}
        >
          <RxResume fontSize={"1.5rem"} />
        </ResumeButton>
      </PauseState>

      <RecordState hidden={recordState !== "record"}>
        <StopButton
          onClick={() => {
            const res = stopRecord();
            if (res !== "err") {
              setRecordState("ready");
            }
          }}
        >
          <BiStopCircle fontSize={"1.5rem"} />
        </StopButton>
      </RecordState>
    </Container>
  );
}
