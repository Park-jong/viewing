import { styled } from "styled-components";
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "../../modules/Other/Axios/customAxios";

const Container = styled.div`
  display: flex;
  padding: 20px;
`;

const SidebarContainer = styled.div`
  width: 222px;
  height: 600px;

  margin-right: 50px;
  border-top: 1px solid #ebebebde;
`;

const Menu = styled.div`
  width: 150px;
  height: 30px;
  font-weight: 300;
  padding-top: 13px;
  padding-left: 15px;
  color: var(--gray-800);
  border-bottom: 1px solid #ebebebde;
  text-decoration: none;

  &:hover {
    background-color: var(--secondary);
  }
  ${({ $active }) =>
    $active &&
    `
    background-color: var(--secondary);
  `}
`;

const Study = styled.div`
  width: 150px;
  height: 30px;
  font-weight: 300;
  padding-top: 13px;
  padding-left: 15px;
  color: var(--gray-800);
  border-bottom: 1px solid #ebebebde;
  text-decoration: none;
  background-color: var(--secondary);
`;

const StyledLink = styled(Link)`
  text-decoration: none;
`;

export default function StudySideBar() {
  const [clicked, setClicked] = useState(document.location.pathname);

  const navigate = useNavigate();
  const location = useLocation();

  const member = useSelector((state) => state.UserReducer);
  const memberId = member.memberId;
  const studyId = location.pathname.split("/")[2];
  const [isLeader, setIsLeader] = useState(false);
  const [menuList, setMenuList] = useState([
    "스터디 정보",
    "스터디 캘린더",
    "스터디 게시판",
    "스터디 채팅",
    "영상회의 참여",
    "참여신청관리",
    "탈퇴하기",
  ]);
  const linkList = [
    `/study/${studyId}`,
    `/study/${studyId}/calendar`,
    `/study/${studyId}/board`,
    `/study/${studyId}/chat`,
    `/study/${studyId}/meeting`,
    `/study/${studyId}/applicant`,
    `/study/${studyId}/withdraw`,
  ];

  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}`)
      .then(({ data }) => {
        console.log(data);
        if (data.leader.member_id === memberId) {
          setIsLeader(() => true);
          menuList[0] = "스터디 관리";
          setMenuList((prev) => [...menuList]);
        }
      });
  }, []);

  function handleClick(menu) {
    setClicked(menu);
  }

  const menuListDoms = menuList.map((menu, idx) => {
    return (
      <StyledLink
        to={linkList[idx]}
        onClick={() => handleClick(menu)}
        key={idx}
      >
        <Menu $active={clicked === menu}>{menu}</Menu>
      </StyledLink>
    );
  });

  return (
    <Container>
      <SidebarContainer>
        <Study>스터디</Study>
        {menuListDoms}
      </SidebarContainer>
      {/* <StudySideBar></StudySideBar> */}
      <div>
        <Outlet />
      </div>
    </Container>
  );
}
