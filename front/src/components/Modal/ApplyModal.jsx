import React, { useState } from "react";
import styled from "styled-components";
import InputBox from "../Common/InputBox";
import DownloadFiles from "../Common/UploadFile";
import MainButton from "../Button/MainButton";
import StudyCard from "./../Study/StudyCard";
import CompanyJobTag from "../Study/CompanyJobTag";
import { useSelector } from "react-redux";
import { customAxios } from "../../modules/Other/Axios/customAxios";

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

export default function ApplyModal({ isModalOpen, onClose, studyData }) {
  const [text, setText] = useState("");
  const [files, setFiles] = useState([]);
  const nickname = useSelector((state) => state.UserReducer.nickname);

  const handleInputChange = (event) => {
    setText(event.target.value);
  };

  const handleApply = () => {
    
    const applicationData = {
      study_id: studyData.study_id,
      text: text,
      files: files,
    };

    const axiosInstance = customAxios();

    axiosInstance
      .post(`studies/${studyData.study_id}/requests`, applicationData)
      .then((response) => {
        console.log("Application submitted successfully!");
        handleCloseModal();
      })
      .catch((error) => {
        console.error("Error submitting application:", error);
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
              onChange={handleInputChange} 

            ></InputBox>
            <DownloadFiles setFiles={setFiles} files={files}></DownloadFiles>
            <ButtonContainer>
              <MainButton
                content="취소하기"
                fontSize={14}
                width={80}
                height={40}
                onClick={handleCloseModal}
              />
              &nbsp;&nbsp;
              <MainButton
                content="신청하기"
                onClick={handleApply} {/* Call handleApply function when button is clicked */}
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
