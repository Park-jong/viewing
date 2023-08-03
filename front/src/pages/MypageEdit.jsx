import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import SubButton from './../components/Button/SubButton';
import SetProfileImage from "../components/MyPage/SetProfileImage";

const EditContainer = styled.div`
`

const ProfileContainer = styled.div`
  width: 300px;
  height: 400px;
`
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
`

const NicknameContainer = styled.div`
  width: 500px;
  height: 500px;
  background-color: aquamarine;
`

const Text = styled.div`

`
const NicknameInput = styled.input`
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
  color: #555;
  outline: none;

  &:focus{
    border-color: #007bff;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  }
`

const ButtonContainer = styled.div`
  margin-top: 30px;
  margin-left: 50px;
`

export default function MypageEdit() {

  const backgroundcolor = "#ff6767"
  const characterimg = "cow"
  const nickname = "james"

  return <EditContainer>
    <MainButton width={100} height={40} content={"저장하기"}></MainButton>
    <h1>닉네임<SubButton width={50} height={20} content={"중복확인"}></SubButton></h1>
    <NicknameInput></NicknameInput>
    <h1>사용가능한 닉네임입니다.</h1>
    <h1>프로필 이미지</h1>
    <SetProfileImage></SetProfileImage>
  </EditContainer>;
}