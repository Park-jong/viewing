import { styled } from "styled-components";
import { useState } from "react";
import { useSelector } from "react-redux";

const Container = styled.div`
  height: 25px;
  border: 1px solid #ccc;
  text-align: center;
  padding-top: 6px;
  cursor: pointer;

  &:hover {
    background-color: #ccc;
  }
`;

export default function MiniMenu({ content, member_id, to }) {
  const author_id = useSelector((state) => state.UserReducer.member_id);
  const token = localStorage.getItem("access_token");

  const handleImageClick = () => {
    const newWindow = window.open(
      `/message?member_id=${member_id}&nickname=${to}`,
      "_blank",
      "width=480,height=480"
    );
  };

  return <Container onClick={handleImageClick}>{content}</Container>;
}
