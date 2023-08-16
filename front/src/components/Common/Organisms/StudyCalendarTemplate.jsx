import { styled } from "styled-components";
import Calendar from "../Calendar";
import TimeBar from "../StudyTimeBar";
import { useState, useEffect } from "react";
import { FakeData } from "../FakeData";
import { FakeData2 } from "../FakeData2";
import moment from "moment";
import StudyCalendarModal from "../../Modal/StudyCalendarModal";
import { customAxios } from "../../../modules/Other/Axios/customAxios";
import { useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import StudyCalendar from "./../StudyCalendar";
import StudyTimeBar from "../StudyTimeBar";
import MainButton from "../../Button/MainButton";

const empty = {
  author: {
    background: null,
    character: null,
    is_leader: null,
    member_id: null,
    nickname: null,
  },
  description: "",
  ended_at: "",
  is_study_calendar: false,
  started_at: "",
};

const Container = styled.div`
  display: ${(props) => {
    if (props.isFlex) {
      return "flex";
    } else {
      return "block";
    }
  }};
  justify-content: center;
  align-items: center;
`;

const ButtonContainer = styled.div`
  margin-top: 20px;
  margin-bottom: 0px;
  display: flex;
  justify-content: right;
`;

const AddButton = styled.button``;

export default function StudyCalendarTemplate({ isFlex, data, dataChange }) {
  const [value, onChange] = useState(new Date());
  const [isOpen, setModal] = useState(false);
  const [detailData, setDetailData] = useState(empty);
  const [type, setType] = useState("");
  const studyId = useParams().studyPk;
  const member = useSelector((state) => state.UserReducer);

  const data2 = data.filter((d) => {
    return (
      moment(value).format("YY.MM.DD") ===
      moment(d.started_at).format("YY.MM.DD")
    );
  });

  const showAdd = () => {
    setType("schedule_add");
    setModal(true);
  };

  const showDetail = (data) => {
    setDetailData(data);
    setType("schedule_view");
    setModal(true);
  };

  const closeDetail = () => {
    setDetailData(empty);
    setModal(false);
  };

  const addSchedule = () => {
    const start = new Date(detailData.started_at);
    const end = new Date(detailData.ended_at);
    if (start.getTime() >= end.getTime()) {
      alert("입력값이 올바르지 않습니다.");
      return;
    }
    const body = {
      started_at: detailData.started_at,
      ended_at: detailData.ended_at,
      description: detailData.description,
      member_id: member.memberId,
    };
    customAxios()
      .post(`studies/${studyId}/calendars`, body)
      .then(() => {
        alert("저장되었습니다.");
        setDetailData(empty);
        window.location.reload();
      })
      .catch(() => {
        alert("저장에 실패하였습니다.");
      });
  };

  const updateSchedule = () => {
    const start = new Date(detailData.started_at);
    const end = new Date(detailData.ended_at);
    if (start.getTime() >= end.getTime()) {
      alert("입력값이 올바르지 않습니다.");
      return;
    }
    customAxios()
      .put(`studies/${studyId}/calendars/${detailData.calendar_id}`, detailData)
      .then(() => {
        alert("저장되었습니다.");
        window.location.reload();
      })
      .catch(() => {
        alert("변경에 실패하였습니다.");
      });
  };

  const deleteSchedule = () => {
    customAxios()
      .delete(`studies/${studyId}/calendars/${detailData.calendar_id}`)
      .then(() => {
        alert("저장되었습니다.");
        window.location.reload();
      })
      .catch(() => {
        alert("삭제에 실패하였습니다.");
      });
  };

  return (
    <Container $isFlex={isFlex}>
      <StudyCalendar
        dataChange={dataChange}
        data={data}
        value={value}
        onChange={onChange}
      ></StudyCalendar>
      <ButtonContainer>
        <MainButton
          onClick={() => {
            showAdd();
          }}
          content={"일정 추가"}
          width={100}
          height={30}
        >
        </MainButton>
      </ButtonContainer>
      <StudyTimeBar
        data={data2}
        isFlex={isFlex}
        showDetail={showDetail}
      ></StudyTimeBar>
      <StudyCalendarModal
        isOpen={isOpen}
        type={type}
        onClose={closeDetail}
        data={detailData}
        setData={setDetailData}
        updateSchedule={updateSchedule}
        deleteSchedule={deleteSchedule}
        addSchedule={addSchedule}
      ></StudyCalendarModal>
    </Container>
  );
}
