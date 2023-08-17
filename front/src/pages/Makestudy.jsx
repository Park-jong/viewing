import React, { useRef, useState } from "react";
import styled from "styled-components";
import { useEffect } from "react";
import TagData from "../components/Study/TagData";
import StyledButton from "../components/Button/StyledButton";
import Capacity from "./../Icons/capacity";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

//https://velog.io/@miyoni/TIL39

const careerOptions = ["전체", "신입", "경력", "인턴"];
const valueOptions = ["ALL", "NEWCOMER", "EXPERIENCED", "INTERN"];

const TAG_LIST = [
  { id: 0, tag_category: "자소서 제출 필수", color: "rgb(246, 246, 246)" },
  { id: 1, tag_category: "합격인증 필수", color: "rgb(240, 248, 248)" },
  { id: 2, tag_category: "압박면접", color: "rgb(238, 237, 244)" },
  { id: 3, tag_category: "정보공유", color: "rgb(238, 245, 246)" },
  { id: 4, tag_category: "영상회의 필수", color: "rgb(236, 249, 247)" },
  { id: 5, tag_category: "중고신입", color: "rgb(239, 251, 243)" },
  { id: 6, tag_category: "온라인 진행", color: "rgb(233, 231, 238)" },
  { id: 7, tag_category: "오프라인 진행", color: "rgb(240, 249, 245)" },
  { id: 8, tag_category: "온오프라인 혼합", color: "rgb(233, 244, 251)" },
  { id: 9, tag_category: "피드백 필수", color: "rgb(236, 241, 241)" },
  { id: 10, tag_category: "초보 환영", color: "rgb(241, 236, 241)" },
];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  padding-top: 20px;
`;

const FlexContainer = styled.div`
  display: flex;
`;

const Title = styled.div`
  font-size: 24px;
  font-weight: 700;
  margin-top: 50px;

  margin-bottom: 30px;
  user-select: none;
`;

const HorizontalLine = styled.div`
  width: 100%;
  height: 3px;
  background-color: var(--gray-100);
  margin-bottom: 20px;
  user-select: none;
`;

const InputContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 20px 0px;
  position: relative;
  user-select: none;
`;

const InputLabel = styled.label`
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
  user-select: none;
`;

const InputField = styled.input`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 400px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: ${(props) =>
    props.$state
      ? "1px solid red"
      : "var(--border-height) solid var(--border-before-color)"};

  margin-right: 50px;

  &:hover {
    background: var(--input-hovered-color);
  }

  &:focus {
    outline: none;
    border-bottom: var(--border-height) solid var(--border-after-color);
  }
`;

const SelectField = styled.select`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 200px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: var(--border-height) solid var(--border-before-color);

  margin-right: 50px;

  &:hover {
    background: var(--input-hovered-color);
  }

  &:focus {
    outline: none;
    border-bottom: var(--border-height) solid var(--border-after-color);
  }
`;

const InputFieldWithIcon = styled.div`
  position: relative;
  display: flex;
  align-items: center;
`;

const DateIcon = styled(Capacity)`
  position: absolute;
  top: 50%;
  right: 12px;
  transform: translateY(-50%);
  pointer-events: none;
`;

const DeadlineInput = styled.input`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 200px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: var(--border-height) solid var(--border-before-color);
  padding-right: 28px;

  &:hover {
    background: var(--input-hovered-color);
  }

  &:focus {
    outline: none;
    border-bottom: var(--border-height) solid var(--border-after-color);
  }
`;

const TextArea = styled.textarea`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 920px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding: 8px;
  border: ${(props) =>
    props.$state
      ? "1px solid red"
      : "var(--border-height) solid var(--border-before-color)"};
  height: 300px;

  font-family: Pretendard;

  &:focus {
    outline: none;

    border: var(--border-height) solid var(--border-after-color);
  }
`;

const TagContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  align-items: center;
`;

const Tag = styled.button`
  background-color: ${(props) => props.bgColor};
  color: ${(props) => (props.isChecked ? "#000000" : "var(--gray-800)")};
  padding: 8px 16px;
  font-size: 14px;
  border: none;
  border-radius: 20px;
  margin: 5px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  box-shadow: ${(props) =>
    props.isChecked ? "0px 2px 4px rgba(0, 0, 0, 0.1)" : "none"};

  &:hover {
    background-color: ${(props) => !props.isChecked && "var(--primary)"};
    color: ${(props) => !props.isChecked && "#fff"};
  }
`;

const BodyTags = styled.div`
  width: 900px;
  border-radius: 5px;

  margin-bottom: 60px;
`;

const TagText = styled.p`
  font-size: 14px;
  margin-bottom: 10px;
`;

const TagPick = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
`;

const Suggestions = styled.div`
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 300px;
  width: 100%;
  max-height: 115px;
  position: absolute;
  top: 100%; /* 입력 필드 아래에 위치 */
  left: 10px;
  overflow: auto;
  word-wrap: break-word;
  z-index: 10; /* 다른 요소들보다 위에 위치 */
  display: ${(props) => props.$visible};
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
  &::-webkit-scrollbar {
    display: none; /* 크롬, 사파리, 오페라, 엣지 */
  }
`;

const SuggestionValue = styled.div`
  color: var(--gray-500);
  padding-top: 5px;
  padding-bottom: 5px;
  &:hover {
    background-color: var(--secondary);
    cursor: pointer;
  }
  background-color: ${(props) =>
    props.$selected ? "var(--secondary)" : "none"};
`;

