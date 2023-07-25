import { styled } from "styled-components"
import { useState } from "react";

const Container = styled.div`
  height: 25px;
  border: 1px solid #ccc;
  text-align: center;
  padding-top: 6px;

  &:hover{
    background-color: #ccc;
  }
`

export default function MiniMenu ({content, from, to}) {
  const [noteText, setNoteText] = useState('');

  const handleNoteChange = (event) => {
    setNoteText(event.target.value);
  };

  const handleImageClick = () => {
    const newWindow = window.open('', '_blank', 'width=395,height=340');
    newWindow.document.write(`
      <html>
      <head>
        <title>쪽지 작성</title>
        <style>
          @import url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.8/dist/web/static/pretendard.css");
          * {
            font-family: "Pretendard";
          }

          textarea{
            resize:none; 
            margin-bottom:20px;
          }

          .container{
            display:flex; 
            justify-content:center;
          }
        </style>
      </head>
      <body>
        <h4>쪽지 작성</h4>
        <h5>받는 사람 : ${to}</h5>
        <textarea rows="12" cols="50">${noteText}</textarea>
        <br />
        <div class="container">
          <button onclick="sendNote()">쪽지 보내기</button>
        </div>
        
        <script>
          function sendNote() {
            const noteText = document.querySelector('textarea').value;
            console.log('보내는 쪽지 내용:', noteText);
            // 여기서 실제로 쪽지를 보내는 로직을 구현할 수 있습니다.
            // 예를 들어, 서버로 AJAX 요청을 보내는 등의 작업이 가능합니다.
            // 이 예제에서는 콘솔에 출력하는 것으로 대체합니다.
            // window.close(); // 작성 창을 닫습니다.
          }
        </script>
      </body>
      </html>
    `);
  }

  return <Container onClick={handleImageClick}>{content}</Container>
}