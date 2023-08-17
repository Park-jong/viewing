import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { customAxios } from "../modules/Other/Axios/customAxios";
import { useDispatch, useSelector } from "react-redux";

import { JWTDecoder } from "../modules/Other/JWT/JWTDecoder";
import { Login } from "../modules/UserReducer/Actions";
import { keyframes, styled } from "styled-components";
import { AiOutlineLoading3Quarters } from "react-icons/ai";

const Container = styled.div`
  margin-top: 400px;
  justify-content: center;
  text-align: center;
  display: flex;
`;

const Icon = styled(AiOutlineLoading3Quarters)`
  animation: rotate_image 6s linear infinite;
  transform-origin: 50% 50%;
  width: 60px;
  height: 60px;
  color: #2e2e2e;
  margin-right: 20px;

  @keyframes rotate_image {
    100% {
      transform: rotate(360deg);
    }
  }
`;

export default function LoginLoading() {
  let params = new URL(document.URL).searchParams;
  let code = params.get("code");
  let navigate = useNavigate();
  const dispatch = useDispatch();
  const web = localStorage.getItem("web");
  localStorage.removeItem("web");

  useEffect(() => {
    customAxios()
      .get(`oauth/${web}?code=${code}`)
      .then((res) => {
        const access_token = res.data.access_token;
        localStorage.setItem("access_token", access_token);
        const payload = JWTDecoder(access_token).payload; //복호화 email, 만료시간, userId

        customAxios()
          .get(`members/${payload.memberId}/`)
          .then((res) => {
            res.data.web = web;
            dispatch(Login(res.data)); // 리덕스에 적용
          })
          .catch((err) => {
            console.log(err, 1);
          });

        if (localStorage.getItem("prevPage")) {
          navigate(localStorage.getItem("prevPage"));
          localStorage.removeItem("prevPage");
        } else {
          navigate("/");
        }
      })
      .catch((err) => {
        alert("로그인에 실패했습니다.");
        console.log(err);
        navigate("/login");
      });
  }, []);

  return (
    <Container>
      <Icon></Icon>
      <div
        style={{
          fontSize: "30px",
        }}
      >
        로그인 중입니다.
        <br></br>잠시만 기다리세요.
      </div>
    </Container>
  );
}
