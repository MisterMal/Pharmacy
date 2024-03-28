import React from "react";
import {Paper} from "@mui/material";
import {useTranslation} from "react-i18next";

function Home() {

    const {t} = useTranslation();


    return (
        <div className="wrapper">
            <Paper elevation={20} className="paper">
                <h1>
                    {t("welcome_public")} </h1>
                <h2>
                    {t("login_or_register_to_continue")} </h2>
            </Paper>
        </div>
    );
}

export default Home;
