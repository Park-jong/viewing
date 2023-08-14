import styled from "styled-components"
import StudyCard from './../components/Study/StudyCard';
import { useEffect,useState} from 'react';
import { customAxios } from '../modules/Other/Axios/customAxios';
import { useSelector} from 'react-redux';
import TagStyled from "../components/Study/TagStyled";
const tags = [
  "자소서 제출 필수",
  "합격인증 필수",
  "압박면접",
  "정보공유",
  "영상회의 필수",
  "중고신입",
  "온라인 진행",
  "오프라인 진행",
  "온오프라인 혼합",
  "피드백 필수",
  "초보 환영",
];

const HorizontalLine = styled.div`
  width: 100%;
  height: 1px;
  background-color: var(--gray-200);
  margin-bottom: 20px;
`;

const TagContainer = styled.div`
  display: flex;
  justify-content: space-between;
  overflow-x: auto;

  padding-bottom: 20px;
`;

const BodyContainer = styled.div`
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
`;

const Container = styled.div`
display: flex;
flex-direction: column;
justify-content: space-between;
`;
export default function MypageStudy() {
  const member_id = useSelector((state)=> state.UserReducer.memberId);
  const [studyData,setStudyData] = useState([]);

  useEffect(()=>{
    customAxios()
    .get(
      `members/${member_id}/studies`
    )
    .then((res)=>{
      setStudyData(()=>res.data)
    })
    .catch(err => console.log(err));
  },[]);


  return (
    <Container>
      <BodyContainer>
        {studyData.length > 0 ? (
          studyData.map((study, idx) => (
            <StudyCard key={idx} study={study} />
          ))
        ) : (
          <p>가입한 스터디가 없습니다.</p>
        )}
      </BodyContainer>
    </Container>
  );
}
