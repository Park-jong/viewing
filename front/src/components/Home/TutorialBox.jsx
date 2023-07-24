import styled from "styled-components";

const Container = styled.div`
  width: 100%;
  background-color: red;
`;

const ContainerFirst = styled.div`
  width: 100%;
`;

const ImgContainer = styled.div`
  width: 200px;
  height: 200px;
`;
const ImgStyled = styled.div`
  background-image: url("/main.png"); /* Replace with the correct path to your image */
`;
const TextContainer = styled.div`
  width: 100%;
`;
const TitleContainer = styled.div`
  width: 100%;
`;
const WritingContainer = styled.div``;

const ContainerSecond = styled.div``;

export default function TutorialBox() {
  return (
    <Container>
      <ContainerFirst>
        <ImgContainer>
          <ImgStyled />
        </ImgContainer>

        <TextContainer>
          <TitleContainer>아아아아아</TitleContainer>
          <WritingContainer>아아아아아</WritingContainer>
        </TextContainer>
      </ContainerFirst>
      <ContainerSecond>
        <TextContainer>
          <TitleContainer></TitleContainer>
          <WritingContainer></WritingContainer>
        </TextContainer>
        <ImgContainer>
          <ImgStyled />
        </ImgContainer>
      </ContainerSecond>
    </Container>
  );
}
