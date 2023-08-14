import styled from "styled-components";
import { BiSolidNote } from "react-icons/bi";

const Container = styled.div`
  border: 1px solid var(--gray-200);
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-100);
`;

export default function FeedbackButton({ toggleSideBar, changeOption }) {
  return (
    <Container
      onClick={() => {
        toggleSideBar(false);
        changeOption("feedback");
      }}
    >
      &nbsp; <BiSolidNote />
      &nbsp;&nbsp; 피드백 &nbsp;&nbsp;
    </Container>
  );
}
