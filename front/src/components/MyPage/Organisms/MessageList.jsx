import styled from "styled-components";
import MessageBox from "../MessageBox";

const EmptyMessage = styled.div`
  padding-top: 30px;
  padding-bottom: 30px;
`;

export default function MessageList({ messages, deleted, setDeleted,isSend}) {
  return (
    <>
      {messages.length > 0 ? (
        messages.map((message, idx) => (
          <MessageBox
            key={idx}
            message_id={message.messageId} // Pass the message_id prop here
            name={isSend ? message.author.nickname : message.receiver.nickname}
            title={message.title}
            date={message.created_at}
            deleted={deleted}
            setDeleted={setDeleted}
          ></MessageBox>
        ))
      ) : (
        <EmptyMessage>메세지가 없습니다 😅</EmptyMessage>
      )}
    </>
  );
}
