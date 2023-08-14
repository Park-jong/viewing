import React from "react";
import styled from "styled-components";
import { NavLink } from "react-router-dom";

const NavbarContainer = styled.div`
  /* background-color: #f5faff; */
  display: flex;
  justify-content: center;
  padding: 10px;
`;

const NavItem = styled(NavLink)`
  font-size: 14px;
  color: #565656;
  text-decoration: none;
  margin: 0 15px;
  padding: 8px 12px;
  border-radius: 4px;
  /* border: 1px solid var(--gray-100); */
  transition: background-color 0.2s ease-in-out, transform 0.2s ease-in-out;

  &:hover {
    background-color: var(--secondary);
    transform: scale(1.05);
  }

  &.active {
    background-color: var(--secondary);
    color: #000000;
  }
`;

export default function BoardNavBar() {
  return (
    <NavbarContainer>
      <NavItem to="/board/free" activeClassName="active">
        자유게시판
      </NavItem>
      <NavItem to="/board/interview" activeClassName="active">
        면접후기
      </NavItem>
      <NavItem to="/board/question" activeClassName="active">
        질문게시판
      </NavItem>
    </NavbarContainer>
  );
}
