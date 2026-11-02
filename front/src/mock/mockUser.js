// 프론트 단독 실행용 목 사용자. 백엔드 응답(ProfileResponseDto)과 같은 필드 구성을 따른다.
export const MOCK_MEMBER = {
  memberId: 1,
  nickname: "뷰잉러",
  backgroundColor: "#a9a9ff",
  backgroundImg: "koala",
};

const encode = (obj) =>
  btoa(JSON.stringify(obj))
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");

// JWTDecoder가 header.payload.signature 형태를 요구하므로 서명 없는 가짜 토큰을 만든다.
export const MOCK_ACCESS_TOKEN = [
  encode({ alg: "none", typ: "JWT" }),
  encode({
    memberId: MOCK_MEMBER.memberId,
    email: "mock@viewing.local",
    exp: 4102444800,
  }),
  "bW9jaw",
].join(".");

export function ensureMockToken() {
  try {
    localStorage.setItem("access_token", MOCK_ACCESS_TOKEN);
  } catch (e) {
    // 저장소를 쓸 수 없는 환경에서는 무시한다.
  }
}
