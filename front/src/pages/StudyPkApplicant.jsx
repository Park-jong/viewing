import { useEffect, useState } from "react";
import ResumeList from "../components/Study/Organism/ResumeList";
import ResumeBox from "../components/Study/ResumeBox";
import StudySideBar from "../components/Study/StudySideBar";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useLocation } from "react-router-dom";
import styled from "styled-components";
import { useSelector } from "react-redux";

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
      <ResumeList data={res}
        setData={setRes} />
    </>
  );
}
