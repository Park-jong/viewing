export const LOGIN = "LOGIN";
export const LOGOUT = "LOGOUT";
export const CHANGEPROFILE = "CHANGEPROFILE";
export const CHANGETOGGLE = "CHANGETOGGLE";
export const CHANGEALARMS = "CHANGEALARMS";
export const CHANGENICKNAME = "CHANGENICKNAME";

export function Login(data) {
  return {
    type: LOGIN,
    data,
  };
}

export function Logout() {
  return {
    type: LOGOUT,
  };
}

export function ChangeProfile(data) {
  return {
    type: CHANGEPROFILE,
    backgroundImg: data.backgroundImg,
    backgroundColor: data.backgroundColor
  };
}

export function ChangeNickname(nickname) {
  return {
    type: CHANGENICKNAME,
    nickname,
  };
}
export function ChangeToggle() {
  return {
    type: CHANGETOGGLE,
  };
}

export function ChangeAlarm(alarms) {
  return {
    type: CHANGEALARMS,
    alarms,
  };
}
