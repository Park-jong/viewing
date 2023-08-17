import React, { useEffect, useState } from "react";
import styled from "styled-components";
import InputBox from "../Common/InputBox";
import UploadFile from "../Common/UploadFile";
import MainButton from "../Button/MainButton";
import StudyCard from "./../Study/StudyCard";
import CompanyJobTag from "../Study/CompanyJobTag";
import { useSelector } from "react-redux";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { useLocation } from "react-router-dom";

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
  z-index: 12;
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  width: 600px;
  height: 430px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
`;

const Title = styled.p`
  font-size: 30px;
  font-weight: 700;
  margin-bottom: 7px;
`;

const AuthorAndDate = styled.p`
  font-size: 13px;
  margin-bottom: 10px;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
`;

const TextLength = styled.div`
  font-size: 10px;
`;

export default function ApplyModal({ isModalOpen, onClose, studyData }) {
  const [text, setText] = useState("");
  const [files, setFiles] = useState([]);
  const nickname = useSelector((state) => state.UserReducer.nickname);
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const token = localStorage.getItem("access_token");
  const study_id = useLocation().pathname.split("/")[2];
  const maxContentLength = 5000;

  const handleApply = () => {
    const formData = new FormData();
    const request = { member_id, content: text };
    // formData.append("member_id", member_id);
    // formData.append("content", text);
    files.forEach((file) => formData.append("request_files", file));

    formData.append(
      "request",
      new Blob([JSON.stringify(request)], { type: "application/json" })
    );

    customAxios()
      .post(`studies/${study_id}/requests`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: "Bearer " + token,
        },
      })
      .then(function (response) {
        alert("신청이 완료되었습니다.");
        handleCloseModal();
      })
      .catch((error) => {
        if (error.response.status === 400) {
          alert("이미 신청한 스터디입니다.");
        }
        console.error("에러가 발생했습니다.:", error);
        handleCloseModal();
      });
  };

  const handleCloseModal = () => {
    onClose();
  };

  const todayTime = () => {
    let now = new Date(); // 현재 날짜 및 시간
    let todayMonth = now.getMonth() + 1;
    let todayDate = now.getDate();
    const week = ["일", "월", "화", "수", "목", "금", "토"];
    let dayOfWeek = week[now.getDay()];
    return todayMonth + "월 " + todayDate + "일 " + dayOfWeek + "요일 ";
  };

  useEffect(() => {
    if (text.length > maxContentLength) {
      setText((prev) => prev.substring(0, maxContentLength));
    }
  }, [text]);

  return (
    <>
      {isModalOpen && (
        <ModalOverlay>
          <ModalContent>
            <CompanyJobTag
              company={studyData.applied_company}
              position={studyData.applied_job}
              career={studyData.career_level}
            />
            <Title>{studyData.title}</Title>
            <AuthorAndDate>
              신청자 {nickname} &nbsp;&nbsp;작성일 {todayTime()}
            </AuthorAndDate>
            <InputBox
              width={578}
              height={150}
              setText={setText}
              text={text}
            ></InputBox>
            <TextLength>
              {text.length}/{maxContentLength}
            </TextLength>
            <UploadFile setFiles={setFiles} files={files}></UploadFile>
            <ButtonContainer>
              <MainButton
                content="취소하기"
                fontSize={14}
                width={80}
                height={40}
                onClick={handleCloseModal}
                marginright={15}
              />
              <MainButton
                content="신청하기"
                onClick={handleApply}
                fontSize={14}
                marginRight={20}
                width={80}
                height={40}
              />
            </ButtonContainer>
          </ModalContent>
        </ModalOverlay>
      )}
    </>
  );
}
