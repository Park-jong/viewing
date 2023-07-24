import { styled } from "styled-components";

const StyledTextArea = styled.textarea`
  height: ${props => `${props.height}px`};
  width: ${props => `${props.width}px`};
  padding: 10px;
  border: 1px solid var(--primary);
  border-radius: 5px;
  font-size: 14px;
  resize: none; /* 사용자의 크기 조정 비활성화 */
  font-family: "Pretendard";
`;

export default function InputBox({width, height, setText, text}) {

  const onChange = (e) => {
    setText(e.target.value)
  }

  return (
      <StyledTextArea
        type="text"
        placeholder="입력하세요..."
        width = {width}
        height = {height}
        onChange={onChange}
        value={text}
      />
  );
}