import { MOCK_MEMBER } from "../../mock/mockUser";

// 프론트 단독 실행: 항상 목 사용자로 로그인된 상태에서 시작한다.
const INITIAL_DATA = {
  ...MOCK_MEMBER,
  toggle: false,
  alarms: [],
};

export function UserReducer(state = INITIAL_DATA, action) {
  switch (action.type) {
    case "LOGIN":
      return {...action.data, toggle:false, alarms:[]};
    case "LOGOUT":
      return { ...INITIAL_DATA, toggle: state.toggle };
    case "CHANGEPROFILE":
      return {
        ...state,
        backgroundColor: action.backgroundColor,
        backgroundImg: action.backgroundImg,
      };
    case "CHANGENICKNAME":
      return { ...state, nickname: action.nickname };
    case "CHANGETOGGLE":
      return { ...state, toggle: !state.toggle };
    case "GETALLALARM":
      return { ...state, alarms: action.alarms };
    case "GETONEALARM":
      return { ...state, alarms: [action.alarm, ...state.alarms] };
    case "HANDLEREAD":
      for (let alarm of state.alarms) {
        if (alarm == action.alarm) {
          alarm.isRead = true
          break
        }
      }
      return {...state}
      
    default:
      return state;
  }
}
