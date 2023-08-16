import styled from "styled-components";
import UserProfile from "../Common/UserProfile";

const Container = styled.div`
  border: 1px solid var(--gray-200);
  /* background-color: var(--gray-200); */
  padding: 10px 10px;
`;

export default function MemberBox({ data }) {
  return (
    <Container>
      <UserProfile
        nickname={data.nickname}
        backgroundcolor={data.backgroundColor}
        characterimg={data.backgroundImg}
        member_id={data.member_id}
      ></UserProfile>
    </Container>
  );
}
