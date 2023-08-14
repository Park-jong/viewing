import React, { useState, useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { Logout } from "../../modules/UserReducer/Actions";
import axios from "axios";

const ProfileImgWrapper = styled.div`
  position: relative;
  cursor: pointer;
  z-index: 10;
`;

const ProfileImg = styled.div`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: ${(props) => props.$backgroundcolor};
  background-image: ${(props) => `url(/profile/${props.$backgroundimg}.png)`};
  background-size: 70%;
  background-repeat: no-repeat;
  background-position: center;
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

export default function DropProfile({ member }) {
  const [menuOpen, setMenuOpen] = useState(false);
  const profileImgRef = useRef(null);
  const dispatch = useDispatch();
  const socialLoginType = useSelector((state) => state.UserReducer.web);
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

  const logout = () => {
    if (socialLoginType == "kakao") {
      const KAKAO_LOGOUT_URL = "https://kauth.kakao.com/oauth/logout";
      const queries = {
        client_id: "2869d34ce0755e095b7d39e5eb3aeafb",
        logout_redirect_uri: process.env.REACT_APP_CLIENT_URL,
      };
      const KAKAO_LOGOUT_PARAMS = new URLSearchParams(queries).toString();
      axios
        .get(`${KAKAO_LOGOUT_URL}?${KAKAO_LOGOUT_PARAMS}`)
        .then((res) => {
          console.log(res);
          localStorage.removeItem("access_token");
          dispatch(Logout());
        })
        .catch
        //로그아웃 실패시?
        ();
    } else if (socialLoginType == "google") {
      localStorage.removeItem("access_token");
      dispatch(Logout());
    } else if (socialLoginType == "github") {
      localStorage.removeItem("access_token");
      dispatch(Logout());
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
        onClick={handleProfileImgClick}
        $backgroundcolor={member.backgroundColor}
        $backgroundimg={member.backgroundImg}
      />
      <DropdownMenu open={menuOpen}>
        <MenuItem to="/mypage/edit" style={{ fontWeight: "600" }}>
          마이페이지
        </MenuItem>
        <MenuItem to="/mypage/study">내 스터디</MenuItem>
        <MenuItem to="/mypage/get">내 쪽지함</MenuItem>
        <MenuItem to="/mypage/myarticle">내 게시글</MenuItem>
        <MenuItem onClick={logout} style={{ color: "red", fontSize: "14px" }}>
          로그아웃
        </MenuItem>
      </DropdownMenu>
    </ProfileImgWrapper>
  );
}
