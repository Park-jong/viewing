import React, { useState } from "react";
import styled from "styled-components";
import Search from "../../Icons/Search";
import { Link, useLocation } from "react-router-dom";
import MainButton from "./../Button/MainButton";
import { customAxios } from "../../modules/Other/Axios/customAxios";

const SearchContainer = styled.div`
  width: 762px;
  height: 30px;
  display: flex;
  align-items: center;
  background-color: var(--gray-100);
  padding: 8px;
  border-radius: 5px;
`;

const VerticalLine = styled.div`
  height: 100%;
  width: 0.5px;
  background-color: var(--gray-300);
`;

const SearchInput = styled.input`
  flex: 1;
  border: none;
  outline: none;
  padding: 12px;
  border-radius: 4px;
  color: var(--gray-700);
  background-color: var(--gray-100);

  ::placeholder {
    /* Change the placeholder font color here */
    color: var(--gray-100);
  }
`;

const Dropdown = styled.select`
  margin-left: 8px;
  padding: 8px;
  border: none;
  outline: none;
  background-color: #ffffff;
  color: var(--gray-500);
  border-radius: 4px;
  cursor: pointer;
  /* appearance: none; Remove default arrow icon on certain browsers */
  background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23ffffff' viewBox='0 0 24 24' width='24' height='24'%3E%3Cpath d='M7 10l5 5 5-5z'/%3E%3C/svg%3E"); /* Custom arrow icon */
  background-repeat: no-repeat;
  background-position: right 8px center; /* Position the arrow icon on the right side */
`;

const ButtonContainer = styled(Link)`
  display: flex;
  align-items: center;
  justify-content: center;
`;

const SearchButton = styled.button`
  width: 40px; /* Adjust the width as needed */
  height: 30px;

  margin-left: 8px;
  border: none;
  outline: none;
  background-color: var(--primary);
  color: white;
  border-radius: 20px;
  cursor: pointer;

  display: flex;
  justify-content: center;
  align-items: center;
`;

const SearchIcon = styled(Search)`
  width: 20px; /* Adjust the size of the Search icon */
`;

export default function SearchBoxBoard() {
  const [input, setInput] = useState("");
  const [selectedOption, setSelectedOption] = useState("title");
  const param = useLocation().pathname.split("/");
  const isStudy = param[1] === "study" ? true : false;

  const handleInputChange = (e) => {
    setInput(e.target.value);
  };

  const handleDropdownChange = (e) => {
    setSelectedOption(e.target.value);
  };

  const handleSearch = () => {
    if (input.trim() === "") {
      alert("검색어를 입력해주세요.");
      return;
    }

    const queryString = isStudy
      ? `/study/${
          param[2]
        }/board?searchBy=${selectedOption}&keyword=${encodeURIComponent(input)}`
      : `/board/${
          param[2]
        }?searchBy=${selectedOption}&keyword=${encodeURIComponent(input)}`;
    window.location.href = queryString;
  };

  return (
    <SearchContainer>
      <SearchInput
        type="text"
        value={input}
        onChange={handleInputChange}
        maxLength={100}
        placeholder="검색어를 입력하세요."
        onKeyUp={(e) => {
          if (e.key === "Enter") {
            handleSearch();
          }
        }}
      />
      <VerticalLine />

      <Dropdown value={selectedOption} onChange={handleDropdownChange}>
        <option value="title">제목</option>
        <option value="content">제목+내용</option>
        <option value="author">작성자</option>
      </Dropdown>
      <ButtonContainer>
        <SearchButton onClick={handleSearch}>
          <SearchIcon />
        </SearchButton>
      </ButtonContainer>
    </SearchContainer>
  );
}
