import { styled } from "styled-components";
import MiniMenu from "../MiniMenu";

const Container = styled.div`
  position: absolute;
  width: 100px;
  top: ${(props) => `${props.$top}px`};
  left: ${(props) => `${props.$left}px`};
  background-color: #fff;
  display: ${(props) => (props.$isVisible ? "block" : "none")};
  z-index: 12;
`;

export default function MiniMenuList({ isVisible, left, top, to, member_id }) {
  return (
    <Container $isVisible={isVisible} $left={left} $top={top}>
      <MiniMenu content="쪽지 보내기" to={to} member_id={member_id}></MiniMenu>
      <MiniMenu content="초대하기" to={to}></MiniMenu>
    </Container>
  );
}
