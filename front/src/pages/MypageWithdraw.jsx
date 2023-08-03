import { useState } from "react";
import MainButton from "../components/Button/MainButton";
import AlertModal from './../components/Modal/AlertModal';

export default function MypageWithdraw() {
  const [isOpen, setIsOpen] = useState(false)

  return <>
    <MainButton width={100} height={50} content={"탈퇴하기"} onClick={() => setIsOpen(true)}></MainButton>
    <AlertModal isOpen={isOpen} onClose={setIsOpen} type={"withdraw"}></AlertModal>
  </>;
}
