import { useEffect, useRef, useState } from "react";
import { styled } from "styled-components";
import MiniMenuList from "./Organisms/MiniMenuList";

const ProfileContainer = styled.div`
  display: flex;
  position: relative;
  align-items: center;
  cursor: pointer;
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
`;

const Username = styled.div`
  max-width: ${(props) => `${props.$nicknameLength}px`};
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`;

export default function UserProfile({
  member_id,
  backgroundcolor,
  characterimg,
  nickname,
  width = 32,
  height = 32,
  nicknameLength = 100,
}) {
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
    const rect = AlertRef.current.getBoundingClientRect();
    const offsetX = event.clientX - rect.left;
    const offsetY = event.clientY - rect.top;
    setMenuPosition({ x: offsetX, y: offsetY });
  };

  return (
    <>
      {nickname || backgroundcolor || characterimg ? (
        <ProfileContainer onClick={toggleMenu} ref={AlertRef}>
          <ProfileImg
            $backgroundcolor={backgroundcolor}
            $characterimg={characterimg}
          ></ProfileImg>
          <Username $nicknameLength={nicknameLength}>{nickname}</Username>

          <MiniMenuList
            to={nickname}
            member_id={member_id}
            isVisible={isMenuVisible}
            top={menuPosition.y}
            left={menuPosition.x}
          ></MiniMenuList>
        </ProfileContainer>
      ) : (
        <ProfileContainer>
          <ProfileImg
            $backgroundcolor={backgroundcolor}
            $characterimg={characterimg}
          ></ProfileImg>
          <Username $nicknameLength={nicknameLength}>{"알 수 없음"}</Username>
        </ProfileContainer>
      )}
    </>
  );
}
