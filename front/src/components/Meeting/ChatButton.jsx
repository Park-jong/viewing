import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

export default function ChatButton({ toggleSideBar, changeOption }) {
  return (
    <Container
      onClick={() => {
        toggleSideBar(false);
        changeOption("chat");
      }}
    >
      {"채팅"}
    </Container>
  );
}
