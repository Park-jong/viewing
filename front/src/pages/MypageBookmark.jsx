import { styled } from "styled-components";
import StudyCard from "../components/Study/StudyCard";

const CardContainer = styled.div`
  display: flex;
  overflow: scroll;
  flex-wrap: wrap;
`

export default function MypageBookmark() {
  return <CardContainer>
    <StudyCard></StudyCard>
    <StudyCard></StudyCard>
    <StudyCard></StudyCard>
    <StudyCard></StudyCard>
    <StudyCard></StudyCard>
  </CardContainer>;
}
