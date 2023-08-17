import { styled } from "styled-components";
import MainButton from "./../Button/MainButton";
import { AiOutlineCloseCircle } from "react-icons/ai";
import { useState } from "react";

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  width: ${(props) => `${props.$width}px`};
  height: ${(props) => `${props.$height}px`};
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const ModalTitle = styled.div`
  font-size: 25px;
  font-weight: 700;
  text-align: center;
  margin-top: 20px;
`;

const ModalText = styled.div`
  font-size: 16px;
  text-align: center;
  margin-top: 20px;

  display: flex;
  justify-content: space-evenly;
  align-items: center;

  /* input {
    width: 50%;
    padding: 8px;
    margin-top: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
  } */
`;

const TitleInput = styled.input`
  width: 70%;
  padding: 8px;
  margin-top: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const TimeInput = styled.input`
  width: 23%;
  padding: 8px;
  margin-top: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const Buttons = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20px;

  button + button {
    margin-left: 10px;
  }
`;

const XButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  cursor: pointer;
  margin-bottom: -30px;
`;

export default function StudyCalendarModal({
  isOpen,
  onClose,
  type,
  data,
  setData,
  addSchedule,
  updateSchedule,
  deleteSchedule,
}) {
  const [date, setDate] = useState("");
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");
  const currentDate = new Date().toISOString().split("T")[0];
  let width = 600;
  let height = 400;
  let content = "";
  switch (type) {
    case "schedule_add":
      width = 500;
      height = 400;
      content = (
        <>
          <ModalTitle> ✍스터디 일정 추가 </ModalTitle>
          <ModalText>
            제목
            <TitleInput
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.description = e.target.value;
                  return obj;
                });
              }}
              maxLength={100}
              placeholder="스터디 일정을 입력해주세요. (EX. 모의면접)"
            ></TitleInput>
          </ModalText>
          <ModalText>
            일시
            <TitleInput
              type="date"
              onChange={(e) => {
                setDate(e.target.value);
                setData((prev) => {
                  const obj = { ...prev };
                  obj.started_at = e.target.value + "T" + start;
                  obj.ended_at = e.target.value + "T" + end;
                  return obj;
                });
              }}
              min={currentDate}
            ></TitleInput>
          </ModalText>
          <ModalText>
            시작시간{" "}
            <TimeInput
              type="time"
              onChange={(e) => {
                setStart(e.target.value);
                setData((prev) => {
                  const obj = { ...prev };
                  obj.started_at = date + "T" + e.target.value;
                  return obj;
                });
              }}
            />
            종료시간{" "}
            <TimeInput
              type="time"
              onChange={(e) => {
                setEnd(e.target.value);
                setData((prev) => {
                  const obj = { ...prev };
                  obj.ended_at = date + "T" + e.target.value;
                  return obj;
                });
              }}
            />
          </ModalText>
          <Buttons>
            <MainButton
              content={"등록"}
              width={50}
              height={30}
              marginright={20}
              onClick={addSchedule}
            ></MainButton>
            <MainButton
              content={"취소"}
              width={50}
              height={30}
              onClick={() => {
                onClose();
              }}
            ></MainButton>
          </Buttons>
        </>
      );
      break;
    case "schedule_view":
      width = 500;
      height = 400;
      console.log(data);
      content = (
        <>
          <ModalTitle> 스터디 일정 조회 </ModalTitle>
          <ModalText>
            제목
            <TitleInput
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.description = e.target.value;
                  return obj;
                });
              }}
              value={data.description}
              disabled={!data.is_study_calendar}
            ></TitleInput>
          </ModalText>
          <ModalText>
            일시
            <TitleInput
              type="date"
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.started_at =
                    e.target.value + "T" + obj.started_at.split("T")[1];
                  obj.ended_at =
                    e.target.value + "T" + obj.ended_at.split("T")[1];
                  return obj;
                });
              }}
              value={data.started_at.split("T")[0]}
              disabled={!data.is_study_calendar}
            ></TitleInput>
          </ModalText>
          <ModalText>
            시작시간{" "}
            <TimeInput
              type="time"
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.started_at =
                    obj.started_at.split("T")[0] + "T" + e.target.value;
                  return obj;
                });
              }}
              value={data.started_at.split("T")[1]}
              disabled={!data.is_study_calendar}
            />
            종료시간{" "}
            <TimeInput
              type="time"
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.ended_at =
                    obj.ended_at.split("T")[0] + "T" + e.target.value;
                  return obj;
                });
              }}
              value={data.ended_at.split("T")[1]}
              disabled={!data.is_study_calendar}
            />
          </ModalText>
          <Buttons>
            <MainButton
              content={"수정"}
              width={50}
              height={30}
              marginright={20}
              disabled={!data.is_study_calendar}
              onClick={() => {
                if (data.is_study_calendar) updateSchedule();
              }}
            ></MainButton>
            <MainButton
              content={"삭제"}
              width={50}
              height={30}
              disabled={!data.is_study_calendar}
              onClick={() => {
                if (data.is_study_calendar) deleteSchedule();
              }}
            ></MainButton>
          </Buttons>
        </>
      );
      break;
    default:
      break;
  }

  return (
    <>
      {isOpen && (
        <ModalOverlay>
          <ModalContent $width={width} $height={height}>
            <XButtonContainer onClick={() => onClose(false)}>
              <AiOutlineCloseCircle />
            </XButtonContainer>
            {content}
          </ModalContent>
        </ModalOverlay>
      )}
    </>
  );
}
