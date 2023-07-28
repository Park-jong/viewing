const INITIAL_DATA = {
  userId: "",
  nickname: "",
  backgroundColor: "",
  backgroundImg: "",
  toggle: false,
  alarms: [],
};

export function UserReducer(state = INITIAL_DATA, action) {
  switch (action.type) {
    case "LOGIN":
      return action.data;
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
    case "CHANGEALARMS":
      return { ...state, alarms: action.alarms };
    default:
      return state;
  }
}
