import {
  articles,
  bookmarkedStudyIds,
  chats,
  commentsByBoard,
  commentsByStudyBoard,
  memberCalendars,
  members,
  messages,
  notifications,
  requests,
  studies,
  studyArticles,
  studyCalendars,
} from "./mockData";

// 목 데이터의 변경 사항을 localStorage에 저장해 새로고침, window.open 팝업 사이에서 공유한다.
// 회원탈퇴의 localStorage.clear()가 초기화 수단이다.
// 주의: 시드(mockData.js)나 저장 형식을 바꾸면 반드시 STATE_VERSION을 올린다.
// (올리지 않으면 이전 시드가 저장된 브라우저에서 새 시드가 반영되지 않는다.)
const STORAGE_KEY = "viewing-mock-state";
const STATE_VERSION = 2;

const ARRAYS = {
  members,
  studies,
  articles,
  studyArticles,
  messages,
  notifications,
  memberCalendars,
};

const OBJECTS = {
  requests,
  studyCalendars,
  commentsByBoard,
  commentsByStudyBoard,
  chats,
};

// 마지막으로 읽거나 쓴 직렬화 문자열
let lastRaw = null;
let warned = false;

const clone = (value) => JSON.parse(JSON.stringify(value));

function serialize() {
  const data = {};
  Object.keys(ARRAYS).forEach((key) => {
    data[key] = ARRAYS[key];
  });
  Object.keys(OBJECTS).forEach((key) => {
    data[key] = OBJECTS[key];
  });
  data.bookmarkedStudyIds = Array.from(bookmarkedStudyIds);
  return JSON.stringify({ version: STATE_VERSION, data });
}

// 다른 모듈이 const 참조를 들고 있으므로 재할당하지 않고 제자리에서 복원한다.
function apply(data) {
  Object.keys(ARRAYS).forEach((key) => {
    if (!Array.isArray(data[key])) return;
    const target = ARRAYS[key];
    target.length = 0;
    target.push(...clone(data[key]));
  });
  Object.keys(OBJECTS).forEach((key) => {
    const source = data[key];
    if (!source || typeof source !== "object" || Array.isArray(source)) return;
    const target = OBJECTS[key];
    Object.keys(target).forEach((k) => delete target[k]);
    Object.assign(target, clone(source));
  });
  if (Array.isArray(data.bookmarkedStudyIds)) {
    bookmarkedStudyIds.clear();
    data.bookmarkedStudyIds.forEach((id) => bookmarkedStudyIds.add(id));
  }
}

export function syncFromStorage() {
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY);
    if (raw === lastRaw) return;
    if (raw === null) {
      // 저장된 상태가 없으면 메모리 상태를 유지하고, 다음 저장 때 다시 기록한다.
      lastRaw = null;
      return;
    }
    lastRaw = raw;
    let parsed = null;
    try {
      parsed = JSON.parse(raw);
    } catch (e) {
      parsed = null;
    }
    if (!parsed || parsed.version !== STATE_VERSION || !parsed.data) {
      window.localStorage.removeItem(STORAGE_KEY);
      lastRaw = null;
      return;
    }
    apply(parsed.data);
  } catch (e) {
    // localStorage 접근 불가 등: 메모리 모드로 계속한다.
  }
}

export function saveToStorage() {
  try {
    const raw = serialize();
    if (raw === lastRaw) return;
    window.localStorage.setItem(STORAGE_KEY, raw);
    lastRaw = raw;
  } catch (e) {
    if (!warned) {
      warned = true;
      console.warn("[mock] 상태를 저장하지 못해 메모리 모드로 동작합니다.", e);
    }
  }
}
