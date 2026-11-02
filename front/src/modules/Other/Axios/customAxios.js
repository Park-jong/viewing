import axios from "axios";
import mockAdapter from "../../../mock/mockAdapter";
import { ensureMockToken } from "../../../mock/mockUser";

// const SERVER = "http://70.12.246.137:8080/"
const SERVER = process.env.REACT_APP_SERVER_URL;
// const SERVER = "http://70.12.246.107:8080/";
// const SERVER = "http://i9a205.q.ssafy.io:8080/"

export const customAxios = () => {
  ensureMockToken();
  const token = localStorage.getItem("access_token");
  const config = token
    ? {
        baseURL: SERVER,
        headers: {
          Authorization: "Bearer " + token,
        },
      }
    : {
        baseURL: SERVER,
      };

  // 실제 서버 대신 목 어댑터가 모든 요청에 응답한다.
  return axios.create({ ...config, adapter: mockAdapter });
};
