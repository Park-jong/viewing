import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import styled from "styled-components";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import MainButton from "../components/Button/MainButton";
import { useLocation, useNavigate } from "react-router-dom";
import { BiChevronLeft, BiChevronRight, BiChevronsLeft, BiChevronsRight } from "react-icons/bi";

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
    setPage(e - 1)
  }

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
          <BiChevronsLeft></BiChevronsLeft>
        </ArrowButton>
        <ArrowButton>
          <BiChevronLeft></BiChevronLeft>
        </ArrowButton>
        <PageButton $now={page} $page={1} onClick={handlePage(1)}>
          1
        </PageButton>
        <PageButton $now={page} $page={2} onClick={handlePage(2)}>
          2
        </PageButton>
        <ArrowButton>
          <BiChevronRight></BiChevronRight>
        </ArrowButton>
        <ArrowButton>
          <BiChevronsRight></BiChevronsRight>
        </ArrowButton>
      </PageContainer>
    </Container>
  );
}
