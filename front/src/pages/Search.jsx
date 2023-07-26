import styled from "styled-components";
import StudyCard from "../components/Study/StudyCard";

const Container = styled.div`
  display: flex;
  justify-content: space-between;
`;

export default function Search() {
  return (
    <Container>
      <StudyCard></StudyCard>
      <StudyCard></StudyCard>
      <StudyCard></StudyCard>
      <StudyCard></StudyCard>
    </Container>
  );
}
