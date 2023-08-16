import { styled } from "styled-components";
import { Link, Outlet, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { useNavigate } from "react-router-dom";

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
  cursor: pointer;
`;

export default function StudySideBar() {
  const [clicked, setClicked] = useState("");

  const location = useLocation();

  const navigate = useNavigate();

  const member = useSelector((state) => state.UserReducer);
  const memberId = member.memberId;
  const studyId = location.pathname.split("/")[2];
  const [isLeader, setIsLeader] = useState(false);
  const [menuList, setMenuList] = useState([
    "스터디 캘린더",
    "스터디 게시판",
    "스터디 채팅",
    "영상회의 참여",
    "탈퇴하기",
  ]);
  const [linkList, setLinkList] = useState([
    `/study/${studyId}/calendar`,
    `/study/${studyId}/board`,
    `/study/${studyId}/chat`,
    `/study/${studyId}/meeting`,
    `/study/${studyId}/withdraw`,
  ]);
  const [manageMenu, setManageMenu] = useState("스터디 정보");

  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/member`)
      .then(({ data }) => {
        if (data.is_leader) {
          setIsLeader(true);
        }
        setList(data.is_leader);
      })
      .catch((err) => {
        alert("접근 권한이 없습니다.");
        window.location.replace("/");
      });
  }, []);

  function setList(leader) {
    if (leader) {
      setLinkList((prevList) => [
        `/study/${studyId}`,
        ...prevList.slice(0, 3),
        `/study/${studyId}/applicant`,
        ...prevList.slice(3),
      ]);
      setMenuList((prevList) => [
        "스터디 관리",
        ...prevList.slice(0, 3),
        "참여신청 관리",
        ...prevList.slice(3),
      ]);
    } else {
      setLinkList((prevList) => [`/study/${studyId}`, ...prevList.slice(0)]);
      setMenuList((prevList) => ["스터디 정보", ...prevList.slice(0)]);
    }
    setClicked(document.location.pathname);
  }

  function handleClick(menu) {
    setClicked(menu);
  }

  const menuListDoms = menuList.map((menu, idx) => {
    if (clicked) {
      return (
        <StyledLink
          to={linkList[idx]}
          onClick={() => handleClick(linkList[idx])}
          key={idx}
        >
          <Menu $active={clicked === linkList[idx]}>{menu}</Menu>
        </StyledLink>
      );
    }
  });

  return (
    <Container>
      <SidebarContainer>
        <Study>스터디</Study>
        {menuListDoms}
      </SidebarContainer>
      <div>
        <Outlet context={{ isLeader, setIsLeader }} />
      </div>
    </Container>
  );
}
