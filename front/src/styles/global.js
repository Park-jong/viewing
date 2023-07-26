import { createGlobalStyle } from "styled-components";

const GlobalVariableStyle = createGlobalStyle`
:root {
  // color
  --primary: #453CF8;
  --secondary: #E2E0F9;
  --background: #F8F8F8;
  --red: red;
  --blue: blue;
  --gray-50: #f9fafb;
  --gray-100: #f3f4f6;
  --gray-200: #e5e7eb;
  --gray-300: #d1d5db;
  --gray-400: #9ca3af;
  --gray-500: #6b7280;
  --gray-600: #4b5563;
  --gray-700: #374151;
  --gray-800: #1f2937;
  --gray-900: #111827;
  --gray-1000: #030712;

  //profile background color
  --bg1:#ff6767;
  --bg2:#a5ffa5;
  --bg3:#a9a9ff;
  --bg4:#ff76f6;
  --bg5:#ffff9e;
  --bg6:#93ffff;
  --bg7:#9c209c;
  --bg8:#ffa600;
  --bg9:#058e00;
  --bg10:#ff00ee;

}
#root {
  font-family: "Pretendard";
}

body {
  background: #F8F8F8;
}
`;

export default GlobalVariableStyle;
