import { styled } from "styled-components";
import { useLocation } from "react-router-dom";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import { useSelector } from "react-redux";
import { useNavigate, useOutletContext } from "react-router-dom";

const Container = styled.div``;

const Guide = styled.div``;

const ExitButton = styled.button``;

export default function StudyPkWithdraw() {
  const location = useLocation();

  const studyId = location.pathname.split("/")[2];

  const member = useSelector((state) => state.UserReducer);

  const navigate = useNavigate();

  const { isLeader } = useOutletContext();

  const exitStudy = () => {
    if (window.confirm("탈퇴 시 삭제된 정보는 되돌릴 수 없습니다.")) {
      customAxios()
        .delete(`studies/${studyId}/members/${member.memberId}/exit`)
        .then(() => {
          alert("탈퇴하였습니다.");
          navigate("/");
        })
        .catch(() => {
          alert("실패하였습니다.");
        });
    }
  };

  return (
    <Container>
      {isLeader ? (
        <>스터디장은 탈퇴할 수 없습니다.</>
      ) : (
        <>
          <Guide>정말 탈퇴하시겠습니까?</Guide>
          <ExitButton
            onClick={() => {
              exitStudy();
            }}
          >
            탈퇴하기
          </ExitButton>
        </>
      )}
    </Container>
  );
}
