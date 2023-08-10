import React from "react";
import styled from "styled-components";

const TagButton = styled.button`
  background-color: ${(props) =>
    props.isChecked ? "var(--primary)" : props.bgColor};
  color: ${(props) => (props.isChecked ? "#fff" : "var(--gray-800)")};
  padding: 8px 16px;
  font-size: 14px;
  border: none;
  border-radius: 20px;
  margin: 5px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  list-style: none;
  box-shadow: ${(props) =>
    props.isChecked ? "0px 2px 4px rgba(0, 0, 0, 0.1)" : "none"};

  &:hover {
    background-color: ${(props) => !props.isChecked && "var(--secondary)"};
    color: ${(props) => !props.isChecked && "#fff"};
  }
`;

function TagData({ tagData, clickTagBtn }) {
  return (
    // <li className="tags">
    <TagButton
      bgColor={tagData.color}
      isChecked={tagData.isChecked}
      onClick={() => clickTagBtn(tagData.id)}
    >
      {tagData.tag_category}
    </TagButton>
    // </li>
  );
}

export default TagData;
