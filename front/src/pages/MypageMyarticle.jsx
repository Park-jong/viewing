import { useEffect, useState } from 'react';
import ArticleList from '../components/Board/Organisms/ArticleList';
import { customAxios } from '../modules/Other/Axios/customAxios';
import { useSelector } from 'react-redux';


export default function MypageMyarticle() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [data, setData] = useState([]);

  

  return <ArticleList data={data} width={800}></ArticleList>;
}
