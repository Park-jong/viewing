import axios from "axios";

// const SERVER = "http://70.12.246.137:8080/"
const SERVER = "http://70.12.246.87:8080/"

export const customAxios = () => axios.create({
  baseURL: SERVER,
  headers : {
    Authorization : "Bearer " + localStorage.getItem("access_token")
  },
})