export default function MakeStudy() {
  const currentDate = new Date().toISOString().split("T")[0]; // Get the current date in YYYY-MM-DD format
  const navigate = useNavigate();

  const [appliedJob, setAppliedJob] = useState("");
  const [appliedCompany, setAppliedCompany] = useState("");
  const [career, setCareer] = useState("ALL");
  const [capacity, setCapacity] = useState(1);
  const [deadline, setDeadline] = useState(currentDate);
  const [studyName, setStudyName] = useState("");
  const [studyDescription, setStudyDescription] = useState("");
  const [tagList, setTagList] = useState(TAG_LIST);

  const [filterTag, setFilterTag] = useState([]);

  const [suggestion, setSuggestion] = useState([]);
  const [isVisible, setIsVisible] = useState(false);

  const [studyCompanyState, setStudyCompanyState] = useState(false);
  const [studyJobState, setStudyJobState] = useState(false);
  const [studyNameState, setStudyNameState] = useState(false);
  const [studyDescState, setStudyDescState] = useState(false);
  const [isClick, setIsClick] = useState({ value: "", click: false });

  const maxContentLength = 2000;

  const [suggestionSelect, setSuggestionSelect] = useState(null);
  const suggestionRef = useRef(null);
  const parentRef = useRef(null);

  const clickSuggestion = (suggestion) => {
    setIsClick((prev) => {
      const result = { ...prev };
      result.click = true;
      result.value = suggestion;
      return result;
    });
  };

  const enterKeySelect = () => {
    if (suggestionSelect != null) {
      setIsClick((prev) => {
        const result = { ...prev };
        result.click = true;
        result.value = suggestion[suggestionSelect];
        return result;
      });
      return;
    }
  };

  const pressDown = () => {
    if (suggestionSelect === null) {
      setSuggestionSelect(0);
    } else {
      setSuggestionSelect(suggestionSelect + 1);
    }
  };

  const pressUp = () => {
    if (suggestionSelect === null) {
      setSuggestionSelect(() => suggestion.length - 1);
    } else {
      setSuggestionSelect((prev) => {
        return prev - 1;
      });
    }
  };

  const handleFocus = (e) => {
    if (e.target.value.length > 0) {
      if (suggestion.length > 0) setIsVisible(true);
      else {
        customAxios()
          .get(`companies/${appliedCompany}`)
          .then(({ data }) => {
            setSuggestion((prev) => {
              return [...data];
            });
          });
      }
    }
  };

  const clickTagBtn = (id) => {
    if (tagList[id].isChecked == undefined) tagList[id].isChecked = true;
    else tagList[id].isChecked = !tagList[id].isChecked;
    setTagList((prev) => [...prev]);
  };

  useEffect(() => {
    setFilterTag(tagList.filter((tag) => tag.isChecked === true));
  }, [tagList]);

  const tagAxios = () => filterTag.map((item) => item.id + 1);
  //redux
  const leaderId = useSelector((state) => state.UserReducer.memberId);

  const validation = (body) => {
    let result = true;

    if (body.title) setStudyNameState(false);
    else {
      setStudyNameState(true);
      result = false;
    }

    if (body.description) setStudyDescState(false);
    else {
      setStudyDescState(true);
      result = false;
    }

    if (body.applied_company) setStudyCompanyState(false);
    else {
      setStudyCompanyState(true);
      result = false;
    }

    if (body.applied_job) setStudyJobState(false);
    else {
      setStudyJobState(true);
      result = false;
    }

    if (suggestion.includes(appliedCompany)) setStudyCompanyState(false);
    else {
      setStudyCompanyState(true);
      result = false;
    }

    return result;
  };

  //axios
  const handleMake = () => {
    const studyData = {
      title: studyName,
      applied_company: appliedCompany,
      capacity: capacity,
      description: studyDescription,
      career_level: career,
      deadline: `${deadline}T00:00:00 `,
      applied_job: appliedJob,
      tags: tagAxios(),
      leader_id: leaderId,
    };
    if (validation(studyData)) {
      customAxios()
        .post(`studies`, studyData)
        .then((response) => {
          alert("생성되었습니다.");
          navigate("/");
        })
        .catch((error) => {
          alert("스터디 생성 중 에러가 발생했습니다.");
        });
    } else {
      alert("입력 값을 확인해 주세요.");
    }
  };

  useEffect(() => {
    if (suggestion.length === 0) setIsVisible(false);
    else setIsVisible(true);
  }, [suggestion]);

  useEffect(() => {
    setSuggestionSelect(null);
    if (isClick.click) {
      setIsClick((prev) => {
        const result = { ...prev };
        result.click = false;
        result.value = "";
        return result;
      });
      setIsVisible(false);
      return;
    }

    if (appliedCompany.length > 0) {
      customAxios()
        .get(`companies/${appliedCompany}`)
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
  }, [appliedCompany]);

  useEffect(() => {
    if (isClick.click) {
      if (appliedCompany === isClick.value) {
        setIsVisible(false);
        setSuggestionSelect(null);
        setIsClick((prev) => {
          const result = { ...prev };
          result.click = false;
          result.value = "";
          return result;
        });
      } else {
        setAppliedCompany(() => isClick.value);
      }
    }
  }, [isClick]);

  useEffect(() => {
    if (suggestionSelect < 0) {
      setSuggestionSelect(suggestion.length - 1);
    } else if (suggestionSelect >= suggestion.length) {
      setSuggestionSelect(0);
    }
    const selectedChild = suggestionRef.current;
    const parentElement = parentRef.current;
    // Scroll the parent element to bring the selected child into view
    if (selectedChild != null && parentElement != null) {
      parentElement.scrollTop = selectedChild.offsetTop;
    }
  }, [suggestionSelect]);

  useEffect(() => {
    setFilterTag([]);
  }, []);

  return (
    <Container>
      <Title>✍ 스터디 기본 정보를 입력해주세요.</Title>
      <HorizontalLine></HorizontalLine>
      <FlexContainer>
        <InputContainer>
          <InputLabel>지원회사</InputLabel>
          <InputField
            type="text"
            value={appliedCompany}
            maxLength={30}
            onChange={(e) => {
              setAppliedCompany(e.target.value);
            }}
            $state={studyCompanyState}
            onBlur={() => {
              setIsVisible(false);
            }}
            onFocus={(e) => {
              handleFocus(e);
            }}
            onKeyUp={(e) => {
              if (e.key === "Enter") {
                enterKeySelect();
              }
            }}
            onKeyDown={(e) => {
              if (e.key === "Down" || e.key === "ArrowDown") {
                e.preventDefault();
                pressDown();
              } else if (e.key === "Up" || e.key === "ArrowUp") {
                e.preventDefault();
                pressUp();
              }
            }}
          />
          <Suggestions $visible={isVisible ? "block" : "none"} ref={parentRef}>
            {suggestion.map((s, idx) => {
              return suggestionSelect === idx ? (
                <SuggestionValue
                  key={idx}
                  onMouseDown={() => {
                    clickSuggestion(s);
                  }}
                  $selected={true}
                  ref={suggestionRef}
                >
                  {s}
                </SuggestionValue>
              ) : (
                <SuggestionValue
                  key={idx}
                  onMouseDown={() => {
                    clickSuggestion(s);
                  }}
                  $selected={false}
                >
                  {s}
                </SuggestionValue>
              );
            })}
          </Suggestions>
        </InputContainer>
        <InputContainer>
          <InputLabel>지원직무</InputLabel>
          <InputField
            type="text"
            value={appliedJob}
            maxLength={20}
            onChange={(e) => {
              setAppliedJob(e.target.value);
            }}
            $state={studyJobState}
          />
        </InputContainer>

        <InputContainer>
          <InputLabel>경력 여부</InputLabel>
          <SelectField
            value={career}
            onChange={(e) => {
              setCareer(e.target.value);
            }}
          >
            {careerOptions.map((option, idx) => (
              <option key={option} value={valueOptions[idx]}>
                {option}
              </option>
            ))}
          </SelectField>
        </InputContainer>
      </FlexContainer>
      <FlexContainer>
        <InputContainer style={{ marginRight: "250px" }}>
          <InputLabel>모집마감</InputLabel>
          <InputFieldWithIcon>
            <DeadlineInput
              type="date"
              value={deadline}
              onChange={(e) => {
                setDeadline(e.target.value);
              }}
              min={currentDate}
            />
            <DateIcon />
          </InputFieldWithIcon>
        </InputContainer>
        <InputContainer>
          <InputLabel>최대인원</InputLabel>
          <SelectField
            value={capacity}
            onChange={(e) => {
              setCapacity(e.target.value);
            }}
          >
            {Array.from({ length: 6 }, (_, i) => i + 1).map((number) => (
              <option key={number} value={number}>
                {number}
              </option>
            ))}
          </SelectField>
        </InputContainer>
      </FlexContainer>

      <Title>📜 스터디에 대해 소개해주세요.</Title>
      <HorizontalLine></HorizontalLine>
      <InputContainer>
        <InputLabel>스터디명</InputLabel>
        <InputField
          type="text"
          value={studyName}
          maxLength={30}
          onChange={(e) => setStudyName(e.target.value)}
          $state={studyNameState}
        />
        <span className="input-border"></span>
      </InputContainer>
      <InputContainer>
        <InputLabel>스터디 소개글</InputLabel>
        <TextArea
          value={studyDescription}
          maxLength={maxContentLength}
          onChange={(e) => setStudyDescription(e.target.value)}
          placeholder="스터디에 대해 소개해주세요."
          $state={studyDescState}
        />
        {studyDescription.length}/{maxContentLength}
        <span className="input-border"></span>
      </InputContainer>
      <InputContainer>
        <InputLabel>태그</InputLabel>
      </InputContainer>
      <BodyTags>
        <TagPick>
          <TagText>
            스터디의 특성을 나타내줄 수 있는 태그를 선택하세요!{" "}
          </TagText>
          <TagContainer>
            {tagList.map((tag) => (
              <TagData
                key={tag.id}
                tagData={tag}
                clickTagBtn={() => {
                  clickTagBtn(tag.id);
                }}
              />
            ))}
          </TagContainer>
          {/* <TagPickText>선택된 태그</TagPickText>
          <TagContainer>
            {filterTag.map((tag) => (
              <Tag key={tag.id} bgColor={tag.color} isChecked>
                {tag.tag_category}
              </Tag>
            ))} */}
          {/* </TagContainer> */}
        </TagPick>
      </BodyTags>
      <StyledButton
        marginright={10}
        width={200}
        height={45}
        fontSize={16}
        content="스터디 생성하기"
        onClick={handleMake}
      />
    </Container>
  );
}
