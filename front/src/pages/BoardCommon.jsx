import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import styled from "styled-components";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import MainButton from "../components/Button/MainButton";
import { useLocation, useNavigate } from "react-router-dom";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default function BoardCommon() {
  const [data, setData] = useState([]);
  const navigate = useNavigate()
  const type = useLocation().pathname.split('/')[2]
  const param = (type) => {
    if (type === "free") {
      return "general"
    } else if (type === "interview") {
      return "review"
    } else if (type === "question") {
      return "qna"
    }
  }
  console.log(param(type))

  useEffect(()=>{
    customAxios().get(`boards/${param(type)}`)
    .then((res) => {
      setData(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  },[type])
  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} width={1000} type={type}/>
      <SearchBoxBoard></SearchBoxBoard>
      <MainButton content={"글쓰기"} width={80} height={35} onClick={() => navigate(`/board/write?type=${type}`)}></MainButton>
    </Container>
  );
}
