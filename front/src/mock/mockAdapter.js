import {
  articles,
  authorOf,
  bookmarkedStudyIds,
  calendarTime,
  chats,
  commentsByBoard,
  commentsByStudyBoard,
  companies,
  memberCalendars,
  members,
  messages,
  newArticleId,
  newCommentId,
  newMessageId,
  newStudyArticleId,
  newStudyId,
  notifications,
  nowString,
  requests,
  studies,
  studyArticles,
  studyCalendars,
  studyMemberOf,
  toArticleDto,
  toCommentDto,
  toMessageDto,
  toStudyArticleDto,
  toStudyDetailDto,
  toStudyDto,
} from "./mockData";
import { saveToStorage, syncFromStorage } from "./mockStore";
import { MOCK_ACCESS_TOKEN, MOCK_MEMBER } from "./mockUser";

// 프론트 단독 실행용 axios 어댑터.
// customAxios() 인스턴스로 나가는 모든 요청을 네트워크 대신 여기서 처리한다.

const ME = MOCK_MEMBER.memberId;

const clone = (value) =>
  value === undefined ? undefined : JSON.parse(JSON.stringify(value));

const notFound = () => {
  const e = new Error("mock: not found");
  e.mockStatus = 404;
  return e;
};

// ---------- 공통 도우미 ----------
function paginate(list, query, defaultSize = 20) {
  const size = Number(query.get("size")) || defaultSize;
  const page = Number(query.get("page")) || 0;
  const totalPages = Math.max(1, Math.ceil(list.length / size));
  return {
    content: list.slice(page * size, page * size + size),
    number: page,
    size,
    totalElements: list.length,
    totalPages,
    first: page === 0,
    last: page >= totalPages - 1,
  };
}

function matchesKeyword(text, keyword) {
  return String(text).toLowerCase().includes(keyword.toLowerCase());
}

function searchArticles(list, query) {
  const keyword = query.get("keyword");
  if (!keyword) return list;
  const searchBy = query.get("searchBy") || "title";
  return list.filter((a) => {
    if (searchBy === "author")
      return matchesKeyword(authorOf(a.author_id).nickname, keyword);
    if (searchBy === "content")
      return (
        matchesKeyword(a.title, keyword) || matchesKeyword(a.content, keyword)
      );
    return matchesKeyword(a.title, keyword);
  });
}

function findArticle(id) {
  const article = articles.find((a) => a.article_id === Number(id));
  if (!article) throw notFound();
  return article;
}

function findStudy(id) {
  return studies.find((s) => s.study_id === Number(id));
}

function findStudyArticle(studyId, articleId) {
  const article = studyArticles.find(
    (a) => a.article_id === Number(articleId) && a.study_id === Number(studyId)
  );
  if (!article) throw notFound();
  return article;
}

// 댓글 트리에서 comment_id로 (부모, 항목, 소속 목록)을 찾는다.
function locateComment(comments, commentId) {
  const id = Number(commentId);
  for (const c of comments) {
    if (c.comment_id === id) return { parent: null, comment: c, list: comments };
    const reply = (c.replies || []).find((r) => r.comment_id === id);
    if (reply) return { parent: c, comment: reply, list: c.replies };
  }
  return null;
}

function removeComment(comments, commentId) {
  const found = locateComment(comments, commentId);
  if (!found) return;
  if (found.parent === null && found.comment.replies.length > 0) {
    // 답글이 있는 댓글은 삭제 표시만 한다.
    found.comment.is_delete = true;
  } else {
    found.list.splice(found.list.indexOf(found.comment), 1);
  }
}

function addComment(store, key, body, parentId) {
  const comments = (store[key] = store[key] || []);
  const item = {
    comment_id: newCommentId(),
    author_id: ME,
    content: (body && body.content) || "",
    created_at: nowString(),
    is_delete: false,
    liked_by: [],
  };
  if (parentId) {
    const found = locateComment(comments, parentId);
    if (found) {
      found.comment.replies.push(item);
      return item.comment_id;
    }
  }
  comments.push({ ...item, replies: [] });
  return item.comment_id;
}

