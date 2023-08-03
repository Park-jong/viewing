import styled from "styled-components";

// Define the styled component for the button
const Container = styled.button`
  width: ${(props) => `${props.width}px`};
  height: ${(props) => `${props.height}px`};

  background: var(--secondary);
  font-family: inherit;
  padding: 0.6em 1.3em;
  font-weight: 600;
  border: 3px solid black;
  border-radius: 0.4em;
  box-shadow: 0.1em 0.1em;

  font-size: ${(props) => `${props.fontSize}px`};
  margin-right: ${(props) => `${props.marginright}px`};

  /* Add styles for :hover state */
  &:hover {
    transform: translate(-0.05em, -0.05em);
    box-shadow: 0.15em 0.15em;
  }

  /* Add styles for :active state */
  &:active {
    transform: translate(0.05em, 0.05em);
    box-shadow: 0.05em 0.05em;
  }
`;

const Span = styled.span`
  color: white;
`;

export default function StyledButton({
  marginright,
  width,
  height,
  fontSize,
  content,
  onClick,
}) {
  return (
    <Container
      onClick={onClick}
      marginright={marginright}
      width={width}
      height={height}
      fontSize={fontSize}
    >
      {/* <Span>{content}</Span> */}
      {content}
    </Container>
  );
}
