import GithubButton from "../components/Button/GithubButton";
import GoogleButton from "../components/Button/GoogleButton";
import KakaoButton from "../components/Button/KakaoButton";
import MainButton from "../components/Button/MainButton";
import Calendar from "../components/Common/Calendar";
import CalenderTemplate from "../components/Common/Organisms/CalenderTemplate";
import TimeBar from "../components/Common/TimeBar";
import SubButton from "./../components/Button/SubButton";
import ReactCalendar from "react-calendar";


export default function Login() {
  return (
    <>
      {/* <GithubButton></GithubButton>
      <br />
      <GoogleButton></GoogleButton>
      <br />
      <KakaoButton></KakaoButton>
      <br />
      <MainButton content="다음으로" />
      <br />
      <SubButton content="스터디 참가하기"></SubButton>
      <br />
      <SubButton content="정보보기"></SubButton>
      <br /> */}
      {/* <Calendar></Calendar> */}
      {/* <ReactCalendar></ReactCalendar> */}
      {/* <TimeBar></TimeBar> */}
      <CalenderTemplate></CalenderTemplate>
    </>
  );
}