function updateComment(comments, commentId, body) {
  const found = locateComment(comments, commentId);
  if (found) {
    found.comment.content = (body && body.content) || found.comment.content;
    found.comment.updated_at = nowString();
  }
}

function setCommentLike(commentId, on) {
  Object.values(commentsByBoard).forEach((comments) => {
    const found = locateComment(comments, commentId);
    if (!found) return;
    const likes = found.comment.liked_by;
    const has = likes.includes(ME);
    if (on && !has) likes.push(ME);
    if (!on && has) likes.splice(likes.indexOf(ME), 1);
  });
}

function setArticleLike(articleId, on) {
  const likes = findArticle(articleId).liked_by;
  const has = likes.includes(ME);
  if (on && !has) likes.push(ME);
  if (!on && has) likes.splice(likes.indexOf(ME), 1);
}

const isMine = (study) => study.member_ids.includes(ME);

// ---------- 라우트 테이블 ----------
// [method, 정규식, 핸들러({ m: 정규식 그룹, query, body }) => 응답 데이터]
const routes = [
  // 인증 / 회원
  ["GET", /^oauth\/\w+$/, () => ({ access_token: MOCK_ACCESS_TOKEN })],
  ["GET", /^login\/nickname\/check\/.*$/, () => ""],
  ["GET", /^members\/(\d+)$/, () => ({ ...members[0] })],
  [
    "PUT",
    /^members\/(\d+)\/nickname$/,
    ({ body }) => {
      if (body && body.nickname) members[0].nickname = body.nickname;
      return "";
    },
  ],
  [
    "PUT",
    /^members\/(\d+)\/profile$/,
    ({ body }) => {
      if (body && body.background) members[0].backgroundColor = body.background;
      if (body && body.character) members[0].backgroundImg = body.character;
      return "";
    },
  ],
  ["DELETE", /^members\/(\d+)$/, () => ""],

  // 알림
  [
    "PUT",
    /^members\/(\d+)\/notification\/(\d+)$/,
    ({ m }) => {
      const n = notifications.find((x) => x.notificationId === Number(m[2]));
      if (n) n.isRead = true;
      return "";
    },
  ],

  // 회사 자동완성
  [
    "GET",
    /^companies\/(.*)$/,
    ({ m }) => {
      const keyword = decodeURIComponent(m[1]);
      return companies.filter((c) => matchesKeyword(c, keyword)).slice(0, 8);
    },
  ],

  // 쪽지
  [
    "GET",
    /^members\/(\d+)\/messages\/(receive|send)$/,
    ({ m }) => ({
      data: messages
        .filter((x) =>
          m[2] === "receive" ? x.receiver_id === ME : x.author_id === ME
        )
        .sort((a, b) => b.messageId - a.messageId)
        .map(toMessageDto),
    }),
  ],
  [
    "GET",
    /^messages\/(\d+)$/,
    ({ m }) => {
      const msg = messages.find((x) => x.messageId === Number(m[1]));
      if (!msg) throw notFound();
      msg.isRead = true;
      return toMessageDto(msg);
    },
  ],
  [
    "POST",
    /^messages$/,
    ({ body }) => {
      const id = newMessageId();
      messages.push({
        messageId: id,
        title: (body && body.title) || "",
        content: (body && body.content) || "",
        created_at: nowString(),
        isRead: false,
        author_id: ME,
        receiver_id: Number(body && body.receiver_id) || 2,
      });
      return { messageId: id };
    },
  ],
  [
    "DELETE",
    /^messages\/(\d+)$/,
    ({ m }) => {
      const idx = messages.findIndex((x) => x.messageId === Number(m[1]));
      if (idx >= 0) messages.splice(idx, 1);
      return "";
    },
  ],

  // 커뮤니티 게시글
  [
    "GET",
    /^boards\/(general|review|qna)$/,
    ({ m, query }) => {
      const list = articles
        .filter((a) => a.board_type === m[1])
        .sort((a, b) => b.article_id - a.article_id);
      const page = paginate(searchArticles(list, query), query);
      return { ...page, content: page.content.map((a) => toArticleDto(a, ME)) };
    },
  ],
  [
    "GET",
    /^boards\/(general|review|qna)\/(\d+)$/,
    ({ m }) => toArticleDto(findArticle(m[2]), ME, true),
  ],
  [
    "POST",
    /^boards\/(general|review|qna)$/,
    ({ m, body }) => {
      const id = newArticleId();
      articles.push({
        article_id: id,
        board_type: m[1],
        author_id: ME,
        title: (body && body.title) || "제목 없음",
        content: (body && body.content) || "",
        created_at: nowString(),
        view_count: 0,
        liked_by: [],
      });
      return id;
    },
  ],
  [
    "PUT",
    /^boards\/(general|review|qna)\/(\d+)$/,
    ({ m, body }) => {
      const article = findArticle(m[2]);
      if (body && body.title) article.title = body.title;
      if (body && body.content !== undefined) article.content = body.content;
      article.updated_at = nowString();
      return "";
    },
  ],
  [
    "POST",
    /^members\/(\d+)\/likes\/boards\/(\d+)$/,
    ({ m }) => {
      setArticleLike(m[2], true);
      return "";
    },
  ],
  [
    "DELETE",
    /^members\/(\d+)\/likes\/boards\/(\d+)$/,
    ({ m }) => {
      setArticleLike(m[2], false);
      return "";
    },
  ],

  // 커뮤니티 댓글
  [
    "GET",
    /^boards\/(\d+)\/comments$/,
    ({ m }) => (commentsByBoard[m[1]] || []).map((c) => toCommentDto(c, ME)),
  ],
  [
    "POST",
    /^boards\/(\d+)\/comments$/,
    ({ m, body }) => addComment(commentsByBoard, m[1], body),
  ],
  [
    "POST",
    /^boards\/(\d+)\/comments\/(\d+)\/replies$/,
    ({ m, body }) => addComment(commentsByBoard, m[1], body, m[2]),
  ],
  [
    "PUT",
    /^boards\/(\d+)\/comments\/(\d+)$/,
    ({ m, body }) => {
      updateComment(commentsByBoard[m[1]] || [], m[2], body);
      return "";
    },
  ],
  [
    "DELETE",
    /^boards\/(\d+)\/comments\/(\d+)$/,
    ({ m }) => {
      removeComment(commentsByBoard[m[1]] || [], m[2]);
      return "";
    },
  ],
  [
    "POST",
    /^members\/(\d+)\/likes\/comments\/(\d+)$/,
    ({ m }) => {
      setCommentLike(m[2], true);
      return "";
    },
  ],
  [
    "DELETE",
    /^members\/(\d+)\/likes\/comments\/(\d+)$/,
    ({ m }) => {
      setCommentLike(m[2], false);
      return "";
    },
  ],

  // 마이페이지 게시글 (작성 / 좋아요 / 댓글)
  [
    "GET",
    /^members\/(\d+)\/article$/,
    ({ query }) => {
      const board = query.get("board");
      const type = query.get("searchType");
      return articles
        .filter((a) => a.board_type === board)
        .filter((a) => {
          if (type === "write") return a.author_id === ME;
          if (type === "favor") return a.liked_by.includes(ME);
          if (type === "comment")
            return (commentsByBoard[a.article_id] || []).some(
              (c) =>
                c.author_id === ME || c.replies.some((r) => r.author_id === ME)
            );
          return true;
        })
        .sort((a, b) => b.article_id - a.article_id)
        .map((a) => toArticleDto(a, ME));
    },
  ],

  // 스터디 목록 / 상세
  [
    "GET",
    /^studies$/,
    ({ query }) => {
      const company = query.get("appliedCompany");
      const job = query.get("appliedJob");
      const career = query.get("careerLevel");
      const onlyRecruiting = query.get("option") === "true";
      const tag = Number(query.get("tag")) || 0;
      const found = studies
        .filter((s) => !company || matchesKeyword(s.applied_company, company))
        .filter((s) => !job || matchesKeyword(s.applied_job, job))
        .filter(
          (s) =>
            !career ||
            career === "ALL" ||
            s.career_level === career ||
            s.career_level === "ALL"
        )
        .filter((s) => !onlyRecruiting || s.recruitment)
        .filter((s) => !tag || s.tags.includes(tag))
        .sort((a, b) => b.study_id - a.study_id);
      const page = paginate(found, query, 9);
      return { ...page, content: page.content.map(toStudyDto) };
    },
  ],
  [
    "POST",
    /^studies$/,
    ({ body }) => {
      const id = newStudyId();
      studies.push({
        study_id: id,
        title: (body && body.title) || "새 스터디",
        description: (body && body.description) || "",
        applied_company: (body && body.applied_company) || "",
        applied_job: (body && body.applied_job) || "",
        capacity: (body && body.capacity) || 4,
        created_at: nowString(),
        deadline: String((body && body.deadline) || nowString())
          .replace("T", " ")
          .trim(),
        recruitment: true,
        leader_id: ME,
        career_level: (body && body.career_level) || "ALL",
        tags: (body && body.tags) || [],
        member_ids: [ME],
      });
      return id;
    },
  ],
  [
    "GET",
    /^studies\/(\d+)$/,
    ({ m }) => {
      const study = findStudy(m[1]);
      if (!study) throw notFound();
      return toStudyDto(study);
    },
  ],
  [
    "GET",
    /^studies\/(\d+)\/detail$/,
    ({ m }) => {
      const study = findStudy(m[1]);
      if (!study) throw notFound();
      return toStudyDetailDto(study);
    },
  ],
  [
    "PUT",
    /^studies\/(\d+)$/,
    ({ m, body }) => {
      const study = findStudy(m[1]);
      if (study && body) {
        if (body.title !== undefined) study.title = body.title;
        if (body.description !== undefined) study.description = body.description;
        if (body.applied_job !== undefined) study.applied_job = body.applied_job;
        if (body.capacity !== undefined) study.capacity = body.capacity;
        if (body.recruitment !== undefined) study.recruitment = body.recruitment;
        if (body.deadline) study.deadline = String(body.deadline).replace("T", " ");
        if (body.career_level) study.career_level = body.career_level;
        if (Array.isArray(body.tags)) study.tags = body.tags;
      }
      return "";
    },
  ],
  [
    "DELETE",
    /^studies\/(\d+)$/,
    ({ m }) => {
      const idx = studies.findIndex((s) => s.study_id === Number(m[1]));
      if (idx >= 0) studies.splice(idx, 1);
      return "";
    },
  ],
  // 같은 API를 두 곳에서 다른 목적으로 쓴다.
  // - 스터디룸(/study/:id/*, 상세 제외): StudySideBar의 진입 권한 검사 -> 항상 통과
  // - 그 외(검색, 마이페이지, 상세의 ApplyModal 카드): StudyCard의 멤버십 판별
  //   -> 멤버가 아니면 404여야 /detail로 이동한다.
  [
    "GET",
    /^studies\/(\d+)\/member$/,
    ({ m }) => {
      const study = findStudy(m[1]);
      const pathname = window.location.pathname;
      const inStudyRoom =
        /^\/study\/\d+(\/|$)/.test(pathname) &&
        !/^\/study\/\d+\/detail\/?$/.test(pathname);
      if (inStudyRoom) {
        return {
          ...studyMemberOf(ME),
          is_leader: study ? study.leader_id === ME : true,
        };
      }
      if (!study || !study.member_ids.includes(ME)) throw notFound();
      return {
        ...studyMemberOf(ME),
        is_leader: study.leader_id === ME,
      };
    },
  ],
  [
    "PUT",
    /^studies\/(\d+)\/members\/leader$/,
    ({ m, body }) => {
      const study = findStudy(m[1]);
      if (study && body && body.after_leader_id) {
        study.leader_id = Number(body.after_leader_id);
      }
      return "";
    },
  ],
  [
    "DELETE",
    /^studies\/(\d+)\/members\/(\d+)\/ban$/,
    ({ m }) => {
      const study = findStudy(m[1]);
      if (study)
        study.member_ids = study.member_ids.filter((id) => id !== Number(m[2]));
      return "";
    },
  ],
  ["DELETE", /^studies\/(\d+)\/members\/(\d+)\/exit$/, () => ""],

  // 스터디 찜 / 내 스터디
  [
    "GET",
    /^members\/(\d+)\/bookmarks\/studies$/,
    () =>
      studies.filter((s) => bookmarkedStudyIds.has(s.study_id)).map(toStudyDto),
  ],
  [
    "GET",
    /^members\/(\d+)\/studies$/,
    () => studies.filter(isMine).map(toStudyDto),
  ],
  [
    "POST",
    /^members\/(\d+)\/studies\/(\d+)\/bookmark$/,
    ({ m }) => {
      bookmarkedStudyIds.add(Number(m[2]));
      return { studyBookmarkId: Number(m[2]) };
    },
  ],
  [
    "DELETE",
    /^members\/(\d+)\/studies\/(\d+)\/bookmark$/,
    ({ m }) => {
      bookmarkedStudyIds.delete(Number(m[2]));
      return "";
    },
  ],

  // 스터디 가입 신청
  ["GET", /^studies\/(\d+)\/requests$/, ({ m }) => requests[m[1]] || []],
  ["POST", /^studies\/(\d+)\/requests$/, () => ""],
  [
    "POST",
    /^studies\/(\d+)\/requests\/(\d+)\/(approval|denial)$/,
    ({ m, body }) => {
      const list = requests[m[1]] || [];
      const idx = list.findIndex((r) => r.request_id === Number(m[2]));
      if (idx >= 0) {
        const [req] = list.splice(idx, 1);
        const study = findStudy(m[1]);
        if (m[3] === "approval" && study) {
          const id = Number((body && body.user_id) || req.user.member_id);
          if (!study.member_ids.includes(id)) study.member_ids.push(id);
        }
      }
      return "";
    },
  ],

  // 스터디 게시판
  [
    "GET",
    /^studies\/(\d+)\/boards$/,
    ({ m, query }) => {
      const list = studyArticles
        .filter((a) => a.study_id === Number(m[1]))
        .sort((a, b) => b.article_id - a.article_id);
      const page = paginate(searchArticles(list, query), query);
      return { ...page, content: page.content.map((a) => toStudyArticleDto(a)) };
    },
  ],
  [
    "POST",
    /^studies\/(\d+)\/boards$/,
    ({ m, body }) => {
      const id = newStudyArticleId();
      studyArticles.push({
        article_id: id,
        study_id: Number(m[1]),
        author_id: ME,
        title: (body && body.title) || "제목 없음",
        content: (body && body.content) || "",
        created_at: nowString(),
      });
      return id;
    },
  ],
  [
    "GET",
    /^studies\/(\d+)\/boards\/(\d+)$/,
    ({ m }) => toStudyArticleDto(findStudyArticle(m[1], m[2]), true),
  ],
  [
    "PUT",
    /^studies\/(\d+)\/boards\/(\d+)$/,
    ({ m, body }) => {
      const article = findStudyArticle(m[1], m[2]);
      if (body && body.title) article.title = body.title;
      if (body && body.content !== undefined) article.content = body.content;
      article.updated_at = nowString();
      return "";
    },
  ],
  [
    "DELETE",
    /^studies\/(\d+)\/boards\/(\d+)$/,
    ({ m }) => {
      const idx = studyArticles.findIndex(
        (a) => a.article_id === Number(m[2]) && a.study_id === Number(m[1])
      );
      if (idx >= 0) studyArticles.splice(idx, 1);
      return "";
    },
  ],
  [
    "GET",
    /^studies\/(\d+)\/boards\/(\d+)\/comments$/,
    ({ m }) =>
      (commentsByStudyBoard[m[2]] || []).map((c) => toCommentDto(c, ME, false)),
  ],
  [
    "POST",
    /^studies\/(\d+)\/boards\/(\d+)\/comments$/,
    ({ m, body }) => addComment(commentsByStudyBoard, m[2], body),
  ],
  [
    "POST",
    /^studies\/(\d+)\/boards\/(\d+)\/comments\/(\d+)\/replies$/,
    ({ m, body }) => addComment(commentsByStudyBoard, m[2], body, m[3]),
  ],
  [
    "PUT",
    /^studies\/(\d+)\/boards\/(\d+)\/comments\/(\d+)$/,
    ({ m, body }) => {
      updateComment(commentsByStudyBoard[m[2]] || [], m[3], body);
      return "";
    },
  ],
  [
    "DELETE",
    /^studies\/(\d+)\/boards\/(\d+)\/comments\/(\d+)$/,
    ({ m }) => {
      removeComment(commentsByStudyBoard[m[2]] || [], m[3]);
      return "";
    },
  ],

  // 스터디 캘린더
  ["GET", /^studies\/(\d+)\/calendars$/, ({ m }) => studyCalendars[m[1]] || []],
  [
    "POST",
    /^studies\/(\d+)\/calendars$/,
    ({ m, body }) => {
      const list = (studyCalendars[m[1]] = studyCalendars[m[1]] || []);
      const id = list.reduce((max, c) => Math.max(max, c.calendar_id), 0) + 1;
      list.push({
        calendar_id: id,
        author: studyMemberOf(ME, true),
        description: (body && body.description) || "",
        started_at: body && body.started_at,
        ended_at: body && body.ended_at,
        is_study_calendar: true,
      });
      return { calendarId: id };
    },
  ],
  [
    "PUT",
    /^studies\/(\d+)\/calendars\/(\d+)$/,
    ({ m, body }) => {
      const item = (studyCalendars[m[1]] || []).find(
        (c) => c.calendar_id === Number(m[2])
      );
      if (item && body) {
        item.description = body.description;
        item.started_at = body.started_at;
        item.ended_at = body.ended_at;
      }
      return "";
    },
  ],
  [
    "DELETE",
    /^studies\/(\d+)\/calendars\/(\d+)$/,
    ({ m }) => {
      const list = studyCalendars[m[1]] || [];
      const idx = list.findIndex((c) => c.calendar_id === Number(m[2]));
      if (idx >= 0) list.splice(idx, 1);
      return "";
    },
  ],

  // 개인 캘린더
  ["GET", /^members\/(\d+)\/calendars$/, () => memberCalendars],
  [
    "POST",
    /^members\/(\d+)\/calendars$/,
    ({ body }) => {
      const id = memberCalendars.reduce((max, c) => Math.max(max, c.id), 0) + 1;
      memberCalendars.push({
        id,
        description: (body && body.description) || "",
        started_at: (body && body.started_at) || calendarTime(0, 9, 0),
        ended_at: (body && body.ended_at) || calendarTime(0, 10, 0),
        is_study_calendar: false,
        author: authorOf(ME),
      });
      return { calendarId: id };
    },
  ],
  [
    "PUT",
    /^members\/(\d+)\/calendars\/(\d+)$/,
    ({ m, body }) => {
      const item = memberCalendars.find((c) => c.id === Number(m[2]));
      if (item && body) {
        item.description = body.description;
        item.started_at = body.started_at;
        item.ended_at = body.ended_at;
      }
      return "";
    },
  ],
  [
    "DELETE",
    /^members\/(\d+)\/calendars\/(\d+)$/,
    ({ m }) => {
      const idx = memberCalendars.findIndex((c) => c.id === Number(m[2]));
      if (idx >= 0) memberCalendars.splice(idx, 1);
      return "";
    },
  ],

  // 스터디 채팅 (이전 대화 기록)
  [
    "GET",
    /^studies\/(\d+)\/chats$/,
    ({ m, query }) => {
      const list = chats[m[1]] || [];
      const start = Number(query.get("startChatId"));
      return start ? list.filter((c) => c.chat_id < start) : list;
    },
  ],

  // 화상회의 세션 / 토큰 (OpenVidu 서버 대신 임의 문자열)
  ["POST", /^studies\/(\d+)\/conference$/, ({ m }) => `mock-session-${m[1]}`],
  [
    "POST",
    /^studies\/(\d+)\/conferences\/members$/,
    ({ m }) => `mock-token-${m[1]}`,
  ],

  // 커뮤니티 게시글 삭제
  [
    "DELETE",
    /^boards\/(general|review|qna)\/(\d+)$/,
    ({ m }) => {
      const idx = articles.findIndex((a) => a.article_id === Number(m[2]));
      if (idx >= 0) articles.splice(idx, 1);
      return "";
    },
  ],
];

