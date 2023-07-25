import styled from "styled-components";
import MessageBox from "../MessageBox";

export default function MessageList() {
  const messages = [
    {
      message_id: 1,
      author: {
        id: 123,
        nickname: "홍길동",
        background: "green",
        character: "rabbit",
        hat: "beanie",
      },
      receiver: {
        id: 123,
        nickname: "홍길동",
        background: "green",
        character: "rabbit",
        hat: "beanie",
      },
      title: "스터디 참여 관련 문의",
      created_at: "2023-07-14",
      is_read: false,
    },
    {
      message_id: 2,
      author: {
        id: 123,
        nickname: "홍길동",
        background: "green",
        character: "rabbit",
        hat: "beanie",
      },
      receiver: {
        id: 123,
        nickname: "홍길동",
        background: "green",
        character: "rabbit",
        hat: "beanie",
      },
      title: "스터디 참여 관련 문의",
      created_at: "2023-07-14",
      is_read: true,
    },
  ];

  return (
    <>
      {messages.map((message) => (
        <MessageBox
          key={message.message_id}
          name={message.author.nickname}
          title={message.title}
          date={message.created_at}
        >
          sdjflk
        </MessageBox>
      ))}
    </>
  );
}
