import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import SubButton from "./../components/Button/SubButton";
import SetProfileImage from "../components/MyPage/SetProfileImage";
import { useDispatch, useSelector } from "react-redux";
import { useForm } from "react-hook-form";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import Error from "../components/Common/Error";
import { useState } from "react";
import {
  ChangeNickname,
  ChangeProfile,
  Login,
} from "../modules/UserReducer/Actions";

const EditContainer = styled.div``;

const Title = styled.div`
  font-size: 24px;
  font-weight: 700;
  margin-top: 50px;

  margin-bottom: 30px;
`;

const ProfileContainer = styled.div`
  width: 300px;
  height: 400px;
`;

const ProfileImg = styled.div`
  width: 250px;
  height: 250px;
  border-radius: 125px;
  background-color: azure;
  margin: auto;
  margin-top: 20px;
  background-color: ${(props) => props.$backgroundcolor};
  background-image: ${(props) => `url(/profile/${props.$characterimg}.png)`};
  background-size: 70%;
  background-repeat: no-repeat;
  background-position: center;
`;

const NicknameContainer = styled.div`
  width: 500px;
  height: 500px;
  background-color: aquamarine;
`;

const Text = styled.div``;

// const NicknameInput = styled.input`
//   padding: 10px;
//   border: 2px solid #ccc;
//   border-radius: 5px;
//   font-size: 16px;
//   color: #555;
//   outline: none;

//   &:focus {
//     border-color: #007bff;
//     box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
//   }
// `;
const NicnameInputContainer = styled.div`
  display: flex;
`;

const NicknameInput = styled.input`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 400px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: var(--border-height) solid var(--border-before-color);

  margin-right: 20px;

  &:hover {
    background: var(--input-hovered-color);
  }

  &:focus {
    outline: none;
    border-bottom: var(--border-height) solid var(--border-after-color);
  }
`;

const FlexContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ButtonContainer = styled.div`
  margin-top: 30px;
  /* margin-left: 50px; */
  display: flex;
  justify-content: center;
`;

const maxLen = 40;

export default function MypageEdit() {
  //

  //유저정보
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const nick = useSelector((state) => state.UserReducer.nickname);
  const color = useSelector((state) => state.UserReducer.backgroundColor);
  const img = useSelector((state) => state.UserReducer.backgroundImg);

  const dispatch = useDispatch();
  const [nickChecked, setNickChecked] = useState(false);
  const [nickname, setNickname] = useState(nick);
  const [selectedColor, setSelectedColor] = useState(color);
  const [selectedImage, setSelectedImage] = useState(img);
  const [nickLength, setNickLength] = useState(nick.length);
  const [checkLength, setCheckLength] = useState(nick.length < maxLen);

  // react-hook-form에서 사용되는 함수
  const {
    register,
    handleSubmit,
    getValues,
    formState: { errors },
    formState: { isSubmitting },
  } = useForm();

  const handleNickname = (e) => {
    setNickLength(e.target.value.length);
    setNickname(e.target.value);
    setNickChecked(false);
  };

  const handleCheck = () => {
    if (nickname.length > maxLen) {
      alert("닉네임은 40자를 넘길 수 없습니다.");
      return;
    }

    customAxios()
      .get(`login/nickname/check/${nickname}`)
      .then((response) => {
        alert("사용 가능한 닉네임입니다.");
        setNickChecked(true);
      })
      .catch((error) => {
        alert("중복된 닉네임입니다.");
      });
  };

  const onSubmit = (type) => {
    if (nickname.length > maxLen) {
      alert("닉네임은 40자를 넘길 수 없습니다.");
      return;
    }

    const data = () => {
      if (type === "nickname") {
        return { member_id: member_id, nickname: nickname };
      } else if (type === "profile") {
        return {
          member_id: member_id,
          background: selectedColor,
          character: selectedImage,
        };
      }
    };

    if (nickChecked === false && type === "nickname") {
      return alert("중복확인해주세요.");
    }

    customAxios()
      .put(`members/${member_id}/${type}`, data())
      .then(function (response) {
        if (type === "nickname") {
          setNickChecked(false);

          dispatch(ChangeNickname(nickname));
          alert("프로필 변경이 완료되었습니다.");
        } else if (type === "profile") {
          dispatch(
            ChangeProfile({
              backgroundImg: selectedImage,
              backgroundColor: selectedColor,
            })
          );
          alert("프로필 변경이 완료되었습니다.");
        }
      })
      .catch((error) => {
        console.error("에러가 발생했습니다.:", error);
      });
  };

  return (
    <EditContainer>
      <FlexContainer>
        <Title>{nickname}님, 환영합니다.</Title>
      </FlexContainer>
      <FlexContainer>
        <form onSubmit={handleSubmit(onSubmit)}>
          <h1>닉네임&nbsp;&nbsp;&nbsp;</h1>
          {/* <NicknameInput
            id="userId"
            defaultValue={nickname}
            onChange={handleNickname}
            maxLength={30}
          /> */}
          <NicnameInputContainer>
            <NicknameInput
              id="userId"
              defaultValue={nickname}
              onChange={handleNickname}
              maxLength={maxLen}
            />
            <div style={{ color: "gray", fontSize: "14px", marginTop: "11px" }}>
              {nickLength}/{maxLen}
            </div>
          </NicnameInputContainer>
          <Error>{errors?.userId?.message}</Error>
          <SubButton
            onClick={() => handleCheck()}
            width={50}
            height={20}
            content={"중복확인"}
          ></SubButton>
          <ButtonContainer>
            <MainButton
              type={"button"}
              width={100}
              height={40}
              content={"저장하기"}
              onClick={() => onSubmit("nickname")}
            ></MainButton>
          </ButtonContainer>
        </form>
      </FlexContainer>
      <SetProfileImage
        selectedColor={selectedColor}
        setSelectedColor={setSelectedColor}
        selectedImage={selectedImage}
        setSelectedImage={setSelectedImage}
      ></SetProfileImage>
      <ButtonContainer>
        <MainButton
          width={100}
          height={40}
          content={"저장하기"}
          onClick={() => onSubmit("profile")}
        ></MainButton>
      </ButtonContainer>
    </EditContainer>
  );
}