// ---------- 요청 처리 ----------
function normalizeUrl(config) {
  let url = String(config.url || "");
  url = url.replace(/^[a-z]+:\/\/[^/]+/i, "");
  const base = String(config.baseURL || "");
  if (base && url.startsWith(base)) url = url.slice(base.length);
  const [path, search = ""] = url.split("?");
  const params = new URLSearchParams(search);
  if (config.params) {
    Object.entries(config.params).forEach(([k, v]) => params.set(k, v));
  }
  return {
    path: path.replace(/^\/+/, "").replace(/\/+$/, ""),
    query: params,
  };
}

async function parseBody(data) {
  if (data === undefined || data === null) return undefined;
  if (typeof data === "string") {
    try {
      return JSON.parse(data);
    } catch (e) {
      return data;
    }
  }
  if (typeof FormData !== "undefined" && data instanceof FormData) {
    const request = data.get("request");
    if (request && typeof request.text === "function") {
      try {
        return JSON.parse(await request.text());
      } catch (e) {
        return {};
      }
    }
    return {};
  }
  return data;
}

function respond(config, status, data) {
  const response = {
    data: clone(data),
    status,
    statusText: status >= 400 ? "Error" : "OK",
    headers: {},
    config,
    request: {},
  };
  if (status >= 200 && status < 300) return Promise.resolve(response);
  const error = new Error(`Request failed with status code ${status}`);
  error.isAxiosError = true;
  error.config = config;
  error.response = response;
  return Promise.reject(error);
}

export default async function mockAdapter(config) {
  const method = String(config.method || "get").toUpperCase();
  const { path, query } = normalizeUrl(config);
  const body = await parseBody(config.data);
  syncFromStorage();

  for (const [routeMethod, pattern, handler] of routes) {
    if (routeMethod !== method) continue;
    const m = path.match(pattern);
    if (!m) continue;
    try {
      const result = handler({ m, query, body });
      saveToStorage();
      return respond(config, 200, result);
    } catch (e) {
      if (e && e.mockStatus) return respond(config, e.mockStatus, "");
      console.warn("[mock] 핸들러 오류", method, path, e);
      return respond(config, 200, method === "GET" ? [] : "");
    }
  }

  console.warn(`[mock] 처리되지 않은 요청: ${method} ${path}`);
  return respond(config, 200, method === "GET" ? [] : "");
}
