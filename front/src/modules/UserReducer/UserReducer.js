const INITIAL_DATA = {
  memberId: "",
  nickname: "",
  backgroundColor: "",
  backgroundImg: "",
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
