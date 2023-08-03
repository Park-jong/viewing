import ArticleList from '../components/Board/Organisms/ArticleList';

const data = [
  {
    author: {
      id: 1,
      nickname: "배고파요",
      hat: "모자",
      character: "cow",
      background: "#ff6767",
    },
    article_id: 123,
    title: "제목입니다",
    view_count: 123,
    comment_count: 123,
    like_count: 25,
  },
  {
    author: {
      id: 1,
      nickname: "배고파요",
      hat: "모자",
      character: "cow",
      background: "#ff6767",
    },
    article_id: 123,
    title: "제목입니다",
    view_count: 123,
    comment_count: 123,
    like_count: 25,
  },
];


export default function MypageMyarticle() {
  return <ArticleList data={data} width={800}></ArticleList>;
}
