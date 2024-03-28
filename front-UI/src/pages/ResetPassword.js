import * as Yup from "yup";
import React, {useState} from "react";
import {Button, CircularProgress, Paper, TextField} from "@mui/material";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import {useTranslation} from "react-i18next";
import {resetPassword} from "../api/mok/accountApi";
import {toast, ToastContainer} from "react-toastify";


const resetPasswordSchema = Yup.object().shape({
    email: Yup.string()
        .email('email_valid')
        .required('email_required'),
});

function ResetPassword() {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(resetPasswordSchema),
    });

    const paperStyle = {padding: '30px 20px', margin: "auto", width: 400}
    const {t} = useTranslation()
    const [loading, setLoading] = useState(false)

    const onSubmit = async ({email}) => {
        resetPassword(email).then((response) => {
            setLoading(false)
            toast.success(t("email_sent"), {
                position: "top-center",
            })
        }).catch((error) => {
            setLoading(false)
            if(error.response.status === 404) {
                toast.error(t("no_such_email"), {
                    position: toast.POSITION.TOP_CENTER,
                });
            } else {
                toast.error(t("server_error"), {
                    position: toast.POSITION.TOP_CENTER,
                });
            }
        })
    }

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem'}}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("send_reset_email")} </h2>
                <form>
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label={t("email")}
                        fullWidth
                        required
                        sx={{mb: 4}}
                        error={errors.email}
                        helperText={t(errors.email?.message)}
                        {...register("email")}
                    />

                    {
                        loading ? <CircularProgress/> :
                            <Button fullWidth
                                    onClick={handleSubmit(onSubmit)} type='submit'
                                    variant='contained'>{t("submit")}</Button>
                    }
                </form>
            </Paper>
            <ToastContainer/>
        </div>
    )
}

export default ResetPassword;