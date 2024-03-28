import React, {useEffect} from 'react';
import {Paper} from "@mui/material";
import {useTranslation} from "react-i18next";
import {useDispatch} from "react-redux";
import {logout} from "../redux/UserSlice";

const Error = () => {

    const dispatch = useDispatch();
    const {t} = useTranslation();

    useEffect(() => {
        dispatch(logout());
    }, [])

    return (
        <div className="wrapper">
            <Paper elevation={20} className="paper">
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("page_not_found_or_access_denied")} </h2>
            </Paper>
        </div>
    );
};

export default Error;