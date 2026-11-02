import { MOCK_MEMBER } from "./mockUser";

// ---------- 날짜 도우미 ----------
const pad = (n) => String(n).padStart(2, "0");

// 오늘 기준 offset일, hour시 minute분 -> "yyyy-MM-dd HH:mm"
export function dateTime(offsetDays = 0, hour = 12, minute = 0) {
  const d = new Date();
  d.setDate(d.getDate() + offsetDays);
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(
    hour
  )}:${pad(minute)}`;
}

// "yyyy-MM-ddTHH:mm" (캘린더 형식)
export function calendarTime(offsetDays = 0, hour = 12, minute = 0) {
  return dateTime(offsetDays, hour, minute).replace(" ", "T");
}

export function nowString() {
  const d = new Date();
  return dateTime(0, d.getHours(), d.getMinutes());
}

const nextId = (list, key) =>
  list.reduce((max, item) => Math.max(max, item[key]), 0) + 1;

// ---------- 회원 ----------
export const members = [
  { ...MOCK_MEMBER },
  { memberId: 2, nickname: "면접마스터", backgroundColor: "#ff6767", backgroundImg: "cow" },
  { memberId: 3, nickname: "취준생민수", backgroundColor: "#a5ffa5", backgroundImg: "penguin" },
  { memberId: 4, nickname: "코딩하는지은", backgroundColor: "#ff76f6", backgroundImg: "dolphin" },
  { memberId: 5, nickname: "개발왕", backgroundColor: "#ffff9e", backgroundImg: "turtle" },
  { memberId: 6, nickname: "알고리즘장인", backgroundColor: "#93ffff", backgroundImg: "octopus" },
];

export const findMember = (id) =>
  members.find((m) => m.memberId === Number(id)) || members[0];

// 게시글/댓글용 작성자 (snake_case)
export const authorOf = (id) => {
  const m = findMember(id);
  return {
    member_id: m.memberId,
    nickname: m.nickname,
    member_profile_background: m.backgroundColor,
    member_profile_image: m.backgroundImg,
  };
};

// 스터디/채팅/캘린더용 멤버 (snake_case)
export const studyMemberOf = (id, isLeader = false) => {
  const m = findMember(id);
  return {
    member_id: m.memberId,
    nickname: m.nickname,
    is_leader: isLeader,
    background: m.backgroundColor,
    character: m.backgroundImg,
  };
};

// 쪽지용 멤버 (camelCase)
export const messageMemberOf = (id) => {
  const m = findMember(id);
  return {
    memberId: m.memberId,
    nickname: m.nickname,
    background: m.backgroundColor,
    character: m.backgroundImg,
  };
};

// ---------- 회사 자동완성 ----------
export const companies = [
  "삼성전자",
  "삼성SDS",
  "삼성물산",
  "삼성생명",
  "삼성에스디에스",
  "네이버",
  "카카오",
  "쿠팡",
  "토스",
  "라인",
  "우아한형제들",
  "당근마켓",
  "현대자동차",
  "LG전자",
  "SK하이닉스",
  "멀티캠퍼스",
];

// ---------- 스터디 ----------
export const STUDY_TAGS = [
  "자소서 제출 필수",
  "합격인증 필수",
  "압박면접",
  "정보공유",
  "영상회의 필수",
  "중고신입",
  "온라인 진행",
  "오프라인 진행",
  "온오프라인 혼합",
  "피드백 필수",
  "초보 환영",
];

export const studies = [
  {
    study_id: 1,
    title: "삼성전자 DS부문 면접 스터디원 모집",
    description:
      "삼성전자 DS부문 하반기 공채 대비 모의 면접 스터디입니다.\n주 2회 화상으로 모여 서로 질문하고 피드백을 나눕니다.",
    applied_company: "삼성전자",
    applied_job: "소프트웨어 개발",
    capacity: 6,
    created_at: dateTime(-12, 10, 30),
    deadline: dateTime(14, 23, 59),
    recruitment: true,
    leader_id: 1,
    career_level: "NEWCOMER",
    tags: [1, 5, 10],
    member_ids: [1, 2, 3, 4],
  },
  {
    study_id: 2,
    title: "네이버 백엔드 직무 면접 같이 준비해요",
    description: "CS 지식과 프로젝트 경험 위주로 꼬리질문 연습을 합니다.",
    applied_company: "네이버",
    applied_job: "백엔드",
    capacity: 5,
    created_at: dateTime(-8, 14, 0),
    deadline: dateTime(7, 23, 59),
    recruitment: true,
    leader_id: 2,
    career_level: "ALL",
    tags: [3, 4, 7],
    member_ids: [2, 1, 5],
  },
  {
    study_id: 3,
    title: "카카오 신입 코딩테스트 + 면접 스터디",
    description: "코딩테스트 풀이 공유와 기술 면접 연습을 병행합니다.",
    applied_company: "카카오",
    applied_job: "서버 개발",
    capacity: 4,
    created_at: dateTime(-20, 9, 0),
    deadline: dateTime(-2, 23, 59),
    recruitment: false,
    leader_id: 3,
    career_level: "NEWCOMER",
    tags: [2, 6, 9],
    member_ids: [3, 4, 5, 6],
  },
  {
    study_id: 4,
    title: "토스 프론트엔드 경력 면접 스터디",
    description: "React, 브라우저 동작 원리, 성능 최적화 질문을 다룹니다.",
    applied_company: "토스",
    applied_job: "프론트엔드",
    capacity: 4,
    created_at: dateTime(-3, 20, 0),
    deadline: dateTime(21, 23, 59),
    recruitment: true,
    leader_id: 4,
    career_level: "EXPERIENCED",
    tags: [4, 5, 8],
    member_ids: [4, 6],
  },
  {
    study_id: 5,
    title: "삼성SDS 인턴 면접 스터디",
    description: "인턴 지원자를 위한 가벼운 면접 연습 모임입니다.",
    applied_company: "삼성SDS",
    applied_job: "클라우드",
    capacity: 5,
    created_at: dateTime(-1, 18, 0),
    deadline: dateTime(10, 23, 59),
    recruitment: true,
    leader_id: 5,
    career_level: "INTERN",
    tags: [11, 7],
    member_ids: [5, 1],
  },
  {
    study_id: 6,
    title: "쿠팡 백엔드 개발자 면접 스터디",
    description: "쿠팡 백엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "쿠팡",
    applied_job: "백엔드",
    capacity: 5,
    created_at: dateTime(-15, 10, 0),
    deadline: dateTime(6, 23, 59),
    recruitment: true,
    leader_id: 2,
    career_level: "EXPERIENCED",
    tags: [4, 5, 10],
    member_ids: [2, 3],
  },
  {
    study_id: 7,
    title: "네이버 프론트엔드 신입 면접 대비",
    description: "네이버 프론트엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "네이버",
    applied_job: "프론트엔드",
    capacity: 6,
    created_at: dateTime(-14, 14, 7),
    deadline: dateTime(9, 23, 59),
    recruitment: true,
    leader_id: 3,
    career_level: "NEWCOMER",
    tags: [1, 7, 11],
    member_ids: [3, 4, 5],
  },
  {
    study_id: 8,
    title: "삼성전자 펌웨어 직무 면접 스터디",
    description: "삼성전자 펌웨어 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "삼성전자",
    applied_job: "펌웨어",
    capacity: 4,
    created_at: dateTime(-13, 20, 14),
    deadline: dateTime(12, 23, 59),
    recruitment: true,
    leader_id: 4,
    career_level: "NEWCOMER",
    tags: [2, 8],
    member_ids: [4, 1],
  },
  {
    study_id: 9,
    title: "라인 서버 개발 기술 면접 스터디",
    description: "라인 서버 개발 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "라인",
    applied_job: "서버 개발",
    capacity: 5,
    created_at: dateTime(-12, 9, 21),
    deadline: dateTime(-1, 23, 59),
    recruitment: false,
    leader_id: 5,
    career_level: "ALL",
    tags: [3, 5, 9],
    member_ids: [5, 6, 2],
  },
  {
    study_id: 10,
    title: "우아한형제들 백엔드 코딩테스트 + 면접",
    description: "우아한형제들 백엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "우아한형제들",
    applied_job: "백엔드",
    capacity: 6,
    created_at: dateTime(-11, 18, 28),
    deadline: dateTime(15, 23, 59),
    recruitment: true,
    leader_id: 6,
    career_level: "NEWCOMER",
    tags: [6, 9, 10],
    member_ids: [6, 3],
  },
  {
    study_id: 11,
    title: "당근마켓 프론트엔드 경력 면접 준비",
    description: "당근마켓 프론트엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "당근마켓",
    applied_job: "프론트엔드",
    capacity: 4,
    created_at: dateTime(-10, 21, 35),
    deadline: dateTime(5, 23, 59),
    recruitment: true,
    leader_id: 2,
    career_level: "EXPERIENCED",
    tags: [4, 5],
    member_ids: [2, 4, 5],
  },
  {
    study_id: 12,
    title: "현대자동차 SW 직군 면접 스터디",
    description: "현대자동차 소프트웨어 개발 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "현대자동차",
    applied_job: "소프트웨어 개발",
    capacity: 8,
    created_at: dateTime(-9, 13, 42),
    deadline: dateTime(20, 23, 59),
    recruitment: true,
    leader_id: 3,
    career_level: "ALL",
    tags: [1, 3, 8],
    member_ids: [3, 1, 6, 2],
  },
  {
    study_id: 13,
    title: "SK하이닉스 직무 면접 같이 준비해요",
    description: "SK하이닉스 반도체 설계 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "SK하이닉스",
    applied_job: "반도체 설계",
    capacity: 5,
    created_at: dateTime(-9, 16, 49),
    deadline: dateTime(-3, 23, 59),
    recruitment: false,
    leader_id: 4,
    career_level: "NEWCOMER",
    tags: [2, 4, 9],
    member_ids: [4, 6, 3, 5, 2],
  },
  {
    study_id: 14,
    title: "카카오 프론트엔드 신입 면접 스터디",
    description: "카카오 프론트엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "카카오",
    applied_job: "프론트엔드",
    capacity: 6,
    created_at: dateTime(-8, 10, 56),
    deadline: dateTime(8, 23, 59),
    recruitment: true,
    leader_id: 5,
    career_level: "NEWCOMER",
    tags: [1, 5, 7],
    member_ids: [5, 4],
  },
  {
    study_id: 15,
    title: "토스 백엔드 경력 이직 면접 스터디",
    description: "토스 백엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "토스",
    applied_job: "백엔드",
    capacity: 4,
    created_at: dateTime(-7, 14, 3),
    deadline: dateTime(11, 23, 59),
    recruitment: true,
    leader_id: 6,
    career_level: "EXPERIENCED",
    tags: [4, 5, 6],
    member_ids: [6, 2],
  },
  {
    study_id: 16,
    title: "LG전자 인턴 면접 스터디",
    description: "LG전자 소프트웨어 개발 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "LG전자",
    applied_job: "소프트웨어 개발",
    capacity: 5,
    created_at: dateTime(-6, 20, 10),
    deadline: dateTime(13, 23, 59),
    recruitment: true,
    leader_id: 2,
    career_level: "INTERN",
    tags: [11, 7],
    member_ids: [2, 3, 4],
  },
  {
    study_id: 17,
    title: "삼성SDS 클라우드 직무 면접 준비",
    description: "삼성SDS 클라우드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "삼성SDS",
    applied_job: "클라우드",
    capacity: 6,
    created_at: dateTime(-6, 9, 17),
    deadline: dateTime(17, 23, 59),
    recruitment: true,
    leader_id: 3,
    career_level: "ALL",
    tags: [3, 10],
    member_ids: [3, 6, 5],
  },
  {
    study_id: 18,
    title: "네이버 데이터 엔지니어 면접 스터디",
    description: "네이버 데이터 엔지니어 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "네이버",
    applied_job: "데이터 엔지니어",
    capacity: 5,
    created_at: dateTime(-5, 18, 24),
    deadline: dateTime(-2, 23, 59),
    recruitment: false,
    leader_id: 4,
    career_level: "NEWCOMER",
    tags: [2, 9, 10],
    member_ids: [4, 2, 6, 3, 5],
  },
  {
    study_id: 19,
    title: "쿠팡 프론트엔드 면접 질문 공유 스터디",
    description: "쿠팡 프론트엔드 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "쿠팡",
    applied_job: "프론트엔드",
    capacity: 6,
    created_at: dateTime(-4, 21, 31),
    deadline: dateTime(10, 23, 59),
    recruitment: true,
    leader_id: 5,
    career_level: "ALL",
    tags: [4, 7, 8],
    member_ids: [5, 3],
  },
  {
    study_id: 20,
    title: "카카오 서버 개발 경력 면접 스터디",
    description: "카카오 서버 개발 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "카카오",
    applied_job: "서버 개발",
    capacity: 4,
    created_at: dateTime(-2, 13, 38),
    deadline: dateTime(16, 23, 59),
    recruitment: true,
    leader_id: 6,
    career_level: "EXPERIENCED",
    tags: [3, 5],
    member_ids: [6, 1, 4],
  },
  {
    study_id: 21,
    title: "삼성전자 인턴 코딩테스트 스터디",
    description: "삼성전자 소프트웨어 개발 직무 면접을 함께 준비하는 스터디입니다. 주 1~2회 모의 면접과 피드백을 진행합니다.",
    applied_company: "삼성전자",
    applied_job: "소프트웨어 개발",
    capacity: 5,
    created_at: dateTime(-1, 16, 45),
    deadline: dateTime(4, 23, 59),
    recruitment: true,
    leader_id: 2,
    career_level: "INTERN",
    tags: [6, 11],
    member_ids: [2, 5, 6],
  },
];

// 찜한 스터디
export const bookmarkedStudyIds = new Set([2]);

export const newStudyId = () => nextId(studies, "study_id");

export function toStudyDto(s) {
  const leader = findMember(s.leader_id);
  return {
    study_id: s.study_id,
    title: s.title,
    description: s.description,
    applied_company: s.applied_company,
    applied_job: s.applied_job,
    capacity: s.capacity,
    head_count: s.member_ids.length,
    created_at: s.created_at,
    deadline: s.deadline,
    recruitment: s.recruitment,
    bookmark: bookmarkedStudyIds.has(s.study_id),
    leader: {
      member_id: leader.memberId,
      nickname: leader.nickname,
      background: leader.backgroundColor,
      character: leader.backgroundImg,
    },
    career_level: s.career_level,
    tags: s.tags.map((t) => STUDY_TAGS[t - 1]).filter(Boolean),
  };
}

export function toStudyDetailDto(s) {
  return {
    ...toStudyDto(s),
    members: s.member_ids.map((id) => studyMemberOf(id, id === s.leader_id)),
  };
}

// 스터디 가입 신청 (스터디별)
export const requests = {
  1: [
    {
      request_id: 1,
      user: studyMemberOf(5),
      content:
        "안녕하세요. 삼성전자 DS부문을 준비 중인 지원자입니다. 성실하게 참여하겠습니다.",
      created_at: dateTime(-1, 21, 10),
      request_files: [{ name: "자기소개서.pdf", file_id: 1 }],
    },
    {
      request_id: 2,
      user: studyMemberOf(6),
      content: "모의 면접 경험이 많지 않아 배우고 싶습니다. 잘 부탁드립니다.",
      created_at: dateTime(0, 9, 5),
      request_files: [],
    },
  ],
};

// ---------- 스터디 캘린더 / 개인 캘린더 ----------
export const studyCalendars = {
  1: [
    {
      calendar_id: 1,
      author: studyMemberOf(1, true),
      description: "정기 모의 면접",
      started_at: calendarTime(0, 19, 0),
      ended_at: calendarTime(0, 21, 0),
      is_study_calendar: true,
    },
    {
      calendar_id: 2,
      author: studyMemberOf(2),
      description: "개인 일정",
      started_at: calendarTime(0, 9, 0),
      ended_at: calendarTime(0, 13, 0),
      is_study_calendar: false,
    },
    {
      calendar_id: 3,
      author: studyMemberOf(3),
      description: "개인 일정",
      started_at: calendarTime(0, 14, 0),
      ended_at: calendarTime(0, 17, 30),
      is_study_calendar: false,
    },
    {
      calendar_id: 4,
      author: studyMemberOf(1, true),
      description: "자소서 피드백 데이",
      started_at: calendarTime(2, 20, 0),
      ended_at: calendarTime(2, 22, 0),
      is_study_calendar: true,
    },
  ],
};

export const memberCalendars = [
  {
    id: 1,
    description: "삼성전자 면접 스터디",
    started_at: calendarTime(0, 19, 0),
    ended_at: calendarTime(0, 21, 0),
    is_study_calendar: true,
    author: authorOf(1),
  },
  {
    id: 2,
    description: "코딩테스트 준비",
    started_at: calendarTime(0, 10, 0),
    ended_at: calendarTime(0, 12, 0),
    is_study_calendar: false,
    author: authorOf(1),
  },
  {
    id: 3,
    description: "알고리즘 복습",
    started_at: calendarTime(1, 15, 0),
    ended_at: calendarTime(1, 17, 0),
    is_study_calendar: false,
    author: authorOf(1),
  },
];

// ---------- 게시판 ----------
// board_type: general(자유) / review(면접 후기) / qna(질문)
export const articles = [
  { article_id: 1, board_type: "general", author_id: 2, title: "요즘 개발자 채용 시장 분위기 어떤가요?", content: "하반기 공채를 준비 중인데 서류 통과율이 많이 낮아진 것 같아요.\n다들 어떻게 준비하고 계신지 궁금합니다.", created_at: dateTime(-5, 10, 0), view_count: 152, liked_by: [2, 3, 4] },
  { article_id: 2, board_type: "general", author_id: 3, title: "면접 스터디 후기 공유합니다", content: "3개월 동안 스터디를 하면서 느낀 점을 정리해봤습니다. 꾸준함이 제일 중요한 것 같아요.", created_at: dateTime(-4, 21, 30), view_count: 98, liked_by: [1] },
  { article_id: 3, board_type: "general", author_id: 1, title: "처음 글 남겨요, 잘 부탁드립니다", content: "뷰잉에서 스터디를 시작해보려고 합니다. 많은 조언 부탁드려요!", created_at: dateTime(-2, 8, 15), view_count: 41, liked_by: [] },
  { article_id: 4, board_type: "review", author_id: 4, title: "[카카오] 서버 개발 1차 기술 면접 후기", content: "자료구조, 네트워크, 데이터베이스 위주로 질문을 받았습니다.\n특히 인덱스 동작 원리를 깊게 물어보셨어요.", created_at: dateTime(-7, 15, 0), view_count: 320, liked_by: [1, 2, 3, 5, 6] },
  { article_id: 5, board_type: "review", author_id: 5, title: "[네이버] 백엔드 최종 면접 후기", content: "임원 면접에서는 협업 경험과 갈등 해결 사례를 많이 물어보셨습니다.", created_at: dateTime(-3, 11, 45), view_count: 210, liked_by: [2, 6] },
  { article_id: 6, board_type: "review", author_id: 2, title: "[삼성전자] 직무 면접 질문 정리", content: "운영체제, 컴퓨터 구조, 코딩 테스트 관련 질문이 나왔습니다.", created_at: dateTime(-1, 19, 20), view_count: 87, liked_by: [] },
  { article_id: 7, board_type: "qna", author_id: 6, title: "자기소개 1분 스피치 어떻게 준비하나요?", content: "시간 안에 핵심만 전달하는 게 어렵습니다. 팁이 있을까요?", created_at: dateTime(-6, 13, 0), view_count: 130, liked_by: [3] },
  { article_id: 8, board_type: "qna", author_id: 1, title: "꼬리질문에 막힐 때 어떻게 대응하세요?", content: "모르는 내용이 나왔을 때 솔직하게 말하는 게 나은지 돌려 말하는 게 나은지 고민입니다.", created_at: dateTime(-2, 22, 10), view_count: 64, liked_by: [2, 4] },
  { article_id: 9, board_type: "general", author_id: 1, title: "면접 스터디 모집 후기와 팁", content: "면접 스터디 모집 후기와 팁에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-40, 17, 39), view_count: 20, liked_by: [1, 3, 4] },
  { article_id: 10, board_type: "general", author_id: 1, title: "자소서 첨삭 받는 방법 공유", content: "자소서 첨삭 받는 방법 공유에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-39, 18, 50), view_count: 33, liked_by: [] },
  { article_id: 11, board_type: "general", author_id: 1, title: "코딩테스트 준비 루틴 공유합니다", content: "코딩테스트 준비 루틴 공유합니다에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-38, 19, 1), view_count: 46, liked_by: [1] },
  { article_id: 12, board_type: "general", author_id: 1, title: "스터디 운영하면서 느낀 점", content: "스터디 운영하면서 느낀 점에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-37, 20, 12), view_count: 59, liked_by: [3, 4] },
  { article_id: 13, board_type: "general", author_id: 1, title: "포트폴리오 정리 어떻게 하세요?", content: "포트폴리오 정리 어떻게 하세요?에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-36, 21, 23), view_count: 72, liked_by: [1] },
  { article_id: 14, board_type: "general", author_id: 1, title: "면접 복장 고민입니다", content: "면접 복장 고민입니다에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-35, 8, 34), view_count: 85, liked_by: [] },
  { article_id: 15, board_type: "general", author_id: 1, title: "하반기 공채 일정 정리", content: "하반기 공채 일정 정리에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-34, 9, 45), view_count: 98, liked_by: [1, 3, 4] },
  { article_id: 16, board_type: "general", author_id: 1, title: "자소서 쓸 때 가장 어려운 부분", content: "자소서 쓸 때 가장 어려운 부분에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-33, 10, 56), view_count: 111, liked_by: [] },
  { article_id: 17, board_type: "general", author_id: 1, title: "코딩테스트 언어 선택 고민", content: "코딩테스트 언어 선택 고민에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-32, 11, 7), view_count: 124, liked_by: [1] },
  { article_id: 18, board_type: "general", author_id: 1, title: "오늘 면접 보고 왔습니다", content: "오늘 면접 보고 왔습니다에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-31, 12, 18), view_count: 137, liked_by: [3, 4] },
  { article_id: 19, board_type: "general", author_id: 1, title: "스터디원과 갈등이 생겼을 때", content: "스터디원과 갈등이 생겼을 때에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-30, 13, 29), view_count: 150, liked_by: [1] },
  { article_id: 20, board_type: "general", author_id: 3, title: "취업 준비 슬럼프 극복법", content: "취업 준비 슬럼프 극복법에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-29, 14, 40), view_count: 163, liked_by: [] },
  { article_id: 21, board_type: "general", author_id: 4, title: "면접 전날 뭐 하세요?", content: "면접 전날 뭐 하세요?에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-28, 15, 51), view_count: 176, liked_by: [1, 3, 4] },
  { article_id: 22, board_type: "general", author_id: 5, title: "포트폴리오 깃허브 정리 팁", content: "포트폴리오 깃허브 정리 팁에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-27, 16, 2), view_count: 189, liked_by: [] },
  { article_id: 23, board_type: "general", author_id: 6, title: "자소서 항목별 작성 요령", content: "자소서 항목별 작성 요령에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-26, 17, 13), view_count: 202, liked_by: [1] },
  { article_id: 24, board_type: "general", author_id: 2, title: "비대면 면접 환경 세팅 공유", content: "비대면 면접 환경 세팅 공유에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-25, 18, 24), view_count: 215, liked_by: [3, 4] },
  { article_id: 25, board_type: "general", author_id: 3, title: "코딩테스트 알고리즘 추천 문제집", content: "코딩테스트 알고리즘 추천 문제집에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-24, 19, 35), view_count: 228, liked_by: [1] },
  { article_id: 26, board_type: "general", author_id: 4, title: "스터디 시간대 어떻게 정하세요?", content: "스터디 시간대 어떻게 정하세요?에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-23, 20, 46), view_count: 241, liked_by: [] },
  { article_id: 27, board_type: "general", author_id: 5, title: "경력직 이직 준비 시작했어요", content: "경력직 이직 준비 시작했어요에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-22, 21, 57), view_count: 254, liked_by: [1, 3, 4] },
  { article_id: 28, board_type: "general", author_id: 6, title: "면접관 입장에서 본 좋은 답변", content: "면접관 입장에서 본 좋은 답변에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-21, 8, 8), view_count: 267, liked_by: [] },
  { article_id: 29, board_type: "general", author_id: 2, title: "개발자 취준 일상 공유", content: "개발자 취준 일상 공유에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-20, 9, 19), view_count: 280, liked_by: [1] },
  { article_id: 30, board_type: "general", author_id: 3, title: "합격 소식 전합니다", content: "합격 소식 전합니다에 대한 이야기를 나눠보고 싶어 글을 남깁니다.\n여러분의 경험과 의견을 댓글로 알려주세요.", created_at: dateTime(-19, 10, 30), view_count: 293, liked_by: [3, 4] },
  { article_id: 31, board_type: "review", author_id: 2, title: "[삼성전자] 1차 기술 면접 후기", content: "삼성전자 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-30, 11, 41), view_count: 50, liked_by: [1, 2] },
  { article_id: 32, board_type: "review", author_id: 3, title: "[네이버] 코딩테스트 후기", content: "네이버 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-29, 12, 52), view_count: 67, liked_by: [3] },
  { article_id: 33, board_type: "review", author_id: 4, title: "[카카오] 서류 전형 후기", content: "카카오 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-28, 13, 3), view_count: 84, liked_by: [3] },
  { article_id: 34, board_type: "review", author_id: 5, title: "[쿠팡] 인성 면접 후기", content: "쿠팡 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-27, 14, 14), view_count: 101, liked_by: [3] },
  { article_id: 35, board_type: "review", author_id: 6, title: "[토스] 임원 면접 후기", content: "토스 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-26, 15, 25), view_count: 118, liked_by: [1, 2] },
  { article_id: 36, board_type: "review", author_id: 2, title: "[라인] 최종 면접 후기", content: "라인 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-25, 16, 36), view_count: 135, liked_by: [3] },
  { article_id: 37, board_type: "review", author_id: 3, title: "[당근마켓] 직무 면접 질문 정리", content: "당근마켓 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-24, 17, 47), view_count: 152, liked_by: [3] },
  { article_id: 38, board_type: "review", author_id: 4, title: "[현대자동차] 1차 기술 면접 후기", content: "현대자동차 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-23, 18, 58), view_count: 169, liked_by: [3] },
  { article_id: 39, board_type: "review", author_id: 5, title: "[SK하이닉스] 코딩테스트 후기", content: "SK하이닉스 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-22, 19, 9), view_count: 186, liked_by: [1, 2] },
  { article_id: 40, board_type: "review", author_id: 6, title: "[LG전자] 서류 전형 후기", content: "LG전자 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-21, 20, 20), view_count: 203, liked_by: [3] },
  { article_id: 41, board_type: "review", author_id: 2, title: "[삼성SDS] 인성 면접 후기", content: "삼성SDS 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-20, 21, 31), view_count: 220, liked_by: [3] },
  { article_id: 42, board_type: "review", author_id: 3, title: "[우아한형제들] 임원 면접 후기", content: "우아한형제들 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-19, 8, 42), view_count: 237, liked_by: [3] },
  { article_id: 43, board_type: "review", author_id: 4, title: "[삼성전자] 최종 면접 후기", content: "삼성전자 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-18, 9, 53), view_count: 254, liked_by: [1, 2] },
  { article_id: 44, board_type: "review", author_id: 5, title: "[네이버] 직무 면접 질문 정리", content: "네이버 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-17, 10, 4), view_count: 271, liked_by: [3] },
  { article_id: 45, board_type: "review", author_id: 6, title: "[카카오] 1차 기술 면접 후기", content: "카카오 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-16, 11, 15), view_count: 288, liked_by: [3] },
  { article_id: 46, board_type: "review", author_id: 2, title: "[쿠팡] 코딩테스트 후기", content: "쿠팡 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-15, 12, 26), view_count: 305, liked_by: [3] },
  { article_id: 47, board_type: "review", author_id: 3, title: "[토스] 서류 전형 후기", content: "토스 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-14, 13, 37), view_count: 322, liked_by: [1, 2] },
  { article_id: 48, board_type: "review", author_id: 4, title: "[라인] 인성 면접 후기", content: "라인 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-13, 14, 48), view_count: 339, liked_by: [3] },
  { article_id: 49, board_type: "review", author_id: 5, title: "[당근마켓] 임원 면접 후기", content: "당근마켓 면접 경험을 공유합니다.\n받았던 질문과 준비하면서 도움이 된 점을 정리했습니다.", created_at: dateTime(-12, 15, 59), view_count: 356, liked_by: [3] },
  { article_id: 50, board_type: "qna", author_id: 2, title: "자소서 지원동기는 어떻게 쓰나요?", content: "자소서 지원동기는 어떻게 쓰나요?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-20, 16, 10), view_count: 30, liked_by: [1] },
  { article_id: 51, board_type: "qna", author_id: 3, title: "면접에서 단점 질문 대응법", content: "면접에서 단점 질문 대응법\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-19, 17, 21), view_count: 39, liked_by: [] },
  { article_id: 52, board_type: "qna", author_id: 4, title: "코딩테스트 시간 배분 팁이 있나요?", content: "코딩테스트 시간 배분 팁이 있나요?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-18, 18, 32), view_count: 48, liked_by: [] },
  { article_id: 53, board_type: "qna", author_id: 5, title: "스터디 인원은 몇 명이 적당할까요?", content: "스터디 인원은 몇 명이 적당할까요?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-17, 19, 43), view_count: 57, liked_by: [1] },
  { article_id: 54, board_type: "qna", author_id: 6, title: "포트폴리오에 넣으면 좋은 프로젝트는?", content: "포트폴리오에 넣으면 좋은 프로젝트는?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-16, 20, 54), view_count: 66, liked_by: [] },
  { article_id: 55, board_type: "qna", author_id: 2, title: "면접 중 모르는 질문이 나오면?", content: "면접 중 모르는 질문이 나오면?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-15, 21, 5), view_count: 75, liked_by: [] },
  { article_id: 56, board_type: "qna", author_id: 3, title: "자소서 분량은 어느 정도가 좋나요?", content: "자소서 분량은 어느 정도가 좋나요?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-14, 8, 16), view_count: 84, liked_by: [1] },
  { article_id: 57, board_type: "qna", author_id: 4, title: "화상 면접 때 시선 처리 질문", content: "화상 면접 때 시선 처리 질문\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-13, 9, 27), view_count: 93, liked_by: [] },
  { article_id: 58, board_type: "qna", author_id: 5, title: "스터디 첫 모임에서 무엇을 하나요?", content: "스터디 첫 모임에서 무엇을 하나요?\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-12, 10, 38), view_count: 102, liked_by: [] },
  { article_id: 59, board_type: "qna", author_id: 6, title: "신입 면접 역질문 추천 부탁드려요", content: "신입 면접 역질문 추천 부탁드려요\n경험이 있으신 분들의 조언 부탁드립니다.", created_at: dateTime(-11, 11, 49), view_count: 111, liked_by: [1] },
];

export const newArticleId = () => nextId(articles, "article_id");

export function countComments(comments = []) {
  return comments.reduce((n, c) => n + 1 + (c.replies || []).length, 0);
}

// 게시글 댓글: { [articleId]: [{ comment_id, author_id, content, created_at, is_delete, liked_by, replies }] }
export const commentsByBoard = {
  1: [
    {
      comment_id: 1,
      author_id: 3,
      content: "저도 비슷하게 느끼고 있어요. 포트폴리오 보강이 필요한 듯합니다.",
      created_at: dateTime(-4, 9, 0),
      is_delete: false,
      liked_by: [1],
      replies: [
        {
          comment_id: 2,
          author_id: 1,
          content: "공감합니다. 프로젝트 경험을 구체적으로 적는 게 도움이 되더라고요.",
          created_at: dateTime(-4, 10, 30),
          is_delete: false,
          liked_by: [],
        },
      ],
    },
    {
      comment_id: 3,
      author_id: 4,
      content: "스터디에서 서로 이력서를 봐주는 것도 추천드립니다.",
      created_at: dateTime(-3, 18, 20),
      is_delete: false,
      liked_by: [],
      replies: [],
    },
  ],
  4: [
    {
      comment_id: 4,
      author_id: 1,
      content: "좋은 후기 감사합니다! 인덱스 쪽은 다시 공부해야겠네요.",
      created_at: dateTime(-6, 20, 0),
      is_delete: false,
      liked_by: [4],
      replies: [],
    },
  ],
  7: [
    {
      comment_id: 5,
      author_id: 2,
      content: "두괄식으로 강점 하나, 경험 하나, 포부 하나 순서로 구성해보세요.",
      created_at: dateTime(-5, 12, 0),
      is_delete: false,
      liked_by: [6, 1],
      replies: [],
    },
  ],
};

// 스터디 게시판 댓글
export const commentsByStudyBoard = {
  1: [
    {
      comment_id: 101,
      author_id: 2,
      content: "확인했습니다. 토요일에 뵙겠습니다.",
      created_at: dateTime(-3, 13, 0),
      is_delete: false,
      liked_by: [],
      replies: [
        {
          comment_id: 102,
          author_id: 1,
          content: "네, 늦지 않게 접속해주세요!",
          created_at: dateTime(-3, 13, 30),
          is_delete: false,
          liked_by: [],
        },
      ],
    },
  ],
  2: [
    {
      comment_id: 103,
      author_id: 4,
      content: "정리 감사합니다. 많은 도움이 됐어요.",
      created_at: dateTime(-2, 20, 0),
      is_delete: false,
      liked_by: [],
      replies: [],
    },
  ],
};

export const newCommentId = () => {
  let max = 0;
  const scan = (lists) =>
    Object.values(lists).forEach((comments) =>
      comments.forEach((c) => {
        max = Math.max(max, c.comment_id);
        (c.replies || []).forEach((r) => (max = Math.max(max, r.comment_id)));
      })
    );
  scan(commentsByBoard);
  scan(commentsByStudyBoard);
  return max + 1;
};

export function toArticleDto(a, memberId, full = false) {
  const base = {
    article_id: a.article_id,
    author: authorOf(a.author_id),
    title: a.title,
    created_at: a.created_at,
    updated_at: a.updated_at || a.created_at,
    view_count: a.view_count,
    comment_count: countComments(commentsByBoard[a.article_id]),
    like_count: a.liked_by.length,
    board_type: a.board_type,
    is_like: a.liked_by.includes(Number(memberId)),
  };
  if (!full) return base;
  return { ...base, content: a.content, article_files: a.files || [] };
}

export function toCommentDto(c, memberId, withLikes = true) {
  const mapOne = (x) => ({
    comment_id: x.comment_id,
    author: authorOf(x.author_id),
    content: x.content,
    created_at: x.created_at,
    updated_at: x.updated_at || x.created_at,
    is_delete: x.is_delete,
    ...(withLikes
      ? {
          like_count: x.liked_by.length,
          is_like: x.liked_by.includes(Number(memberId)),
        }
      : {}),
  });
  return {
    ...mapOne(c),
    comment_count: (c.replies || []).length,
    replies: (c.replies || []).map(mapOne),
  };
}

// 스터디 게시판
export const studyArticles = [
  { article_id: 1, study_id: 1, author_id: 1, title: "이번 주 모의 면접 일정 공지", content: "이번 주 토요일 오후 7시에 화상회의로 모의 면접을 진행합니다.\n각자 자기소개 1분 스피치를 준비해주세요.", created_at: dateTime(-3, 12, 0) },
  { article_id: 2, study_id: 1, author_id: 2, title: "삼성전자 기출 질문 정리했어요", content: "작년 기출 질문들을 모아서 정리했습니다. 확인 후 의견 부탁드려요.", created_at: dateTime(-2, 17, 40), files: [{ name: "기출질문.txt", fileId: 1 }] },
  { article_id: 3, study_id: 1, author_id: 3, title: "피드백 감사합니다", content: "지난 모의 면접에서 받은 피드백 덕분에 많이 개선했어요.", created_at: dateTime(-1, 22, 0) },
  { article_id: 4, study_id: 2, author_id: 2, title: "스터디 운영 규칙 안내", content: "지각은 사전에 공유 부탁드립니다.", created_at: dateTime(-5, 10, 0) },
  { article_id: 5, study_id: 1, author_id: 1, title: "모의 면접 질문 리스트 공유", content: "모의 면접 질문 리스트 공유에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-30, 9, 0) },
  { article_id: 6, study_id: 1, author_id: 2, title: "이번 주 발표 순서 안내", content: "이번 주 발표 순서 안내에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-29, 10, 13) },
  { article_id: 7, study_id: 1, author_id: 3, title: "자소서 피드백 요청드립니다", content: "자소서 피드백 요청드립니다에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-28, 11, 26) },
  { article_id: 8, study_id: 1, author_id: 4, title: "회의 녹화본 정리했습니다", content: "회의 녹화본 정리했습니다에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-27, 12, 39) },
  { article_id: 9, study_id: 1, author_id: 1, title: "다음 모임 일정 투표", content: "다음 모임 일정 투표에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-26, 13, 52) },
  { article_id: 10, study_id: 1, author_id: 2, title: "면접 후기 공유합니다", content: "면접 후기 공유합니다에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-25, 14, 5) },
  { article_id: 11, study_id: 1, author_id: 3, title: "추천 CS 자료 모음", content: "추천 CS 자료 모음에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-24, 15, 18) },
  { article_id: 12, study_id: 1, author_id: 4, title: "스터디 규칙 개정 안내", content: "스터디 규칙 개정 안내에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-23, 16, 31) },
  { article_id: 13, study_id: 1, author_id: 1, title: "지난 모임 회고", content: "지난 모임 회고에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-22, 17, 44) },
  { article_id: 14, study_id: 1, author_id: 2, title: "코딩테스트 풀이 공유", content: "코딩테스트 풀이 공유에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-21, 18, 57) },
  { article_id: 15, study_id: 1, author_id: 3, title: "모의 면접 질문 리스트 공유 (2)", content: "모의 면접 질문 리스트 공유 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-20, 19, 10) },
  { article_id: 16, study_id: 1, author_id: 4, title: "이번 주 발표 순서 안내 (2)", content: "이번 주 발표 순서 안내 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-19, 20, 23) },
  { article_id: 17, study_id: 1, author_id: 1, title: "자소서 피드백 요청드립니다 (2)", content: "자소서 피드백 요청드립니다 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-18, 9, 36) },
  { article_id: 18, study_id: 1, author_id: 2, title: "회의 녹화본 정리했습니다 (2)", content: "회의 녹화본 정리했습니다 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-17, 10, 49) },
  { article_id: 19, study_id: 1, author_id: 3, title: "다음 모임 일정 투표 (2)", content: "다음 모임 일정 투표 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-16, 11, 2) },
  { article_id: 20, study_id: 1, author_id: 4, title: "면접 후기 공유합니다 (2)", content: "면접 후기 공유합니다 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-15, 12, 15) },
  { article_id: 21, study_id: 1, author_id: 1, title: "추천 CS 자료 모음 (2)", content: "추천 CS 자료 모음 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-14, 13, 28) },
  { article_id: 22, study_id: 1, author_id: 2, title: "스터디 규칙 개정 안내 (2)", content: "스터디 규칙 개정 안내 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-13, 14, 41) },
  { article_id: 23, study_id: 1, author_id: 3, title: "지난 모임 회고 (2)", content: "지난 모임 회고 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-12, 15, 54) },
  { article_id: 24, study_id: 1, author_id: 4, title: "코딩테스트 풀이 공유 (2)", content: "코딩테스트 풀이 공유 (2)에 대한 안내입니다. 확인 후 의견 남겨주세요.", created_at: dateTime(-11, 16, 7) },
];

export const newStudyArticleId = () => nextId(studyArticles, "article_id");

export function toStudyArticleDto(a, full = false) {
  const base = {
    article_id: a.article_id,
    study_id: a.study_id,
    author: authorOf(a.author_id),
    title: a.title,
    created_at: a.created_at,
    updated_at: a.updated_at || a.created_at,
    comment_count: countComments(commentsByStudyBoard[a.article_id]),
  };
  if (!full) return base;
  return { ...base, content: a.content, article_files: a.files || [] };
}

// ---------- 채팅 ----------
export const chats = {
  1: [
    { chat_id: 1, member: studyMemberOf(2), content: "다들 안녕하세요! 이번 주 일정 확인하셨나요?", created_at: dateTime(-1, 20, 1) },
    { chat_id: 2, member: studyMemberOf(3), content: "네, 토요일 7시 맞죠?", created_at: dateTime(-1, 20, 3) },
    { chat_id: 3, member: studyMemberOf(1, true), content: "맞습니다. 자기소개 스피치 꼭 준비해 오세요.", created_at: dateTime(-1, 20, 5) },
    { chat_id: 4, member: studyMemberOf(4), content: "확인했습니다!", created_at: dateTime(0, 9, 12) },
  ],
};

// ---------- 쪽지 ----------
export const messages = [
  { messageId: 1, title: "스터디 관련 문의드립니다", content: "안녕하세요. 삼성전자 스터디에 참여하고 싶은데 아직 자리가 있을까요?", created_at: dateTime(-2, 15, 0), isRead: true, author_id: 5, receiver_id: 1 },
  { messageId: 2, title: "모의 면접 일정 조율", content: "이번 주 토요일 대신 일요일로 변경 가능할까요?", created_at: dateTime(-1, 11, 20), isRead: false, author_id: 3, receiver_id: 1 },
  { messageId: 3, title: "자료 공유 감사합니다", content: "공유해주신 기출 질문 덕분에 큰 도움이 됐어요.", created_at: dateTime(0, 8, 40), isRead: false, author_id: 2, receiver_id: 1 },
  { messageId: 4, title: "스터디 참여 신청 문의", content: "신청서 제출했는데 확인 부탁드립니다.", created_at: dateTime(-3, 18, 0), isRead: true, author_id: 1, receiver_id: 4 },
  { messageId: 5, title: "회의 링크 문의", content: "화상회의는 스터디 메뉴의 영상회의 참여에서 입장하시면 됩니다.", created_at: dateTime(-1, 9, 10), isRead: true, author_id: 1, receiver_id: 6 },
  { messageId: 6, title: "스터디 참여 문의", content: "스터디 참여 문의 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-10, 9, 0), isRead: false, author_id: 2, receiver_id: 1 },
  { messageId: 7, title: "일정 조율 부탁드립니다", content: "일정 조율 부탁드립니다 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-9, 10, 9), isRead: true, author_id: 3, receiver_id: 1 },
  { messageId: 8, title: "자료 공유 감사합니다", content: "자료 공유 감사합니다 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-8, 11, 18), isRead: true, author_id: 4, receiver_id: 1 },
  { messageId: 9, title: "면접 후기 문의", content: "면접 후기 문의 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-7, 12, 27), isRead: false, author_id: 5, receiver_id: 1 },
  { messageId: 10, title: "스터디 규칙 문의", content: "스터디 규칙 문의 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-6, 13, 36), isRead: true, author_id: 6, receiver_id: 1 },
  { messageId: 11, title: "피드백 감사합니다", content: "피드백 감사합니다 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-5, 14, 45), isRead: true, author_id: 2, receiver_id: 1 },
  { messageId: 12, title: "모의 면접 일정 안내", content: "모의 면접 일정 안내 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-4, 15, 54), isRead: false, author_id: 3, receiver_id: 1 },
  { messageId: 13, title: "자소서 첨삭 요청", content: "자소서 첨삭 요청 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-3, 16, 3), isRead: true, author_id: 4, receiver_id: 1 },
  { messageId: 14, title: "화상회의 접속 문의", content: "화상회의 접속 문의 관련하여 쪽지 드립니다. 확인 부탁드립니다.", created_at: dateTime(-2, 17, 12), isRead: true, author_id: 5, receiver_id: 1 },
  { messageId: 15, title: "면접 후기 문의", content: "면접 후기 문의 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-12, 10, 0), isRead: true, author_id: 1, receiver_id: 2 },
  { messageId: 16, title: "스터디 규칙 문의", content: "스터디 규칙 문의 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-11, 11, 7), isRead: true, author_id: 1, receiver_id: 3 },
  { messageId: 17, title: "피드백 감사합니다", content: "피드백 감사합니다 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-10, 12, 14), isRead: true, author_id: 1, receiver_id: 4 },
  { messageId: 18, title: "모의 면접 일정 안내", content: "모의 면접 일정 안내 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-9, 13, 21), isRead: true, author_id: 1, receiver_id: 5 },
  { messageId: 19, title: "자소서 첨삭 요청", content: "자소서 첨삭 요청 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-8, 14, 28), isRead: true, author_id: 1, receiver_id: 6 },
  { messageId: 20, title: "화상회의 접속 문의", content: "화상회의 접속 문의 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-7, 15, 35), isRead: true, author_id: 1, receiver_id: 2 },
  { messageId: 21, title: "다음 모임 장소 문의", content: "다음 모임 장소 문의 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-6, 16, 42), isRead: true, author_id: 1, receiver_id: 3 },
  { messageId: 22, title: "스터디 참여 문의", content: "스터디 참여 문의 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-5, 17, 49), isRead: true, author_id: 1, receiver_id: 4 },
  { messageId: 23, title: "일정 조율 부탁드립니다", content: "일정 조율 부탁드립니다 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-4, 18, 56), isRead: true, author_id: 1, receiver_id: 5 },
  { messageId: 24, title: "자료 공유 감사합니다", content: "자료 공유 감사합니다 관련하여 쪽지 드립니다. 답변 기다리겠습니다.", created_at: dateTime(-3, 19, 3), isRead: true, author_id: 1, receiver_id: 6 },
];

export const newMessageId = () => nextId(messages, "messageId");

export function toMessageDto(m) {
  return {
    messageId: m.messageId,
    title: m.title,
    content: m.content,
    created_at: m.created_at,
    createdAt: m.created_at,
    isRead: m.isRead,
    author: messageMemberOf(m.author_id),
    receiver: messageMemberOf(m.receiver_id),
  };
}

// ---------- 알림 ----------
export const notifications = [
  { notificationId: 1, memberId: 1, content: "[삼성전자 DS부문 면접 스터디원 모집] 새로운 가입 신청이 있습니다.", notificationType: "StudyRequest", url: "1", isRead: false, createdAt: dateTime(0, 9, 5) },
  { notificationId: 2, memberId: 1, content: "면접마스터님으로부터 쪽지가 도착했습니다.", notificationType: "Message", url: null, isRead: false, createdAt: dateTime(0, 8, 40) },
  { notificationId: 3, memberId: 1, content: "[삼성전자 DS부문 면접 스터디원 모집] 새 게시글이 등록되었습니다.", notificationType: "StudyArticle", url: "1 2", isRead: true, createdAt: dateTime(-2, 17, 40) },
  { notificationId: 4, memberId: 1, content: "내 게시글에 새 댓글이 달렸습니다.", notificationType: "BoardComment", url: "free 1", isRead: true, createdAt: dateTime(-4, 9, 0) },
  { notificationId: 5, memberId: 1, content: "[네이버 백엔드 직무 면접 같이 준비해요] 새 게시글이 등록되었습니다.", notificationType: "StudyArticle", url: "2 4", isRead: true, createdAt: dateTime(-5, 10, 0) },
  { notificationId: 6, memberId: 1, content: "개발왕님으로부터 쪽지가 도착했습니다.", notificationType: "Message", url: null, isRead: true, createdAt: dateTime(-6, 14, 20) },
  { notificationId: 7, memberId: 1, content: "내 게시글에 새 댓글이 달렸습니다.", notificationType: "BoardComment", url: "free 3", isRead: true, createdAt: dateTime(-7, 9, 30) },
  { notificationId: 8, memberId: 1, content: "[삼성SDS 인턴 면접 스터디] 가입 신청이 승인되었습니다.", notificationType: "StudyRequest_Approve", url: "5", isRead: true, createdAt: dateTime(-8, 16, 0) },
  { notificationId: 9, memberId: 1, content: "[삼성전자 DS부문 면접 스터디원 모집] 새 일정이 등록되었습니다.", notificationType: "StudyMeeting", url: "1", isRead: true, createdAt: dateTime(-9, 11, 15) },
];
