import styled from "styled-components";
import UserProfile from "../Common/UserProfile";

const Container = styled.div`
  border: 1px solid black;
`;

export default function MemberBox({data}) {
  return (
  <Container>
    <UserProfile
      nickname={data.nickname}
      backgroundcolor={data.background}
      characterimg={data.character}
    ></UserProfile>
  </Container>
  );
}
