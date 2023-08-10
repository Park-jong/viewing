import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

const WriteFeedbackTextarea = styled.textarea``;

export default function MeetingFeedbackBox({ writeFeedback, subscriber }) {
  return (
    <Container>
      <WriteFeedbackTextarea
        onChange={(e) => {
          writeFeedback(subscriber, e.target.value);
        }}
      ></WriteFeedbackTextarea>
    </Container>
  );
}
