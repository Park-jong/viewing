import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import styled from "styled-components";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import MainButton from "../components/Button/MainButton";
import { useLocation, useNavigate } from "react-router-dom";
import Pagination from "./../components/Common/Pagination";
import { useSelector } from "react-redux";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const BottomContainer = styled.div`
  display: flex;
  align-items: center;
  margin-top: 20px;
  margin-bottom: 15px;
`;

const MarginLeft = styled.div`
  margin-left: 20px;
`;

export default function BoardCommon() {
  const url = new URL(document.URL);
  const query = url.searchParams;
  const searchBy = query.get("searchBy");
  const keyword = query.get("keyword");
  const member_id = useSelector((state) => state.UserReducer.memberId);

  const [data, setData] = useState([]);
  const [page, setPage] = useState(0);
  const navigate = useNavigate();
  const type = useLocation().pathname.split("/")[2];
  const param = (type) => {
    if (type === "free") {
      return "general";
    } else if (type === "interview") {
      return "review";
    } else if (type === "question") {
      return "qna";
    }
  };

  const getSearchUrl = () =>
    `boards/${param(type)}?${searchBy ? "searchBy=" + searchBy : ""}&${
      keyword ? "keyword=" + keyword : ""
    }`;

  function navigateWrite() {
    customAxios()
      .get(`members/${member_id}`)
      .then((res) => navigate(`/board/write?type=${type}`))
      .catch((err) => {
        window.alert("로그인이 필요합니다.");
        navigate(`/login`);
      });
  }

  function handleData(e) {
    customAxios()
      .get(getSearchUrl() + `&size=20&page=${e}`) // 페이지size, page
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  useEffect(() => {
    setPage(0);
    handleData(0);
  }, [type]);

  return (
    <Container>
      <BoardNavBar />
      {data.content ? (
        <ArticleList data={data.content} width={1000} type={type} />
      ) : (
        <></>
      )}
      <BottomContainer>
        <SearchBoxBoard></SearchBoxBoard>
        <MarginLeft>
          <MainButton
            content={"글쓰기"}
            width={80}
            height={35}
            onClick={() => navigateWrite()}
          ></MainButton>
        </MarginLeft>
      </BottomContainer>
      <Pagination
        handleData={handleData}
        page={page}
        setPage={setPage}
        maxPage={data.totalPages - 1}
      ></Pagination>
    </Container>
  );
}
