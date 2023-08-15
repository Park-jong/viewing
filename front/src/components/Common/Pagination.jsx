import {
  BiChevronLeft,
  BiChevronRight,
  BiChevronsLeft,
  BiChevronsRight,
} from "react-icons/bi";
import { useLocation } from "react-router-dom";
import { styled } from "styled-components";
import { customAxios } from "../../modules/Other/Axios/customAxios";

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

export default function Pagination({ setData, page, setPage, maxPage }) {
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

  const handlePage = (e) => {
    if (e < 0) {
      e = 0;
    } else if (e > maxPage) {
      e = maxPage;
    }
    setPage(e);
    customAxios()
      .get(`boards/${param(type)}?size=20&page=${e}`) // 페이지size, page
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const a = [page];
  let num = 1;
  let temp = 1;
  let flag1 = 1;
  let flag2 = 1;
  while (flag1 || flag2) {
    if (page - temp >= 0) {
      a.unshift(page - temp);
      num += 1;
      if (num == 5) {
        break
      }
    } else {
      flag1 = 0;
    }
    if (page + temp <= maxPage) {
      a.push(page + temp);
      num += 1;
      if (num == 5) {
        break
      }
    } else {
      flag2 = 0;
    }
    temp += 1;
  }

    const pageButtons = a
      .map((p, idx) => <PageButton key={idx} $now={page} $page={p} onClick={() => handlePage(p)}>
      {p + 1}
    </PageButton>);

  return (
    <PageContainer>
      <ArrowButton>
        <BiChevronsLeft onClick={() => handlePage(0)}></BiChevronsLeft>
      </ArrowButton>
      <ArrowButton>
        <BiChevronLeft onClick={() => handlePage(page - 1)}></BiChevronLeft>
      </ArrowButton>
      {pageButtons}
      <ArrowButton>
        <BiChevronRight onClick={() => handlePage(page + 1)}></BiChevronRight>
      </ArrowButton>
      <ArrowButton>
        <BiChevronsRight onClick={() => handlePage(maxPage)}></BiChevronsRight>
      </ArrowButton>
    </PageContainer>
  );
}
