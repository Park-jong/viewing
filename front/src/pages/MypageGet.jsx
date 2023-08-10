import React, { useEffect, useState } from "react";
import MessageList from "../components/MyPage/Organisms/MessageList";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useSelector } from "react-redux";
import { UserReducer } from "./../modules/UserReducer/UserReducer";

export default function MypageGet() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [data, setData] = useState([]);

  useEffect(() => {
    customAxios()
      .get(`members/${member_id}/messages/receive`)
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
