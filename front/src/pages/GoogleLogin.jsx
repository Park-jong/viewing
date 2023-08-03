import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useDispatch, useSelector } from "react-redux";
import { UserReducer } from "./../modules/UserReducer/UserReducer";
import { JWTDecoder } from "../modules/Other/JWT/JWTDecoder";
import { Login } from "../modules/UserReducer/Actions";

export default function GoogleLogin() {
  let params = new URL(document.URL).searchParams;
  let code = params.get("code");
  let navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    customAxios
      .get(`oauth/google?code=${code}`)
      .then((res) => {
        const access_token = res.data.access_token;
        localStorage.setItem("access_token", access_token);

        const payload = JWTDecoder(access_token).payload; //복호화 email, 만료시간, userId

        customAxios
          .get(`users/${payload.memberId}/`)
          .then((res) => {
            dispatch(Login(res.data)); // 리덕스에 적용
            console.log(res.data);
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

  return <div>로그인 중입니다. 잠시만 기다리세요.</div>;
}
