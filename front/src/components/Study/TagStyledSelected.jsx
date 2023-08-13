import styled from "styled-components";

const ContainerTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: fit-content;
  height: 20px;
  background-color: var(--gray-500);
  color: var(--secondary);

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

export default function TagStyled({ content }) {
  return <ContainerTag>{content}</ContainerTag>;
}
