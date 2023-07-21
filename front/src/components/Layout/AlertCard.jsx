//안씀 !! DropAlert에 한번에 구현할게요

// import styled from "styled-components";
// import { Link } from "react-router-dom";
// import { useState } from "react";
// // props로 message와 is_read로 받는다.

// const Container = styled(Link)`
//   width: 430px;
//   height: 150px;
//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
//   background-color: var(--gray-50);
//   color: var(--gray-700);
//   font-weight: 300;
//   border: 1px var(--gray-200) solid;

//   text-decoration: none;
// `;

// const TitleContainer = styled.div``;

// const DateContainer = styled.div``;

// export default function AlertCard() {
//   // Function to get the appropriate link based on the alarm_type
//   const [alert, setAlert] = useState("");

//   // const getLink = () => {
//   //   switch (alert.alarm_type) {
//   //     case "message":
//   //       return "/mypage/get";
//   //     case "approve":
//   //       return "/link-for-alarm-type-2";
//   //     case "apply":
//   //       return "/link-for-alarm-type-3";
//   //     case "studyArticle":
//   //       return "/link-for-alarm-type-4";
//   //     case "studyMeeting":
//   //       return "/link-for-alarm-type-5";
//   //     case "studyComment":
//   //       return "/link-for-alarm-type-5";
//   //     case "studyReply":
//   //       return "/link-for-alarm-type-5";
//   //     case "boardComment":
//   //       return "/link-for-alarm-type-5";
//   //     case "boardReply":
//   //       return "/link-for-alarm-type-5";
//   //     case "leader":
//   //       return "/link-for-alarm-type-5";
//   //     default:
//   //       return "/";
//   //   }
//   // };

//   // const getMessageByAlarmType = () => {
//   //   switch (alert.alarm_type) {
//   //     case "message":
//   //       return "새로운 쪽지가 도착했습니다.";
//   //     case "approve":
//   //       return "신청하신 스터디가 승인되었습니다.";
//   //     case "apply":
//   //       return "스터디에 가입신청이 도착했습니다.";
//   //     case "studyArticle":
//   //       return "스터디에 새로운 게시글이 작성되었습니다.";
//   //     case "studyMeeting":
//   //       return "스터디 모임이 예정되었습니다.";
//   //     case "studyComment":
//   //       return "스터디 게시글에 댓글이 달렸습니다.";
//   //     case "studyReply":
//   //       return "스터디 게시글 댓글에 답글이 달렸습니다.";
//   //     case "boardComment":
//   //       return "커뮤니티 게시글에 댓글이 달렸습니다.";
//   //     case "boardReply":
//   //       return "커뮤니티 게시글 댓글에 답글이 달렸습니다.";
//   //     case "leader":
//   //       return "스터디 리더로 지정되었습니다.";
//   //     default:
//   //       return "알 수 없는 알림입니다.";
//   //   }
//   // };

//   return (
//     // < to={getLink(alarm_type)}>
//     <Container>
//       {/* <TitleContainer>{alert.alarm_type}</TitleContainer> */}
//       <TitleContainer>새로운 쪽지가 도착했습니다.</TitleContainer>
//       <DateContainer>2020.02.02</DateContainer>
//     </Container>
//   );
// }
