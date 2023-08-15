import { styled } from "styled-components";
import { FakeData } from "../components/Common/FakeData";
import CalendarTemplate from "../components/Common/Organisms/CalendarTemplate";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useSelector } from "react-redux";
import { UserReducer } from './../modules/UserReducer/UserReducer';
import MainButton from "../components/Button/MainButton";
import AlertModal from './../components/Modal/AlertModal';
import moment from "moment";

const Container = styled.div`
  width: 820px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default function MypageCalendar() {
  const [modal, setModal] = useState(false)
  const [value, onChange] = useState(new Date());

  return (
    <Container>
      <CalendarTemplate isFlex={false} value={value} onChange={onChange} modal={modal} setModal={setModal}></CalendarTemplate>
      
      <AlertModal isOpen={modal} onClose={() => setModal(false)} type={"schedule"} value={value} onChange={onChange}></AlertModal>
    </Container>
  );
}
