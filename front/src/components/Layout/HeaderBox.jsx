import styled from "styled-components";
import Logo from "../../Icons/Logo";
import Alarm from "../../Icons/Alarm";
// import ProfileImg from "./ProfileImg";
import { Link, useLocation } from "react-router-dom";
import DropProfile from "./DropProfile";
import DropAlert from "./DropAlert";
import { useSelector } from "react-redux";
import { UserReducer } from "./../../modules/UserReducer/UserReducer";

const HeaderStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 64px;
  background-color: white; /* Optional: Add a background color for the header */
  border-bottom: 0.5px var(--gray-200) solid;
`;

const FirstContainer = styled(Link)`
  padding-left: 170px;
  text-decoration: none;
`;

const LogoStyled = styled.div`
  font-family: "HSSummer";
  text-decoration: none;
  color: var(--primary);
  font-size: 25px;
`;

const SecondContainer = styled.div`
  display: flex;
  text-decoration: none; /* Optional: Remove default link underline */
`;

const MenuItem = styled(Link)`
  margin-left: 20px; /* Add some space between menu items */
  cursor: pointer; /* Optional: Show pointer cursor when hovering over the menu items */
  text-decoration: none; /* Optional: Remove default link underline */
  color: var(--gray-800);
`;

const ThirdContainer = styled(Link)`
  padding-right: 170px;
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
  cursor: pointer;
  color: var(--gray-800);
`;

const ForthContainer = styled.div`
  padding-right: 170px;
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
  cursor: pointer;
  color: var(--gray-800);
`;

const RightMenu = styled.div`
  display: flex;
  align-items: center;
`;

const MarginRight = styled.div`
  margin-right: 20px;
`;

export default function HeaderBox() {
  const member = useSelector((state) => state.UserReducer);
  const isSocialLogin = member.memberId;
  const location = useLocation();
  const clickHandle = () => {
    if (location.pathname === "/search") window.location.href = "/search";
  };
  return (
    <HeaderStyle>
      <FirstContainer to={"/"}>
        {/* <Logo /> */}
        <LogoStyled>뷰잉</LogoStyled>
      </FirstContainer>
      <SecondContainer>
        <MenuItem
          to="/search"
          onClick={() => {
            clickHandle();
          }}
        >
          스터디
        </MenuItem>
        <MenuItem to="/board/free">커뮤니티</MenuItem>
      </SecondContainer>
      {/* 로그인 상태에 따라 알맞은 내용 표시 */}
      {isSocialLogin ? (
        <ForthContainer>
          <RightMenu>
            <MarginRight>
              <DropAlert />
            </MarginRight>
            <DropProfile member={member} />
          </RightMenu>
        </ForthContainer>
      ) : (
        <ThirdContainer to={"/login"}>로그인/회원가입</ThirdContainer>
      )}
    </HeaderStyle>
  );
}
