import React from "react";

const ChatList = ({ chatData }) => {
  return (
    <div>
      {chatData.map((data) => {
        return <ChatLine data={data}></ChatLine>;
      })}
    </div>
  );
};

const ChatLine = ({ data }) => {
  return (
    <>
      {data.sender === "notice" ? (
        <b>{data.message}</b>
      ) : (
        <b>
          {JSON.parse(data.sender).clientData.nickname} : {data.message}
        </b>
      )}
      <hr></hr>
    </>
  );
};

export default ChatList;
