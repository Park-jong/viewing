import styled from "styled-components";

const Container = styled.div`
  width: ${(props) => `${props.width}px`};
  height: ${(props) => `${props.height}px`};
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--primary);
  color: var(--gray-50);
  font-weight: 500;
  border-radius: 15px;
  font-size: ${(props) => `${props.fontSize}px`};
  margin-right: ${(props) => `${props.marginRight}px`};
`;

export default function MainButton({
  marginRight,
  width,
  height,
  fontSize,
  content,
  onClick,
}) {
  return <Container 
  onClick={onClick}
  marginRight={marginRight}
  width={width}
  height={height}
  fontSize={fontSize}
  >{content}</Container>;
}
