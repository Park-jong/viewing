import { useEffect, useState } from 'react';
import { customAxios } from '../modules/Other/Axios/customAxios';
import MessageList from './../components/MyPage/Organisms/MessageList';
import { useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';
import { styled } from 'styled-components';

const Title = styled.div`
  margin-top: 20px;
  font-size: 30px;
  margin-bottom: 30px;
`

export default function MypageSend() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [data, setData] = useState([]);
  const param = useLocation().pathname.split('/')[2]
  const type = ()=>{
    if (param === "get") {
      return "receive"
    }
    return "send"
  }

  useEffect(() => {
    customAxios()
      .get(`members/${member_id}/messages/${type()}`)
      .then((res) => {
        setData(res.data.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <>
      <Title>보낸 메세지함</Title>
      <MessageList messages={data} />
      
    </>
  );
} 
