import styled from "styled-components";
import CamBox from "../CamBox";
import React from "react";

const Container = styled.div`
  border: 1px solid var(--gray-200);
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  width: 100%;
  height: 100%;
`;

const CamContainer = styled.div`
  /* border: 5px solid black; */
  position: relative;
  width: 360px;
  /* width: auto; */
  height: 202.5px;
  border-radius: 15px;
`;

function MeetingMain({ publisher, subscribers, view }) {
  return (
    <>
      {view === "layout" ? (
        <Container>
          <CamContainer>
            <CamBox streamManager={publisher}></CamBox>
          </CamContainer>

          {subscribers.subs.map((sub, i) => {
            return (
              <CamContainer>
                <CamBox streamManager={sub} key={i}></CamBox>
              </CamContainer>
            );
          })}
        </Container>
      ) : (
        <div>발표자보기</div>
      )}
    </>
  );
}

export default React.memo(MeetingMain);
