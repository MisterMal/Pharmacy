import React, {useEffect, useState} from 'react';
import {confirmAccount} from "../api/mok/accountApi";
import {toast, ToastContainer} from "react-toastify";
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {Button, CircularProgress, Paper, TextField} from "@mui/material";
import 'react-toastify/dist/ReactToastify.css';
import {Pathnames} from "../router/Pathnames";


const ConfirmAccount = () => {

    const {token} = useParams();
    const [loading, setLoading] = useState(false)
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [errorDuringConfirmation, setErrorDuringConfirmation] = useState(false)

    useEffect(() => {
        confirmAccount(token).then((response) => {
            setLoading(false)
            setErrorDuringConfirmation(false)
            toast(t("account_confirmed"), {
                position: toast.POSITION.TOP_CENTER,
            });
        }).catch((error) => {
            setErrorDuringConfirmation(true)
            if (error.response.status === 417 || error.response.status === 404) {
                toast.error(t(error.response.data.message), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)
            } else {
                toast.error(t("server_error"), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)
            }
        })
    }, [])


    return (
        <div className="wrapper">
            <Paper elevation={20} className="paper">
                {
                    loading ? <CircularProgress/> :
                        <>
                            {
                                errorDuringConfirmation ? <h2>
                                        {t("error_during_confirmation")} </h2> :
                                    <div><h2>
                                        {t("account_confirmed_login")} </h2>
                                        <Button fullWidth
                                                onClick={() => navigate(Pathnames.public.login)} type='submit'
                                                variant='contained'>{t("login")}</Button>
                                    </div>

                            }
                        </>
                }
            </Paper>
            <ToastContainer/>
        </div>
    );
};

export default ConfirmAccount;