import { useLocation } from "react-router-dom";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useEffect, useState } from "react";
import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import UserProfile from "../components/Common/UserProfile";

const Title = styled.div``;
const Content = styled.div``;

export default function MessageDetail() {
  const param = useLocation().pathname.split("/")[3];
  const [data, setData] = useState({});

  useEffect(() => {
    customAxios()
      .get(`messages/${param}`)
      .then((res) => {
        console.log(res.data);
        setData(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <>
      <Title>{data.title}</Title>
      <Content>{data.content}</Content>
      {data.author ? (
        <UserProfile
          member_id={data.author.memberId}
          backgroundcolor={data.author.background}
          characterimg={data.author.character}
          nickname={data.author.nickname}
        ></UserProfile>
      ) : (
        <></>
      )}

      <MainButton width={90} height={40} content={"답장하기"}></MainButton>
    </>
  );
}
