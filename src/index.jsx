import React from "react";
import ReactDOM from "react-dom/client";
import ResetStyle from "./styles/reset";
import GlobalVariableStyle from "./styles/global";
import { RouterProvider } from "react-router-dom";
import router from "./Router";
import "./fonts/font.css";
// import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
// import { RecoilRoot } from "recoil";
// import ReactGA from "react-ga4";

// ReactGA.initialize("나중에 하면 좋음.");

// 리액트 쿼리 쓰게 될 때 사용한다.
// const client = new QueryClient();

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  //   <RecoilRoot>
  //   <QueryClientProvider client={client}>
  <>
    <ResetStyle />
    <GlobalVariableStyle />
    <RouterProvider router={router} />
  </>
  //   </QueryClientProvider>
  //   </RecoilRoot>
);
