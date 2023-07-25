import styled from "styled-components";
import Logo from "../../Icons/Logo";
import Alarm from "../../Icons/Alarm";
// import ProfileImg from "./ProfileImg";
import { Link } from "react-router-dom";
import DropProfile from "./DropProfile";
import DropAlert from "./DropAlert";

const HeaderStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 64px;
  background-color: white; /* Optional: Add a background color for the header */
`;

const FirstContainer = styled(Link)`
  padding-left: 40px;
`;

const SecondContainer = styled.div`
  display: flex;
`;

const MenuItem = styled(Link)`
  margin-left: 20px; /* Add some space between menu items */
  cursor: pointer; /* Optional: Show pointer cursor when hovering over the menu items */
  text-decoration: none; /* Optional: Remove default link underline */
`;

const ThirdContainer = styled.div`
  padding-right: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const RightMenu = styled.div`
  display: flex;
  align-items: center;
`;

const MarginRight = styled.div`
  margin-right: 20px;
`;

export default function HeaderBox() {
  const isSocialLogin = true; // true라고 가정하겠습니다. 실제 상태는 알맞게 설정해주세요.

  return (
    <HeaderStyle>
      <FirstContainer to={"/"}>
        <Logo />
      </FirstContainer>
      <SecondContainer>
        <MenuItem to="/search">스터디</MenuItem>
        <MenuItem to="/board">커뮤니티</MenuItem>
      </SecondContainer>
      {/* 로그인 상태에 따라 알맞은 내용 표시 */}
      {isSocialLogin ? (
        <ThirdContainer>
          <RightMenu>
            <MarginRight>
              <DropAlert />
            </MarginRight>
            <DropProfile />
          </RightMenu>
        </ThirdContainer>
      ) : (
        <ThirdContainer>로그인/회원가입</ThirdContainer>
      )}
    </HeaderStyle>
  );
}
