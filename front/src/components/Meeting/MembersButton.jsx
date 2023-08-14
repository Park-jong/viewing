import styled from "styled-components";

import { PiUsersThreeFill } from "react-icons/pi";

const Container = styled.div`
  border: 1px solid var(--gray-200);
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-100);
`;

export default function MembersButton({ toggleSideBar, changeOption }) {
  return (
    <Container
      onClick={() => {
        toggleSideBar(false);
        changeOption("member");
      }}
    >
      &nbsp; <PiUsersThreeFill />
      &nbsp; &nbsp;참여자&nbsp;&nbsp;
    </Container>
  );
}
