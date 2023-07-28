import React from "react";
import ReactDOM from "react-dom/client";
import ResetStyle from "./styles/reset";
import GlobalVariableStyle from "./styles/global";
import { RouterProvider } from "react-router-dom";
import router from "./Router";
import "./fonts/font.css";
import {createStore} from 'redux'
import rootReducer from "./rootReducer";
import { Provider } from 'react-redux';
import {persistStore} from 'redux-persist'
import {PersistGate} from 'redux-persist/integration/react'
// import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
// import { RecoilRoot } from "recoil";
// import ReactGA from "react-ga4";

// ReactGA.initialize("나중에 하면 좋음.");

// 리액트 쿼리 쓰게 될 때 사용한다.
// const client = new QueryClient();

const store = createStore(rootReducer)
const persistor = persistStore(store)

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  //   <RecoilRoot>
  //   <QueryClientProvider client={client}>
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <>
        <ResetStyle />
        <GlobalVariableStyle />
        <RouterProvider router={router} />
      </>
    </PersistGate>
  </Provider>
  //   </QueryClientProvider>
  //   </RecoilRoot>
);
