import { useEffect, useRef, useState } from "react";
import { styled } from "styled-components";
import MiniMenuList from "./Organisms/MiniMenuList";

const ProfileContainer = styled.div`
  display: flex;
  align-items: center;
`;

const ProfileImg = styled.div`
  height: 32px;
  width: 32px;
  border-radius: 16px;
  background-color: ${(props) => props.$backgroundcolor};
  background-image: ${(props) => `url(/profile/${props.$characterimg}.png)`};
  margin-right: 8px;
  background-size: 70%;
  background-repeat: no-repeat;
  background-position: center;
`

const Username =styled.div`
  
`


export default function UserProfile({ backgroundcolor, characterimg, nickname, width=32, height=32 }) {
  const [isMenuVisible, setIsMenuVisible] = useState(false);
  const [menuPosition, setMenuPosition] = useState({ x: 0, y: 0 });
  const AlertRef = useRef(null);

  const handleClickOutside = (event) => {
    if (AlertRef.current && !AlertRef.current.contains(event.target)) {
      setIsMenuVisible(false);
    }
  };

  useEffect(() => {
    window.addEventListener("click", handleClickOutside);
    return () => {
      window.removeEventListener("click", handleClickOutside);
    };
  }, []);

  const toggleMenu = (event) => {
    setIsMenuVisible(!isMenuVisible);
    setMenuPosition({ x: event.clientX, y: event.clientY });
  };


  return <ProfileContainer onClick={toggleMenu} ref={AlertRef}>
    <ProfileImg $backgroundcolor={backgroundcolor} $characterimg={characterimg}></ProfileImg>
    <Username>{nickname}</Username>

    <MiniMenuList
      isVisible={isMenuVisible}
      style={{ top: menuPosition.y, left: menuPosition.x }}
    >
    </MiniMenuList>
  </ProfileContainer>
}
