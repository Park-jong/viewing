import axios from "axios";

const SERVER = "http://70.12.246.137:8080/"

export const customAxios = axios.create({
  baseURL: SERVER,
  headers : {
    Authorization : "bearer " + localStorage.getItem("access_token")
  },
})