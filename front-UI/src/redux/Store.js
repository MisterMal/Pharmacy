import { configureStore } from '@reduxjs/toolkit';
import userReducer from "./UserSlice";

const store = configureStore({
    reducer: {
        user: userReducer,
    },
});

export const RootState = store.getState;
export const StoreDispatch = store.dispatch;
export default store;