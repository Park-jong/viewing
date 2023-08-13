import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import MessageList from "./../components/MyPage/Organisms/MessageList";
import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";
import { styled } from "styled-components";
import MainButton from "./../components/Button/MainButton";

const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const Title = styled.div`
  margin-top: 20px;
  font-size: 30px;
  margin-bottom: 30px;
`;

const AllCheck = styled.input`
  margin-bottom: 15px;
`

export default function MypageMessages() {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [data, setData] = useState([]);
  const [deleted, setDeleted] = useState([]);
  const param = useLocation().pathname.split("/")[2];
  const type = () => {
    if (param === "get") {
      return "receive";
    }
    return "send";
  };

  const handleDelete = async () => {
    await deleted.forEach((d) => {
      customAxios()
        .delete(`messages/${d}`)
        .then((res) => console.log(res))
        .catch((err) => alert(`${d}번 쪽지를 삭제하는데 실패했습니다.`));
    });
    alert("삭제완료했습니다.");
    setDeleted([]);
    customAxios()
      .get(`members/${member_id}/messages/${type()}`)
      .then((res) => {
        setData(res.data.data);
      })
      .catch((err) => console.log(err));
  };

  const handleAllCheck = (e) => {
    if (e.target.checked) {
      setDeleted(data.map((d) => d.messageId));
    } else {
      setDeleted([]);
    }
  };

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
      <TitleContainer>
        {param === "send" ? (
          <Title>보낸 쪽지함</Title>
        ) : (
          <Title>받은 쪽지함</Title>
        )}
        {data.length > 0 ? (
          <MainButton
            width={80}
            height={30}
            content={"삭제하기"}
            onClick={() => handleDelete()}
          ></MainButton>
        ) : (
          <></>
        )}
      </TitleContainer>
      {data.length > 0 ? (
        <>
          <AllCheck
            type="checkbox"
            checked={data.length === deleted.length ? true : false}
            onChange={handleAllCheck}
          />
          전체 선택
        </>
      ) : (
        <></>
      )}

      <MessageList messages={data} deleted={deleted} setDeleted={setDeleted} />
    </>
  );
}
