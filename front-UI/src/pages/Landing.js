import React, {useState, useEffect} from "react";
import axios from "axios";
import {useTranslation} from "react-i18next";
import { Paper, Button } from "@mui/material";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import i18n from "i18next";
import {getSelfAccountDetails} from "../api/mok/accountApi";
import {Pathnames} from "../router/Pathnames";


function Landing() {
    const user = useSelector((state) => state.user);
    const { t } = useTranslation();
    const navigate = useNavigate();
    const userRole = useSelector((state) => state.user.cur);
    const isAdmin = userRole === "ADMIN";

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getSelfAccountDetails();
                if (response.data.language === "en") {
                    i18n.changeLanguage("en");
                } else if (response.data.language === "pl") {
                    i18n.changeLanguage("pl");
                } else if (response.data.language === "cs") {
                    i18n.changeLanguage("cs");
                } else {
                    i18n.changeLanguage("en");
                }

            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);


    const handleCreateAccount = () => {
        navigate(Pathnames.admin.createAccount);
    };

    return (
        <div className="wrapper">
            <Paper elevation={20} className="paper">
                <h2>{t("welcome_back")} {user.sub} </h2>
            </Paper>
        </div>
    );
}

export default Landing;
