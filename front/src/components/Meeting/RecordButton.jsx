import { useState } from "react";
import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

const ReadyState = styled.div``;
const PauseState = styled.div``;
const RecordState = styled.div``;

const RecordingButton = styled.div`
  border: 1px solid black;
  display: inline;
`;
const PauseButton = styled.div`
  border: 1px solid black;
  display: inline;
`;
const ResumeButton = styled.div`
  border: 1px solid black;
  display: inline;
`;
const StopButton = styled.div`
  border: 1px solid black;
  display: inline;
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
          {"녹화"}
        </RecordingButton>
      </ReadyState>

      <PauseState hidden={recordState !== "pause"}>
        <ResumeButton
          onClick={() => {
            const res = resumeRecord();
            if (res !== "err") {
              setRecordState("record");
            }
          }}
        >
          {"재개"}
        </ResumeButton>
        <StopButton
          onClick={() => {
            const res = stopRecord();
            if (res !== "err") {
              setRecordState("ready");
            }
          }}
        >
          {"종료"}
        </StopButton>
      </PauseState>

      <RecordState hidden={recordState !== "record"}>
        <PauseButton
          onClick={() => {
            const res = pauseRecord();
            if (res !== "err") {
              setRecordState("pause");
            }
          }}
        >
          {"중지"}
        </PauseButton>
        <StopButton
          onClick={() => {
            const res = stopRecord();
            if (res !== "err") {
              setRecordState("ready");
            }
          }}
        >
          {"종료"}
        </StopButton>
      </RecordState>
    </Container>
  );
}
