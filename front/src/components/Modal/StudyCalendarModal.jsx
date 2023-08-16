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
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  width: ${(props) => `${props.$width}px`};
  height: ${(props) => `${props.$height}px`};
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
`;

const ModalText = styled.div`
  font-size: 22px;
  text-align: center;
  margin-top: 45px;
`;

const Buttons = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 50px;
`;

const XButtonContainer = styled.div`
  display: flex;
  justify-content: right;
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
  let width = 600;
  let height = 400;
  let content = "";
  switch (type) {
    case "schedule_add":
      width = 500;
      height = 400;
      content = (
        <>
          <ModalText> 스터디 일정 추가 </ModalText>
          <ModalText>
            제목
            <input
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.description = e.target.value;
                  return obj;
                });
              }}
              maxLength={100}
            ></input>
          </ModalText>
          <ModalText>
            일시
            <input
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
            ></input>
          </ModalText>
          <ModalText>
            시작시간{" "}
            <input
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
            <input
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
          <ModalText> 스터디 일정 조회 </ModalText>
          <ModalText>
            제목
            <input
              onChange={(e) => {
                setData((prev) => {
                  const obj = { ...prev };
                  obj.description = e.target.value;
                  return obj;
                });
              }}
              value={data.description}
              disabled={!data.is_study_calendar}
            ></input>
          </ModalText>
          <ModalText>
            일시
            <input
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
            ></input>
          </ModalText>
          <ModalText>
            시작시간{" "}
            <input
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
            <input
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
              onClick={() => updateSchedule()}
            ></MainButton>
            <MainButton
              content={"삭제"}
              width={50}
              height={30}
              disabled={!data.is_study_calendar}
              onClick={() => deleteSchedule()}
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
