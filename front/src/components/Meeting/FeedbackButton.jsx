import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

export default function FeedbackButton({ toggleSideBar, changeOption }) {
  return (
    <Container
      onClick={() => {
        toggleSideBar(false);
        changeOption("feedback");
      }}
    >
      {"피드백"}
    </Container>
  );
}
