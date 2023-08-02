import styled from "styled-components";
import Logo from "../../Icons/Logo";
import Alarm from "../../Icons/Alarm";
// import ProfileImg from "./ProfileImg";
import { Link } from "react-router-dom";
import DropProfile from "./DropProfile";
import DropAlert from "./DropAlert";
import { useEffect } from "react";
import { useState } from "react";

const HeaderStyle = styled.div`
  position: fixed;
  top: 0; /* Align the top edge to the top of the viewport */
  left: 0; /* Align the left edge to the left of the viewport */
  right: 0; /* Align the right edge to the right of the viewport */

  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 64px;
  /* background-color: transparent; Optional: Add a background color for the header */
  background-color: ${(props) =>
    props.isScrolled ? "#ffffff" : "transparent"};
  transition: background-color 0.3s ease; /* Add a smooth transition effect for background color change */

  z-index: 99;
  /* border-bottom: 0.5px solid #d5d5d573; */
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

const ThirdContainer = styled.div`
  padding-right: 170px;
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

export default function HeaderHome() {
  const [isScrolled, setIsScrolled] = useState(false);

  // Handle scroll event and update isScrolled state
  const handleScroll = () => {
    if (window.scrollY > 0) {
      setIsScrolled(true);
    } else {
      setIsScrolled(false);
    }
  };

  useEffect(() => {
    // Add event listener when the component mounts
    window.addEventListener("scroll", handleScroll);

    // Remove event listener when the component unmounts to avoid memory leaks
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  const isSocialLogin = true; // true라고 가정하겠습니다. 실제 상태는 알맞게 설정해주세요.

  return (
    <HeaderStyle isScrolled={isScrolled}>
      <FirstContainer to={"/"}>
        {/* <Logo /> */}
        <LogoStyled>뷰잉</LogoStyled>
      </FirstContainer>
      <SecondContainer>
        <MenuItem to="/search">스터디</MenuItem>
        <MenuItem to="/board/notice">커뮤니티</MenuItem>
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
