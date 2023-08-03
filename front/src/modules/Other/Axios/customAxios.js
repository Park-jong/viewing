import axios from "axios";

const SERVER = "http://70.12.246.137:8080/"
console.log(localStorage.getItem("access_token"))

export const customAxios = () => axios.create({
  baseURL: SERVER,
  headers : {
    Authorization : "Bearer " + localStorage.getItem("access_token")
  },
})