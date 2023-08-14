import { useState } from "react";
import styled from "styled-components";
import { BiVideoRecording, BiStopCircle } from "react-icons/bi";
import { AiOutlinePauseCircle, AiFillPlayCircle } from "react-icons/ai";

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
          <IconBiVideoRecording /> &nbsp;녹화&nbsp;
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
          style={{ marginRight: "17px" }}
        >
          <AiFillPlayCircle />
        </ResumeButton>
        <StopButton
          onClick={() => {
            const res = stopRecord();
            if (res !== "err") {
              setRecordState("ready");
            }
          }}
        >
          <BiStopCircle />
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
          <AiOutlinePauseCircle />
        </PauseButton>
        {/* <StopButton
          onClick={() => {
            const res = stopRecord();
            if (res !== "err") {
              setRecordState("ready");
            }
          }}
        >
          <BiStopCircle />
        </StopButton> */}
      </RecordState>
    </Container>
  );
}
