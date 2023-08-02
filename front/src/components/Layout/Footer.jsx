import styled from "styled-components";
import { Link } from "react-router-dom";

const FooterStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 120px;
  background-color: var(--gray-100);
  color: var(--gray-600);
`;

const FooterLink = styled(Link)`
  margin: 0 10px;
  text-decoration: none;
  color: var(--gray-400);
  font-size: 12px;
  font-weight: 300;
`;

export default function Footer() {
  return (
    <FooterStyle>
      <FooterLink to="/contact">사용자 이용약관</FooterLink>
      <FooterLink to="/privacy">개인정보 처리방침</FooterLink>
    </FooterStyle>
  );
}
