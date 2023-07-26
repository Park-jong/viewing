import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";

const Container = styled.div`
  position: relative;
`;

const SidebarContainer = styled.div`
  position: absolute;
  top: 0px;

  width: 150px;
  height: 100%;
  /* background-color: var(--gray-50); */
  /* display: inline; */
  font-size: 12px;
  font-weight: 300;
`;

const SidebarList = styled(Link)`
  list-style: none;
  padding: 0;
  margin: 0;

  text-decoration: none;
  color: var(--gray-800);
`;

const SidebarListItem = styled.li`
  padding: 10px 20px;
  border-bottom: 1px solid #ebebebde;
  cursor: pointer;

  &:hover {
    background-color: var(--secondary);
  }
`;

export default function StudySideBar({ isLeader }) {
  return (
    <Container>
      <SidebarContainer>
        <SidebarList>
          {isLeader ? (
            <SidebarListItem>스터디 관리</SidebarListItem>
          ) : (
            <SidebarListItem>스터디 정보</SidebarListItem>
          )}

          <SidebarListItem>스터디 캘린더</SidebarListItem>
          <SidebarListItem>스터디 게시판</SidebarListItem>
          <SidebarListItem>스터디 채팅</SidebarListItem>
          <SidebarListItem>영상회의 참여</SidebarListItem>

          {isLeader ? (
            <SidebarListItem>참여신청관리</SidebarListItem>
          ) : (
            <SidebarListItem>탈퇴하기</SidebarListItem>
          )}
        </SidebarList>
      </SidebarContainer>
    </Container>
  );
}
