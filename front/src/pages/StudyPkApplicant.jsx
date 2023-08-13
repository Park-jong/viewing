import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import ResumeList from "../components/Study/Organism/ResumeList";
import { customAxios } from "../modules/Other/Axios/customAxios";

export default function StudyPkApplicant() {
  const param = useLocation().pathname.split("/")[2]; // studyId
  const [res, setRes] = useState([]);

  useEffect(() => {
    customAxios()
      .get(`studies/${param}/requests`)
      .then((response) => {
        // 받아온 응답 데이터를 상태 변수에 저장
        setRes(response.data);
      });
  }, []);

  return (
    <>
      <ResumeList data={res} setData={setRes} />
    </>
  );
}
