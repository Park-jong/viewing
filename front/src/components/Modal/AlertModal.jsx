import { styled } from "styled-components";
import MainButton from "./../Button/MainButton";
import { AiOutlineCloseCircle } from "react-icons/ai";
import { useState } from "react";
import { useSelector} from 'react-redux';
import { useNavigate} from "react-router-dom";
import { customAxios } from '../../modules/Other/Axios/customAxios';

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

export default function AlertModal({ isOpen, onClose, type}) {
  let width = 600;
  let height = 400;
  let content = "";
  const [title, setTitle] = useState("");
  const [date, setDate] = useState("");
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");
  const member_id = useSelector((state)=> state.UserReducer.memberId);
  let navigate = useNavigate();
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
      height = 400;
      content = (
        <>
          <ModalText> 스터디 일정 추가 </ModalText>
          <ModalText>
            제목
            <input onChange={(e) => setTitle(e.target.value)}></input>
          </ModalText>
          <ModalText>
            일시
            <input
              type="date"
              onChange={(e) => setDate(e.target.value)}
            ></input>
          </ModalText>
          <ModalText>
            시작시간{" "}
            <input type="time" onChange={(e) => setStart(e.target.value)} />
            종료시간{" "}
            <input type="time" onChange={(e) => setEnd(e.target.value)} />
          </ModalText>
        </>
      );
      break;
  }

  //확인버튼 onclick 함수
    function confirm({type}){
      switch(type){
        case "withdraw":
          customAxios()
          .delete(`/members/${member_id}`)
          .then(()=>{
            console.log("확인버튼 입력");
            navigate(`/`);
            localStorage.clear();
          })
          .catch();
          break;
      }
      onClose(false);
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
                  onClick={() => confirm({type})}
                ></MainButton>
                <MainButton content={"취소"} width={50} height={30}></MainButton>
              </Buttons>
            </ModalContent>
          </ModalOverlay>
        )}
      </>
    );
}
