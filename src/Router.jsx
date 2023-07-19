import { createBrowserRouter } from "react-router-dom";
import Home from "./pages/Home";
import NotFound from "./pages/NotFound";
import MypageEdit from "./pages/MypageEdit";
import Root from "./Root";
import MypageLike from './pages/MypageLike';
import MypageBookmark from './pages/MypageBookmark';
import MypageCalendar from "./pages/MypageCalendar";
import MypageComment from './pages/MypageComment';
import MypageGet from './pages/MypageGet';
import MypageMyarticle from './pages/MypageMyarticle';
import MypageSend from './pages/MypageSend';
import MypageStudy from './pages/MypageStudy';
import MypageWithdraw from './pages/MypageWithdraw';
import StudyPkAdmin from './pages/StudyPkAdmin';
import StudyPkApplicant from './pages/StudyPkApplicant';
import StudyPkBoard from './pages/StudyPkBoard';
import StudyPkBoardPk from './pages/StudyPkBoardPk';
import StudyPkBoardWrite from './pages/StudyPkBoardWrite';
import StudyPkCalendar from './pages/StudyPkCalendar';
import StudyPkChat from './pages/StudyPkChat';
import StudyPkDetail from './pages/StudyPkDetail';
import StudyPkInfo from './pages/StudyPkInfo';
import StudyPkMeeting from './pages/StudyPkMeeting';
import StudyPkWithdraw from './pages/StudyPkWithdraw';
import Board from "./pages/Board";
import BoardFree from './pages/BoardFree';
import BoardFreePk from './pages/BoardFreePk';
import BoardInterview from './pages/BoardInterview';
import BoardInterviewPk from './pages/BoardInterviewPk';
import BoardNotice from './pages/BoardNotice';
import BoardNoticePk from './pages/BoardNoticePk';
import BoardQuestion from './pages/BoardQuestion';
import BoardQuestionPk from './pages/BoardQuestionPk';
import BoardWrite from './pages/BoardWrite';
import Login from './pages/Login';
import Search from './pages/Search';
import Setimg from './pages/Setimg';
import Setname from "./pages/Setname";
import MeetingPk from "./pages/MeetingPk";
import MeetingPkReady from "./pages/MeetingPkReady";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "mypage/edit",
        element: <MypageEdit />,
      },
      {
        path: "mypage/like",
        element: <MypageLike />,
      },
      {
        path: "mypage/bookmark",
        element: <MypageBookmark />,
      },
      {
        path: "mypage/calendar",
        element: <MypageCalendar />,
      },
      {
        path: "mypage/comment",
        element: <MypageComment />,
      },
      {
        path: "mypage/get",
        element: <MypageGet />,
      },
      {
        path: "mypage/myarticle",
        element: <MypageMyarticle />,
      },
      {
        path: "mypage/send",
        element: <MypageSend />,
      },
      {
        path: "mypage/study",
        element: <MypageStudy />,
      },
      {
        path: "mypage/withdraw",
        element: <MypageWithdraw />,
      },
      {
        path: "study/:studyPk/admin",
        element: <StudyPkAdmin />,
      },
      {
        path: "study/:studyPk/applicant",
        element: <StudyPkApplicant />,
      },
      {
        path: "study/:studyPk/board",
        element: <StudyPkBoard />,
      },
      {
        path: "study/:studyPk/board/:boardPk",
        element: <StudyPkBoardPk />,
      },
      {
        path: "study/:studyPk/board/write",
        element: <StudyPkBoardWrite />,
      },
      {
        path: "study/:studyPk/calendar",
        element: <StudyPkCalendar />,
      },
      {
        path: "study/:studyPk/chat",
        element: <StudyPkChat />,
      },
      {
        path: "study/:studyPk/detail",
        element: <StudyPkDetail />,
      },
      {
        path: "study/:studyPk/info",
        element: <StudyPkInfo />,
      },
      {
        path: "study/:studyPk/meeting",
        element: <StudyPkMeeting />,
      },
      {
        path: "study/:studyPk/withdraw",
        element: <StudyPkWithdraw />,
      },
      {
        path: "board",
        element: <Board />,
      },
      {
        path: "board/free",
        element: <BoardFree />,
      },
      {
        path: "board/free/:articlePk",
        element: <BoardFreePk />,
      },
      {
        path: "board/interview",
        element: <BoardInterview />,
      },
      {
        path: "board/interview/:articlePk",
        element: <BoardInterviewPk />,
      },
      {
        path: "board/notice",
        element: <BoardNotice />,
      },
      {
        path: "board/notice/:articlePk",
        element: <BoardNoticePk />,
      },
      {
        path: "board/question",
        element: <BoardQuestion />,
      },
      {
        path: "board/question/:articlePk",
        element: <BoardQuestionPk />,
      },
      {
        path: "board/write",
        element: <BoardWrite />,
      },
      {
        path: "login",
        element: <Login />,
      },
      {
        path: "search",
        element: <Search />,
      },
      {
        path: "setimg",
        element: <Setimg />,
      },
      {
        path: "setname",
        element: <Setname />,
      }
    ],
    errorElement: <NotFound />,
  },
  {
    path: "meeting/:meetingPK",
    element: <MeetingPk/>,
  },
  {
    path: "meeting/:meetingPK/ready",
    element: <MeetingPkReady/>,
  }

]);

export default router;
