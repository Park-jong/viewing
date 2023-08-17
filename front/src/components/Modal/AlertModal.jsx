import { styled } from "styled-components";
import MainButton from "./../Button/MainButton";
import { AiOutlineCloseCircle } from "react-icons/ai";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import moment from "moment";

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
  cursor: pointer;
`;

const ScheduleSelected = styled.div`
  width: 30px;
  height: 30px;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${(props) =>
    props.$active ? "var(--primary)" : "var(--gray-200)"};
  margin-left: 10px;
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

const ModalTitle = styled.div`
  font-size: 25px;
  font-weight: 700;
  text-align: center;
  margin-top: 20px;
`;


export default function AlertModal({ isOpen, onClose, type, value, onChange }) {
  let width = 600;
  let height = 400;
  let content = "";
  const [title, setTitle] = useState("");
  const date = moment(value).format("YYYY-MM-DD");
  const setDate = onChange;

  // const [date, setDate] = useState(moment(value).format("YYYY-MM-DD"));
  const [start, setStart] = useState("00:00");
  const [end, setEnd] = useState("12:00");
  const member_id = useSelector((state) => state.UserReducer.memberId);
  let navigate = useNavigate();

  const currentDate = new Date().toISOString().split("T")[0];
  switch (type) {
    case "withdraw":
      width = 300;
      height = 180;
      content = <ModalText>정말 탈퇴하시겠습니까?</ModalText>;
      break;
    case "withdraw_super":
      width = 300;
      height = 180;
      content = <ModalText>방장은 탈퇴할 수 없습니다.</ModalText>;
      break;
    case "delete":
      width = 300;
      height = 180;
      content = <ModalText>정말 삭제하시겠습니까?</ModalText>;
      break;
    case "schedule":
      width = 500;
      height = 420;
      content = (
        <>
          <ModalTitle> 개인 일정 추가 </ModalTitle>
          <ModalText>
            제목
            <TitleInput
              onChange={(e) => setTitle(e.target.value)}
              maxLength={100}
            ></TitleInput>
          </ModalText>
          <ModalText>
            일시
            <TitleInput
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              min={currentDate}
            ></TitleInput>
          </ModalText>
          <ModalText>
            시작시간{" "}
            <TimeInput
              type="time"
              onChange={(e) => {
                setStart(e.target.value);
              }}
              value={start}
            />
            종료시간{" "}
            <TimeInput
              type="time"
              onChange={(e) => {
                setEnd(e.target.value);
              }}
              value={end}
            />
          </ModalText>
        </>
      );
      break;
  }

  //확인버튼 onclick 함수
  function confirm({ type }) {
    switch (type) {
      case "withdraw":
        customAxios()
          .delete(`members/${member_id}`)
          .then(() => {
            onClose(false);
            localStorage.clear();
            navigate(`/`);
          })
          .catch((err) => {
            if (err.response.status === 400)
              alert("스터디장인 스터디가 존재합니다.");
          });
        break;
      case "schedule":
        if (start >= end) {
          alert("종료시간이 시작시간보다 빠릅니다.");
          break;
        } else if (!title.trim()) {
          alert("내용을 입력하세요.");
          break;
        }
        customAxios()
          .post(`members/${member_id}/calendars`, {
            description: title,
            ended_at: `${date}T${end}`,
            started_at: `${date}T${start}`,
          })
          .then((res) => {
            onClose(false);
          })
          .catch((err) => {
            console.log(err);
            alert("에러가 발생했습니다.");
            onClose(false);
          });
    }
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
            <Buttons>
              <MainButton
                content={"확인"}
                width={50}
                height={30}
                marginright={20}
                onClick={() => confirm({ type })}
              ></MainButton>
              <MainButton
                content={"취소"}
                width={50}
                height={30}
                onClick={() => onClose(false)}
              ></MainButton>
            </Buttons>
          </ModalContent>
        </ModalOverlay>
      )}
    </>
  );
}
