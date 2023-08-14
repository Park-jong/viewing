import styled from "styled-components";
import { BsFillChatFill } from "react-icons/bs";

const Container = styled.div`
  border: 1px solid var(--gray-200);
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-100);
`;

export default function ChatButton({ toggleSideBar, changeOption }) {
  return (
    <Container
      onClick={() => {
        toggleSideBar(false);
        changeOption("chat");
      }}
    >
      &nbsp;
      <BsFillChatFill />
      &nbsp;&nbsp;채팅&nbsp;&nbsp;
    </Container>
  );
}
