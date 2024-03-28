import {createSlice} from "@reduxjs/toolkit";
import {notifyAccessLevelChange} from "../api/mok/accountApi";
import {ACCESS_LEVEL, JWT_TOKEN} from "../constants/Constants";

const initialState = {
    sub: "",
    roles: [],
    cur: "",
    exp: "",
};

export const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        logout: () => {
            localStorage.removeItem(ACCESS_LEVEL);
            localStorage.removeItem(JWT_TOKEN);
            return initialState;
        },
        login: (state, action) => {
            const data = action.payload;

            if (
                !localStorage.getItem(ACCESS_LEVEL) ||
                localStorage.getItem(ACCESS_LEVEL) === ""
            ) {
                const res = {
                    sub: data.sub,
                    roles: data.roles.split(","),
                    cur: data.roles.split(",")[0],
                    exp: data.exp,
                };
                localStorage.setItem(ACCESS_LEVEL, res.cur);
                return { ...state, ...res };
            } else {
                const res = {
                    sub: data.sub,
                    roles: data.roles.split(","),
                    cur: localStorage.getItem(ACCESS_LEVEL),
                    exp: data.exp,
                };
                return { ...state, ...res };
            }
        },
        changeLevel: (state, action) => {
            const data = action.payload;
            const res = {
                sub: data.sub,
                roles: data.roles,
                cur: data.roles[data.index],
                exp: data.exp,
            };
            localStorage.setItem(ACCESS_LEVEL, res.cur);
            notifyAccessLevelChange(res.cur).then().catch();
            return { ...state, ...res };
        },
    },
});

export const { login, logout, changeLevel } = userSlice.actions;
export default userSlice.reducer;