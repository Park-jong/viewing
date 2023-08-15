import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Search from "../../Icons/Search";
import { Link } from "react-router-dom";
import { customAxios } from "../../modules/Other/Axios/customAxios";

const SearchContainer = styled.div`
  position: relative;

  width: ${(props) => `${props.$width}px`};
  height: 30px;
  display: flex;
  align-items: center;
  background-color: #ffffff;
  padding: 8px;
  border-radius: 24px;

  border: 0.5px var(--gray-200) solid;

  &:focus-within {
    border-color: var(--primary);
    border-width: 2px;
    box-shadow: 0 0 2px rgba(0, 0, 0, 0.207); /* Change the shadow color and size when focused */
  }
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

const Suggestions = styled.div`
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 300px;
  width: 100%;
  max-height: 100px;
  position: absolute;
  top: 100%; /* 입력 필드 아래에 위치 */
  left: 20px;
  overflow: auto;
  word-wrap: break-word;
  z-index: 10; /* 다른 요소들보다 위에 위치 */
  display: ${(props) => props.$visible};
`;

const SuggestionValue = styled.div``;

export default function SearchBox({ width, appliedCompany, job, careerLevel }) {
  const [input1, setInput1] = useState(appliedCompany ? appliedCompany : "");
  const [input2, setInput2] = useState(job ? job : "");
  const [suggestion, setSuggestion] = useState([]);
  const [isVisible, setIsVisible] = useState(false);
  const [selectedOption, setSelectedOption] = useState(
    careerLevel ? careerLevel : "ALL"
  );

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

  const clickSuggestion = (suggestion) => {
    setInput1(suggestion);
  };

  useEffect(() => {
    if (suggestion.length === 0) setIsVisible(false);
    else if (suggestion.length === 1 && input1 === suggestion[0])
      setIsVisible(false);
    else setIsVisible(true);
  }, [suggestion]);

  useEffect(() => {
    if (input1.length > 0) {
      customAxios()
        .get(`companies/${input1}`)
        .then(({ data }) => {
          setSuggestion((prev) => {
            return [...data];
          });
        });
    } else {
      const arr = [];
      setSuggestion((prev) => {
        return arr;
      });
    }
  }, [input1]);
  return (
    <SearchContainer $width={width}>
      <SearchInput
        type="text"
        name="input1"
        value={input1}
        maxLength={30}
        onChange={(e) => {
          handleInputChange(e);
        }}
        placeholder="회사명을 입력하세요 (필수)"
        onKeyUp={(e) => {
          if (e.key === "Enter") {
            handleSearch();
          }
        }}
      />
      <VerticalLine />
      <SearchInput
        type="text"
        name="input2"
        value={input2}
        maxLength={20}
        onChange={(e) => {
          handleInputChange(e);
        }}
        placeholder="직무를 입력하세요 (선택)"
        onKeyUp={(e) => {
          if (e.key === "Enter") {
            handleSearch();
          }
        }}
        maxLength={100}
      />
      <VerticalLine />

      <Dropdown
        value={selectedOption}
        onChange={(e) => {
          handleDropdownChange(e);
        }}
      >
        <option value="ALL">전체</option>
        <option value="NEWCOMER">신입</option>
        <option value="EXPERIENCED">경력</option>
        <option value="INTERN">인턴</option>
      </Dropdown>
      <ButtonContainer>
        <SearchButton
          onClick={() => {
            handleSearch();
          }}
        >
          <SearchIcon />
        </SearchButton>
      </ButtonContainer>
      <Suggestions $visible={isVisible ? "block" : "none"}>
        {suggestion.map((s, idx) => {
          return (
            <SuggestionValue
              key={idx}
              onClick={() => {
                clickSuggestion(s);
              }}
            >
              {s}
            </SuggestionValue>
          );
        })}
      </Suggestions>
    </SearchContainer>
  );
}
