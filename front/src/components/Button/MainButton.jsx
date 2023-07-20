import styled from "styled-components";

const Container = styled.div`
  width: 120px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--primary);
  color: var(--gray-50);
  font-weight: 500;
  border-radius: 15px;

`;

export default function MainButton(props) {
  return <Container>{props.content}</Container>
}