import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

export default function MembersButton({ toggleSideBar, changeOption }) {
  return (
    <Container
      onClick={() => {
        toggleSideBar(false);
        changeOption("member");
      }}
    >
      {"참여자"}
    </Container>
  );
}
