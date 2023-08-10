import React from "react";

function StudyPage() {
  const CLIENT_URL = process.env.REACT_APP_CLIENT_URL;

  return (
    <>
      <h1>스터디페이지(영상회의입장 버튼을 누르는 곳)</h1>
      <button
        onClick={() => {
          window.location.replace(`/test/ready`);
        }}
      >
        <h1>입장</h1>
      </button>
    </>
  );
}

export default StudyPage;
