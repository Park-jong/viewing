import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios'


export default function GitHubLogin() {

    let params = new URL(document.URL).searchParams;
    let code = params.get('code')

    let navigate = useNavigate()

    useEffect (() => {
      axios.get(`http://70.12.246.137:8080/oauth/github?code=${code}`)
      .then((res) => {
        localStorage.setItem('access_token', res.data.access_token)
        if (localStorage.getItem('prevPage')) {
          navigate(localStorage.getItem('prevPage'))
          localStorage.removeItem('prevPage')
        } 
      })
      .catch((err) => {
        alert("로그인에 실패했습니다.");
        console.log(err);
        navigate("/login");
      })
    }, [])

    return <div>로그인 중입니다. 잠시만 기다리세요.</div>
  }
  