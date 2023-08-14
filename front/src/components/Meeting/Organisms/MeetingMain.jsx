import styled from "styled-components";
import CamBox from "../CamBox";
import React from "react";

const Container = styled.div`
  border: 1px solid black;
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 100%;
`;

const CamContainer = styled.div`
  border: 5px solid black;
  position: relative;
  width: 360px;
  height: 202.5px;
`;

function MeetingMain({ publisher, subscribers, getActive }) {
  return (
    <Container>
      <CamContainer>
        <CamBox streamManager={publisher} getActive={getActive}></CamBox>
      </CamContainer>
      {subscribers.subs.map((sub, i) => {
        return (
          <CamContainer>
            <CamBox streamManager={sub} key={i} getActive={getActive}></CamBox>
          </CamContainer>
        );
      })}
    </Container>
  );
}

export default React.memo(MeetingMain);
