import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";

const ProfileImgWrapper = styled.div`
  position: relative;
  cursor: pointer;
`;

const ProfileImg = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 50%;
`;

const DropdownMenu = styled.div`
  position: absolute;
  top: 53px; /* Adjust the distance from the ProfileImg */
  right: 0;
  width: 150px;
  background-color: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  padding: 10px;
  text-decoration: none;
  display: ${(props) => (props.open ? "block" : "none")};
`;

const MenuItem = styled(Link)`
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
  color: var(--gray-900);
  padding: 8px;
  cursor: pointer;
  &:hover {
    background-color: #f0f0f0;
  }
`;

export default function DropProfile() {
  const [menuOpen, setMenuOpen] = useState(false);
  const profileImgRef = useRef(null);

  const handleProfileImgClick = () => {
    setMenuOpen(!menuOpen);
  };

  const handleClickOutside = (event) => {
    if (
      profileImgRef.current &&
      !profileImgRef.current.contains(event.target)
    ) {
      setMenuOpen(false);
    }
  };

  useEffect(() => {
    window.addEventListener("click", handleClickOutside);
    return () => {
      window.removeEventListener("click", handleClickOutside);
    };
  }, []);

  return (
    <ProfileImgWrapper ref={profileImgRef}>
      <ProfileImg
        src={"/ty.jpg"}
        alt="프로필사진"
        onClick={handleProfileImgClick}
      />
      <DropdownMenu open={menuOpen}>
        <MenuItem to="/mypage/edit" style={{ fontWeight: "600" }}>
          마이페이지
        </MenuItem>
        <MenuItem to="/mypage/study">내 스터디</MenuItem>
        <MenuItem to="/mypage/get">내 쪽지함</MenuItem>
        <MenuItem to="/mypage/myarticle">내 게시글</MenuItem>
        <MenuItem to="/" style={{ color: "red", fontSize: "14px" }}>
          로그아웃
        </MenuItem>
      </DropdownMenu>
    </ProfileImgWrapper>
  );
}
