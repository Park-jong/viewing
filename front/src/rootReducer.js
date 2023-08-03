import { persistReducer } from "redux-persist";
import { combineReducers } from "redux";
import storage from "redux-persist/lib/storage";
import { UserReducer } from "./modules/UserReducer/UserReducer";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["UserReducer"],
};

const rootReducer = combineReducers({
  UserReducer,
})

export default persistReducer(persistConfig, rootReducer)
