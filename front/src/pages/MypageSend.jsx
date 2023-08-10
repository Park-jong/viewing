import { useEffect, useState } from 'react';
import { customAxios } from '../modules/Other/Axios/customAxios';
import MessageList from './../components/MyPage/Organisms/MessageList';
import { useSelector } from 'react-redux';

export default function MypageSend() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [data, setData] = useState([]);

  useEffect(() => {
    customAxios()
      .get(`members/${member_id}/messages/send`)
      .then((res) => {
        console.log(res)
        setData(res.data.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <>
      <MessageList messages={data} />
      
    </>
  );
} 
