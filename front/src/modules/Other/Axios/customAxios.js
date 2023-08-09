import axios from "axios";

// const SERVER = "http://70.12.246.137:8080/"
const SERVER = "http://70.12.246.87:8080/";
// const SERVER = "http://70.12.246.107:8080/";
// const SERVER = "http://i9a205.q.ssafy.io:8080/"

export const customAxios = () => {
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

  return axios.create(config);
};
