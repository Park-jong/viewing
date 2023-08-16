import styled from "styled-components";
import CamBox from "../CamBox";
import React from "react";
import Slide from "../Slide";

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

const Container2 = styled.div`
  border: 1px solid var(--gray-200);
  display: flex;
  /* flex-wrap: wrap; */
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  /* background-color: black; */
  /* flex-wrap: nowrap; nowrap으로 수정 */
  /* width: 100%; */

  /* border: 1px solid var(--gray-200); */
  /* display: flex; */
  /* width: 100%; */
  /* overflow-x: auto; */
`;

const MeContainer = styled.div``;

const MeCamContainer = styled.div`
  /* border: 5px solid black; */
  position: relative;
  width: 800px;
  /* width: auto; */
  height: 450px;
  border-radius: 15px;
`;

const OthersContainer = styled.div`
  display: flex;
  /* overflow-x: auto; 가로 스크롤 가능하도록 설정 */
  flex-wrap: nowrap; /* nowrap으로 수정 */
`;

const OthersCamContainer = styled.div`
  position: relative;
  width: 210px;
  /* width: auto; */
  height: 118px;
  border-radius: 15px;
  margin-right: 10px; /* 각 카메라 컨테이너 간 간격 */

  /* overflow: auto; 스크롤 적용 */
`;

export default function MeetingMain({ publisher, subscribers, view }) {
  console.log("subscribers: 레이아웃뷰" + subscribers);
  console.log("길이11111" + subscribers.length);

  return (
    <>
      {view === "layout" ? (
        <Container>
          <CamContainer>
            <CamBox streamManager={publisher}></CamBox>
          </CamContainer>

          {subscribers ? (
            subscribers.subs.map((sub, i) => {
              return (
                <CamContainer>
                  <CamBox streamManager={sub} key={i}></CamBox>
                </CamContainer>
              );
            })
          ) : (
            <></>
          )}
        </Container>
      ) : (
        //레이아웃뷰
        <Container2>
          <OthersContainer>
            {subscribers.subs.map((sub, i) => {
              return (
                <OthersCamContainer>
                  <CamBox streamManager={sub} key={i}></CamBox>
                </OthersCamContainer>
              );
            })}

            {/* <Slide subscribers={subscribers} /> */}
          </OthersContainer>
          <MeContainer>
            <MeCamContainer>
              <CamBox streamManager={publisher}></CamBox>
            </MeCamContainer>
          </MeContainer>
        </Container2>
      )}
    </>
  );
}
