import styled from "styled-components";
import ArticleBox from "../ArticleBox";
import { useLocation } from "react-router-dom";
import Pagination from "../Pagination";
import { useState } from "react";

const ArticleContainer = styled.div``;

const HeaderContainer = styled.div`
  width: ${(props) => `${props.$width}px`};
  height: 35px;
  display: flex;
  align-items: center;
  /* background-color: #ffffff; */
  /* border-radius: 8px; */
  position: relative;
  /* border-top: 0.5px solid #e0e0e0; Add a top border */
  border-bottom: 1px solid #424242; /* Add a bottom border */

  font-size: 13px;
  color: #333;
  font-weight: 700;

  /* background-color: #f5f5f5; */
`;

const ProfileContainer = styled.div`
  position: absolute;
  left: 20px;
`;

const TitleContainer = styled.div`
  position: absolute;
  left: 170px;
  font-size: 13px;
  margin: 0;
  color: #333;

  text-decoration: none;
`;

const MetaContainer = styled.div`
  display: flex;
  align-items: center;

  position: absolute;
  right: 10px;
`;

const MetaItem = styled.div`
  font-size: 12px;
  display: flex;
  align-items: center;
  margin-right: 31px;
`;

export default function ArticleList({ data, width, type }) {
  const location = useLocation();
  const study_id = useLocation().pathname.split("/")[2];
  const isStudyBoard = location.pathname === `/study/${study_id}/board`;

  // const [page, setPage] = useState(1); //페이지
  // const limit = 5; // posts가 보일 최대한의 갯수
  // const offset = (page - 1) * limit; // 시작점과 끝점을 구하는 offset

  // const postsData = (data) => {
  //   if (data) {
  //     let result = data.slice(offset, offset + limit);
  //     return result;
  //   }
  // };

  /*
1. page : 현재의 page
2. setPage : 변경될 page를 만드는 useState함수
3. limit : 한번에 posts의 최대 갯수
*/

  return (
    <>
      <ArticleContainer>
        <HeaderContainer $width={width}>
          <ProfileContainer>작성자</ProfileContainer>
          <TitleContainer>제목</TitleContainer>

          <MetaContainer>
            {isStudyBoard ? null : <MetaItem>조회수</MetaItem>}
            <MetaItem>댓글</MetaItem>
            {isStudyBoard ? null : <MetaItem>하트</MetaItem>}
          </MetaContainer>
        </HeaderContainer>
        {isStudyBoard
          ? data.map((article, idx) => (
              <ArticleBox
                url={"/study/" + type + "/board/" + article.article_id}
                key={idx}
                title={article.title}
                width={width}
                nickname={article.author.nickname}
                member_id={article.author.member_id}
                backgroundcolor={article.author.member_profile_background}
                character={article.author.member_profile_image}
                commentCount={article.comment_count}
              />
            ))
          : data.map((article, idx) => (
              <ArticleBox
                url={"/board/" + type + "/" + article.article_id}
                key={idx}
                nickname={article.author.nickname}
                member_id={article.author.member_id}
                backgroundcolor={article.author.member_profile_background}
                character={article.author.member_profile_image}
                title={article.title}
                commentCount={article.comment_count}
                heartCount={article.like_count}
                viewCount={article.view_count}
                width={width}
              />
            ))}
      </ArticleContainer>
      {/* <Pagination
        limit={limit}
        page={page}
        totalPosts={data.length} //4. totalPosts : 데이터의 총 posts 갯수
        setPage={setPage}
      /> */}
    </>
  );
}
