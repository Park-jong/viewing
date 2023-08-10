import styled from "styled-components";
import MessageBox from "../MessageBox";

export default function MessageList({messages}) {
  console.log(messages)

  return (
    <>
      {messages.length>0 ? messages.map((message,idx) => (
        <MessageBox
          key={idx}
         message_id={message.messageId} // Pass the message_id prop here
          name={message.author.nickname}
          title={message.title}
          date={message.created_at}
        ></MessageBox>
      ))
    : <>메세지가 없습니다...</>}
    </>
  );
}
