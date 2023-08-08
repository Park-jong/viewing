import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

export default function BeforeExitModal() {
  return <Container>{"나가기전에 나오는 모달"}</Container>;
}
