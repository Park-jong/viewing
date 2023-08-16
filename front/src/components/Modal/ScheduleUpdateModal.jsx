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

export default function ScheduleUpdateModal({
  isOpen,
  onClose,
  value,
  onChange,
}) {
  let width = 600;
  let height = 400;
  const [title, setTitle] = useState("");
  const date = moment(value).format("YYYY-MM-DD");
  const setDate = onChange;

  // const [date, setDate] = useState(moment(value).format("YYYY-MM-DD"));
  const [start, setStart] = useState("00:00");
  const [end, setEnd] = useState("12:00");
  const member_id = useSelector((state) => state.UserReducer.memberId);

  const content = (
    <>
      <ModalText> 개인 일정 변경 </ModalText>
      <ModalText>
        제목
        <input value={title} onChange={(e) => setTitle(e.target.value)}></input>
      </ModalText>
      <ModalText>
        일시
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        ></input>
      </ModalText>
      <ModalText>
        시작시간{" "}
        <input
          type="time"
          onChange={(e) => {
            setStart(e.target.value);
          }}
          value={start}
        />
        종료시간{" "}
        <input
          type="time"
          onChange={(e) => {
            setEnd(e.target.value);
          }}
          value={end}
        />
      </ModalText>
    </>
  );

  //확인버튼 onclick 함수
  function confirm() {
        if (start >= end) {
          alert("종료시간이 시작시간보다 빠릅니다.");
          return
        } else if (!title.trim()) {
          alert("내용을 입력하세요.");
          return
        }
        customAxios()
          .post(`members/${member_id}/calendars`, {
            description: title,
            ended_at: `${date}T${end}`,
            started_at: `${date}T${start}`,
          })
          .then((res) => {
            console.log(res);
            onClose(false);
          })
          .catch((err) => {
            console.log(err);
            alert("에러가 발생했습니다.");
            onClose(false);
          });
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
