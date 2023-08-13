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

const FlexContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ButtonContainer = styled.div`
  margin-right: 20px;
`;
export default function BoardCommon() {
  const [data, setData] = useState([]);
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
  console.log(param(type));

  useEffect(() => {
    customAxios()
      .get(`boards/${param(type)}`) // 페이지size, page
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [type]);
  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} width={1000} type={type} />
      <FlexContainer>
        <ButtonContainer>
          <MainButton
            content={"글쓰기"}
            width={80}
            height={35}
            onClick={() => navigate(`/board/write?type=${type}`)}
          ></MainButton>
        </ButtonContainer>
        <SearchBoxBoard></SearchBoxBoard>
      </FlexContainer>
    </Container>
  );
}
