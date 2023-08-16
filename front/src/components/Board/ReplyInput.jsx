import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { BiLike, BiCommentDetail } from "react-icons/bi";
import UserProfile from "../Common/UserProfile";
import { customAxios } from "../../modules/Other/Axios/customAxios";
import { useLocation, useNavigate } from "react-router-dom";
import CommentAxios from "../../modules/Other/Axios/CommentAxios";
import { useSelector } from "react-redux";

const ReplyContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  /* border: 1px solid var(--gray-100); */
  //border-radius: 8px;
  display: flex;
  align-items: flex-start;

  /* display: block; */
`;

const InputStyled = styled.textarea`
  flex-grow: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
  resize: none;
  height: 16px;
  /* Higher specificity for the placeholder color */

  color: var(--gary-800);
  font-family: "Pretendard";
  font-size: 13px;

  &::placeholder {
    color: var(--gary-300);
    font-family: "Pretendard";
    font-size: 13px;

    font-weight: 300;
  }
`;

const ButtonReply = styled.button`
  padding: 10px 16px;
  font-size: 12px;
  color: #fff;
  background-color: var(--secondary);
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: var(--primary);
  }
`;

export default function ReplyInput({
  commentId,
  setReply,
  value,
  update = false,
  setIsUpdating,
}) {
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [text, setText] = useState("");
  const param = useLocation().pathname.split("/")[3];

  const isStudy =
    useLocation().pathname.split("/")[1] === "study" ? true : false;
  const navigate = useNavigate();
  const location = useLocation().pathname;
  const study_id = useLocation().pathname.split("/")[2]; // 스터디일때만 사용
  const article_id = useLocation().pathname.split("/")[4]; //스터디일때만

  useEffect(() => {
    if (value) setText(value);
  }, []);

  const handleInput = (e) => {
    setText(e.target.value);
  };

  const handleClick = () => {
    customAxios()
      .get(`members/${member_id}`)
      .then((res) => {
        if (text.trim() === "") {
          alert("댓글을 입력해주세요.");
          return;
        }
        if (update) {
          const url = isStudy
            ? `/studies/${study_id}/boards/${article_id}/comments/${commentId}`
            : `boards/${param}/comments/${commentId}/`;
          customAxios()
            .put(url, { content: text })
            .then((res) => {
              !isStudy
                ? CommentAxios(setReply, param)
                : customAxios()
                    .get(`studies/${study_id}/boards/${article_id}/comments`)
                    .then((response) => {
                      setReply(response.data);
                    })
                    .catch((err) => console.log(err));
              setIsUpdating(false);
            });
        } else {
          let url = "";
          if (isStudy) {
            url = commentId
              ? `studies/${study_id}/boards/${article_id}/comments/${commentId}/replies`
              : `studies/${study_id}/boards/${article_id}/comments`;

            customAxios()
              .post(url, { content: text })
              .then((res) => {
                customAxios()
                  .get(`studies/${study_id}/boards/${article_id}/comments`)
                  .then((response) => {
                    setReply(response.data);
                  })
                  .catch((err) => console.log(err));
                setText("");
              });
          } else {
            url = commentId
              ? `boards/${param}/comments/${commentId}/replies`
              : `boards/${param}/comments`;

            customAxios()
              .post(url, { content: text })
              .then((res) => {
                CommentAxios(setReply, param);
                setText("");
              });
          }
        }
      })
      .catch((err) => {
        window.alert("로그인이 필요합니다.");
        return;
      });
  };

  return (
    <ReplyContainer>
      <InputStyled
        as="textarea"
        placeholder="댓글을 입력하세요. (최대 200자)"
        value={text}
        onChange={handleInput}
        maxLength={200}
      />
      <ButtonReply onClick={handleClick}>등록</ButtonReply>
    </ReplyContainer>
  );
}
