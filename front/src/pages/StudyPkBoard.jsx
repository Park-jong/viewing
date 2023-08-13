import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import SearchBoxBoard from "../components/Board/SearchBoxBoard";
import MainButton from "../components/Button/MainButton";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { styled } from "styled-components";
import BoardNavBar from "../components/Board/BoardNavBar";
import ArticleList from "../components/Board/Organisms/ArticleList";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default function StudyPkBoard() {
  const study_id = useLocation().pathname.split("/")[2];
  const [data, setData] = useState([]);

  useEffect(() => {
    customAxios()
      .get(`/studies/${study_id}/boards`)
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <Container>
      <BoardNavBar />
      <ArticleList data={data} width={1000} type={study_id} />
      <SearchBoxBoard></SearchBoxBoard>
      <MainButton content={"글쓰기"} width={80} height={35}></MainButton>
    </Container>
  );
}
