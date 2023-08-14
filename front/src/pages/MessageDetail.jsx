import { useLocation } from "react-router-dom";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useEffect, useState } from "react";
import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import UserProfile from "../components/Common/UserProfile";
import { useSelector } from "react-redux";
import { UserReducer } from "./../modules/UserReducer/UserReducer";

const Title = styled.div`
  margin-top: 20px;
  font-size: 40px;
  margin-bottom: 20px;
`;

const FlexContainer = styled.div`
  display: flex;
  align-items: center;
`;

const Content = styled.div`
  margin-top: 20px;
  font-size: 15px;
  margin-bottom: 20px;
`;

const FromTo = styled.div`
  font-size: 20px;
  margin-right: 10px;
`;

export default function MessageDetail() {
  const param = useLocation().pathname.split("/")[3];
  const type = useLocation().pathname.split("/")[2];
  const [data, setData] = useState({});
  const to = data.author
    ? type === "get"
      ? data.author.nickname
      : data.receiver.nickname
    : null;
  const memberId = data.author
    ? type === "get"
      ? data.author.memberId
      : data.receiver.memberId
    : null;
  const handleImageClick = () => {
    const newWindow = window.open(
      `/message?member_id=${memberId}&nickname=${to}`,
      "_blank",
      "width=400,height=400"
    );
  };

  useEffect(() => {
    customAxios()
      .get(`messages/${param}`)
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <>
      <Title>{data.title}</Title>
      <FlexContainer>
        {type === "get" ? <FromTo>From</FromTo> : <FromTo>To </FromTo>}
        {data.author ? (
          type === "get" ? (
            <UserProfile
              member_id={data.author.memberId}
              backgroundcolor={data.author.background}
              characterimg={data.author.character}
              nickname={data.author.nickname}
              nicknameLength={500}
            ></UserProfile>
          ) : (
            <UserProfile
              member_id={data.receiver.memberId}
              backgroundcolor={data.receiver.background}
              characterimg={data.receiver.character}
              nickname={data.receiver.nickname}
              nicknameLength={500}
            ></UserProfile>
          )
        ) : (
          <></>
        )}
      </FlexContainer>

      <Content>{data.content}</Content>

      <MainButton
        width={90}
        height={40}
        content={"답장하기"}
        onClick={handleImageClick}
      ></MainButton>
    </>
  );
}
