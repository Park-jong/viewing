import { createBrowserRouter } from "react-router-dom";
import Home from "./pages/Home";
import NotFound from "./pages/NotFound";
import MypageEdit from "./pages/MypageEdit";
import Root from "./Root";
import MypageLike from "./pages/MypageLike";
import MypageBookmark from "./pages/MypageBookmark";
import MypageCalendar from "./pages/MypageCalendar";
import MypageComment from "./pages/MypageComment";
import MypageGet from "./pages/MypageGet";
import MypageMyarticle from "./pages/MypageMyarticle";
import MypageSend from "./pages/MypageSend";
import MypageStudy from "./pages/MypageStudy";
import MypageWithdraw from "./pages/MypageWithdraw";
import StudyPkAdmin from "./pages/StudyPkAdmin";
import StudyPkApplicant from "./pages/StudyPkApplicant";
import StudyPkBoard from "./pages/StudyPkBoard";
import StudyPkBoardPk from "./pages/StudyPkBoardPk";
import StudyPkBoardWrite from "./pages/StudyPkBoardWrite";
import StudyPkCalendar from "./pages/StudyPkCalendar";
import StudyPkChat from "./pages/StudyPkChat";
import StudyPkDetail from "./pages/StudyPkDetail";
import StudyPkInfo from "./pages/StudyPkInfo";
import StudyPkMeeting from "./pages/StudyPkMeeting";
import StudyPkWithdraw from "./pages/StudyPkWithdraw";
import Board from "./pages/Board";
import BoardFree from "./pages/BoardFree";
import BoardFreePk from "./pages/BoardFreePk";
import BoardInterview from "./pages/BoardInterview";
import BoardInterviewPk from "./pages/BoardInterviewPk";
import BoardQuestion from "./pages/BoardQuestion";
import BoardQuestionPk from "./pages/BoardQuestionPk";
import BoardWrite from "./pages/BoardWrite";
import Login from "./pages/Login";
import Search from "./pages/Search";
import Setimg from "./pages/Setimg";
import Setname from "./pages/Setname";
import MeetingPk from "./pages/MeetingPk";
import MeetingPkReady from "./pages/MeetingPkReady";
import MypageGetPk from "./pages/MypageGetPk";
import MypageSendPk from "./pages/MypageSendPk";
import KaKaoLogin from "./pages/KakaoLogin";
import GoogleLogin from "./pages/GoogleLogin";
import GitHubLogin from "./pages/GitHubLogin";
import SideBar from "./components/MyPage/SideBar";
import LoginLoading from "./pages/LoginLoading";
import BoardPk from "./pages/BoardPk";
import StudyPk from "./pages/StudyPk";
import StudySideBar from "./components/Study/StudySideBar";
import ConferencePage from "./testWebRTC/Pages/ConferencePage";
import ReadyPage from "./testWebRTC/Pages/ReadyPage";
import StudyPage from "./testWebRTC/Pages/StudyPage";
import BoardCommon from "./pages/BoardCommon";
import BoardUpdate from "./pages/BoardUpdate";
import MakeStudy from "./pages/Makestudy";
import Message from "./pages/Message";
import MypageArticles from "./pages/MypageArticles";
import MypageMessages from "./pages/MypageMessages";
import MessageDetail from "./pages/MessageDetail";
import StudyPkBoardUpdate from "./pages/StudyPkBoardUpdate";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/",
    element: <Root />,
    children: [
      {
        path: "mypage",
        element: <SideBar />,
        children: [
          {
            path: "edit",
            element: <MypageEdit />,
          },
          {
            path: "like",
            element: <MypageArticles />,
          },
          {
            path: "bookmark",
            element: <MypageBookmark />,
          },
          {
            path: "calendar",
            element: <MypageCalendar />,
          },
          {
            path: "comment",
            element: <MypageArticles />,
          },
          {
            path: "get",
            element: <MypageMessages />,
          },
          {
            path: "get/:messagePk",
            element: <MessageDetail />,
          },
          {
            path: "myarticle",
            element: <MypageArticles />,
          },
          {
            path: "send",
            element: <MypageMessages />,
          },
          {
            path: "send/:messagePk",
            element: <MessageDetail />,
          },
          {
            path: "study",
            element: <MypageStudy />,
          },
          {
            path: "withdraw",
            element: <MypageWithdraw />,
          },
        ],
      },
      { path: "makestudy", element: <MakeStudy /> },
      {
        path: "study/:studyPk/detail",
        element: <StudyPkDetail />,
      },
      {
        path: "study/:studyPk",
        element: <StudySideBar />,
        children: [
          {
            path: "",
            element: <StudyPk />,
          },
          {
            path: "admin",
            element: <StudyPk />,
          },
          {
            path: "applicant",
            element: <StudyPkApplicant />,
          },
          {
            path: "board",
            element: <StudyPkBoard />,
          },
          {
            path: "board/:boardPk",
            element: <StudyPkBoardPk />,
          },
          {
            path: "board/:boardPk/update",
            element: <StudyPkBoardUpdate />,
          },
          {
            path: "board/write",
            element: <StudyPkBoardWrite />,
          },
          {
            path: "calendar",
            element: <StudyPkCalendar />,
          },
          {
            path: "chat",
            element: <StudyPkChat />,
          },

          {
            path: "info",
            element: <StudyPk />,
          },
          {
            path: "meeting",
            element: <StudyPkMeeting />,
          },
          {
            path: "withdraw",
            element: <StudyPkWithdraw />,
          },
        ],
      },
      {
        path: "board",
        element: <Board />,
      },
      {
        path: "board/free",
        element: <BoardCommon />,
      },
      {
        path: "board/free/:articlePk",
        element: <BoardPk />,
      },
      {
        path: "board/free/:articlePk/update",
        element: <BoardUpdate />,
      },
      {
        path: "board/interview",
        element: <BoardCommon />,
      },
      {
        path: "board/interview/:articlePk",
        element: <BoardPk />,
      },
      {
        path: "board/interview/:articlePk/update",
        element: <BoardUpdate />,
      },
      {
        path: "board/question",
        element: <BoardCommon />,
      },
      {
        path: "board/question/:articlePk",
        element: <BoardPk />,
      },
      {
        path: "board/question/:articlePk/update",
        element: <BoardUpdate />,
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
      },
    ],
    errorElement: <NotFound />,
  },
  {
    path: "meeting/:meetingPK",
    element: <MeetingPk />,
  },
  {
    path: "meeting/:meetingPK/ready",
    element: <MeetingPkReady />,
  },
  {
    path: "login/kakao",
    element: <KaKaoLogin />,
  },
  {
    path: "login/google",
    element: <GoogleLogin />,
  },
  {
    path: "login/github",
    element: <GitHubLogin />,
  },
  {
    path: "login/loading",
    element: <LoginLoading />,
  },
  {
    path: "test/main",
    element: <ConferencePage />,
  },
  {
    path: "test",
    element: <StudyPage />,
  },
  {
    path: "test/ready",
    element: <ReadyPage />,
  },
  {
    path: "message",
    element: <Message />,
  },
]);

export default router;
