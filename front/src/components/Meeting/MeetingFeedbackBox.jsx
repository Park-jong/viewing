import styled from "styled-components";

const Container = styled.div`
  /* border: 1px solid black; */
  padding: 10px;
`;

const WriteFeedbackTextarea = styled.textarea`
  width: 92%;
  height: 400px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
  resize: none;
`;

const DIV = styled.div`
  font-size: 13px;
  color: blue;
  margin-top: 10px;
  font-weight: 300;
`;

export default function MeetingFeedbackBox({ writeFeedback, subscriber }) {
  return (
    <Container>
      <WriteFeedbackTextarea
        onChange={(e) => {
          writeFeedback(subscriber, e.target.value);
        }}
      ></WriteFeedbackTextarea>
      <DIV>현재 위치를 벗어나셔도 피드백은 자동으로 저장됩니다.</DIV>
    </Container>
  );
}
