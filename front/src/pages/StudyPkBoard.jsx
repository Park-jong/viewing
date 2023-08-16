import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import MainButton from "../components/Button/MainButton";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { styled } from "styled-components";
import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";
import Pagination from "./../components/Common/Pagination";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
const FlexContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ButtonContainer = styled.div`
  margin-right: 20px;
`;

export default function StudyPkBoard() {
  const url = new URL(document.URL);
  const query = url.searchParams;
  const searchBy = query.get("searchBy");
  const keyword = query.get("keyword");
  const navigate = useNavigate();
  const study_id = useLocation().pathname.split("/")[2];
  const [data, setData] = useState([]);

  const [page, setPage] = useState(0);
  useEffect(() => {
    setPage(0);
    handleData(0);
  }, []);

  const getSearchUrl = () =>
    `studies/${study_id}/boards?${searchBy ? "searchBy=" + searchBy : ""}&${
      keyword ? "keyword=" + keyword : ""
    }`;

  function handleData(e) {
    console.log(getSearchUrl());
    customAxios()
      .get(getSearchUrl() + `&size=20&page=${e}`) // 페이지size, page
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  return (
    <Container>
      {data.content ? (
        <ArticleList data={data.content} width={1000} type={study_id} />
      ) : (
        <></>
      )}
      <FlexContainer>
        <ButtonContainer>
          <MainButton
            content={"글쓰기"}
            width={80}
            height={35}
            onClick={() => navigate(`write`)}
          ></MainButton>
        </ButtonContainer>
        <SearchBoxBoard></SearchBoxBoard>
      </FlexContainer>
      <Pagination
        handleData={handleData}
        page={page}
        setPage={setPage}
        maxPage={data.totalPages - 1}
      ></Pagination>
    </Container>
  );
}
