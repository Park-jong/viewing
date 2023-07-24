import React, { useState } from "react";
import styled from "styled-components";
import Search from "../../Icons/Search";
import { Link } from "react-router-dom";

const SearchContainer = styled.div`
  width: 762px;
  height: 30px;
  display: flex;
  align-items: center;
  background-color: #ffffff;
  padding: 8px;
  border-radius: 24px;
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

export default function SearchBox() {
  const [input1, setInput1] = useState("");
  const [input2, setInput2] = useState("");
  const [selectedOption, setSelectedOption] = useState("option1");

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "input1") {
      setInput1(value);
    } else if (name === "input2") {
      setInput2(value);
    }
  };

  const handleDropdownChange = (e) => {
    setSelectedOption(e.target.value);
  };

  const handleSearch = () => {
    if (input1.trim() === "") {
      alert("회사명을 입력해주세요.");
      return;
    }

    // Construct the query string
    const queryString = `/search?appliedCompany=${encodeURIComponent(
      input1
    )}&job=${encodeURIComponent(input2)}&careerLevel=${selectedOption}`;
    // Navigate to the search results page with the query string
    window.location.href = queryString;
  };

  // Disable the search button if input1 is empty
  const isInput1Empty = input1.trim() === "";

  return (
    <SearchContainer>
      <SearchInput
        type="text"
        name="input1"
        value={input1}
        onChange={handleInputChange}
        placeholder="회사명을 입력하세요 (필수)"
      />
      <VerticalLine />
      <SearchInput
        type="text"
        name="input2"
        value={input2}
        onChange={handleInputChange}
        placeholder="직무를 입력하세요 (선택)"
      />
      <VerticalLine />

      <Dropdown value={selectedOption} onChange={handleDropdownChange}>
        <option value="option1">전체</option>
        <option value="option2">신입</option>
        <option value="option3">경력</option>
        <option value="option4">경력무관</option>
      </Dropdown>
      <ButtonContainer>
        <SearchButton onClick={handleSearch} disabled={isInput1Empty}>
          <SearchIcon />
        </SearchButton>
      </ButtonContainer>
    </SearchContainer>
  );
}
