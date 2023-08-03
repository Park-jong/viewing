import React, { useState } from "react";
import styled from "styled-components";
import InputBox from "../Common/InputBox";
import DownloadFiles from "../Common/UploadFile";
import MainButton from "../Button/MainButton";

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

const CompanyAndJob = styled.p`
  font-size: 20px;
  margin-bottom: 7px;
`;

const Title = styled.p`
  font-size: 35px;
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

export default function ApplyModal({ isOpen, onClose }) {
  const [text, setText] = useState("");
  const [files, setFiles] = useState([]);

  const handleCloseModal = () => {
    onClose();
  };

  return (
    <>
      {isOpen && (
        <ModalOverlay>
          <ModalContent>
            <CompanyAndJob>삼성 SDS/ 머시깽이 직무</CompanyAndJob>
            <Title>스터디명 완전 좋아</Title>
            <AuthorAndDate>신청자 김김김 작성일 2023.07.24</AuthorAndDate>
            <InputBox
              width={578}
              height={200}
              setText={setText}
              text={text}
            ></InputBox>
            <DownloadFiles setFiles={setFiles} files={files}></DownloadFiles>
            <ButtonContainer>
              <MainButton
                content="신청하기"
                onClick={() => console.log(text, files)}
                fontSize={14}
                marginRight={20}
                width={80}
                height={40}
              />
              <MainButton
                content="취소하기"
                fontSize={14}
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
