import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import styled from "styled-components";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import MainButton from "../components/Button/MainButton";
import { useLocation, useNavigate } from "react-router-dom";
import {
  BiChevronLeft,
  BiChevronRight,
  BiChevronsLeft,
  BiChevronsRight,
} from "react-icons/bi";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const BottomContainer = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 15px;
`;

const MarginLeft = styled.div`
  margin-left: 20px;
`;

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const PageButton = styled.div`
  padding: 5px;
  border-radius: 10px;
  background-color: ${(props) => {
    if (props.$now === props.$page) {
      return "var(--gray-200)";
    } else {
      return "";
    }
  }};
  &:hover {
    background-color: var(--gray-200);
  }
`;

const ArrowButton = styled.div`
  padding: 3px;
  border-radius: 10px;
  &:hover {
    background-color: var(--gray-200);
  }
`;

export default function BoardCommon() {
  const [data, setData] = useState([]);
  const [page, setPage] = useState(0);
  const [maxPage, setMaxPage] = useState(1);
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

  useEffect(() => {
    setPage(0);
    customAxios()
      .get(`boards/${param(type)}?size=5&page=${page}`) // 페이지size, page
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [type]);

  const handlePage = (e) => {
    if(e < 0) {
      e = 0
    } else if (e >maxPage) {
      e = maxPage
    }
    setPage(e);
    customAxios()
      .get(`boards/${param(type)}?size=5&page=${e}`) // 페이지size, page
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} width={1000} type={type} />
      <BottomContainer>
        <SearchBoxBoard></SearchBoxBoard>
        <MarginLeft>
          <MainButton
            content={"글쓰기"}
            width={80}
            height={35}
            onClick={() => navigate(`/board/write?type=${type}`)}
          ></MainButton>
        </MarginLeft>
      </BottomContainer>
      <PageContainer>
        <ArrowButton>
          <BiChevronsLeft onClick={() => handlePage(0)} ></BiChevronsLeft>
        </ArrowButton>
        <ArrowButton>
          <BiChevronLeft onClick={() => handlePage(page-1)} ></BiChevronLeft>
        </ArrowButton>
        <PageButton $now={page} $page={0} onClick={() => handlePage(0)}>
          1
        </PageButton>
        <PageButton $now={page} $page={1} onClick={() => handlePage(1)}>
          2
        </PageButton>
        <ArrowButton>
          <BiChevronRight onClick={() => handlePage(page+1)} ></BiChevronRight>
        </ArrowButton>
        <ArrowButton>
          <BiChevronsRight onClick={() => handlePage(maxPage)} ></BiChevronsRight>
        </ArrowButton>
      </PageContainer>
    </Container>
  );
}
