export const LOGIN = "LOGIN";
export const LOGOUT = "LOGOUT";
export const CHANGEPROFILE = "CHANGEPROFILE";
export const CHANGETOGGLE = "CHANGETOGGLE";
export const GETALLALARM = "GETALLALARM";
export const CHANGENICKNAME = "CHANGENICKNAME";
export const GETONEALARM = "GETONEALARM"
export const HANDLEREAD = "HANDLEREAD"

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

export function GetAllAlarm(alarms) {
  return {
    type: GETALLALARM,
    alarms,
  };
}
export function GetOneAlarm(alarm) {
  return {
    type: GETONEALARM,
    alarm,
  };
}

export function HandleRead(alarm) {
  return {
    type: HANDLEREAD,
    alarm
  };
}

