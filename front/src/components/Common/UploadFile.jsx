import { useState } from "react";
import { styled } from "styled-components";
import MainButton from "../Button/MainButton";
import SubButton from "../Button/SubButton";

const Container = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  width: 100%;
  background-color: white;
`;

const InputButton = styled.div`
  height: 25px;
  width: 100px;
  background-color: var(--primary);
  color: var(--gray-50);
  text-align: center;
  padding-top: 10px;
  border-radius: 15px;
  border: none;
  font-weight: 300;
  font-size: 15px;
`;

const CurrentFile = styled.div`
  display: flex;
  align-items: center;
  overflow: hidden;
  width: 400px;
  height: 50px;
`;

const SelectedFile = styled.div`
  display: flex;
  align-items: center;
`;

const FileName = styled.div`
  margin-right: 10px;
  max-width: 300px;
  padding-top: 8px;
  height: 25px;
  overflow: hidden;
  white-space: nowrap; // 줄바꿈 없애기
`;

const Input = styled.input`
  display: none;
`;

export default function UploadFile({ width, height, setFiles, files }) {
  const handleFileChange = (e) => {
    setFiles([...files, ...e.target.files]);

  };
  const deleteFile = (file, files) => {
    files.splice(files.findIndex((f) => f===file), 1)
    const temp = [...files]
    setFiles(temp);
  };

  return (
    <Container>
      <CurrentFile>
        {files.length > 0 ? (
          files.map((file, idx) => 
          <SelectedFile key={idx}>
            <FileName>{file.name}</FileName>
            <div onClick={() => deleteFile(file, files)}>
              <SubButton content="삭제하기" ></SubButton>   
            </div>
          </SelectedFile>)
        ) : (
          "파일을 업로드하세요."
        )}
      </CurrentFile>
      <Input
        type="file"
        name="file"
        id="uploadFile"
        onChange={handleFileChange}
      />
      <label htmlFor="uploadFile">
        <InputButton>파일 업로드</InputButton>
      </label>
    </Container>
  );
}
