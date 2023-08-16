import { useLocation } from "react-router-dom";
import { customAxios } from "./customAxios";

export default function ArticleAxios(callback, param, board) {
  const type = () => {
    if (board === "question") {
      return "qna";
    } else if (board === "free") {
      return "general";
    } else if (board === "interview") {
      return "review";
    }
  };

  return customAxios()
    .get(`boards/${type()}/${param}`)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      console.log(err);
    });
}
