import { styled } from "styled-components"
import MiniMenu from "../MiniMenu"

const Container = styled.div`
  position: absolute;
  width: 100px;
  top: 40px;
  right: 0;
  background-color: #fff;
  display: ${(props) => (props.$isVisible ? "block" : "none")};
`

export default function MiniMenuList ({isVisible, style}) {
  return <Container $isVisible={isVisible} style={style}>
    <MiniMenu content="쪽지 보내기" from={"james"} to={"faker"}></MiniMenu>
    <MiniMenu content="초대하기" from={"james"} to={"faker"}></MiniMenu>
  </Container>
}