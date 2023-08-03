import { useLocation } from "react-router-dom";

export default function StudyPkChat() {
  console.log(useLocation().pathname.split("/")[2])

  return <></>;
}
