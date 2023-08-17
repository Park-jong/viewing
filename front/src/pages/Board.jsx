import { useDispatch, useSelector } from "react-redux";
import { ChangeToggle } from "../modules/UserReducer/Actions";
import AlertModal from "./../components/Modal/AlertModal";
import { useState } from "react";

export default function Board() {
  const [isOpen, setIsOpen] = useState(true);

  return (
    <>
      <AlertModal isOpen={isOpen} onClose={setIsOpen}></AlertModal>
    </>
  );
}
