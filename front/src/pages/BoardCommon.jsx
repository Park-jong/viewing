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

const BottomContainer = styled.div`
  display: flex;
  align-items: center;
`

const MarginLeft = styled.div`
  margin-left: 20px;
`

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
`

const BottomContainer = styled.div`
  display: flex;
  align-items: center;
`

const MarginLeft = styled.div`
  margin-left: 20px;
`

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
`

export default function BoardCommon() {
  const [data, setData] = useState([]);
  const [page, setPage] = useState(0)
  const navigate = useNavigate()
  const type = useLocation().pathname.split('/')[2]
  const param = (type) => {
    if (type === "free") {
      return "general";
    } else if (type === "interview") {
      return "review";
    } else if (type === "question") {
      return "qna";
    }
  }

  useEffect(()=>{
    setPage(0)
    customAxios().get(`boards/${param(type)}?size=5&page=${page}`) // 페이지size, page
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
      <BottomContainer>
        <SearchBoxBoard></SearchBoxBoard>
        <MarginLeft>
          <MainButton content={"글쓰기"} width={80} height={35} onClick={() => navigate(`/board/write?type=${type}`)}></MainButton>
        </MarginLeft>
      </BottomContainer>
      <PageContainer>

      </PageContainer>
    </Container>
  );
}
